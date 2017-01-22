package nl.zeesoft.zodb.database;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryError;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.database.query.QryFetchList;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.database.query.QryObjectList;
import nl.zeesoft.zodb.database.query.QryRemove;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.file.FileObject;
import nl.zeesoft.zodb.model.MdlCollection;
import nl.zeesoft.zodb.model.MdlCollectionProperty;
import nl.zeesoft.zodb.model.MdlCollectionReference;
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
import nl.zeesoft.zodb.model.impl.DbUser;

/**
 * This class represents the main index of the database.
 * 
 * Internal database programs like batch procedures can use its public methods to execute database queries.
 * The server protocol uses it for this purpose as well.
 */
public final class DbIndex {
	public static final int							BLOCK_SIZE			= 1000;
	
	private static final int						MAX_LOADERS			= 10;

	private static final int						BREATHE				= 1; // Milliseconds
	private static final int						BREATHE_QUERIES		= 10; // Every X queries

	private static DbIndex							index				= null;

	private	long									uid					= 0;
	private SortedMap<Long,MdlObjectRef>			idObjectMap			= new TreeMap<Long,MdlObjectRef>();
	private SortedMap<Integer,MdlObjectRefList>		fileNumObjectMap	= new TreeMap<Integer,MdlObjectRefList>();
	
	private Object									indexIsLockedBy		= null;

	private boolean									colIndexesChanged	= false;
	private SortedMap<Long,MdlObjectRef>			changedObjects		= new TreeMap<Long,MdlObjectRef>();
	private SortedMap<Long,MdlObjectRef>			removedObjects		= new TreeMap<Long,MdlObjectRef>();
	private List<Integer>							changedFileNums		= new ArrayList<Integer>();

	private	SortedMap<String,Long>					changedCollections	= new TreeMap<String,Long>();
	
	private List<MdlCollection>						preloadCollections	= new ArrayList<MdlCollection>();
	private boolean									preloadOrderChanged = false;
	
	private boolean									checkReferences		= true;
	
	private DbIndex() {
		preloadCollections = DbIndexPreloadManager.getInstance().getPreloadCollections();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}

	public static DbIndex getInstance() {
		if (index==null) {
			index = new DbIndex();
		}
		return index;
	}

	/**
	 * Used for debugging purposes when index is not active.
	 * 
	 * @return The size of the index
	 */
	public int getSize() {
		return idObjectMap.size();
	}

	/**
	 * @param checkReferences the checkReferences to set
	 * 
	 * Default value is true.
	 * When true, referential integrity is checked.
	 * Set to false when restoring backup files when the index is not active.
	 */
	public void setCheckReferences(boolean checkReferences) {
		this.checkReferences = checkReferences;
	}
	
	public Exception executeTransaction(QryTransaction transaction,Object source) {
		Exception e = null;
		if (QryObjectList.checkTransaction(transaction)) {
			int size = transaction.getQueries().size();
			int curr = 0;
			for (QryObject qry: transaction.getQueries()) {
				qry.setSource(source);
				try {
					lockIndex(source);
					executeTransactionQueryNoLock(transaction,qry,source);
				} catch (Exception ex) {
					e = ex;
				} finally {
					unlockIndex(source);
				}
				if (e!=null) {
					break;
				}
				curr++;
				if (curr>=size) {
					break;
				}
				if ((curr % BREATHE_QUERIES) == 0) {
					try {
						Thread.sleep(BREATHE);
					} catch (InterruptedException ie) {
						// Ignore
					}
				}
			}
		}
		if (e!=null) {
			String callStack = Generic.getCallStackString(e.getStackTrace(),"");
			String msg = "Exception while executing transaction: " + e.toString() + "\n" + callStack;
			Messenger.getInstance().error(this,msg);
		}
		return e;
	}
	
	public Exception executeFetchList(QryFetchList fetchList,Object source) {
		Exception e = null;
		if (QryObjectList.checkFetchList(fetchList,fetchList.getUser())) {
			List<QryObject> done = new ArrayList<QryObject>();
			for (QryObject qry: fetchList.getQueries()) {
				qry.setSource(source);
				QryFetch fetch = (QryFetch) qry;
				if (executeFetchUseCacheNoLock(fetch, source)) {
					done.add(qry);
				}
			}
			if (done.size()<fetchList.getQueries().size()) {
				int size = fetchList.getQueries().size();
				int curr = 0;
				for (QryObject qry: fetchList.getQueries()) {
					try {
						lockIndex(source);
						QryFetch fetch = (QryFetch) qry;
						executeFetchNoLock(fetch, source);
					} catch (Exception ex) {
						e = ex;
					} finally {
						unlockIndex(source);
					}
					if (e!=null) {
						break;
					}
					curr++;
					if (curr>=size) {
						break;
					}
					if ((curr % BREATHE_QUERIES) == 0) {
						try {
							Thread.sleep(BREATHE);
						} catch (InterruptedException ie) {
							// Ignore
						}
					}
				}
			}
		}
		if (e!=null) {
			String callStack = Generic.getCallStackString(e.getStackTrace(),"");
			String msg = "Exception while executing fetch list: " + e.toString() + "\n" + callStack;
			Messenger.getInstance().error(this,msg);
		}
		return e;
	}

	public Exception executeFetch(QryFetch fetch,DbUser user,Object source) {
		Exception e = null;
		if (QryObject.checkFetch(fetch,user)) {
			fetch.setSource(source);
			if (!executeFetchUseCacheNoLock(fetch, source)) {
				lockIndex(source);
				try {
					executeFetchNoLock(fetch, source);
				} catch (Exception ex) {
					e = ex;
				}
				unlockIndex(source);
			}
		}
		if (e!=null) {
			String callStack = Generic.getCallStackString(e.getStackTrace(),"");
			String msg = "Exception while executing fetch: " + e.toString() + "\n" + callStack;
			Messenger.getInstance().error(this,msg);
		}
		return e;
	}
	
	/**************************** PROTECTED METHODS **************************/
	
