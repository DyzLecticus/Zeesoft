package nl.zeesoft.zodb.database;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.file.XMLElem;
import nl.zeesoft.zodb.file.XMLFile;
import nl.zeesoft.zodb.model.MdlCollection;
import nl.zeesoft.zodb.model.MdlCollectionProperty;
import nl.zeesoft.zodb.model.MdlCollectionUniqueConstraint;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.MdlObject;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.MdlObjectRefList;
import nl.zeesoft.zodb.model.MdlObjectRefListMap;
import nl.zeesoft.zodb.model.ZODBModel;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtIdRefList;
import nl.zeesoft.zodb.model.datatypes.DtObject;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;
import nl.zeesoft.zodb.model.impl.BtcLog;
import nl.zeesoft.zodb.model.impl.BtcProgram;

/**
 * Provides methods to build, load, remove, restore or update the database.
 * All methods assume the DbConfig has been initialized as required.
 */
public final class DbFactory {
	
	public void buildDatabase(Object source) {
		Date start = new Date();

		DbIndexSaveWorker.getInstance().start();
		
		File d = new File(DbConfig.getInstance().getInstallDir());
		if ((d.exists()) && (d.isDirectory()) && (d.canRead()) && (d.canWrite())) {
			File dataDir = new File(DbConfig.getInstance().getFullDataDir());
			if ((dataDir.exists()) || (dataDir.mkdir())) {
				File indexDir = new File(DbConfig.getInstance().getFullIndexDir());
				if (!indexDir.exists()) {
					if (!indexDir.mkdir()) {
						Messenger.getInstance().error(this,"Unable to create directory: " + indexDir.getAbsolutePath());
					}
				}
				File cacheDir = new File(DbConfig.getInstance().getFullCacheDir());
				if (!cacheDir.exists()) {
					if (!cacheDir.mkdir()) {
						Messenger.getInstance().error(this,"Unable to create directory: " + cacheDir.getAbsolutePath());
					}
				}
				for (MdlCollection cls: DbConfig.getInstance().getModel().getCollections()) {
					File clsDataDir = new File(Generic.dirName(DbConfig.getInstance().getFullDataDir() + cls.getName()));
					clsDataDir.mkdir();
					if (!clsDataDir.exists()) {
						Messenger.getInstance().error(this,"Unable to create directory: " + clsDataDir.getAbsolutePath());
					}
				}
				File backupDir = new File(DbConfig.getInstance().getFullBackupDir());
				if (!backupDir.exists()) {
					if (!backupDir.mkdir()) {
						Messenger.getInstance().error(this,"Unable to create directory: " + backupDir.getAbsolutePath());
					}
				}
			} else {
				Messenger.getInstance().error(this,"Unable to create directory: " + dataDir.getAbsolutePath());
			}
		} else {
			Messenger.getInstance().error(this,"Unable to create database in directory: " + d.getAbsolutePath());
		}
		
		DbConfig.getInstance().serialize();
		DbConfig.getInstance().setDebug(true);
		DbCache.getInstance().serializeCache(source); // Initializes the cache
		DbConfig.getInstance().getModel().generateInitialData(source);
		DbIndexSaveWorker.getInstance().stop();
		
		long time = ((new Date()).getTime() - start.getTime());
		Messenger.getInstance().debug(this, "Database build in: " + DbConfig.getInstance().getInstallDir() + " took: " + time + " ms");
		DbConfig.getInstance().setDebug(false);
	}
	
	public void loadDatabase(Object source) {
		Date start = new Date();
		Date st = null;

		DbConfig.getInstance().unserialize();
		
		DbConfig.getInstance().getModel().getCrc();

		st = new Date();
		DbCache.getInstance().unserializeCache(source);
		Messenger.getInstance().debug(this, "Unserialize cache took: " + (new Date().getTime() - st.getTime()) + " ms (size: " + DbCache.getInstance().getSize(this) + ", preloaded objects: " + DbCache.getInstance().getPreloadedObjects().size() + ")");

		st = new Date();
		DbIndexPreloadManager.getInstance().unserializePreloadCollections();
		Messenger.getInstance().debug(this, "Unserialize index preloader took: " + (new Date().getTime() - st.getTime()) + " ms (size: " + DbConfig.getInstance().getModel().getCollections().size() + ")");

		st = new Date();
		DbIndex.getInstance().unserializeIndex(source);
		Messenger.getInstance().debug(this, "Unserialize index took: " + (new Date().getTime() - st.getTime()) + " ms (size: " + DbIndex.getInstance().getSize() + ")");
		
		long time = ((new Date()).getTime() - start.getTime());
		Messenger.getInstance().debug(this, "Database load from: " + DbConfig.getInstance().getInstallDir() + " took: " + time + " ms");
	}

