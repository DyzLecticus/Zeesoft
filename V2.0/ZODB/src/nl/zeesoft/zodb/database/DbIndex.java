package nl.zeesoft.zodb.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.cache.CcCacheWorker;
import nl.zeesoft.zodb.database.gui.GuiController;
import nl.zeesoft.zodb.database.index.IdxClass;
import nl.zeesoft.zodb.database.index.IdxLink;
import nl.zeesoft.zodb.database.index.IdxNumber;
import nl.zeesoft.zodb.database.index.IdxObject;
import nl.zeesoft.zodb.database.index.IdxString;
import nl.zeesoft.zodb.database.index.IdxUniqueConstraint;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.model.MdlLink;
import nl.zeesoft.zodb.database.model.MdlModel;
import nl.zeesoft.zodb.database.model.MdlNumber;
import nl.zeesoft.zodb.database.model.MdlObject;
import nl.zeesoft.zodb.database.model.MdlProperty;
import nl.zeesoft.zodb.database.model.MdlString;
import nl.zeesoft.zodb.database.model.MdlUniqueConstraint;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.database.request.ReqError;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.database.request.ReqGetFilter;
import nl.zeesoft.zodb.database.request.ReqObject;
import nl.zeesoft.zodb.database.request.ReqObjectChange;
import nl.zeesoft.zodb.database.request.ReqObjectGetAndChange;
import nl.zeesoft.zodb.database.request.ReqRemove;
import nl.zeesoft.zodb.database.request.ReqUpdate;

/**
 * This class is the heart of the database.
 * 
 * It ensures all sub indexes (on properties and unique constraints) contain a correct and up to date representation of the data.
 * It takes requests from the database request queue and instantiates workers to handle those requests. 
 */
public final class DbIndex {
	public static final int	 							BLOCK_SIZE					= 100;

	private static final int							MAX_READERS_PER_REQUEST		= 5;
	private static final int							MAX_WRITERS_PER_CLASS		= 5;
	
	private static DbIndex								index						= null;
	
	private SortedMap<String,IdxObject>					indexes						= new TreeMap<String,IdxObject>();
	
	private List<DbDataObjectWriteWorker>				dataObjectWriteWorkers		= new ArrayList<DbDataObjectWriteWorker>();
	
	private List<DbRequestWorker>						requestWorkers				= new ArrayList<DbRequestWorker>();
	