	protected void unserializeIndex(Object source) {
		lockIndex(source);
		
		uid = 0;
		
		idObjectMap.clear();
		for (MdlCollection col: DbConfig.getInstance().getModel().getCollections()) {
			col.getIdRefMap().clear();
			col.getNameRefListMap().clear();
		}
		
		fileNumObjectMap.clear();

		List<DbIndexLoadWorkerObject> loaders = new ArrayList<DbIndexLoadWorkerObject>();
		
		File dir = new File(DbConfig.getInstance().getFullIndexDir());
		String[] indexFiles = dir.list();
		List<Integer> allFileNums = new ArrayList<Integer>();
		for (int i = 0; i < indexFiles.length; i++) {
			if (Generic.isNumeric(indexFiles[i])) {
				allFileNums.add(Integer.parseInt(indexFiles[i]));
			}
		}
		int filesPerLoader = 1;
		if (indexFiles.length>MAX_LOADERS) {
			int mod = (indexFiles.length % MAX_LOADERS);
			if (mod == 0) {
				mod = MAX_LOADERS;
			}
			filesPerLoader = (((indexFiles.length - mod) + MAX_LOADERS) / MAX_LOADERS);
		}
		
		for (int l = 0; l < MAX_LOADERS; l++) {
			boolean added = false;
			DbIndexLoadFileWorker loader = new DbIndexLoadFileWorker();
			for (int f = 0; f < filesPerLoader; f++) {
				if (allFileNums.size()<=0) {
					break;
				}
				Integer fileNum = allFileNums.get(0);
				loader.getFileNums().add(fileNum);
				allFileNums.remove(0);
				added = true;
			}
			if (added) {
				loaders.add(loader);
				loader.start();
			}
			if (allFileNums.size()<=0) {
				break;
			}
		}
		
		ZODBModel mdl = DbConfig.getInstance().getModel();
		List<MdlCollectionUniqueConstraint> ucs = new ArrayList<MdlCollectionUniqueConstraint>();
		List<MdlCollectionReference> rs = new ArrayList<MdlCollectionReference>();
		for (MdlCollection c: mdl.getCollections()) {
			for (MdlCollectionUniqueConstraint uc: c.getUniqueConstraints()) {
				ucs.add(uc);
				if (ucs.size()>=10) {
					DbIndexLoadUniqueConstraintWorker loader = new DbIndexLoadUniqueConstraintWorker();
					loader.setUniqueConstraints(ucs);
					loaders.add(loader);
					loader.start();
					ucs = new ArrayList<MdlCollectionUniqueConstraint>();
				}
			}
			for (MdlCollectionReference r: mdl.getCollectionReferences(c.getName())) {
				rs.add(r);
				if (rs.size()>=10) {
					DbIndexLoadReferenceWorker loader = new DbIndexLoadReferenceWorker();
					loader.setReferences(rs);
					loaders.add(loader);
					loader.start();
					rs = new ArrayList<MdlCollectionReference>();
				}
			}
		}
		if (ucs.size()>0) {
			DbIndexLoadUniqueConstraintWorker loader = new DbIndexLoadUniqueConstraintWorker();
			loader.setUniqueConstraints(ucs);
			loaders.add(loader);
			loader.start();
		}
		if (rs.size()>0) {
			DbIndexLoadReferenceWorker loader = new DbIndexLoadReferenceWorker();
			loader.setReferences(rs);
			loaders.add(loader);
			loader.start();
		}
		
		unlockIndex(source);
		
        if (loaders.size()>0) {
	        for (DbIndexLoadWorkerObject worker: loaders) {
	        	while (!worker.isDone()) {
	        		try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						Messenger.getInstance().error(this, "A load worker was interrupted");
					}
	        	}
	        }
        }
	}

	protected void serializeIndex(Object source) {
		lockIndex(source);
		for (Entry<Integer,MdlObjectRefList> entry: fileNumObjectMap.entrySet()) {
			serializeIndexFileNoLock(entry.getKey(),source);
		}
		unlockIndex(source);
	}

	protected void serializeObjects(Object source) {
		lockIndex(source);
		Collection<MdlObjectRef> refList = idObjectMap.values(); 
		for (MdlObjectRef ref: refList) {
			DbIndexSaveWorker.serializeObject(ref,source);
		}
		changedObjects.clear();
		removedObjects.clear();
		unlockIndex(source);
	}
	
	protected void addLoadedReferences(SortedMap<Integer,MdlObjectRefList> fNumObjectMap,SortedMap<Long,Integer> idFNumMap,Object source) {
		lockIndex(source);
		for (Entry<Integer,MdlObjectRefList> entry: fNumObjectMap.entrySet()) {
			fileNumObjectMap.put(entry.getKey(),entry.getValue());
			for (MdlObjectRef ref: entry.getValue().getReferences()) {
				if (uid<=ref.getId().getValue()) {
					uid=ref.getId().getValue();
				}
				ref.setFileNum(entry.getKey());
				addObjectRefToIndexNoLock(ref,false,source);
			}
		}
		unlockIndex(source);
	}
	
	protected boolean addChangesToFileWorker(int maxFiles,int maxObjects,Object source) {
		lockIndex(source);
		boolean changed = false;
		if (
			(colIndexesChanged) ||
			(changedFileNums.size()>0) ||
			(changedObjects.size()>0) ||
			(removedObjects.size()>0)
			) {
			if (maxFiles>0) {
				StringBuffer sb = null;
				
				List<Integer> fileNums = new ArrayList<Integer>(changedFileNums);
				int files = 0;
				for (Integer fileNum: fileNums) {
					DbIndexSaveWorker.getInstance().addWriteFile(DbConfig.getInstance().getFullIndexDir() + fileNum,indexFileToStringBufferNoLock(fileNum,source));
					changedFileNums.remove(fileNum);
					files++;
					if (files>=maxFiles) {
						break;
					}
				}
				
				if ((files<maxFiles) && (colIndexesChanged)) {
					boolean collectionIndexesChanged = false;
					ZODBModel mdl = DbConfig.getInstance().getModel();
					for (MdlCollection c: mdl.getCollections()) {
						for (MdlCollectionUniqueConstraint uc: c.getUniqueConstraints()) {
							sb = uc.getStringBufferIfChanged();
							if (sb!=null) {
								collectionIndexesChanged = true;
								DbIndexSaveWorker.getInstance().addWriteFile(uc.getFullFileName(),sb);
								files++;
								if (files>=maxFiles) {
									break;
								}
							}
						}
						if (files<maxFiles) {
							for (MdlCollectionReference r: mdl.getCollectionReferences(c.getName())) {
								sb = r.getStringBufferIfChanged();
								if (sb!=null) {
									collectionIndexesChanged = true;
									DbIndexSaveWorker.getInstance().addWriteFile(r.getFullFileName(),sb);
									files++;
									if (files>=maxFiles) {
										break;
									}
								}
							}
						}
						if (files>=maxFiles) {
							break;
						}
					}
					colIndexesChanged = collectionIndexesChanged;
				}
				if (files>0) {
					changed = true;
				}
			}
			if (maxObjects>0) {
				int objects = 0;
				SortedMap<Long,MdlObjectRef> changedList = new TreeMap<Long,MdlObjectRef>(changedObjects); 
				for (Entry<Long,MdlObjectRef> entry: changedList.entrySet()) {
					DbIndexSaveWorker.getInstance().addWriteObject(entry.getValue());
					changedObjects.remove(entry.getKey());
					objects++;
					if (objects>=maxObjects) {
						break;
					}
				}
				if (objects<=maxObjects) {
					SortedMap<Long,MdlObjectRef> removedList = new TreeMap<Long,MdlObjectRef>(removedObjects); 
					for (Entry<Long,MdlObjectRef> entry: removedList.entrySet()) {
						DbIndexSaveWorker.getInstance().addRemoveObject(entry.getValue());
						removedObjects.remove(entry.getKey());
						objects++;
						if (objects>=maxObjects) {
							break;
						}
					}
				}
				if (objects>0) {
					changed = true;
				}
			}
		}
		unlockIndex(source);
		return changed;
	}

	protected MdlObjectRefList getPreloadObjectList(int maxTotal,int maxPerCollection,Object source) {
		MdlObjectRefList r = new MdlObjectRefList();
		if (maxTotal<=0) {
			maxTotal=1;
		}
		if (maxTotal>=1000) {
			maxTotal=1000;
		}
		if (maxPerCollection<=0) {
			maxPerCollection=1;
		}
		if (maxPerCollection>=100) {
			maxPerCollection=100;
		}
		int loaded = 0;

		lockIndex(source);
		List<MdlCollection> collections = new ArrayList<MdlCollection>(preloadCollections);
		unlockIndex(source);
		
		for (MdlCollection col: collections) {
			int doneCol = 0;
			lockIndex(source);
			
			MdlObjectRefList colRefs = col.getRefList();
			if (colRefs!=null) {
				List<MdlObjectRef> refs = colRefs.getUnloadedRefs();
				if (refs.size()>0) {
					for (MdlObjectRef ref: refs) {
						r.getReferences().add(MdlObjectRef.copy(ref));
						loaded++;
						doneCol++;
						if (
							(doneCol>=maxPerCollection) ||
							(loaded>=maxTotal)
							) {
							break;
						}
					}
				}
			}
			unlockIndex(source);
		}
		return r;
	}

	protected MdlObjectRefList getPreloadObjectList(MdlCollection collection,boolean backward,int max,Object source) {
		MdlObjectRefList r = new MdlObjectRefList();
		lockIndex(source);
		int load = 0;
		MdlObjectRefList colRefs = collection.getRefList();
		if (colRefs!=null) {
			List<MdlObjectRef> refs = colRefs.getUnloadedRefs();
			if (refs.size()>0) {
				if (backward) {
					for (int i = (refs.size() -1); i >=0; i--) {
						r.getReferences().add(MdlObjectRef.copy(refs.get(i)));
						load++;
						if (load>=max) {
							break;
						}
					}
				} else {
					for (MdlObjectRef ref: refs) {
						r.getReferences().add(MdlObjectRef.copy(ref));
						load++;
						if (load>=max) {
							break;
						}
					}
				}
			}
		}
		unlockIndex(source);
		return r;
	}
	
	protected int setPreloadObjectList(MdlObjectRefList loadedList,Object source) {
		int done = 0;
		for (MdlObjectRef loadedRef: loadedList.getReferences()) {
			if (loadedRef.getDataObject()!=null) {
				lockIndex(source);
				MdlObjectRef idxRef = getObjectRefUseIdIndexNoLock(loadedRef.getId().getValue(),source);
				if ((idxRef!=null) && (idxRef.getDataObject()==null)) {
					idxRef.setDataObject(loadedRef.getDataObject());
					done++;
				}
				unlockIndex(source);
			}
		}
		return done;
	}

	protected List<MdlCollection> getPreloadCollectionList(Object source) {
		List<MdlCollection> newList = null;
		lockIndex(source);
		if (preloadOrderChanged) {
			newList = new ArrayList<MdlCollection>(preloadCollections);
			preloadOrderChanged = false;
		}
		unlockIndex(source);
		return newList; 
	}

	/**************************** PRIVATE METHODS **************************/
	private synchronized void getNewId(MdlObject obj) {
		if (obj.getId().getValue()==0) {
			uid++;
			obj.getId().setValue(uid);
		}
	}

	private synchronized void lockIndex(Object source) {
		int attempt = 0;
		while (indexIsLocked()) {
		    try {
                wait();
            } catch (InterruptedException e) { 
            	
            }
            attempt ++;
			if (attempt>=1000) {
				Messenger.getInstance().warn(this,"Lock failed after " + attempt + " attempts. Source:" + source);
				attempt = 0;
			}
		}
		indexIsLockedBy = source;
		changedCollections.clear();
	}

	private synchronized void unlockIndex(Object source) {
		if (indexIsLockedBy==source) {
			for (Entry<String,Long> entry: changedCollections.entrySet()) {
				DbCache.getInstance().updateCollectionChanged(entry.getKey(), entry.getValue(), source);
			}
			changedCollections.clear();
			indexIsLockedBy=null;
			notifyAll();
		}
	}
	
	private synchronized boolean indexIsLocked() {
		return (indexIsLockedBy!=null);
	}

	private void addedObjectNoLock(MdlDataObject object, Object source) {
		MdlObjectRef ref = idObjectMap.get(object.getId().getValue());
		changedObjects.put(ref.getId().getValue(), ref);
		if (ref.getFileNum()>=0) {
			if (!changedFileNums.contains(ref.getFileNum())) {
				changedFileNums.add(ref.getFileNum());
			}
		}
		colIndexesChanged = true;
		changedCollectionDataNoLock(ref,source);
	}

	private void updatedObjectNoLock(MdlDataObject original,MdlDataObject copy, Object source) {
		MdlObjectRef ref = idObjectMap.get(copy.getId().getValue());
		if (original.getId().getValue()!=ref.getId().getValue()) {
			changedObjects.remove(original.getId().getValue());
		}
		changedObjects.put(ref.getId().getValue(), ref);
		if (ref.getFileNum()>=0) {
			if (!changedFileNums.contains(ref.getFileNum())) {
				changedFileNums.add(ref.getFileNum());
			}
		}
		colIndexesChanged = true;
		changedCollectionDataNoLock(ref,source);
	}

	private void removedObjectNoLock(MdlObjectRef ref, Object source) {
		changedObjects.remove(ref.getId().getValue());
		removedObjects.put(ref.getId().getValue(), ref);
		if (ref.getFileNum()>=0) {
			if (!changedFileNums.contains(ref.getFileNum())) {
				changedFileNums.add(ref.getFileNum());
			}
		}
		colIndexesChanged = true;
		changedCollectionDataNoLock(ref,source);
	}
	
	private void changedCollectionDataNoLock(MdlObjectRef ref, Object source) {
		changedCollections.put(ref.getClassName().getValue(), new Date().getTime());
	}
	
	private MdlObjectRef getObjectRefUseIdIndexNoLock(long id,Object source) {
		return idObjectMap.get(id);
	}

	private MdlObjectRefList getObjectRefListUseIdIndexListNoLock(List<Long> idList,Object source) {
		return getObjectRefListUseIdIndexListNoLock(idList,0,0,source);
	}

	private MdlObjectRefList getObjectRefListUseIdIndexListNoLock(List<Long> idList,int start, int limit,Object source) {
		MdlObjectRefList r = new MdlObjectRefList();
		if (limit<=0) {
			for (long id: idList) {
				MdlObjectRef ref = idObjectMap.get(id);
				if (ref!=null) {
					r.getReferences().add(ref);
				}
			}
		} else {
			int size = idList.size();
			if (start < size) {
				int end = start + limit;
				if (end>size) {
					end = size;
				}
				for (int i = start; i < end; i++) {
					MdlObjectRef ref = idObjectMap.get(idList.get(i));
					if (ref!=null) {
						r.getReferences().add(ref);
					}
				}
			}
		}
		return r;
	}
	
	private MdlObjectRefList getObjectRefListUseNameIndexListNoLock(String className,List<String> nameList,Object source) {
		MdlObjectRefList r = new MdlObjectRefList();
		MdlCollection col = DbConfig.getInstance().getModel().getCollectionByName(className);
		for (String name: nameList) {
			MdlObjectRefList rt = col.getNameRefListMap().get(name);
			if (rt!=null) {
				for (MdlObjectRef ref: rt.getReferences()) {
					r.getReferences().add(ref);
				}
			}
		}
		return r;
	}
	
	private MdlObjectRefList getObjectRefListUseClassNameIndexNoLock(String className,String orderBy,boolean orderAscending,Object source) {
		return getObjectRefListUseClassNameIndexNoLock(className,orderBy,orderAscending,0,0,source);
	}

	private MdlObjectRefList getObjectRefListUseClassNameIndexNoLock(String className,String orderBy,boolean orderAscending,int start, int limit,Object source) {
		MdlObjectRefList r = null;
		MdlCollection col = DbConfig.getInstance().getModel().getCollectionByName(className);
		boolean limited = false;
		if (
			(orderBy!=null) && (orderBy.length()>0) &&
			(orderBy.equals(col.getOrderedBy())) && (orderAscending==col.isOrderedAscending()) 
			) {
			r = new MdlObjectRefList();
			int i = 0;
			for (MdlObjectRef ref: col.getOrderedRefList().getReferences()) {
				if ((limit==0) || ((i>=start) && (i<(start + limit)))) {
					r.getReferences().add(getObjectRefUseIdIndexNoLock(ref.getId().getValue(), source));
				}
				i++;
			}
			limited = true;
		} else {
			r = col.getRefList();
			int unloaded = r.getUnloadedRefs().size();
			if ((orderBy!=null) && (orderBy.length()>0) && (unloaded==0)) {
				if (!orderBy.equals(MdlObject.PROPERTY_ID)) {
					r.sortObjects(orderBy,orderAscending);
				}
				col.setOrderedBy(orderBy);
				col.setOrderedAscending(orderAscending);
				col.getOrderedRefList().getReferences().clear();
				for (MdlObjectRef ref: r.getReferences()) {
					col.getOrderedRefList().getReferences().add(ref);
				}
			}
		}
		if ((!limited) && (limit>0)) {
			MdlObjectRefList t = r;
			r = new MdlObjectRefList();
			int size = t.getReferences().size();
			if (start < size) {
				int end = start + limit;
				if (end>size) {
					end = size;
				}
				for (int i = start; i < end; i++) {
					r.getReferences().add(t.getReferences().get(i));
				}
			}
		}
		return r;
	}

	private int getObjectRefListSizeForClassNameNoLock(String className,Object source) {
		int size = 0;
		MdlCollection col = DbConfig.getInstance().getModel().getCollectionByName(className);
		if (col!=null) {
			size = col.getIdRefMap().size();
		}
		return size;
	}

	private void updateObjectInIndexNoLock(MdlDataObject original,MdlDataObject changed,Object source) {
		MdlObjectRef ref = idObjectMap.get(original.getId().getValue());
		
		ZODBModel model = DbConfig.getInstance().getModel();
		
		MdlCollection col = model.getCollectionByName(original.getClassName().getValue());

		if (col.getOrderedBy().length()>0) {
			String ori = original.getPropertyValue(col.getOrderedBy()).toString();
			String chg = changed.getPropertyValue(col.getOrderedBy()).toString();
			if (!ori.equals(chg)) {
				col.clearOrderedRefList();
			}
		}
		
		if (!changed.getName().getValue().equals(original.getName().getValue())) {
			// Remove from name index
			String name = original.getName().getValue();
			MdlObjectRefList refList = col.getNameRefListMap().get(name);
			if (refList!=null) {
				refList.getReferences().remove(ref);
				if (refList.getReferences().size()==0) {
					col.getNameRefListMap().remove(name);
				}
			}
			
			// Add to name index
			name = changed.getName().getValue();
			refList = col.getNameRefListMap().get(name);
			if (refList==null) {
				refList = new MdlObjectRefList();
				col.getNameRefListMap().put(name, refList);
			}
			refList.getReferences().add(ref);
		}
		
		// Update unique constraint indexes
		for (MdlCollectionUniqueConstraint uc: model.getCollectionUniqueConstraints(ref.getClassName().getValue())) {
			boolean update = false;
			for (MdlCollectionProperty p: uc.getProperties()) {
				DtObject oValue = original.getPropertyValue(p.getName());
				DtObject cValue = changed.getPropertyValue(p.getName());
				if (!oValue.toString().equals(cValue.toString())) {
					update = true;
					break;
				}
			}
			if (update) {
				uc.removeObject(original);
				uc.addObject(changed);
			}
		}

		// Update reference indexes
		for (MdlCollectionReference r: model.getCollectionReferences(ref.getClassName().getValue())) {
			DtObject oValue = original.getPropertyValue(r.getName());
			DtObject cValue = changed.getPropertyValue(r.getName());
			if (!oValue.toString().equals(cValue.toString())) {
				r.removeChildObject(original);
				r.addChildObject(changed);
			}
		}
		
		// Update actual index reference object
		ref.setDataObject(changed);
		ref.getChangedOn(); // Trigger reference update
	}

	private void addObjectRefToIndexNoLock(MdlObjectRef ref,boolean updateCollectionIndexes,Object source) {
		if (ref.getDataObject()!=null) {
			MdlDataObject obj = ref.getDataObject(); 
			if (obj.getId().getValue()==0) {
				getNewId(obj);
			} else if (obj.getId().getValue() > uid) {
				uid = obj.getId().getValue();
			}
		}

		// Add to id index
		idObjectMap.put(ref.getId().getValue(), ref);
		
		ZODBModel model = DbConfig.getInstance().getModel();
		
		MdlCollection col = model.getCollectionByName(ref.getClassName().getValue());

		// Add to name index
		String name = ref.getName().getValue();
		MdlObjectRefList refList = col.getNameRefListMap().get(name);
		if (refList==null) {
			refList = new MdlObjectRefList();
			col.getNameRefListMap().put(name, refList);
		}
		refList.getReferences().add(ref);

		// Add to collection index
		col.getIdRefMap().put(ref.getId().getValue(), ref);
		if (col.getOrderedBy().length()>0) {
			col.clearOrderedRefList();
		}

		if (updateCollectionIndexes) {
			// Add to unique constraint indexes
			if (ref.getDataObject()!=null) {
				for (MdlCollectionUniqueConstraint uc: model.getCollectionUniqueConstraints(ref.getClassName().getValue())) {
					uc.addObject(ref.getDataObject());
				}
			}
			
			// Add to reference indexes
			for (MdlCollectionReference r: model.getCollectionReferencesByReferenceClass(ref.getClassName().getValue())) {
				r.addParentObject(ref);
			}
			if (ref.getDataObject()!=null) {
				for (MdlCollectionReference r: model.getCollectionReferences(ref.getClassName().getValue())) {
					r.addChildObject(ref.getDataObject());
				}
			}
		}

		// Add to file map
		if (ref.getFileNum()<0) {
			ref.setFileNum(getFileNumForNewReferenceNoLock(source));
			MdlObjectRefList refs = fileNumObjectMap.get(ref.getFileNum());
			if (refs==null) {
				refs = new MdlObjectRefList();
				fileNumObjectMap.put(ref.getFileNum(),refs);
			}
			refs.getReferences().add(ref);
		}
	}

	private int getFileNumForNewReferenceNoLock(Object source) {
		int num = 0;
		boolean found = false;
		for (Entry<Integer,MdlObjectRefList> entry: fileNumObjectMap.entrySet()) {
			if (entry.getValue().getReferences().size()<BLOCK_SIZE) {
				num = entry.getKey();
				found = true;
				break;
			}
		}
		if (!found) {
			num = fileNumObjectMap.size();
		}
		return num;
	}

	private void removeObjectRefFromIndexNoLock(MdlObjectRef ref,Object source) {
		// Remove from id index
		ref = idObjectMap.remove(ref.getId().getValue());
		
		ZODBModel model = DbConfig.getInstance().getModel();
		
		MdlCollection col = model.getCollectionByName(ref.getClassName().getValue());

		// Remove from name index
		String name = ref.getName().getValue();
		MdlObjectRefList refList = col.getNameRefListMap().get(name);
		if (refList!=null) {
			refList.getReferences().remove(ref);
			if (refList.getReferences().size()==0) {
				col.getNameRefListMap().remove(name);
			}
		}

		// Remove from collection index
		col.getIdRefMap().remove(ref.getId().getValue());
		if (col.getOrderedBy().length()>0) {
			col.clearOrderedRefList();
		}

		// Remove from unique constraint indexes
		if (ref.getDataObject()!=null) {
			for (MdlCollectionUniqueConstraint uc: model.getCollectionUniqueConstraints(ref.getClassName().getValue())) {
				uc.removeObject(ref.getDataObject());
			}
		}

		// Remove from reference indexes
		for (MdlCollectionReference r: model.getCollectionReferencesByReferenceClass(ref.getClassName().getValue())) {
			r.removeParentObject(ref);
		}
		if (ref.getDataObject()!=null) {
			for (MdlCollectionReference r: model.getCollectionReferences(ref.getClassName().getValue())) {
				r.removeChildObject(ref.getDataObject());
			}
		}
		
		// Remove from file map
		if (ref.getFileNum()>=0) {
			MdlObjectRefList refs = fileNumObjectMap.get(ref.getFileNum());
			if (refs!=null) {
				refs.getReferences().remove(refs.getMdlObjectRefById(ref.getId().getValue()));
			}
		}
	}

	private void serializeIndexFileNoLock(int fileNum,Object source) {
		FileObject f = new FileObject();
		String fileName = DbConfig.getInstance().getFullIndexDir() + fileNum;
		f.writeFile(fileName, indexFileToStringBufferNoLock(fileNum,source));
	}

	private StringBuffer indexFileToStringBufferNoLock(int fileNum,Object source) {
		StringBuffer sb = new StringBuffer();
		MdlObjectRefList refs = fileNumObjectMap.get(fileNum);
		if (refs!=null) {
			for (MdlObjectRef ref: refs.getReferences()) {
				sb.append(ref.toString());
				sb.append("\n");
			}
		}
		return sb;
	}
	
	private boolean checkUnserializeObjectNoLock(MdlObjectRef ref, Object source) {
		boolean unserialize = true;
		if (ref.getDataObject()==null) {
			MdlObjectRef idxRef = getObjectRefUseIdIndexNoLock(ref.getId().getValue(),source);
			if ((idxRef!=null) && (idxRef.getDataObject()!=null)) {
				ref.setDataObject(idxRef.getDataObject());
				unserialize = false;
			}
		} else {
			unserialize = false;
		}
		return unserialize;
	}

	private void unserializeObjectNoLock(MdlObjectRef ref, Object source) {
		if (ref.getDataObject()==null) {
			DbIndexPreloadWorker.unserializeObject(ref, source);
			if (ref.getDataObject()!=null) {
				MdlObjectRef idxRef = getObjectRefUseIdIndexNoLock(ref.getId().getValue(),source);
				if ((idxRef!=null) && (ref!=idxRef)) {
					idxRef.setDataObject(ref.getDataObject());
				}
			}
		}
	}
	
	private int unserializeObjectsNoLock(List<MdlObjectRef> refs, Object source) {
		boolean unserialize = false;
		int unserialized = 0;
		for (MdlObjectRef ref: refs) {
			if (checkUnserializeObjectNoLock(ref,source)) {
				unserialize = true;
				break;
			}
		}
		if (unserialize) {
			for (MdlObjectRef ref: refs) {
				if (ref.getDataObject()==null) {
					unserializeObjectNoLock(ref,source);
					unserialized++;
				}
			}
		}
		return unserialized;
	}

	private int unserializeObjectsNoLock(MdlObjectRefList references, Object source) {
		int unserialized = 0;
		List<MdlObjectRef> refs = references.getUnloadedRefs();
		int size = refs.size(); 
		if (size>=200) {
			int refsPerLoader = (size / DbIndexLoadObjectWorker.MAX_FETCH_WORKERS) + 1;
			int doneLoader = 0;
			List<DbIndexLoadObjectWorker> workers = new ArrayList<DbIndexLoadObjectWorker>();
			DbIndexLoadObjectWorker worker = new DbIndexLoadObjectWorker();
			for (int i = 0; i < size; i ++) {
				worker.getLoadObjectList().getReferences().add(refs.get(i));
				doneLoader++;
				if (doneLoader>=refsPerLoader) {
					doneLoader = 0;
					workers.add(worker);
					worker.start();
					worker = new DbIndexLoadObjectWorker();
				}
			}
			if (doneLoader>0) {
				doneLoader = 0;
				workers.add(worker);
				worker.start();
			}
			for (DbIndexLoadObjectWorker fetchWorker: workers) {
	        	while (!fetchWorker.isDone()) {
	        		try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						Messenger.getInstance().error(this, "A fetch worker was interrupted");
					}
	        	}
	        	for (MdlObjectRef ref: fetchWorker.getLoadObjectList().getReferences()) {
	    			if (ref.getDataObject()!=null) {
	    				MdlObjectRef idxRef = getObjectRefUseIdIndexNoLock(ref.getId().getValue(),source);
	    				if ((idxRef!=null) && (ref!=idxRef)) {
	    					idxRef.setDataObject(ref.getDataObject());
	    				}
	    			} else {
						Messenger.getInstance().error(this, "Failed to unserialize object: " + ref.getId().getValue());
	    			}
	        	}
			}
			if (references.getUnloadedRefs().size()>0) {
				Messenger.getInstance().error(this, "Failed to unserialize objects");
			} else {
				unserialized = size;
			}
		} else {
			unserialized = unserializeObjectsNoLock(refs,source);
		}
		return unserialized;
	}

	private boolean executeFetchUseCacheNoLock(QryFetch fetch,Object source) {
		boolean done = false;
		if ((fetch.isUseCache()) && (DbConfig.getInstance().isCache())) {
			Date startTime = new Date();
			QryFetch fc = DbCache.getInstance().getFetchFromCache(fetch, source);
			if (fc!=null) {
				if (fc!=fetch) {
					fetch.setCount(fc.getCount());
					fetch.setResults(fc.getResults());
					for (Entry<Long,String> entry: fc.getExtendedReferences().entrySet()) {
						fetch.getExtendedReferences().put(entry.getKey(), entry.getValue());
					}
					fetch.setTime((new Date().getTime() - startTime.getTime()));
					fetch.getLog().append("Fetched results from cache in ");
					fetch.getLog().append( fetch.getTime());
					fetch.getLog().append(" ms\nCached log:\n");
					fetch.getLog().append(fc.getLog());
				}
				done = true;
			}
		}
		return done;
	}

	private void executeFetchNoLock(QryFetch fetch,Object source) {
		if (executeFetchUseCacheNoLock(fetch,source)) {
			return;
		}
		Date startTime = new Date();
		
		fetch.initializeResults();
		
		MdlObjectRefList refList = getInitialRefListForFetchNoLock(fetch,source);

		if (fetch.getType().equals(QryFetch.TYPE_COUNT_REFERENCES)) {
			fetch.setCount(refList.getReferences().size());
			fetch.setTime(new Date().getTime() - startTime.getTime());
			return;
		} 

		fetch.setCount(getObjectRefListSizeForClassNameNoLock(fetch.getClassName(),source));

		int size = refList.getReferences().size();
		if (!fetch.isLimitedInitialResults()) {
			if ((size>0) && (fetch.getLimit()>0) && (fetch.getStart()>=size)) {
				refList.getReferences().clear();
				fetch.addLogLine("Cleared results because start: " + fetch.getStart() + " > size: " + size);
				size = 0;
			}
		}
		
		int loaded = 0;
		if ((size>1) && 
			(fetch.getOrderBy().length()>0) &&
			(!fetch.getOrderBy().equals(MdlObject.PROPERTY_CLASSNAME)) &&
			(!fetch.isOrderedInitialResults())
			) {
			fetch.addLogLine("Order by: " + fetch.getOrderBy() + ", ascending: " + fetch.isOrderAscending());
			if (
				(!fetch.getOrderBy().equals(MdlObject.PROPERTY_ID)) &&
				(!fetch.getOrderBy().equals(MdlObject.PROPERTY_NAME)) &&
				(!fetch.getOrderBy().equals(MdlObject.PROPERTY_CLASSNAME)) &&
				(!fetch.getOrderBy().equals(MdlObject.PROPERTY_CREATEDON)) &&
				(!fetch.getOrderBy().equals(MdlObject.PROPERTY_CREATEDBY)) &&
				(!fetch.getOrderBy().equals(MdlObject.PROPERTY_CHANGEDON)) &&
				(!fetch.getOrderBy().equals(MdlObject.PROPERTY_CHANGEDBY))
				) {
				loaded = unserializeObjectsNoLock(refList,this);
				if (loaded > 0) {
					fetch.addLogLine("Unserialized objects: " + loaded);
				}
			}
			refList.sortObjects(fetch.getOrderBy(),fetch.isOrderAscending());
		}
		
		size = refList.getReferences().size();
		if (!fetch.isLimitedInitialResults()) {
			if ((fetch.getLimit()>0) && (size>0) && (fetch.getStart()<size)) {
				fetch.addLogLine("Start: " + fetch.getStart() + ", limit: " + fetch.getLimit() + ", size: " + size);
				MdlObjectRefList newList = new MdlObjectRefList();
				int newSize = 0;
	    		for (int num = 0; num < size; num++) {
					if (
						(num >= fetch.getStart()) && (num < (fetch.getStart() + fetch.getLimit()))
						) {
						newList.getReferences().add(refList.getReferences().get(num));
						newSize++;
					}
	    		}
	    		refList = newList;
	    		size = newSize;
			}
		}

		if (size>DbConfig.getInstance().getMaxFetchResults()) {
			fetch.addLogLine("WARNING: Limiting results due to maximum fetch results: " + size + " > " + DbConfig.getInstance().getMaxFetchResults());
			fetch.setResultsIncomplete(true);
			int remove = DbConfig.getInstance().getMaxFetchResults();
			for (int i = (size - 1); i >= DbConfig.getInstance().getMaxFetchResults(); i--) {
				refList.getReferences().remove(remove);
			}
		}
		int unloaded = checkFetchRefListLoadLimit(fetch,refList,loaded); 
		
		if ((!fetch.getType().equals(QryFetch.TYPE_FETCH_REFERENCES)) && (loaded==0) && (unloaded>0)) {
			loaded = unserializeObjectsNoLock(refList,this);
			if (loaded > 0) {
				fetch.addLogLine("Unserialized objects: " + loaded);
			}
		}
		
		fetch.setMainResults(MdlObjectRefList.copy(refList));
		
		if (!fetch.getType().equals(QryFetch.TYPE_FETCH_REFERENCES)) {
			int done = 0;
			int doneRef = 0;
			for (MdlObjectRef ref: fetch.getMainResults().getReferences()) {
				if (ref.getDataObject()!=null) {
					MdlDataObject obj = MdlDataObject.copy(ref.getDataObject());
					ref.setDataObject(obj);
					done++;
					if (fetch.getType().equals(QryFetch.TYPE_FETCH_OBJECTS_EXTEND)) {
						for (MdlCollectionProperty refProp: DbConfig.getInstance().getModel().getCollectionReferences(fetch.getClassName())) {
							DtObject valObj = obj.getPropertyValue(refProp.getName());
							String name = "";
							if (valObj instanceof DtIdRef) {
								DtIdRef idRef = (DtIdRef) valObj;
								long id = idRef.getValue();
								if (id>0) {
									if (!fetch.getExtendedReferences().containsKey(id)) {
										name = getObjectRefUseIdIndexNoLock(id,source).getName().getValue();
										fetch.getExtendedReferences().put(id, name);
										doneRef++;
									}
								}
							} else if (valObj instanceof DtIdRefList) {
								DtIdRefList idRefList = (DtIdRefList) valObj;
								for (long id: idRefList.getValue()) {
									if (!fetch.getExtendedReferences().containsKey(id)) {
										name = getObjectRefUseIdIndexNoLock(id,source).getName().getValue();
										fetch.getExtendedReferences().put(id, name);
										doneRef++;
									}
								}
							}
						}
					}
				}
			}
			if (done>0) {
				fetch.addLogLine("Copied model objects: " + done);
			}
			if (doneRef>0) {
				fetch.addLogLine("Added extended references: " + doneRef);
			}
		}
		
		if (
			(!fetch.isResultsIncomplete()) && 
			(fetch.getType().equals(QryFetch.TYPE_FETCH_OBJECTS_ENTITY)) &&
			(fetch.getMainResults().getReferences().size()>0) && 
			(fetch.getEntities().size()>0)
			) {
			int doneTotal = 0; 
			Date entityTime = new Date();
			
			fetchChildEntityReferences(fetch,loaded,fetch.getClassName(),fetch.getMainResults(),fetch.getEntities(),source);
			checkFetchEntityLimit(fetch,loaded);
			int done = unserializeFetchEntityResults(fetch);
			if (done>0) {
				fetch.addLogLine("Unserialized child entity objects: " + done);
				loaded = loaded + done; 
			}
			if (!fetch.isResultsIncomplete()) {
				fetchReferencedEntitiesNoLock(fetch,loaded,fetch.getEntities(),source);
				checkFetchEntityLimit(fetch,loaded);
				done = unserializeFetchEntityResults(fetch);
				if (done>0) {
					fetch.addLogLine("Unserialized referenced entity objects: " + done);
					loaded = loaded + done; 
				}
			}
			for (String entity: fetch.getEntities()) {
				MdlObjectRefList entityResults = fetch.getResults().getReferenceListForCollection(entity);
				done = 0;
				if (entityResults!=null) {
					done = entityResults.getReferences().size();
					for (MdlObjectRef ref: entityResults.getReferences()) {
						if (ref.getDataObject()!=null) {
							MdlDataObject obj = MdlDataObject.copy(ref.getDataObject());
							ref.setDataObject(obj);
						}
					}
				}
				fetch.addLogLine("Fetched " + done + " " + entity + " objects");
				doneTotal = doneTotal + done;
			}
			fetch.addLogLine("Fetched " + doneTotal + " entity objects in " + (new Date().getTime() - entityTime.getTime()) + " ms");
		}
		
		fetch.setTime(new Date().getTime() - startTime.getTime());

		String objects = "objects";
		if (fetch.getType().equals(QryFetch.TYPE_FETCH_REFERENCES)) {
			objects = "references";
		} else if (fetch.getType().equals(QryFetch.TYPE_FETCH_OBJECTS_ENTITY)) {
			objects = "entities";
		}
		if (refList.getReferences().size()==1) {
			if (fetch.getType().equals(QryFetch.TYPE_FETCH_REFERENCES)) {
				objects = "reference";
			} else if (fetch.getType().equals(QryFetch.TYPE_FETCH_OBJECTS_ENTITY)) {
				objects = "entity";
			} else {
				objects = "object";
			}
		}

		fetch.addLogLine("Result: " + refList.getReferences().size() + " " + objects + " in " + fetch.getTime() + " ms");

		if (DbConfig.getInstance().isCache()) {
			DbCache.getInstance().addFetchToCache(fetch, source);
		}
	}

	private int checkFetchRefListLoadLimit(QryFetch fetch, MdlObjectRefList refList, int loaded) {
		int unloaded = (loaded + refList.getUnloadedRefs().size());
		if (unloaded>DbConfig.getInstance().getMaxFetchLoad()) {
			fetch.addLogLine("WARNING: Limiting results due to maximum fetch load: " + unloaded + " > " + DbConfig.getInstance().getMaxFetchLoad());
			fetch.setResultsIncomplete(true);

			int load = loaded;
			int size = refList.getReferences().size();
			int get = 0;
			for (int i = 0; i < size; i++) {
				MdlObjectRef ref = refList.getReferences().get(get);
				get++;
				if (ref.getDataObject()==null) {
					load++;
					if (load > DbConfig.getInstance().getMaxFetchLoad()) {
						get--;
						refList.getReferences().remove(get);
					}
				}
			}
			unloaded = DbConfig.getInstance().getMaxFetchLoad();

			MdlCollection col = DbConfig.getInstance().getModel().getCollectionByName(fetch.getClassName());
			if (preloadCollections.indexOf(col)!=0) {
				preloadCollections.remove(col);
				preloadCollections.add(0,col);
				preloadOrderChanged = true;
			}
		}
		return unloaded;
	}
	
	private void checkFetchEntityLimit(QryFetch fetch, int loaded) {
		if (!fetch.isResultsIncomplete()) {
			int results = fetch.getResults().getSize();
			if (results>DbConfig.getInstance().getMaxFetchResults()) {
				fetch.addLogLine("WARNING: Limiting results due to maximum fetch results: " + results + " > " + DbConfig.getInstance().getMaxFetchResults());
				fetch.setResultsIncomplete(true);
				List<Long> idList = fetch.getResults().getIdList();
				int result = 0;
				for (long id: idList) {
					result++;
					if (result>DbConfig.getInstance().getMaxFetchResults()) {
						MdlObjectRef ref = fetch.getResults().getMdlObjectRefById(id);
						fetch.getResults().getReferenceListForCollection(ref.getClassName().getValue()).getReferences().remove(ref);
					}
				}
			}
			int unloaded = (loaded + fetch.getResults().getUnloadedSize());
			if (unloaded>DbConfig.getInstance().getMaxFetchLoad()) {
				fetch.addLogLine("WARNING: Limiting results due to maximum fetch load: " + unloaded + " > " + DbConfig.getInstance().getMaxFetchLoad());
				fetch.setResultsIncomplete(true);
				List<Long> idList = fetch.getResults().getIdList();
				int load = loaded;
				for (long id: idList) {
					MdlObjectRef ref = fetch.getResults().getMdlObjectRefById(id);
					if (ref.getDataObject()==null) {
						load++;
						if (load>DbConfig.getInstance().getMaxFetchLoad()) {
							fetch.getResults().getReferenceListForCollection(ref.getClassName().getValue()).getReferences().remove(ref);
						}
					}
				}
				MdlCollection col = DbConfig.getInstance().getModel().getCollectionByName(fetch.getClassName());
				if (preloadCollections.indexOf(col)!=0) {
					preloadCollections.remove(col);
					preloadCollections.add(0,col);
					preloadOrderChanged = true;
				}
			}
		}
	}
	
	private int unserializeFetchEntityResults(QryFetch fetch) {
		int loaded = 0;
		if (fetch.getResults().getUnloadedSize()>0) {
			int done = 0;
			for (String entity: fetch.getEntities()) {
				done = done + unserializeObjectsNoLock(fetch.getResults().getReferenceListForCollection(entity), this);
			}
			loaded = loaded + done;
		}
		return loaded;
	}
	
	private void fetchChildEntityReferences(QryFetch masterFetch, int loaded, String parentClassName, MdlObjectRefList parentResults, List<String> entities, Object source) {
		if (parentResults.getReferences().size()>0) {
			MdlObjectRefListMap childResults = new MdlObjectRefListMap();
			
			MdlCollection mCol = DbConfig.getInstance().getModel().getCollectionByName(masterFetch.getClassName());
			MdlCollection pCol = DbConfig.getInstance().getModel().getCollectionByName(parentClassName);
			
			for (String entity: entities) {
				MdlCollection cCol = DbConfig.getInstance().getModel().getCollectionByName(entity);
				if ((pCol!=cCol) && (mCol!=cCol)) {
					for (MdlCollectionReference colRef: DbConfig.getInstance().getModel().getCollectionReferences(entity)) {
						if ((colRef.isEntity()) && (colRef.getReference().getName().equals(parentClassName))) {
							for (MdlObjectRef ref: parentResults.getReferences()) {
								List<Long> cIdList = colRef.getChildIdListForParentId(ref.getId().getValue());
								if (cIdList!=null) {
									MdlObjectRefList cRefList = this.getObjectRefListUseIdIndexListNoLock(cIdList, source);
									MdlObjectRefList entityResults = masterFetch.getResults().getReferenceListForCollection(cCol.getName());
									for (MdlObjectRef cRef: cRefList.getReferences()) {
										if (entityResults.getMdlObjectRefById(cRef.getId().getValue())==null) {
											entityResults.getReferences().add(MdlObjectRef.copy(cRef));
											childResults.addReference(cCol.getName(),cRef);
											checkFetchEntityLimit(masterFetch,loaded);
										}
									}
								}
							}
						}
					}
				}
			}

			if (childResults.getCollectionList().size()>0) {
				for (String collection:childResults.getCollectionList()) {
					fetchChildEntityReferences(masterFetch,loaded,collection,childResults.getReferenceListForCollection(collection),entities,source);
				}
			}
		}
	}
	
	private void fetchReferencedEntitiesNoLock(QryFetch masterFetch, int loaded, List<String> entities, Object source) {
		List<Long> idList = masterFetch.getResults().getIdList();
		for (String collection: masterFetch.getResults().getCollectionList()) {
			MdlObjectRefList results = masterFetch.getResults().getReferenceListForCollection(collection);
			List<MdlObjectRef> tempList = new ArrayList<MdlObjectRef>(results.getReferences());
			String className = collection;
			if (collection.equals(QryFetch.MAIN_RESULTS)) {
				className = masterFetch.getClassName();
			}
			for (MdlCollectionReference colRef: DbConfig.getInstance().getModel().getCollectionReferences(className)) {
				for (String entity: entities) {
					MdlObjectRefList saveResults = masterFetch.getResults().getReferenceListForCollection(entity);
					if (colRef.getReference().getName().equals(entity)) {
						for (MdlObjectRef ref: tempList) {
							DtObject refValue = ref.getDataObject().getPropertyValue(colRef.getName());
							if (refValue instanceof DtIdRef) {
								DtIdRef idRef = (DtIdRef) refValue;
								if (idRef.getValue()>0) {
									if (!idList.contains(idRef.getValue())) {
										saveResults.getReferences().add(MdlObjectRef.copy(getObjectRefUseIdIndexNoLock(idRef.getValue(), source)));
										idList.add(idRef.getValue());
									}
								}
							} else if (refValue instanceof DtIdRefList) {
								DtIdRefList idRefList = (DtIdRefList) refValue;
								for (long id: idRefList.getValue()) {
									if (!idList.contains(id)) {
										saveResults.getReferences().add(MdlObjectRef.copy(getObjectRefUseIdIndexNoLock(id, source)));
										idList.add(id);
									}
								}
							}
						}
					}
					checkFetchEntityLimit(masterFetch,loaded);			
				}
			}
		}
	}
	
	private MdlObjectRefList getInitialRefListForFetchNoLock(QryFetch fetch,Object source) {
		// id
		long idValue = 0;
		
		// name
		String nameValue = "";
		
		// uniqueConstraint
		MdlCollectionUniqueConstraint uniqueConstraint = null;
		String keyValue = "";

		// reference
		MdlCollectionReference reference = null;
		long parentIdValue = 0;
		
		String useIndex = "";

		// cross reference
		MdlCollectionReference crossReference = null;
		long crossReferenceParentIdValue = 0;

		List<QryFetchCondition> remainingConditions = new ArrayList<QryFetchCondition>(fetch.getConditions());
		
		if (fetch.getConditions().size()>0) {
			// Check for id or name index
			for (QryFetchCondition c: fetch.getConditions()) {
				if (
					(!c.isInvert()) &&
					(c.getOperator().equals(QryFetchCondition.OPERATOR_EQUALS))
					) {
					if (c.getProperty().equals(MdlObject.PROPERTY_ID)) {
						idValue = ((Long) c.getValue().getValue());
						remainingConditions.remove(c);
						useIndex = "id";
						break;
					} else if (c.getProperty().equals(MdlObject.PROPERTY_NAME)) {
						nameValue = Generic.getSerializableStringValue(((String) c.getValue().getValue()));
						remainingConditions.remove(c);
						useIndex = "name";	
						break;
					}
				}
			}
			
			// Check for uniqueConstraint index
			if (useIndex.length()==0) {
				for (MdlCollectionUniqueConstraint uc: DbConfig.getInstance().getModel().getCollectionUniqueConstraints(fetch.getClassName())) {
					boolean foundAll = true;
					keyValue = "";
					List<QryFetchCondition> removeConditions = new ArrayList<QryFetchCondition>();
					for (MdlCollectionProperty p: uc.getProperties()) {
						boolean foundCondition = false;
						for (QryFetchCondition c: fetch.getConditions()) {
							if ((!c.isInvert()) &&
								(c.getProperty().equals(p.getName())) &&
								(c.getOperator().equals(QryFetchCondition.OPERATOR_EQUALS))
								) {
								foundCondition = true;
								if (keyValue.length()>0) {
									keyValue = keyValue + Generic.SEP_STR;
								}
								keyValue = keyValue + Generic.getSerializableStringValue(c.getValue().toString());
								removeConditions.add(c);
								break;
							}
						}
						if (!foundCondition) {
							foundAll = false;
							break;
						}
					}
					if (foundAll) {
						useIndex = "uniqueConstraint";	
						uniqueConstraint = uc;
						for (QryFetchCondition c: removeConditions) {
							remainingConditions.remove(c);
						}
						break;
					}
				}
			}
			
			// Check for (cross) reference index
			for (QryFetchCondition c: fetch.getConditions()) {
				if (
					(!c.isInvert()) &&
					(c.getOperator().equals(QryFetchCondition.OPERATOR_CONTAINS))
					) {
					MdlCollectionProperty p = DbConfig.getInstance().getModel().getCollectionPropertyByName(fetch.getClassName(),c.getProperty());
					if ((p!=null) && (p instanceof MdlCollectionReference)) {
						if (useIndex.length()==0) {
							reference = (MdlCollectionReference) p;
							parentIdValue = ((Long) c.getValue().getValue());
							remainingConditions.remove(c);
							useIndex = "reference";
						} else {
							crossReference = (MdlCollectionReference) p;
							crossReferenceParentIdValue = ((Long) c.getValue().getValue());
							remainingConditions.remove(c);
							break;
						}
					}
				}
			}
		}

		MdlObjectRefList refList = new MdlObjectRefList();
		
		if (useIndex.equals("id")) {
			fetch.addLogLine("Index: " + fetch.getClassName() + "." + useIndex + ", value: " + idValue);
			if ((idValue>0) && (crossReference!=null)) {
				fetch.addLogLine("Cross reference: " + fetch.getClassName() + "." + crossReference.getName() + ", value: " + crossReferenceParentIdValue + ", result size: 1");
				List<Long> crossRefIdList = crossReference.getChildIdListForParentId(crossReferenceParentIdValue);
				if ((crossRefIdList==null) || (!crossRefIdList.contains(idValue))) {
					idValue = 0;
				}
			}
			if (idValue>0) {
				List<Long> idList = new ArrayList<Long>();
				idList.add(idValue);
				refList = getObjectRefListUseIdIndexListNoLock(idList,source);
			}
		} else if (useIndex.equals("name")) {
			fetch.addLogLine("Index: " + fetch.getClassName() + "." + useIndex + ", value: " + nameValue);
			List<String> nameList = new ArrayList<String>();
			nameList.add(nameValue);
			refList = getObjectRefListUseNameIndexListNoLock(fetch.getClassName(),nameList,this);
			if ((refList.getReferences().size()>0) && (crossReference!=null)) {
				fetch.addLogLine("Cross reference: " + fetch.getClassName() + "." + crossReference.getName() + ", value: " + crossReferenceParentIdValue + ", result size: " + refList.getReferences().size());
				List<Long> crossRefIdList = crossReference.getChildIdListForParentId(crossReferenceParentIdValue);
				if (crossRefIdList!=null) {
					int size = refList.getReferences().size();
					for (int i = 0; i < size; i++) {
						long id = refList.getReferences().get(i).getId().getValue();
						if (!crossRefIdList.contains(id)) {
							refList.getReferences().remove(i);
							size--;
							i--;
						}
					}
				} else {
					refList.getReferences().clear();
				}
			}
		} else if (useIndex.equals("uniqueConstraint")) {
			fetch.addLogLine("Index: " + useIndex + ": " + fetch.getClassName() + "." + uniqueConstraint + ", value: " + keyValue);
			Long id = uniqueConstraint.getIdForKey(keyValue);
			if ((id!=null) && (crossReference!=null)) {
				idValue = id;
				fetch.addLogLine("Cross reference: " + fetch.getClassName() + "." + crossReference.getName() + ", value: " + crossReferenceParentIdValue + ", result size: 1");
				List<Long> crossRefIdList = crossReference.getChildIdListForParentId(crossReferenceParentIdValue);
				if ((crossRefIdList==null) || (!crossRefIdList.contains(idValue))) {
					id = null;
				}
			}
			if (id!=null) {
				List<Long> idList = new ArrayList<Long>();
				idList.add(id);
				refList = getObjectRefListUseIdIndexListNoLock(idList,source);
			}
		} else if (useIndex.equals("reference")) {
			fetch.addLogLine("Index: " + useIndex + ": " + fetch.getClassName() + "." + reference.getName() + ", value: " + parentIdValue);
			List<Long> cIdList = reference.getChildIdListForParentId(parentIdValue);
			if (cIdList!=null) {
				if ((cIdList.size()>0) && (crossReference!=null)) {
					fetch.addLogLine("Cross reference: " + fetch.getClassName() + "." + crossReference.getName() + ", value: " + crossReferenceParentIdValue + ", result size: " + cIdList.size());
					List<Long> crossRefIdList = crossReference.getChildIdListForParentId(crossReferenceParentIdValue);
					cIdList = new ArrayList<Long>(cIdList);
					if (crossRefIdList==null) {
						cIdList.clear();
					} else {
						List<Long> tempList = new ArrayList<Long>(cIdList);
						for (Long id: tempList) {
							if (!crossRefIdList.contains(id)) {
								cIdList.remove(id);
							}
						}
					}
				}
				if (
					(fetch.getOrderBy().length()==0) && 
					(fetch.getLimit()>0) &&
					(remainingConditions.size()==0)
					) {
					int s = fetch.getStart();
					int l = fetch.getLimit();
					refList = getObjectRefListUseIdIndexListNoLock(cIdList,s,l,source);
					fetch.setLimitedInitialResults(true);
					fetch.addLogLine("Limited initial results");
				} else {
					refList = getObjectRefListUseIdIndexListNoLock(cIdList,source);
				}
			}
		} else  {
			fetch.addLogLine("Index: className, value: " + fetch.getClassName());
			if (
				(fetch.getLimit()>0) &&
				(remainingConditions.size()==0)
				) {
				int s = fetch.getStart();
				int l = fetch.getLimit();
				refList = getObjectRefListUseClassNameIndexNoLock(fetch.getClassName(),fetch.getOrderBy(),fetch.isOrderAscending(),s,l,this);
				fetch.setLimitedInitialResults(true);
				fetch.addLogLine("Limited initial results");
			} else {
				refList = getObjectRefListUseClassNameIndexNoLock(fetch.getClassName(),fetch.getOrderBy(),fetch.isOrderAscending(),this);
			}
			int unloaded = refList.getUnloadedRefs().size();
			if (unloaded==0) {
				fetch.setOrderedInitialResults(true);
				fetch.addLogLine("Ordered initial results");
			}
		}
		
		fetch.addLogLine("Initial result size: " + refList.getReferences().size());
		
		if (refList.getReferences().size()>DbConfig.getInstance().getMaxFetchLoad()) {
			int unloaded = refList.getUnloadedRefs().size();
			fetch.addLogLine("Unloaded: " + unloaded);
			
			if (unloaded>DbConfig.getInstance().getMaxFetchLoad()) {
				boolean loadConditionFound = false;
				if (
					(fetch.getOrderBy().length()>0) &&
					(!fetch.getOrderBy().equals(MdlObject.PROPERTY_ID)) &&
					(!fetch.getOrderBy().equals(MdlObject.PROPERTY_NAME)) &&
					(!fetch.getOrderBy().equals(MdlObject.PROPERTY_CLASSNAME)) &&
					(!fetch.getOrderBy().equals(MdlObject.PROPERTY_CREATEDON)) &&
					(!fetch.getOrderBy().equals(MdlObject.PROPERTY_CREATEDBY)) &&
					(!fetch.getOrderBy().equals(MdlObject.PROPERTY_CHANGEDON)) &&
					(!fetch.getOrderBy().equals(MdlObject.PROPERTY_CHANGEDBY))
					) {
					loadConditionFound = true;
				}
				if (!loadConditionFound) {
					for (QryFetchCondition c: remainingConditions) {
						if (
							(!c.getProperty().equals(MdlObject.PROPERTY_ID)) &&
							(!c.getProperty().equals(MdlObject.PROPERTY_NAME)) &&
							(!c.getProperty().equals(MdlObject.PROPERTY_CLASSNAME)) &&
							(!c.getProperty().equals(MdlObject.PROPERTY_CREATEDON)) &&
							(!c.getProperty().equals(MdlObject.PROPERTY_CREATEDBY)) &&
							(!c.getProperty().equals(MdlObject.PROPERTY_CHANGEDON)) &&
							(!c.getProperty().equals(MdlObject.PROPERTY_CHANGEDBY))
							) {
							loadConditionFound = true;
							break;
						}
					}
				}
				if (loadConditionFound) {
					checkFetchRefListLoadLimit(fetch,refList,0);
				}
			}
		}
		
		return filterInitialRefListRemainingConditionsNoLock(fetch,refList,remainingConditions,source);
	}

	private MdlObjectRefList filterInitialRefListRemainingConditionsNoLock(QryFetch fetch,MdlObjectRefList refList, List<QryFetchCondition> remainingConditions,Object source) {
		if ((refList.getReferences().size()>0) && (remainingConditions.size()>0)) {
			List<MdlObjectRef> l = new ArrayList<MdlObjectRef>(refList.getReferences());
			for (QryFetchCondition c: remainingConditions) {
				for (MdlObjectRef ref: l) {
					if (
						(c.getProperty().equals(MdlObject.PROPERTY_ID)) ||
						(c.getProperty().equals(MdlObject.PROPERTY_NAME)) ||
						(c.getProperty().equals(MdlObject.PROPERTY_CLASSNAME)) ||
						(c.getProperty().equals(MdlObject.PROPERTY_CREATEDON)) ||
						(c.getProperty().equals(MdlObject.PROPERTY_CREATEDBY)) ||
						(c.getProperty().equals(MdlObject.PROPERTY_CHANGEDON)) ||
						(c.getProperty().equals(MdlObject.PROPERTY_CHANGEDBY))
						) {
						DtObject value = null;
						if (c.getProperty().equals(MdlObject.PROPERTY_ID)) {
							value = ref.getId();
						} else if (c.getProperty().equals(MdlObject.PROPERTY_NAME)) {
							value = ref.getName();
						} else if (c.getProperty().equals(MdlObject.PROPERTY_CLASSNAME)) {
							value = ref.getClassName();
						} else if (c.getProperty().equals(MdlObject.PROPERTY_CREATEDON)) {
							value = ref.getCreatedOn();
						} else if (c.getProperty().equals(MdlObject.PROPERTY_CREATEDBY)) {
							value = ref.getCreatedBy();
						} else if (c.getProperty().equals(MdlObject.PROPERTY_CHANGEDON)) {
							value = ref.getChangedOn();
						} else if (c.getProperty().equals(MdlObject.PROPERTY_CHANGEDBY)) {
							value = ref.getChangedBy();
						}
						if (!c.valueValidForOperatorValue(value)) {
							refList.getReferences().remove(ref);
						}
					} else {
						if (ref.getDataObject()==null) {
							if (checkUnserializeObjectNoLock(ref,source)) {
								unserializeObjectNoLock(ref,source);
							}
						}
						if (ref.getDataObject()!=null) {
							MdlObject obj = (MdlObject) ref.getDataObject();
							DtObject value = obj.getPropertyValue(c.getProperty());
							if (!c.valueValidForOperatorValue(value)) {
								refList.getReferences().remove(ref);
							}
						} else {
							String msg = "Unable to unserialize object: @1";
							Messenger.getInstance().error(this,msg);
							fetch.addError("0006","",msg,ref.toString());
							refList.getReferences().remove(ref);
						}
					}
				}
				fetch.addLogLine("Filtered condition: " + c + ", result size: " + refList.getReferences().size());
				if (refList.getReferences().size()==0) {
					break;
				}
			}
			
			fetch.addLogLine("Result size after filtering: " + refList.getReferences().size());
		}
		
		return refList;
	}

	private void executeTransactionQueryNoLock(QryTransaction transaction,QryObject qry,Object source) {
		if (qry instanceof QryUpdate) {
			Date dateTime = new Date();
			boolean succes = false;
			QryUpdate qryUpd = (QryUpdate) qry;
			if ((qryUpd.getDataObject()!=null) && (qryUpd.getFetch()!=null)) {
				qryUpd.addLogLine("Executing fetch for update ...");
				executeFetchNoLock(qryUpd.getFetch(), source);
				for (MdlObjectRef ref: qryUpd.getFetch().getMainResults().getReferences()) {
					MdlDataObject obj = ref.getDataObject(); 
					if (obj!=null) {
						qryUpd.getDataObject().getId().setValue(obj.getId().getValue());
						qryUpd.getDataObject().getCreatedBy().setValue(obj.getCreatedBy().getValue());
						qryUpd.getDataObject().getCreatedOn().setValue(obj.getCreatedOn().getValue());
						qryUpd.getDataObject().getChangedBy().setValue(obj.getChangedBy().getValue());
						qryUpd.getDataObject().getChangedOn().setValue(obj.getChangedOn().getValue());
						if (
							(obj.checkObjectConstraintsForUserQuery(transaction.getUser(),qry)) &&
							(checkQueryObjectReferencesNoLock(qry,obj)) &&
							(checkUniqueConstraintsForTransactionQueryObjectNoLock(transaction,qryUpd,qryUpd.getDataObject()))
							) {
							transactionUpdateObjectNoLock(transaction,qryUpd,dateTime,obj,qryUpd.getDataObject(),source);
							succes = true;
							qryUpd.setTime(new Date().getTime() - dateTime.getTime());
							qryUpd.addLogLine("Completed update in: " + qryUpd.getTime() + " ms");
						}
					}
				}
			}
			if (!succes) {
				qryUpd.setTime(new Date().getTime() - dateTime.getTime());
			}
		} else if (qry instanceof QryAdd) {
			Date dateTime = new Date();
			boolean succes = false;
			QryAdd qryAdd = (QryAdd) qry;
			MdlDataObject obj = qryAdd.getDataObject();
			if (obj!=null) {
				if (obj.getId().getValue()!=0) {
					if (getObjectRefUseIdIndexNoLock(obj.getId().getValue(),source)!=null) {
						qryAdd.addError("0107",MdlObject.PROPERTY_ID,"Object already exists with ID: @1","" + obj.getId());
					}
				}
				if (qry.getErrors().size()==0) {
					obj.getCreatedOn().setValue(dateTime);
					obj.getCreatedBy().setValue(transaction.getUser());
					obj.getChangedOn().setValue(dateTime);
					obj.getChangedBy().setValue(transaction.getUser());
					if (
						(obj.checkObjectConstraintsForUserQuery(transaction.getUser(),qry)) &&
						(checkQueryObjectReferencesNoLock(qry,obj)) &&
						(checkUniqueConstraintsForTransactionQueryObjectNoLock(transaction,qryAdd,obj))
						) {
						// Add a copy to ensure further changes are not made available in the object in memory
						obj = MdlDataObject.copy(obj);
						qryAdd.addLogLine("Adding " + obj.getClassName().getValue() + " object ...");
						addObjectRefToIndexNoLock(new MdlObjectRef(obj), true, source);
						transaction.addAddedId(obj.getId().getValue());
						qryAdd.addLogLine("Added object: " + obj.getId().getValue());
						addedObjectNoLock(obj,source);
						// Update ID in original for references in further usage
						qryAdd.getDataObject().getId().setValue(obj.getId().getValue());
						succes = true;
						qryAdd.setTime(new Date().getTime() - dateTime.getTime());
						qryAdd.addLogLine("Completed add in: " + qryAdd.getTime() + " ms");
					}
				}
			}
			if (!succes) {
				qryAdd.setTime(new Date().getTime() - dateTime.getTime());
			}
		} else if (qry instanceof QryRemove) {
			Date dateTime = new Date();
			boolean succes = false;
			QryRemove qryRem = (QryRemove) qry;
			if (qryRem.getFetch()!=null) {
				qryRem.addLogLine("Executing fetch for remove ...");
				executeFetchNoLock(qryRem.getFetch(), source);
				for (MdlObjectRef ref: qryRem.getFetch().getMainResults().getReferences()) {
					MdlObjectRefList removeList = new MdlObjectRefList();
					MdlObjectRefList updateList = new MdlObjectRefList();
					
					boolean ok = true;

					QryError limitErr = getRemoveObjectReferencesNoLock(removeList,ref,source);
					qryRem.addLogLine("Remove object reference list:" + removeList.getReferences().size());
					if (limitErr==null) {
						limitErr = getUpdateObjectReferencesFromRemoveObjectReferencesNoLock(updateList,removeList,source);
						qryRem.addLogLine("Update object reference list:" + updateList.getReferences().size());
					}
					if (limitErr!=null) {
						qryRem.addError(limitErr.getCode(),"",limitErr.getMessage());
						ok = false;
					}
					if (ok) {
						for (MdlObjectRef refUpd: updateList.getReferences()) {
							MdlDataObject original = getObjectRefUseIdIndexNoLock(refUpd.getId().getValue(), source).getDataObject();
							if (original!=null) {
								QryUpdate qryUpd = new QryUpdate(refUpd.getDataObject());
								if (
									(!refUpd.getDataObject().checkObjectConstraintsForUserQuery(transaction.getUser(),qryUpd))
									) {
									MdlCollection col = DbConfig.getInstance().getModel().getCollectionByName(refUpd.getDataObject().getClassName().getValue());
									qryRem.addError("0301","","Unable to remove object due to referential constraints: @1",col.getNameMulti());
									// Copy update error details to remove log
									QryError error = qryUpd.getErrors().get(0);
									String err = "";
									if (error.getProperties().size()>0) {
										for (String prop: error.getProperties()) {
											if (err.length()>0) {
												err = err + ", ";
											}
											err = err + prop;
										}
										err = err + ": " + error.toString();
									} else {
										err = error.toString();
									}
									qryRem.addLogLine(refUpd.getDataObject().getClassName().getValue() + ": " + err);
									ok = false;
									break;
								}
							}
						}
					}
					if (ok) {
						int loaded = unserializeObjectsNoLock(removeList, source);
						if (loaded > 0) {
							qryRem.addLogLine("Loaded objects for remove: " + loaded);
						}
						for (MdlObjectRef refRem: removeList.getReferences()) {
							QryRemove qrySubRem = new QryRemove(refRem.getDataObject());
							if (
								(!refRem.getDataObject().checkObjectConstraintsForUserRemoveQuery(transaction.getUser(),qrySubRem))
								) {
								MdlCollection col = DbConfig.getInstance().getModel().getCollectionByName(refRem.getDataObject().getClassName().getValue());
								qryRem.addError("0302","","Unable to remove object due to (referential) removal constraints: @1",col.getNameMulti());
								if (qrySubRem.getErrors().size()>0) {
									// Copy update error details to remove log
									QryError error = qrySubRem.getErrors().get(0);
									String err = "";
									if (error.getProperties().size()>0) {
										for (String prop: error.getProperties()) {
											if (err.length()>0) {
												err = err + ", ";
											}
											err = err + prop;
										}
										err = err + ": " + error.toString();
									} else {
										err = error.toString();
									}
									qryRem.addLogLine(refRem.getDataObject().getClassName().getValue() + ": " + err);
								}
								ok = false;
								break;
							}
						}
					}
					if (ok) {
						boolean errors = false;
						for (MdlObjectRef refUpd: updateList.getReferences()) {
							MdlDataObject original = getObjectRefUseIdIndexNoLock(refUpd.getId().getValue(), source).getDataObject();
							if (original!=null) {
								QryUpdate qryUpd = new QryUpdate(refUpd.getDataObject());
								if (checkUniqueConstraintsForTransactionQueryObjectNoLock(transaction,qryUpd,refUpd.getDataObject())) {
									transactionUpdateObjectNoLock(transaction,qryRem,dateTime,original,refUpd.getDataObject(),source);
								} else {
									MdlCollection col = DbConfig.getInstance().getModel().getCollectionByName(qryUpd.getDataObject().getClassName().getValue());
									qryRem.addError("0303","","Unable to remove object due to unique constraints: @1",col.getNameMulti());
									// Copy remove error details to remove log
									QryError error = qryUpd.getErrors().get(0);
									String err = "";
									if (error.getProperties().size()>0) {
										for (String prop: error.getProperties()) {
											if (err.length()>0) {
												err = err + ", ";
											}
											err = err + prop;
										}
										err = err + ": " + error.toString();
									} else {
										err = error.toString();
									}
									qryRem.addLogLine(qryUpd.getDataObject().getClassName().getValue() + ": " + err);
									errors = true;
									break;
								}
							}
						}
						if (!errors) {
							for (MdlObjectRef refRem: removeList.getReferences()) {
								MdlObjectRef idxRef = idObjectMap.get(refRem.getId().getValue());								
								removeObjectRefFromIndexNoLock(refRem, source);
								transaction.addRemovedId(refRem.getId().getValue());
								qryRem.addLogLine("Removed " + refRem.getClassName().getValue() + " object: " + refRem.getId().getValue());
								removedObjectNoLock(idxRef,source);
							}
							succes = true;
							qryRem.setTime(new Date().getTime() - dateTime.getTime());
							qryRem.addLogLine("Completed remove in: " + qryRem.getTime() + " ms");
						}
					}
				}
			}
			if (!succes) {
				qryRem.setTime(new Date().getTime() - dateTime.getTime());
			}
		}
	}

	private void transactionUpdateObjectNoLock(QryTransaction t, QryObject q, Date dateTime, MdlDataObject original, MdlDataObject changed, Object source) {
		q.addLogLine("Updating " + original.getClassName().getValue() + " object: " + original.getId());
		MdlDataObject copy = MdlDataObject.copy(original);
		for (MdlCollectionProperty prop: DbConfig.getInstance().getModel().getCollectionProperties(original.getClassName().getValue())) {
			if (
				(!prop.getName().equals(MdlObject.PROPERTY_CLASSNAME)) &&
				(!prop.getName().equals(MdlObject.PROPERTY_ID)) &&
				(!prop.getName().equals(MdlObject.PROPERTY_CREATEDON)) &&
				(!prop.getName().equals(MdlObject.PROPERTY_CREATEDBY)) &&
				(!prop.getName().equals(MdlObject.PROPERTY_CHANGEDON)) &&
				(!prop.getName().equals(MdlObject.PROPERTY_CHANGEDBY))
				) {
				DtObject value = changed.getPropertyValue(prop.getName());
				copy.setPropertyValue(prop.getName(), DtObject.copy(value));
			}
		}
		copy.getChangedOn().setValue(dateTime);
		copy.getChangedBy().setValue(t.getUser());
		q.addLogLine("Updating index ...");
		updateObjectInIndexNoLock(original,copy,source);
		t.addUpdatedId(original.getId().getValue());
		q.addLogLine("Updated object: " + original.getId().getValue());
		updatedObjectNoLock(original,copy,source);
	}
	
	private boolean checkUniqueConstraintsForTransactionQueryObjectNoLock(QryTransaction t, QryObject q, MdlDataObject obj) {
		boolean ok = true;
		for (MdlCollectionUniqueConstraint uc: DbConfig.getInstance().getModel().getCollectionUniqueConstraints(obj.getClassName().getValue())) {
			String key = uc.getIndexKeyForObject(obj);
			Long id = uc.getIdForKey(key);
			if (id!=null) {
				MdlObjectRef ref = this.idObjectMap.get(obj.getId().getValue());
				if ((ref!=null) && (ref.getId().getValue().equals(id))) {
					id = null;
				}
			}
			if (id!=null) {
				ok = false;
				QryError error = q.addError("0201","Unique constraint violation");
				for (MdlCollectionProperty m: uc.getProperties()) {
					error.getProperties().add(m.getName());
				}
			}
		}
		return ok;
	}
	
	private QryError getRemoveObjectReferencesNoLock(MdlObjectRefList removeList, MdlObjectRef parent, Object source) {
		QryError err = null;
		if (removeList.getReferences().size()>=DbConfig.MAX_DB_LOAD) {
			err = new QryError("0401","","Remove impact too large");
			return err;
		}
		if (removeList.getMdlObjectRefById(parent.getId().getValue())==null) {
			removeList.getReferences().add(0,parent);
			for (MdlCollectionReference r: DbConfig.getInstance().getModel().getCollectionReferencesByReferenceClass(parent.getClassName().getValue())) {
				if (r.isRemoveMe()) {
					List<Long> cIdList = r.getChildIdListForParentId(parent.getId().getValue());
					if (cIdList!=null) {
						MdlObjectRefList cRefs = getObjectRefListUseIdIndexListNoLock(cIdList, source);
						for (MdlObjectRef refC: cRefs.getReferences()) {
							err = getRemoveObjectReferencesNoLock(removeList,refC,source);
							if (err!=null) {
								return err;
							}
						}
					}
				}
			}
		}
		return err;
	}
	
	private QryError getUpdateObjectReferencesFromRemoveObjectReferencesNoLock(MdlObjectRefList updateList, MdlObjectRefList removeList, Object source) {
		QryError err = null;
		
		int removeListSize = removeList.getReferences().size();
		
		for (MdlObjectRef ref: removeList.getReferences()) {
			MdlDataObject obj = ref.getDataObject();
			if (obj!=null) {
				for (MdlCollectionReference r: DbConfig.getInstance().getModel().getCollectionReferencesByReferenceClass(obj.getClassName().getValue())) {
					if (!r.isRemoveMe()) {
						List<Long> cIdList = r.getChildIdListForParentId(obj.getId().getValue());
						if (cIdList!=null) {
							MdlObjectRefList cRefs = getObjectRefListUseIdIndexListNoLock(cIdList, source);
							unserializeObjectsNoLock(cRefs, source);
							for (MdlObjectRef refC: cRefs.getReferences()) {
								if ((removeListSize + updateList.getReferences().size())>=DbConfig.MAX_DB_LOAD) {
									err = new QryError("0401","","Remove impact too large");
									return err;
								}
								MdlDataObject child = MdlDataObject.copy(refC.getDataObject());

								MdlObjectRef refUpd = updateList.getMdlObjectRefById(child.getId().getValue());
								if (refUpd==null) {
									refUpd = new MdlObjectRef(child);
									updateList.getReferences().add(0,refUpd);
								}
	
								DtObject value = DtObject.copy(refUpd.getDataObject().getPropertyValue(r.getName()));
								if (value instanceof DtIdRef) {
									value.setValue(new Long(0));
								} else if (value instanceof DtIdRefList) {
									List<Long> v = ((DtIdRefList) value).getValue();
									v.remove(obj.getId().getValue());
								}
								refUpd.getDataObject().setPropertyValue(r.getName(), value);
							}
						}
					}
				}
			}
		}
		
		return err;
	}

	private boolean checkQueryObjectReferencesNoLock(QryObject query, MdlDataObject obj) {
		boolean ok = true;
		if (checkReferences) {
			for (MdlCollectionReference reference: DbConfig.getInstance().getModel().getCollectionReferences(obj.getClass().getName())) {
				if (
					(!reference.getName().equals(MdlObject.PROPERTY_CREATEDBY)) && 
					(!reference.getName().equals(MdlObject.PROPERTY_CHANGEDBY)) 
					) {
					DtObject value = obj.getPropertyValue(reference.getName());
					if (value instanceof DtIdRef) {
						DtIdRef val = (DtIdRef) value;
						// Exclude admin user reference
						if (val.getValue()>1) {
							if (!reference.getReference().getIdRefMap().containsKey(val.getValue())) {
								query.addError("0501",reference.getName(),"Reference not found in referenced collection: @1",obj.getClassName().getValue() + "." + reference.getName() + ":" + val.getValue());
								ok = false;
							}
						} else if ((val.getValue()==1) && (!reference.getReference().getName().equals(DbUser.class.getName()))) {
							query.addError("0501",reference.getName(),"Reference not found in referenced collection: @1",obj.getClassName().getValue() + "." + reference.getName() + ":" + val.getValue());
							ok = false;
						}
					} else if (value instanceof DtIdRefList) {
						DtIdRefList vals = (DtIdRefList) value;
						for (long val: vals.getValue()) {
							// Exclude admin user reference
							if (val>1) {
								if (!reference.getReference().getIdRefMap().containsKey(val)) {
									query.addError("0501",reference.getName(),"Reference not found in referenced collection: @1",obj.getClassName().getValue() + "." + reference.getName() + ":" + val);
									ok = false;
									break;
								}
							} else if ((val==1) && (!reference.getReference().getName().equals(DbUser.class.getName()))) {
								query.addError("0501",reference.getName(),"Reference not found in referenced collection: @1",obj.getClassName().getValue() + "." + reference.getName() + ":" + val);
								ok = false;
								break;
							}
						}
					}
					if (!ok) {
						break;
					}
				}
			}
		}
		return ok;
	}
}	