	public void restoreDatabaseFromBackup(Object source) {
		File dir = new File(DbConfig.getInstance().getFullBackupDir());
		if ((!dir.exists()) || (!dir.isDirectory()) || (!dir.canRead())) {
			Messenger.getInstance().error(this, "Unable to read from: " + DbConfig.getInstance().getFullBackupDir());
			return;
		}
		String[] backupFiles = dir.list();
		if (backupFiles.length<=0) {
			Messenger.getInstance().error(this, "Backup directory is empty: " + DbConfig.getInstance().getFullBackupDir());
			return;
		}
		
		removeDatabaseFiles();
		
		DbIndex.getInstance().setCheckReferences(false);
		DbIndexSaveWorker.getInstance().start();

		Date now = new Date();

		List<BtcProgram> reschedulePrograms = new ArrayList<BtcProgram>();
		
		int restored = 0;
		Date start = new Date();
		for (int i = 0; i < backupFiles.length; i++) {
			QryTransaction t = new QryTransaction(DbConfig.getInstance().getModel().getAdminUser(source));
			XMLFile file = new XMLFile();
			String err = file.parseFile(DbConfig.getInstance().getFullBackupDir() + backupFiles[i]);
			if (err.length()>0) {
				Messenger.getInstance().error(this, "Error parsing backup XML file: " + backupFiles[i] + ", error: " + err);
			} else {
				for (XMLElem objElem: file.getRootElement().getChildren()) {
					XMLFile xmlObj = new XMLFile();
					xmlObj.setRootElement(objElem);
					MdlDataObject obj = MdlDataObject.fromXml(xmlObj);
					boolean add = true;
					if (obj.getId().getValue()==1) {
						add = false;
					} else if (obj instanceof BtcProgram) {
						BtcProgram program = (BtcProgram) obj;
						if (program.getExecuting().getValue()) {
							program.getExecuting().setValue(false);
						}
						if ((program.getActive().getValue()) && (program.getStart().getValue().getTime()<now.getTime())) {
							reschedulePrograms.add(program);
						}
					} else if (obj instanceof BtcLog) {
						BtcLog log = (BtcLog) obj;
						if (log.getStopped().getValue()==null) {
							log.getStopped().setValue(new Date());
						}
					}
					if (add) {
						t.addQuery(new QryAdd(obj));
					}
					xmlObj.cleanUp();
				}
			}
			file.cleanUp();
			DbIndex.getInstance().executeTransaction(t, source);
			restored = restored + t.getAddedIdList().size();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// Ignore
			}
		}

		Messenger.getInstance().warn(this, "Restored: " + restored + " objects");
		
		rescheduleBatchPrograms(reschedulePrograms, source);
		
		DbCache.getInstance().clearCache(source);
		DbCache.getInstance().serializeCache(source);
		
		DbIndexSaveWorker.getInstance().stop();
		