	private SortedMap<String,Object>					lockedClassNameSourceMap	= new TreeMap<String,Object>();
	
	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}

	public static DbIndex getInstance() {
		if (index==null) {
			index = new DbIndex();
			index.initialize();
			DbConfig.getInstance().getCache(); // Trigger cache initialization
		}
		return index;
	}

	public void debug(Object source) {
		Messenger.getInstance().debug(this,"======== INDEX DEBUG BEGIN ========");
		lockClasses(DbConfig.getInstance().getModel().getClassFullNameList(),source);
		for (Entry<String,IdxObject> entry: indexes.entrySet()) {
			if (entry.getValue() instanceof IdxClass) {
				IdxClass idx = (IdxClass) entry.getValue();
				Messenger.getInstance().debug(this,"Class:  " + idx.getCls().getFullName() + ", size: " + idx.getNewIdObjectMap().size());
			} else if (entry.getValue() instanceof IdxString) {
				IdxString idx = (IdxString) entry.getValue();
				Messenger.getInstance().debug(this,"String: " + idx.getString().getFullName() + ", size: " + idx.getSize() + ", values: " + idx.getSizeValues());
			} else if (entry.getValue() instanceof IdxNumber) {
				IdxNumber idx = (IdxNumber) entry.getValue();
				Messenger.getInstance().debug(this,"Number: " + idx.getNumber().getFullName() + ", size: " + idx.getSize());
			} else if (entry.getValue() instanceof IdxLink) {
				IdxLink idx = (IdxLink) entry.getValue();
				Messenger.getInstance().debug(this,"Link:   " + idx.getLink().getFullName() + ", size: " + idx.getSize());
			} else if (entry.getValue() instanceof IdxUniqueConstraint) {
				IdxUniqueConstraint idx = (IdxUniqueConstraint) entry.getValue();
				Messenger.getInstance().debug(this,"Unique: " + idx.getUniqueConstraint().getFullName() + ", size: " + idx.getSize());
			}
		}
		unlockClasses(DbConfig.getInstance().getModel().getClassFullNameList(),source);
		Messenger.getInstance().debug(this,"======== INDEX DEBUG END ========");
	}

	/**************************** PROTECTED METHODS **************************/

	protected void unserialize(Object source) {
		initialize();
		
		int todo = 0;
		if (DbConfig.getInstance().isShowGUI()) {
			for (Entry<String,IdxObject> entry: indexes.entrySet()) {
				todo = todo + Generic.getDirNumberedFileNames(entry.getValue().getDirName()).size();
			}
		}
		todo = todo + 2;
		GuiController.getInstance().setProgressFrameTodo(todo);
		
		lockClasses(DbConfig.getInstance().getModel().getClassFullNameList(),source);
		List<DbIndexReadWorker> readWorkers = new ArrayList<DbIndexReadWorker>();
		for (Entry<String,IdxObject> entry: indexes.entrySet()) {
			DbIndexReadWorker worker = new DbIndexReadWorker(entry.getValue());
        	worker.start();
        	readWorkers.add(worker);
        }
        if (readWorkers.size()>0) {
        	Date start = null;
        	if (DbConfig.getInstance().isDebugPerformance()) {
    			start = new Date();
    		}
	        boolean interrupted = false;
	        for (DbIndexReadWorker worker: readWorkers) {
	        	while (!worker.isDone()) {
        			GuiController.getInstance().refreshProgressFrame();
	        		try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						interrupted = true;
						Messenger.getInstance().error(this, "A read worker was interrupted");
					}
	        	}
	        }
	        if (!interrupted) {
	        	if (DbConfig.getInstance().isDebugPerformance()) {
	        		Messenger.getInstance().debug(this, "Reading index files took: " + (new Date().getTime() - start.getTime()) + " ms");
	        	}
	        }
        }
        GuiController.getInstance().incrementProgressFrameDone();
        unlockClasses(DbConfig.getInstance().getModel().getClassFullNameList(),source);
    }

	protected void startWorkers(boolean showProgress) {
		Messenger.getInstance().debug(this, "Starting index workers ...");
		DbIndexWriteWorker.getInstance().start();
		for (DbDataObjectWriteWorker worker: dataObjectWriteWorkers) {
			worker.start();
		}
		CcCacheWorker.getInstance().start();
		DbRequestQueueWorker.getInstance().start();
		if (showProgress) {
	        GuiController.getInstance().incrementProgressFrameDone();
		}
		Messenger.getInstance().debug(this, "Started index workers");
	}

	protected boolean stopWorkers(boolean showProgress) {
		Messenger.getInstance().debug(this, "Stopping index workers ...");
		boolean stopped = false;
		DbRequestQueueWorker.getInstance().stop();
		DbRequestQueue.getInstance().clearQueue(this);
		if (requestWorkers.size()>0) {
			if (showProgress) {
				GuiController.getInstance().setProgressFrameTitle("Finishing requests ...");
			}
			Messenger.getInstance().debug(this,"Waiting for request workers to finish ...");
			while (requestWorkers.size()>0) {
				List<DbRequestWorker> testWorkers = new ArrayList<DbRequestWorker>(requestWorkers);
				for (DbRequestWorker worker: testWorkers) {
					if (worker.isFinalized()) {
						requestWorkers.remove(worker);
						worker.cleanUp();
					}
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// Ignore
				}
			} 
			Messenger.getInstance().debug(this,"All request workers have finished");
		}
		if (showProgress) {
			GuiController.getInstance().setProgressFrameTitle("Stopping database ...");
		}
		int todo = 5;
		for (DbDataObjectWriteWorker worker: dataObjectWriteWorkers) {
			worker.stop();
			todo += worker.getWriteFiles().size();
		}
		DbIndexWriteWorker.getInstance().stop();
		todo += DbIndexWriteWorker.getInstance().getWriteFiles().size();
		if (showProgress) {
			GuiController.getInstance().setProgressFrameTodo(todo);
		}
		for (DbDataObjectWriteWorker worker: dataObjectWriteWorkers) {
			worker.writeFiles(showProgress);
		}
		DbIndexWriteWorker.getInstance().writeFiles(showProgress);

		CcCacheWorker.getInstance().stop();
		if (showProgress) {
			GuiController.getInstance().incrementProgressFrameDone();
		}
		
		stopped = true;
		Messenger.getInstance().debug(this, "Stopped index workers");
		return stopped;
	}
	
	protected void restart(Object source) {
		if (!DbIndexWriteWorker.getInstance().isWorking()) {
			unserialize(source);
			DbConfig.getInstance().getCache().reinitialize(source);
			startWorkers(false);
		}
	}
	
	protected void processNextRquestsFromQueue(Object source) {
		int activeWorkers = 0;
		if (requestWorkers.size()>0) {
			List<DbRequestWorker> testWorkers = new ArrayList<DbRequestWorker>(requestWorkers);
			for (DbRequestWorker worker: testWorkers) {
				if (!worker.isDone()) {
					activeWorkers++;
				} else if (worker.isFinalized()) {
					requestWorkers.remove(worker);
					ReqObject nextReq = worker.getNextReq();
					worker.cleanUp();
					if (nextReq!=null) {
						worker = new DbRequestWorker(nextReq);
						requestWorkers.add(worker);
						worker.start();
						activeWorkers++;
					}
				}
			}
		}
		if (activeWorkers<DbConfig.getInstance().getMaxRequestWorkers()) {
			ReqObject req = DbRequestQueue.getInstance().getRequest(0,source);
			if (req!=null) {
				DbRequestWorker worker = null;
				if (req instanceof ReqObjectGetAndChange) {
					ReqObjectGetAndChange r = (ReqObjectGetAndChange) req;
					worker = new DbRequestWorker(r.getGet());
					worker.setNextReq(r);
				} else {
					worker = new DbRequestWorker(req);
				}
				requestWorkers.add(worker);
				DbRequestQueue.getInstance().removeRequest(req, source);
				worker.start();
			}
		}
	}

	protected SortedMap<String,StringBuilder> getChangedIndexFiles(int maxFiles,Object source) {
		SortedMap<String,StringBuilder> changedFiles = new TreeMap<String,StringBuilder>();
		if (maxFiles < 1) {
			maxFiles = 1;
		}
		lockClasses(DbConfig.getInstance().getModel().getClassFullNameList(),source);
		int added = 0;
		SortedMap<Integer, StringBuilder> changedIndexFiles = null;
		for (Entry<String,IdxObject> entry: indexes.entrySet()) {
			changedIndexFiles = entry.getValue().getChangedFiles(1);
			if (changedIndexFiles.size()>0) {
				Integer firstKey = changedIndexFiles.firstKey();
				String fileName = entry.getValue().getDirName() + firstKey;
				changedFiles.put(fileName,changedIndexFiles.get(firstKey));
				added++;
				if (added>=maxFiles) {
					break;
				}
			}
		}
		unlockClasses(DbConfig.getInstance().getModel().getClassFullNameList(),source);
		return changedFiles;
	}
	
	protected int getTodoForRequest(ReqObject req, Object source) {
		int todo = 0;
		lockClasses(req.getClassNames(),source);
		if (req instanceof ReqGet) {
			ReqGet r = (ReqGet) req;
			setInitialIdListInGetRequest(r,source);
			if (!r.finishedGetIdListRequest()) {
				r.applyStartLimitToInitialIdList();
				todo = r.getInitialIdList().size();
			} else {
				getChildIndexIdListForGetRequestObjects(r,source);
			}
		} else if (req instanceof ReqAdd) {
			if (checkObjectLinkValues(req,source) && 
				checkObjectUniqueIndexes(req,source) &&
				checkObjectIds(req,source)
				) {
				todo = req.getObjects().size();
			}
		} else if (req instanceof ReqObjectGetAndChange) {
			boolean lockObjects = true;
			if (req instanceof ReqUpdate) {
				if (!checkObjectLinkValues(req,source)) { 
					lockObjects = false;
				}
			}
			if (req instanceof ReqRemove) {
				for (ReqDataObject object:req.getObjects()) {
					if (object.getChildIndexIdList().size()>0) {
						StringBuilder children = new StringBuilder();
						boolean first = true;
						for (Entry<String,List<Long>> entry: object.getChildIndexIdList().entrySet()) {
							if (!first) {
								children.append(", ");
							}
							first = false;
							children.append(entry.getKey());
						}
						req.addObjectError(object,ReqObject.ERROR_CODE_UNABLE_TO_REMOVE_OBJECT,"Unable to remove object: " + object.getDataObject().getId() + ", children: " + children);
						lockObjects = false;
					}
				}
			}
			if (lockObjects) {
				IdxClass classIndex = (IdxClass) indexes.get(req.getClassName());
				List<Long> idList = new ArrayList<Long>();
				List<DbDataObject> changedObjectList = new ArrayList<DbDataObject>();
				for (ReqDataObject object: req.getObjects()) {
					idList.add(object.getDataObject().getId());
					if (req instanceof ReqUpdate) {
						changedObjectList.add(object.getDataObject());
					}
				}
				if (!classIndex.lockObjectIds(idList, changedObjectList, source)) {
					req.addError(ReqObject.ERROR_CODE_UNABLE_TO_LOCK_OBJECTS,"Unable to lock objects for request");
				} else {
					todo = req.getObjects().size();
				}
			}
		}
		unlockClasses(req.getClassNames(),source);
		return todo;
	}

	protected int getNextDoneForRequest(ReqObject req, int doneSoFar, Object source) {
		int done = doneSoFar;
		lockClasses(req.getClassNames(),source);
		if (req instanceof ReqGet) {
			ReqGet r = (ReqGet) req;
			List<Long> unserializeIdList = new ArrayList<Long>();
			IdxClass classIndex = (IdxClass) indexes.get(req.getClassName());
			for (int i = doneSoFar; i < (doneSoFar + (MAX_READERS_PER_REQUEST * 10)); i++) {
				if (i < r.getInitialIdList().size()) {
					long id = r.getInitialIdList().get(i);
					if (classIndex.idObjectMapContainsId(id)) {
						unserializeIdList.add(id);
					} else {
						// Object was removed while get request was executing
						done++;
					}
				} else {
					break;
				}
			}
			if (unserializeIdList.size()>0) {
				SortedMap<Long,DbDataObject> bufferedObjs = getBufferedDataObjectsForRequest(r, unserializeIdList, source);
				unserializeDataObjectsForRequest(r,unserializeIdList,bufferedObjs,source);
				done = done + unserializeIdList.size();
				MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(req.getClassName());
				for (int i = (req.getObjects().size() - unserializeIdList.size()); i < req.getObjects().size(); i++) {
					ReqDataObject object = req.getObjects().get(i);
					getChildIndexIdListForGetRequestObject(r,object,cls,source);
				}
			}
		} else if (req instanceof ReqObjectChange) {
			if (done<req.getObjects().size()) {
				ReqDataObject object = req.getObjects().get(done);
				MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(req.getClassName());
				IdxClass classIndex = (IdxClass) indexes.get(req.getClassName());
				boolean impacted = false;
				if (req instanceof ReqAdd) {
					if (checkRequestDataObjectLinkValues(req, object, cls, source) &&
						checkRequestDataObjectUniqueIndexes(req, object, cls, source) &&
						checkRequestDataObjectId(req, object, classIndex, source)
						) {
						addDataObjectToIndexes(req.getClassName(),object.getDataObject(),source);
						//req.addLogLine("Added object " + object.getDataObject().getId() + " to indexes");
						impacted = true;
					}
				} else if (req instanceof ReqUpdate) {
					if (checkRequestDataObjectLinkValues(req, object, cls, source) &&
						checkRequestDataObjectUniqueIndexes(req, object, cls, source)
						) {
						ReqUpdate r = (ReqUpdate) req;
						DbDataObject objectBefore = r.getGet().getObjects().get(done).getDataObject();
						if (updateDataObjectInIndexes(req.getClassName(), objectBefore, object.getDataObject(), source)) {
							//req.addLogLine("Updated object " + object.getDataObject().getId() + " in indexes");
							impacted = true;
						}
					} else {
						classIndex.unlockObjectId(object.getDataObject().getId(),false,false,object.getDataObject(),source);
					} 
				} else if (req instanceof ReqRemove) {
					if (checkRequestDataObjectChildren(req, object, cls, source)) {
						removeDataObjectFromIndexes(req.getClassName(), object.getDataObject(),source);
						//req.addLogLine("Removed object " + object.getDataObject().getId() + " from indexes");
						impacted = true;
					} else {
						classIndex.unlockObjectId(object.getDataObject().getId(),false,false,object.getDataObject(),source);
					}
				}
				if (impacted) {
					((ReqObjectChange)req).getImpactedIds().add(object.getDataObject().getId());
				}
				done++;
			}
		}
		unlockClasses(req.getClassNames(),source);
		return done;
	}
	
	/**************************** PRIVATE METHODS **************************/
	private synchronized void lockClasses(List<String> classNames,Object source) {
		int attempt = 0;
		while (classesAreLocked(classNames)) {
		    try {
                wait();
            } catch (InterruptedException e) { 
            	// Ignore
            }
            attempt ++;
			if (attempt>=10000) {
				Messenger.getInstance().warn(this,Locker.getLockFailedMessage(attempt,source));
				attempt = 0;
			}
		}
		for (String className: classNames) {
			lockedClassNameSourceMap.put(className, source);
		}
	}

	private synchronized void unlockClasses(List<String> classNames,Object source) {
		if (classesAreLockedBy(classNames,source)) {
			for (String className: classNames) {
				lockedClassNameSourceMap.remove(className);
			}
			notifyAll();
		} else {
			Messenger.getInstance().error(this,"Invalid attempted to unlock classes by source: " + source);
		}
	}
	
	private synchronized boolean classesAreLocked(List<String> classNames) {
		boolean locked = false;
		for (String className: classNames) {
			if (lockedClassNameSourceMap.containsKey(className)) {
				locked = true;
				break;
			}
		}
		return locked;
	}

	private synchronized boolean classesAreLockedBy(List<String> classNames,Object source) {
		boolean lockedBy = true;
		for (String className: classNames) {
			Object lockSrc = lockedClassNameSourceMap.get(className);
			if (lockSrc==null || lockSrc!=source) {
				lockedBy = false;
				break;
			}
		}
		return lockedBy;
	}

	private void getChildIndexIdListForGetRequestObjects(ReqGet req, Object source) {
		MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(req.getClassName());
		if (req.getChildIndexes().size()>0 && req.getObjects().size()>0) {
			for (ReqDataObject object: req.getObjects()) {
				getChildIndexIdListForGetRequestObject(req,object,cls,source);
			}
		}
	}
	
	private void getChildIndexIdListForGetRequestObject(ReqGet req, ReqDataObject object, MdlClass cls, Object source) {
		if (object.getChildIndexIdList().size()==0) {
			for (MdlLink cLink: cls.getChildLinks()) {
				if (req.getChildIndexes().contains(cLink.getFullName())) {
					IdxLink linkIndex = (IdxLink) indexes.get(cLink.getFullName());
					List<Long> cIdList = linkIndex.getChildIdListForParentId(object.getDataObject().getId());
					for (Long cId: cIdList) {
						List<Long> childIndexIdList = object.getChildIndexIdList().get(cLink.getFullName());
						if (childIndexIdList == null) {
							childIndexIdList = new ArrayList<Long>();
							object.getChildIndexIdList().put(cLink.getFullName(),childIndexIdList);
						}
						childIndexIdList.add(cId);
					}
				}
			}
		}
	}

	private SortedMap<Long,DbDataObject> getBufferedDataObjectsForRequest(ReqGet req,List<Long> idList,Object source) {
		SortedMap<Long,DbDataObject> bufferedObjects = new TreeMap<Long,DbDataObject>();
		if (idList.size()>0) {
			IdxClass classIndex = (IdxClass) indexes.get(req.getClassName());
			for (long id: idList) {
				DbDataObject bufferedObject = classIndex.getChangedDataObject(id,source);
				if (bufferedObject==null) {
					bufferedObject = DbConfig.getInstance().getCache().getClassObjectById(req.getClassName(),id,source);
				}
				if (bufferedObject!=null) {
					bufferedObjects.put(id,bufferedObject.copy(null));
				}
			}
		}
		return bufferedObjects;
	}

	private void unserializeDataObjectsForRequest(ReqGet req,List<Long> idList,SortedMap<Long,DbDataObject> bufferedObjs,Object source) {
		if (idList.size()>0) {
			List<DbDataObjectReadWorker> workers = new ArrayList<DbDataObjectReadWorker>();
			int read = (idList.size() - bufferedObjs.size());
			if (read>0) {
				for (int i = 0; i < MAX_READERS_PER_REQUEST; i++) {
					DbDataObjectReadWorker worker = new DbDataObjectReadWorker(req.getClassName());
					workers.add(worker);
				}
				int workNum = 0;
				for (long id: idList) {
					if (!bufferedObjs.containsKey(id)) {
						DbDataObjectReadWorker worker = workers.get(workNum);
						worker.getIdList().add(id);
						workNum++;
						if (workNum>=MAX_READERS_PER_REQUEST) {
							workNum=0;
						}
					}
				}
				Date start = null;
	        	if (DbConfig.getInstance().isDebugPerformance()) {
	        		start = new Date();
	        	}
				for (DbDataObjectReadWorker worker: workers) {
					worker.start();
				}
		        boolean interrupted = false;
		        for (DbDataObjectReadWorker worker: workers) {
		        	while (!worker.isDone()) {
		        		try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							interrupted = true;
							Messenger.getInstance().error(this, "An object read worker was interrupted");
						}
		        	}
		        }
		        if (!interrupted) {
		        	if (DbConfig.getInstance().isDebugPerformance()) {
		        		Messenger.getInstance().debug(this, "Reading " + read + " " + req.getClassName() + " object(s) took: " + (new Date().getTime() - start.getTime()) + " ms");
		        	}
		        }
			}
	        for (long id: idList) {
				DbDataObject memObject = bufferedObjs.get(id);
				if (memObject!=null) {
					req.getObjects().add(new ReqDataObject(memObject));
				} else {
					for (DbDataObjectReadWorker worker: workers) {
			        	if (worker.getIdList().contains(id)) {
			        		DbDataObject object = worker.getObjects().get(worker.getIdList().indexOf(id));
			        		DbConfig.getInstance().getCache().addClassObject(req.getClassName(),object.copy(null),source);
			        		req.getObjects().add(new ReqDataObject(object));
			        	}
					}
				}
	        }
		}
	}
	
	private void setInitialIdListInGetRequest(ReqGet req,Object source) {
		List<Long> initialIdList = getIdListForGetRequestIndex(req,0,source);
		req.addLogLine("Initial result size: " + initialIdList.size());
		for (int i = 1; i < ReqGet.MAX_INDEXES; i++) {
			if (req.getIndex()[i]!=null) {
				List<Long> crossRefIdList = getIdListForGetRequestIndex(req,i,source);
				if (crossRefIdList.size()>0) {
					List<Long> newIdList = new ArrayList<Long>();
					for (long id: initialIdList) {
						if (crossRefIdList.contains(id)) {
							newIdList.add(id);
						}
					}
					initialIdList = newIdList;
				} else {
					initialIdList.clear();
				}
				req.addLogLine("Result size after cross reference: " + initialIdList.size());
			}
		}
		req.setInitialIdList(initialIdList);
	}

	private List<Long> getIdListForGetRequestIndex(ReqGet req,int indexNumber,Object source) {
		List<Long> idList = null;
		if (indexNumber<0) {
			indexNumber=0;
		} else if (indexNumber>4) {
			indexNumber=4;
		}

		IdxObject index = getIndexObjectFromGetRequest(req,indexNumber,source);
		List<ReqGetFilter> indexFilters = req.getIndexFilters().get(indexNumber);
		
		MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(req.getClassName()); 
		
		if (index instanceof IdxUniqueConstraint) {
			// Build object using index values
			DbDataObject object = new DbDataObject();
			for (ReqGetFilter filter: indexFilters) {
				MdlProperty prop = cls.getPropertyByName(filter.getProperty());
				if (prop instanceof MdlLink) {
					List<Long> value = new ArrayList<Long>();
					value.add(Long.parseLong(filter.getProperty()));
					object.setLinkValue(((MdlLink)prop).getName(), value);
				} else {
					object.setPropertyValue(prop.getName(),new StringBuilder(filter.getValue()));
				}
			}
			idList = new ArrayList<Long>();
			String classNameId = (((IdxUniqueConstraint) index).getClassNameIdForObject(req.getClassName(),object));
			if (classNameId!=null) {
				idList.add(Long.parseLong(classNameId.split(":")[1]));
			}
			object.cleanup();
		} else if (index instanceof IdxString) {
			// Build object using index values
			DbDataObject object = new DbDataObject();
			boolean isEqualsFilter = false;
			boolean isInvert = false;
			for (ReqGetFilter filter: indexFilters) {
				object.setPropertyValue(filter.getProperty(),new StringBuilder(filter.getValue()));
				isInvert = filter.isInvert();
				if (filter.getOperator().equals(ReqGetFilter.EQUALS)) {
					isEqualsFilter = true;
				}
				break;
			}
			if (isEqualsFilter) {
				idList = ((IdxString) index).getIdListForObject(object,isInvert);
			} else {
				idList = ((IdxString) index).getIdListForObjectContains(object,isInvert);
			}
			object.cleanup();
		} else if (index instanceof IdxNumber) {
			// Build objects using index values
			DbDataObject objectFrom = null;
			DbDataObject objectTo = null;
			boolean fromEquals = false;
			boolean toEquals = false;
			boolean isEqualsFilter = false;
			for (ReqGetFilter filter: indexFilters) {
				if (filter.getOperator().equals(ReqGetFilter.EQUALS)) {
					isEqualsFilter = true;
					objectFrom = new DbDataObject();
					objectFrom.setPropertyValue(filter.getProperty(),new StringBuilder(filter.getValue()));
					break;
				} else if (filter.getOperator().equals(ReqGetFilter.GREATER)) {
					objectFrom = new DbDataObject();
					objectFrom.setPropertyValue(filter.getProperty(),new StringBuilder(filter.getValue()));
				} else if (filter.getOperator().equals(ReqGetFilter.GREATER_OR_EQUALS)) {
					objectFrom = new DbDataObject();
					objectFrom.setPropertyValue(filter.getProperty(),new StringBuilder(filter.getValue()));
					fromEquals = true;
				} else if (filter.getOperator().equals(ReqGetFilter.LESS)) {
					objectTo = new DbDataObject();
					objectTo.setPropertyValue(filter.getProperty(),new StringBuilder(filter.getValue()));
				} else if (filter.getOperator().equals(ReqGetFilter.LESS_OR_EQUALS)) {
					objectTo = new DbDataObject();
					objectTo.setPropertyValue(filter.getProperty(),new StringBuilder(filter.getValue()));
					toEquals = true;
				}
			}
			if (objectFrom==null || objectTo==null) {
				MdlNumber nmbr = ((IdxNumber) index).getNumber();
				if (objectFrom==null) {
					objectFrom = new DbDataObject();
					objectFrom.setPropertyValue(nmbr.getName(),new StringBuilder(nmbr.getMinValue().toString()));
				}
				if (objectTo==null && !isEqualsFilter) {
					objectTo = new DbDataObject();
					objectTo.setPropertyValue(nmbr.getName(),new StringBuilder(nmbr.getMaxValue().toString()));
				}
			}
			if (isEqualsFilter) {
				idList = new ArrayList<Long>(((IdxNumber) index).getIdListForObject(objectFrom));
			} else {
				idList = ((IdxNumber) index).getIdListForObjectFromTo(objectFrom,fromEquals,objectTo,toEquals);
			}
			objectFrom.cleanup();
			if (objectTo!=null) {
				objectTo.cleanup();
			}
		} else if (index instanceof IdxLink) {
			for (ReqGetFilter filter: indexFilters) {
				idList = ((IdxLink) index).getChildIdListForParentId(Long.parseLong(filter.getValue()));
				break;
			}
		} else if (index instanceof IdxClass) {
			if (indexFilters.size()>0) {
				idList = new ArrayList<Long>();
				for (ReqGetFilter filter: indexFilters) {
					long id = Long.parseLong(filter.getValue().toString());
					if (((IdxClass) index).idObjectMapContainsId(id)) {
						idList.add(id);
						break;
					}
				}
			} else {
				idList = new ArrayList<Long>(((IdxClass) index).getNewIdObjectMap().keySet());
			}
		}
		return idList;
	}

	private IdxObject getIndexObjectFromGetRequest(ReqGet req,int indexNumber,Object source) {
		IdxObject index = null;
		if (indexNumber<0) {
			indexNumber=0;
		} else if (indexNumber>4) {
			indexNumber=4;
		}
		MdlObject indexObj = req.getIndex()[indexNumber];
		if (indexObj instanceof MdlUniqueConstraint) {
			index = indexes.get(((MdlUniqueConstraint)indexObj).getFullName());
		} else if (indexObj instanceof MdlString) {
			index = indexes.get(((MdlString)indexObj).getFullName());
		} else if (indexObj instanceof MdlNumber) {
			index = indexes.get(((MdlNumber)indexObj).getFullName());
		} else if (indexObj instanceof MdlLink) {
			index = indexes.get(((MdlLink)indexObj).getFullName());
		} else if (indexObj instanceof MdlClass) {
			index = indexes.get(((MdlClass)indexObj).getFullName());
		}
		return index;
	}
	
	private boolean checkObjectLinkValues(ReqObject req, Object source) {
		boolean ok = true;
		MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(req.getClassName());
		for (ReqDataObject object: req.getObjects()) {
			ok = checkRequestDataObjectLinkValues(req,object,cls,source);
			if (!ok) {
				break;
			}
		}
		return ok;
	}
	
	private boolean checkRequestDataObjectLinkValues(ReqObject req, ReqDataObject object, MdlClass cls, Object source) {
		boolean ok = true;
		for (MdlProperty prop: cls.getPropertiesExtended()) {
			if (prop instanceof MdlLink) {
				MdlLink lnk = (MdlLink) prop; 
				List<Long> idList = object.getDataObject().getLinkValue(lnk.getName());
				if (idList!=null && idList.size()>0) {
					IdxClass indexClassTo = (IdxClass) indexes.get(lnk.getClassTo());
					for (long id: idList) {
						if (id==0 || !indexClassTo.idObjectMapContainsId(id)) {
							ReqError error = req.addObjectError(object,ReqObject.ERROR_CODE_OBJECT_ID_NOT_FOUND,"Object id not found: " + lnk.getClassTo() + ":" + id);
							error.getProperties().add(lnk.getFullName());
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
		return ok;
	}

	private boolean checkObjectUniqueIndexes(ReqObject req, Object source) {
		boolean ok = true;
		MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(req.getClassName());
		for (ReqDataObject object: req.getObjects()) {
			ok = checkRequestDataObjectUniqueIndexes(req,object,cls,source);
			if (!ok) {
				break;
			}
		}
		return ok;
	}

	private boolean checkRequestDataObjectUniqueIndexes(ReqObject req, ReqDataObject object, MdlClass cls, Object source) {
		boolean ok = true;
		for (MdlUniqueConstraint uc: cls.getUniqueConstraintList()) {
			IdxUniqueConstraint ucIndex = (IdxUniqueConstraint) indexes.get(uc.getFullName());
			String classNameId = ucIndex.getClassNameIdForObject(cls.getFullName(),object.getDataObject());
			if (classNameId!=null && classNameId.contains(":")) {
				String[] clsId = classNameId.split(":");
				String className = clsId[0];
				long id = Long.parseLong(clsId[1]);
				if (id!=object.getDataObject().getId() || !className.equals(className)) {
					ReqError error = req.addObjectError(object,ReqObject.ERROR_CODE_UNIQUE_INDEX_VIOLATION,"Unique index violation: " + uc.getFullName());
					for (MdlProperty prop: uc.getPropertiesListForClass(cls.getFullName())) {
						error.getProperties().add(prop.getFullName());
					}
					ok = false;
					break;
				}
			}
		}
		return ok;
	}

	private boolean checkObjectIds(ReqObject req, Object source) {
		boolean ok = true;
		IdxClass classIndex = (IdxClass) indexes.get(req.getClassName());
		for (ReqDataObject object: req.getObjects()) {
			ok = checkRequestDataObjectId(req,object,classIndex,source);
			if (!ok) {
				break;
			}
		}
		return ok;
	}

	private boolean checkRequestDataObjectId(ReqObject req, ReqDataObject object, IdxClass classIndex, Object source) {
		boolean ok = true;
		if (object.getDataObject().getId()>0 && classIndex.idObjectMapContainsId(object.getDataObject().getId())) {
			ReqError error = req.addObjectError(object,ReqObject.ERROR_CODE_UNIQUE_INDEX_VIOLATION,"Unique index violation: " + classIndex.getCls().getFullName() + ", id: " + object.getDataObject().getId());
			error.getProperties().add(MdlProperty.ID);
			ok = false;
		}
		return ok;
	}

	private boolean checkRequestDataObjectChildren(ReqObject req, ReqDataObject object, MdlClass cls, Object source) {
		boolean ok = true;
		StringBuilder children = new StringBuilder();
		for (MdlLink lnk: cls.getChildLinks()) {
			IdxLink linkIdx = (IdxLink) indexes.get(lnk.getFullName());
			if (linkIdx.getChildIdListForParentId(object.getDataObject().getId()).size()>0) {
				if (children.length()>0) {
					children.append(", ");
				}
				children.append(lnk.getFullName());
			}
		}
		if (children.length()>0) {
			req.addObjectError(object,ReqObject.ERROR_CODE_UNABLE_TO_REMOVE_OBJECT,"Unable to remove object: " + object.getDataObject().getId() + ", children: " + children);
			ok = false;
		}
		return ok;
	}
	
	private void addDataObjectToIndexes(String className, DbDataObject object, Object source) {
		IdxClass classIndex = (IdxClass) indexes.get(className);
		if (object.getId()==0) {
			object.setId(classIndex.getNewId());
		}
		classIndex.addId(object.getId());
		DbConfig.getInstance().getCache().addClassObject(className,object.copy(null),source);
		classIndex.lockObjectId(object.getId(), object, source);

		for (MdlProperty prop: classIndex.getCls().getPropertiesExtended()) {
			if (prop.isIndex()) {
				if (prop instanceof MdlString) {
					IdxString stringIndex = (IdxString) indexes.get(prop.getFullName());
					stringIndex.addObject(object);
				} else if (prop instanceof MdlNumber) {
					IdxNumber numberIndex = (IdxNumber) indexes.get(prop.getFullName());
					numberIndex.addObject(object);
				} else if (prop instanceof MdlLink) {
					IdxLink linkIndex = (IdxLink) indexes.get(prop.getFullName());
					List<Long> linkValues = object.getLinkValue(prop.getName());
					linkIndex.addChildValues(object.getId(),linkValues);
				}
			}
		}

		for (MdlLink link: classIndex.getCls().getChildLinks()) {
			if (link.getCls()!=classIndex.getCls()) {
				IdxLink linkIndex = (IdxLink) indexes.get(link.getFullName());
				linkIndex.addParentId(object.getId());
			}
		}
		for (MdlUniqueConstraint uc: classIndex.getCls().getUniqueConstraintList()) {
			IdxUniqueConstraint ucIndex = (IdxUniqueConstraint) indexes.get(uc.getFullName());
			ucIndex.addObject(classIndex.getCls().getFullName(),object);
		}

		classIndex.unlockObjectId(object.getId(),true,false,object,source);
	}

	private boolean updateDataObjectInIndexes(String className, DbDataObject objectBefore, DbDataObject objectAfter, Object source) {
		IdxClass classIndex = (IdxClass) indexes.get(className);
		MdlClass cls = classIndex.getCls();

		List<MdlProperty> changedProp = getDataObjectChangedProperties(className,objectBefore,objectAfter);
		boolean changed = (changedProp.size()>0);
		if (changed) {
			for (MdlProperty prop: changedProp) {
				if (prop.isIndex()) {
					if (prop instanceof MdlString) {
						IdxString stringIndex = (IdxString) indexes.get(prop.getFullName());
						stringIndex.removeObject(objectBefore);
						stringIndex.addObject(objectAfter);
					} else if (prop instanceof MdlNumber) {
						IdxNumber numberIndex = (IdxNumber) indexes.get(prop.getFullName());
						numberIndex.removeObject(objectBefore);
						numberIndex.addObject(objectAfter);
					} else if (prop instanceof MdlLink) {
						IdxLink linkIndex = (IdxLink) indexes.get(prop.getFullName());
						List<Long> linkValuesBefore = objectBefore.getLinkValue(prop.getName());
						List<Long> linkValuesAfter = objectAfter.getLinkValue(prop.getName());
						linkIndex.removeChildValues(objectBefore.getId(),linkValuesBefore);
						linkIndex.addChildValues(objectBefore.getId(),linkValuesAfter);
					}
				}
			}
			for (MdlUniqueConstraint uc: cls.getUniqueConstraintList()) {
				boolean changedIndex = false;
				IdxUniqueConstraint ucIndex = (IdxUniqueConstraint) indexes.get(uc.getFullName());
				for (MdlProperty idxProp: ucIndex.getUniqueConstraint().getPropertiesListForClass(cls.getFullName())) {
					for (MdlProperty prop: changedProp) {
						if (prop.getName().equals(idxProp.getName())) {
							changedIndex = true;
							break;
						}
					}
					if (changedIndex) {
						break;
					}
				}
				if (changedIndex) {
					ucIndex.removeObject(cls.getFullName(),objectBefore);
					ucIndex.addObject(cls.getFullName(),objectAfter);
				}
			}
		}

		classIndex.unlockObjectId(objectAfter.getId(),changed,false,objectAfter,source);
		
		return changed;
	}

	private void removeDataObjectFromIndexes(String className, DbDataObject object, Object source) {
		IdxClass classIndex = (IdxClass) indexes.get(className);
		MdlClass cls = classIndex.getCls();

		for (MdlProperty prop: cls.getPropertiesExtended()) {
			if (prop.isIndex()) {
				if (prop instanceof MdlString) {
					IdxString stringIndex = (IdxString) indexes.get(prop.getFullName());
					stringIndex.removeObjectId(object.getId());
				} else if (prop instanceof MdlNumber) {
					IdxNumber numberIndex = (IdxNumber) indexes.get(prop.getFullName());
					numberIndex.removeObjectId(object.getId());
				} else if (prop instanceof MdlLink) {
					IdxLink linkIndex = (IdxLink) indexes.get(prop.getFullName());
					linkIndex.removeChildId(object.getId());
				}
			}
		}

		for (MdlLink link: classIndex.getCls().getChildLinks()) {
			if (link.getCls()!=classIndex.getCls()) {
				IdxLink linkIndex = (IdxLink) indexes.get(link.getFullName());
				linkIndex.removeParentId(object.getId());
			}
		}
		
		for (MdlUniqueConstraint uc: cls.getUniqueConstraintList()) {
			IdxUniqueConstraint ucIndex = (IdxUniqueConstraint) indexes.get(uc.getFullName());
			ucIndex.removeObjectId(object.getId());
		}
		
		classIndex.removeId(object.getId());
		
		classIndex.unlockObjectId(object.getId(),false,true,null,source);
	}

	private List<MdlProperty> getDataObjectChangedProperties(String className, DbDataObject objectBefore, DbDataObject objectAfter) {
		MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(className);
		List<MdlProperty> changedProp = new ArrayList<MdlProperty>(); 
		for (MdlProperty prop: cls.getPropertiesExtended()) {
			if (prop instanceof MdlLink) {
				List<Long> valBefore = objectBefore.getLinkValue(prop.getName());
				List<Long> valAfter = objectAfter.getLinkValue(prop.getName());
				if (!Generic.idListEquals(valBefore,valAfter)) {
					changedProp.add(prop);
				}
			} else {
				StringBuilder valBefore = objectBefore.getPropertyValue(prop.getName());
				StringBuilder valAfter = objectAfter.getPropertyValue(prop.getName());
				if (!Generic.stringBuilderEquals(valBefore,valAfter)) {
					changedProp.add(prop);
				}
			}
		}
		return changedProp;
	}

	private void initialize() {
		indexes.clear();
		dataObjectWriteWorkers.clear();
		MdlModel model = DbConfig.getInstance().getModel();
		for (MdlClass cls: model.getClasses()) { 
			IdxClass idxCls = new IdxClass(cls);
			indexes.put(cls.getFullName(),idxCls);
			for (int i = 0; i < MAX_WRITERS_PER_CLASS; i++) {
				DbDataObjectWriteWorker worker = new DbDataObjectWriteWorker(idxCls);
				dataObjectWriteWorkers.add(worker);
			}
			for (MdlProperty prop: cls.getPropertiesExtended()) {
				if (prop.isIndex()) {
					if (prop instanceof MdlString) {
						IdxString idxStr = new IdxString((MdlString) prop); 
						indexes.put(prop.getFullName(),idxStr);
					} else if (prop instanceof MdlNumber) {
						IdxNumber idxNum = new IdxNumber((MdlNumber) prop); 
						indexes.put(prop.getFullName(),idxNum);
					} else if (prop instanceof MdlLink) {
						IdxLink idxLnk = new IdxLink((MdlLink) prop);
						indexes.put(prop.getFullName(),idxLnk);
					}
				}
			}
		}
		for (MdlUniqueConstraint uc: model.getUniqueConstraints()) {
			IdxUniqueConstraint idxUC = new IdxUniqueConstraint(uc);
			indexes.put(uc.getFullName(),idxUC);
		}
	}
}