		long time = ((new Date()).getTime() - start.getTime());
		Messenger.getInstance().debug(this, "Database restore from: " + DbConfig.getInstance().getFullBackupDir() + " took: " + time + " ms");
	}
	
	public void removeDatabaseFiles() {
		List<String> dirs = new ArrayList<String>();
		
		Messenger.getInstance().debug(this, "Removing database files ...");
		File indexDir = new File(DbConfig.getInstance().getFullIndexDir());
		if (indexDir.exists()) {
			dirs.add(Generic.dirName(indexDir.getAbsolutePath()));
		}
		File cacheDir = new File(DbConfig.getInstance().getFullCacheDir());
		if (cacheDir.exists()) {
			dirs.add(Generic.dirName(cacheDir.getAbsolutePath()));
		}
		for (MdlCollection cls: DbConfig.getInstance().getModel().getCollections()) {
			File clsDataDir = new File(Generic.dirName(DbConfig.getInstance().getFullDataDir() + cls.getName()));
			if (clsDataDir.exists()) {
				dirs.add(Generic.dirName(clsDataDir.getAbsolutePath()));
			}
		}
		
		List<DbFactoryRemoveFileWorker> workers = new ArrayList<DbFactoryRemoveFileWorker>(); 
		for (String dirName: dirs) {
			File realDir = new File(dirName);
			DbFactoryRemoveFileWorker worker = new DbFactoryRemoveFileWorker(realDir);
			workers.add(worker);
			worker.start();
		}
		for (DbFactoryRemoveFileWorker worker: workers) {
			while (worker.isWorking()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					Messenger.getInstance().error(this, "A remove file worker was interrupted: " + e);
				}
			}
		}
	}
	
	public void updateDatabaseFromBackup(String fromDataDir,Object source) {
		File dbConfigFile = new File(Generic.dirName(fromDataDir) + "DbConfig.xml");
		if ((!dbConfigFile.exists()) || (!dbConfigFile.isFile()) || (!dbConfigFile.canRead())) {
			Messenger.getInstance().error(this, "Unable to read from: " + dbConfigFile.getAbsolutePath());
			return;
		}
		
		String encryptionKey = "";
		XMLFile dbConfigXML = new XMLFile();
		dbConfigXML.parseFile(dbConfigFile.getAbsolutePath());
		if ((dbConfigXML.getRootElement()!=null) && (dbConfigXML.getRootElement().getName().equals("config"))) {
			for (XMLElem v: dbConfigXML.getRootElement().getChildren()) {
				if (v.getName().equals("encryptionKey")) {
					encryptionKey = Generic.decompress(new StringBuffer(v.getValue())).toString();
				}
			}
		}
		dbConfigXML.cleanUp();
		if (encryptionKey.length()==0) {
			Messenger.getInstance().error(this, "Encryption key not found");
			return;
		}
		
		String backupDir = Generic.dirName(fromDataDir) + "backup/";
		
		File dir = new File(backupDir);
		if ((!dir.exists()) || (!dir.isDirectory()) || (!dir.canRead())) {
			Messenger.getInstance().error(this, "Unable to read from: " + backupDir);
			return;
		}
		String[] backupFiles = dir.list();
		if (backupFiles.length<=0) {
			Messenger.getInstance().error(this, "Backup directory is empty: " + backupDir);
			return;
		}
		
		loadDatabase(source);
		
		DbIndexSaveWorker.getInstance().start();

		int added = 0;
		int updated = 0;
		int skipped = 0;
		int errors = 0;
		
		Date start = new Date();

		List<BtcProgram> reschedulePrograms = new ArrayList<BtcProgram>();
		
		ZODBModel model = DbConfig.getInstance().getModel();
		
		// Build an MdlObjectRefList and map of all objects in the backup files
		Messenger.getInstance().debug(this, "Loading backup objects ...");
		MdlObjectRefList backupRefList = new MdlObjectRefList();
		MdlObjectRefListMap backupRefListMap = new MdlObjectRefListMap();
		for (int i = 0; i < backupFiles.length; i++) {
			XMLFile file = new XMLFile();
			String err = file.parseFile(backupDir + backupFiles[i]);
			if (err.length()>0) {
				Messenger.getInstance().error(this, "Error parsing backup XML file: " + backupFiles[i] + ", error: " + err);
			} else {
				for (XMLElem objElem: file.getRootElement().getChildren()) {
					SortedMap<String,StringBuffer> keyValueMap = new TreeMap<String,StringBuffer>();
					for (XMLElem valueElem: objElem.getChildren()) {
						keyValueMap.put(valueElem.getName(),valueElem.getValue());
					}
					String className = keyValueMap.get(MdlObject.PROPERTY_CLASSNAME).toString();
					if (className!=null) {
						MdlDataObject object = model.getObjectForUpdateObject(keyValueMap);
						if (object!=null) {
							MdlObjectRef objRef = new MdlObjectRef(object);
							backupRefList.getReferences().add(objRef);
							backupRefListMap.getReferenceListForCollection(className).getReferences().add(objRef);
						}
					}
				}
			}
			file.cleanUp();
		}
		// Order the list by id
		backupRefList.sortObjects(MdlObject.PROPERTY_ID);
		Messenger.getInstance().debug(this, "Loaded backup objects: " + backupRefList.getReferences().size());
		
		// Build an MdlObjectRefList of all objects in the database
		Messenger.getInstance().debug(this, "Loading database objects ...");
		MdlObjectRefList databaseRefList = new MdlObjectRefList();
		for (MdlCollection col: model.getCollections()) {
			boolean repeat = true;
			int fetchStart = 0;
			while (repeat) {
				QryFetch fetch = new QryFetch(col.getName());
				fetch.setStartLimit(fetchStart, 1000);
				fetchStart = fetchStart + 1000;
				DbIndex.getInstance().executeFetch(fetch, model.getAdminUser(source), this);
				int size = fetch.getMainResults().getReferences().size();
				if (size>0) {
					for (MdlObjectRef ref: fetch.getMainResults().getReferences()) {
						MdlObjectRef objRef = new MdlObjectRef(ref.getDataObject());
						databaseRefList.getReferences().add(objRef);
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						repeat = false;
						break;
					}
				} else {
					repeat = false;
				}
			}
		}
		// Order the list by id
		databaseRefList.sortObjects(MdlObject.PROPERTY_ID);
		Messenger.getInstance().debug(this, "Loaded database objects: " + databaseRefList.getReferences().size());
		
		// Build the oldId newId maps based on the new objects
		Messenger.getInstance().debug(this, "Initialize id translation map ...");
		SortedMap<Long,Long> oldIdNewIdMap = new TreeMap<Long,Long>();
		oldIdNewIdMap.put(0L,0L);
		List<String> uniqueIndexMissing = new ArrayList<String>();
		for (MdlObjectRef newRef: databaseRefList.getReferences()) {
			MdlCollectionUniqueConstraint uniqueConstraint = null;
			for (MdlCollectionUniqueConstraint uc: model.getCollectionByName(newRef.getClassName().getValue()).getUniqueConstraints()) {
				uniqueConstraint = uc;
				break;
			}
			if (uniqueConstraint!=null) {
				MdlObjectRef foundRef = null;
				for (MdlObjectRef oldRef: backupRefListMap.getReferenceListForCollection(newRef.getClassName().getValue()).getReferences()) {
					if (!oldIdNewIdMap.containsKey(oldRef.getId().getValue())) {
						boolean propertiesMatch = true;
						for (MdlCollectionProperty prop: uniqueConstraint.getProperties()) {
							DtObject newValObj = newRef.getDataObject().getPropertyValue(prop.getName());
							DtObject oldValObj = oldRef.getDataObject().getPropertyValue(prop.getName());
							if (oldValObj instanceof DtIdRef) {
								DtIdRef idRef = new DtIdRef();
								Long newId = oldIdNewIdMap.get(oldValObj.getValue());
								if (newId!=null) {
									idRef.setValue(newId);
								} else {
									propertiesMatch = false;
									break;
								}
								oldValObj = idRef;
							} else if (oldValObj instanceof DtIdRefList) {
								DtIdRefList idRefList = new DtIdRefList();
								for (Long id: ((DtIdRefList) oldValObj).getValue()) {
									Long newId = oldIdNewIdMap.get(id);
									if (newId!=null) {
										idRefList.getValue().add(newId);
									} else {
										propertiesMatch = false;
										break;
									}
								}
								if (propertiesMatch) {
									oldValObj = idRefList;
								} else {
									break;
								}
							}
							String newVal = newValObj.toString();
							String oldVal = oldValObj.toString();
							if (!oldVal.equals(newVal)) {
								propertiesMatch = false;
								break;
							}
						}
						if (propertiesMatch) {
							foundRef = oldRef;
							break;
						}
					}
				}
				if (foundRef!=null) {
					Messenger.getInstance().debug(this, "Old id -> new id: " + foundRef.getId().getValue() + " -> " + newRef.getId().getValue());
					oldIdNewIdMap.put(foundRef.getId().getValue(),newRef.getId().getValue());
				}
			} else {
				if (!uniqueIndexMissing.contains(newRef.getClassName().getValue())) {
					uniqueIndexMissing.add(newRef.getClassName().getValue());
					Messenger.getInstance().warn(this, "No unique index found for collection: " + newRef.getClassName().getValue());
				}
			}
		}
		Messenger.getInstance().debug(this, "Initialized id translation map: " + oldIdNewIdMap.size());

		// Update the database
		// Build maps for old object reference properties and values that require updating later 
		SortedMap<Long,List<MdlCollectionProperty>> updateReferenceProperties = new TreeMap<Long,List<MdlCollectionProperty>>();
		SortedMap<Long,List<DtObject>> updateReferenceValues = new TreeMap<Long,List<DtObject>>();
		Date now = new Date();
		for (MdlObjectRef oldRef: backupRefList.getReferences()) {
			MdlCollection col = model.getCollectionByName(oldRef.getClassName().getValue());
			String method = model.getUpdateMethodForCollection(col);
			boolean skip = false;
			Long newObjectId = oldIdNewIdMap.get(oldRef.getId().getValue());
			if (method.equals(ZODBModel.METHOD_SKIP)) {
				skip = true;
			} else {
				if (oldRef.getId().getValue()==1) {
					// Skip admin user
					skip = true;
				} else if (newObjectId!=null) {
					if (method.equals(ZODBModel.METHOD_ADD_ONLY)) {
						skip = true;
					}
				} else {
					if (method.equals(ZODBModel.METHOD_UPDATE_ONLY)) {
						skip = true;
					}
				}
			}
			if (!skip) {
				MdlDataObject oldObj = oldRef.getDataObject();
				boolean idNotFound = false;
				
				if (newObjectId!=null) {
					MdlDataObject newObj = databaseRefList.getMdlObjectRefById(newObjectId).getDataObject();
					// Copy values from new object if update not allowed
					for (MdlCollectionProperty prop: col.getProperties()) {
						DtObject oldValObj = oldObj.getPropertyValue(prop.getName());
						if (
							(!prop.getName().equals(MdlObject.PROPERTY_CREATEDON)) &&
							(!prop.getName().equals(MdlObject.PROPERTY_CHANGEDON)) &&
							(!prop.getName().equals(MdlObject.PROPERTY_CLASSNAME)) &&
							(!prop.getName().equals(MdlObject.PROPERTY_ID)) &&
							(!(oldValObj instanceof DtIdRef)) &&
							(!(oldValObj instanceof DtIdRefList)) &&
							(!prop.getConstraints().contains(DtObject.CONSTRAIN_PASSWORD)) &&
							(!model.canUpdateCollectionProperty(col,prop))
							) {
							oldObj.setPropertyValue(prop.getName(),newObj.getPropertyValue(prop.getName()));
						}
					}
				}
							
				// Convert id references and passwords
				for (MdlCollectionProperty prop: col.getProperties()) {
					DtObject oldValObj = oldObj.getPropertyValue(prop.getName());
					if (oldValObj instanceof DtIdRef) {
						DtIdRef idRef = new DtIdRef();
						Long newId = oldIdNewIdMap.get(oldValObj.getValue());
						if (newId!=null) {
							idRef.setValue(newId);
						} else {
							if (!prop.getConstraints().contains(DtObject.CONSTRAIN_PROPERTY_MANDATORY)) {
								List<MdlCollectionProperty> props = updateReferenceProperties.get(oldObj.getId().getValue());
								List<DtObject> vals = updateReferenceValues.get(oldObj.getId().getValue());
								if ((props==null) && (vals==null)) {
									props = new ArrayList<MdlCollectionProperty>();
									vals = new ArrayList<DtObject>();
									updateReferenceProperties.put(oldObj.getId().getValue(), props);
									updateReferenceValues.put(oldObj.getId().getValue(), vals);
								}
								props.add(prop);
								vals.add(DtObject.copy(oldValObj));
								idRef.setValue(0);
							} else {
								Messenger.getInstance().error(this, col.getName() + "." + prop.getName() + ": translated id not found for: " + oldValObj.getValue());
								idNotFound = true;
								break;
							}
						}
						oldValObj.setValue(idRef.getValue());
					} else if (oldValObj instanceof DtIdRefList) {
						DtIdRefList idRefList = new DtIdRefList();
						for (Long idVal: ((DtIdRefList) oldValObj).getValue()) {
							Long newId = oldIdNewIdMap.get(idVal);
							if (newId!=null) {
								idRefList.getValue().add(newId);
							} else {
								if (!prop.getConstraints().contains(DtObject.CONSTRAIN_PROPERTY_MANDATORY)) {
									List<MdlCollectionProperty> props = updateReferenceProperties.get(oldObj.getId().getValue());
									List<DtObject> vals = updateReferenceValues.get(oldObj.getId().getValue());
									if ((props==null) && (vals==null)) {
										props = new ArrayList<MdlCollectionProperty>();
										vals = new ArrayList<DtObject>();
										updateReferenceProperties.put(oldObj.getId().getValue(), props);
										updateReferenceValues.put(oldObj.getId().getValue(), vals);
									}
									props.add(prop);
									vals.add(DtObject.copy(oldValObj));
									idRefList.setValue(new ArrayList<Long>());
									break;
								} else {
									Messenger.getInstance().error(this, col.getName() + "." + prop.getName() + ": translated ID not found for: " + idVal);
									idNotFound = true;
									break;
								}
							}
						}
						if (idNotFound) {
							break;
						} else {
							oldValObj.setValue(idRefList.getValue());
						}
					} else if (prop.getConstraints().contains(DtObject.CONSTRAIN_PASSWORD)) {
						if (oldValObj instanceof DtStringBuffer) {
							DtStringBuffer pwd = new DtStringBuffer();
							StringBuffer val = ((DtStringBuffer) oldValObj).getValue();
							pwd.setValue(Generic.decodeKey(val,encryptionKey,0));
							oldValObj.setValue(DbConfig.getInstance().encodePassword(pwd.getValue()));
						}
					}
				}
				if (idNotFound) {
					errors++;
					skipped++;
				} else {
					QryTransaction t = new QryTransaction(model.getAdminUser(source));
					Long oldObjectId = oldObj.getId().getValue();
					if (oldObj instanceof BtcProgram) {
						BtcProgram program = (BtcProgram) oldObj;
						if (program.getExecuting().getValue()) {
							program.getExecuting().setValue(false);
						}
					}
					if (newObjectId!=null) {
						oldObj.getId().setValue(newObjectId);
						QryUpdate upd = new QryUpdate(oldObj);
						t.addQuery(upd);
					} else {
						oldObj.getId().setValue(0L);
						QryAdd add = new QryAdd(oldObj);
						t.addQuery(add);
					}
					DbIndex.getInstance().executeTransaction(t, source);
					if (t.getUpdatedIdList().size()>0) {
						updated++;
						if (oldObj instanceof BtcProgram) {
							BtcProgram program = (BtcProgram) oldObj;
							if ((program.getActive().getValue()) && (program.getStart().getValue().getTime()<now.getTime())) {
								reschedulePrograms.add(program);
							}
						}
					} else if (t.getAddedIdList().size()>0) {
						Messenger.getInstance().debug(this, "Old id -> new id: " + oldObjectId + " -> " + oldObj.getId().getValue());
						oldIdNewIdMap.put(oldObjectId,oldObj.getId().getValue());
						added++;
						if (oldObj instanceof BtcProgram) {
							BtcProgram program = (BtcProgram) oldObj;
							if ((program.getActive().getValue()) && (program.getStart().getValue().getTime()<now.getTime())) {
								reschedulePrograms.add(program);
							}
						}
					} else {
						errors++;
						skipped++;
					}
				}
			} else {
				skipped++;
			}
		}
		
		if (updateReferenceProperties.size()>0) {
			Messenger.getInstance().debug(this, "Updating references ...");
			for (Entry<Long,List<MdlCollectionProperty>> entry: updateReferenceProperties.entrySet()) {
				Long newObjectId = oldIdNewIdMap.get(entry.getKey());
				if (newObjectId!=null) {
					MdlObjectRef oldRef = backupRefList.getMdlObjectRefById(newObjectId);
					if (oldRef!=null) {
						MdlDataObject oldObj = oldRef.getDataObject();
						List<DtObject> vals = updateReferenceValues.get(entry.getKey());
						int num = 0;
						boolean idNotFound = false;
						boolean updatedObj = false;
						for (MdlCollectionProperty prop: entry.getValue()) {
							DtObject oldValObj = vals.get(num);
							num++;
							boolean updateProp = true;
							if (oldValObj instanceof DtIdRef) {
								DtIdRef idRef = (DtIdRef) oldValObj;
								Long newId = oldIdNewIdMap.get(idRef.getValue());
								if (newId!=null) {
									idRef.setValue(newId);
								} else {
									Messenger.getInstance().error(this, oldObj.getClassName().getValue() + "." + prop.getName() + ": translated id not found for: " + oldValObj.getValue());
									updateProp = false;
									idNotFound = true;
								}
							} else if (oldValObj instanceof DtIdRefList) {
								DtIdRefList idRefList = new DtIdRefList();
								for (Long idVal: ((DtIdRefList) oldValObj).getValue()) {
									Long newId = oldIdNewIdMap.get(idVal);
									if (newId!=null) {
										idRefList.getValue().add(newId);
									} else {
										Messenger.getInstance().error(this, oldObj.getClassName().getValue() + "." + prop.getName() + ": translated id not found for: " + idVal);
										updateProp = false;
										idNotFound = true;
										break;
									}
								}
								if (updateProp) {
									oldValObj.setValue(idRefList.getValue());
								}
							}
							if (updateProp) {
								oldObj.setPropertyValue(prop.getName(),oldValObj);
								updatedObj = true;
							}
						}
						if (idNotFound) {
							errors++;
						}
						if (updatedObj) {
							QryTransaction t = new QryTransaction(model.getAdminUser(source));
							QryUpdate upd = new QryUpdate(oldObj);
							t.addQuery(upd);
							DbIndex.getInstance().executeTransaction(t, source);
							if (t.getUpdatedIdList().size()==0) {
								errors++;
							}
						}
					} else {
						Messenger.getInstance().error(this, "Backup object not found with new id: " + newObjectId);
						errors++;
					}
				} else {
					Messenger.getInstance().error(this, "Translated id not found for old object: " + entry.getKey());
					errors++;
				}
			}
			Messenger.getInstance().debug(this, "Updated references: " + updateReferenceProperties.size());
		}
		
		Messenger.getInstance().warn(this, "Updated: " + updated + " objects");
		Messenger.getInstance().warn(this, "Added: " + added + " objects");
		Messenger.getInstance().warn(this, "Skipped: " + skipped + " objects (errors: " + errors + ")");

		rescheduleBatchPrograms(reschedulePrograms, source);
		
		DbCache.getInstance().clearCache(source);
		DbCache.getInstance().serializeCache(source);
		
		DbIndexSaveWorker.getInstance().stop();
		
		long time = ((new Date()).getTime() - start.getTime());
		Messenger.getInstance().debug(this, "Database update from: " + Generic.dirName(fromDataDir) + " took: " + time + " ms");
	}
	
	private void rescheduleBatchPrograms(List<BtcProgram> reschedulePrograms, Object source) {
		if (reschedulePrograms.size()>0) {
			QryTransaction t = new QryTransaction(DbConfig.getInstance().getModel().getAdminUser(source));
			for (BtcProgram program: reschedulePrograms) {
				program.reschedule(false);
				t.addQuery(new QryUpdate(program));
			}
			DbIndex.getInstance().executeTransaction(t, source);
			Messenger.getInstance().debug(this, "Rescheduled batch programs");
		}
	}
}
