package nl.zeesoft.zodb.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.LockedCode;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zodb.db.idx.IndexConfig;
import nl.zeesoft.zodb.db.idx.SearchIndex;

public class Index extends Locker {
	private Database								db						= null;
	private IndexConfig								indexConfig				= null;
	private IndexObjectReaderWorker					objectReader			= null;
	
	private int										indexBlockSize			= 1000;
	private int										dataBlockSize			= 10;
	
	private SortedMap<Long,IndexElement>			elementsById			= new TreeMap<Long,IndexElement>();
	private SortedMap<Integer,List<IndexElement>>	elementsByIndexFileNum	= new TreeMap<Integer,List<IndexElement>>();
	private SortedMap<Integer,List<IndexElement>>	elementsByDataFileNum	= new TreeMap<Integer,List<IndexElement>>();
	private Set<Integer>							availableIndexFileNums	= new HashSet<Integer>();
	private Set<Integer>							availableDataFileNums	= new HashSet<Integer>();
	
	private Set<Integer>							changedIndexFileNums	= new HashSet<Integer>();
	private Set<Integer>							changedDataFileNums		= new HashSet<Integer>();
	
	private boolean									open					= false;
	
	protected Index(Messenger msgr,WorkerUnion uni,Database db,IndexConfig indexConfig,int idxBlkSize,int datBlkSize) {
		super(msgr);
		this.db = db;
		this.indexConfig = indexConfig;
		objectReader = new IndexObjectReaderWorker(msgr,uni,this);
		this.indexBlockSize = idxBlkSize;
		this.dataBlockSize = datBlkSize;
	}
	
	protected StringBuilder getKey() {
		return db.getKey();
	}
	
	protected IndexObjectReaderWorker getObjectReader() {
		return objectReader;
	}
	
	protected IndexElement addObject(ZStringBuilder name,JsFile obj,List<ZStringBuilder> errors) {
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				IndexElement r = null;
				if (name.length()>0 && open) {
					r = addObjectNoLock(name,obj,errors);
				}
				return r;
			}
		};
		return (IndexElement) doLocked(this,code);
	}

	protected IndexElement getObjectById(long id,int readTimeOutSeconds,List<ZStringBuilder> errors) {
		IndexElement r = null;
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				IndexElement r = null;
				if (id>0 && open) {
					r = elementsById.get(id);
					if (r!=null) {
						r = r.copy();
					}
				}
				return r;
			}
		};
		r = (IndexElement) doLocked(this,code);
		if (r!=null) {
			readObject(r,readTimeOutSeconds,errors);
			if (r.obj==null) {
				r = null;
			}
		}
		return r;
	}
	
	protected IndexElement getObjectByName(ZStringBuilder name,int readTimeOutSeconds,List<ZStringBuilder> errors) {
		IndexElement r = null;
		List<IndexElement> elements = indexConfig.listObjects(IndexConfig.IDX_NAME,true,false,DatabaseRequest.OP_EQUALS,name);
		if (elements.size()>0) {
			r = elements.get(0);
		}
		if (r!=null) {
			readObject(r,readTimeOutSeconds,errors);
			if (r.obj==null) {
				r = null;
			}
		}
		return r;
	}

	protected List<IndexElement> listObjectsUseIndex(int start, int max,boolean ascending,String indexName,boolean invert,String operator,ZStringBuilder value,long modAfter,long modBefore,List<Integer> data) {
		return listObjectsUsingIndex(start,max,ascending,indexName,invert,operator,value,modAfter,modBefore,data);
	}
	
	protected List<IndexElement> listObjects(int start, int max,long modAfter,long modBefore,List<Integer> data) {
		return listObjectsUsingIndex(start,max,true,IndexConfig.IDX_NAME,false,DatabaseRequest.OP_STARTS_WITH,null,modAfter,modBefore,data);
	}
	
	protected List<IndexElement> getObjectsUseIndex(boolean ascending,String indexName,boolean invert,String operator,ZStringBuilder value,long modAfter,long modBefore,int readTimeOutSeconds,List<ZStringBuilder> errors) {
		return getObjectsUsingIndex(ascending,indexName,invert,operator,value,modAfter,modBefore,readTimeOutSeconds,errors);
	}
	
	protected void setObject(long id, JsFile obj, List<ZStringBuilder> errors) {
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				if (id>0 && open) {
					setObjectNoLock(id,obj,errors);
				}
				return null;
			}
		};
		doLocked(this,code);
	}

	protected void setObjectName(long id,ZStringBuilder name,int readTimeOutSeconds,List<ZStringBuilder> errors) {
		Database.removeSpecialCharacters(name);
		lockMe(this);
		IndexElement element = elementsById.get(id);
		if (element!=null) {
			element = element.copy();
		}
		unlockMe(this);
		if (element!=null) {
			if (indexConfig.objectHasUpdateIndexes(name)) {
				readObject(element,readTimeOutSeconds,errors);
				if (element.obj==null) {
					element = null;
				}
			}
		}
		if (element!=null) {
			LockedCode code = new LockedCode() {
				@Override
				public Object doLocked() {
					setObjectNameNoLock(id,name,errors);
					return null;
				}
			};
			doLocked(this,code);
		}
	}

	protected IndexElement removeObject(long id,List<ZStringBuilder> errors) {
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				IndexElement r = null;
				if (id>0 && open) {
					r = removeObjectNoLock(id,errors);
				}
				return r;
			}
		};
		return (IndexElement) doLocked(this,code);
	}

	protected List<IndexElement> removeObjectsUseIndex(String indexName,boolean invert,String operator,ZStringBuilder value,long modAfter,long modBefore,List<ZStringBuilder> errors) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		List<IndexElement> elements = listObjectsUsingIndex(true,indexName,invert,operator,value,modAfter,modBefore);
		for (IndexElement element: elements) {
			removeObject(element.id,errors);
			r.add(element);
		}
		return r;
	}
	
	@SuppressWarnings("unchecked")
	protected void readObjects(SortedMap<Long,JsFile> idObjMap) {
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				List<IndexElement> elements = new ArrayList<IndexElement>();
				for (Entry<Long,JsFile> entry: idObjMap.entrySet()) {
					IndexElement element = elementsById.get(entry.getKey());
					if (element!=null && element.obj==null) {
						element.obj = entry.getValue();
						elements.add(element.copy());
					}
				}
				return elements;
			}
		};
		indexConfig.setObjects((List<IndexElement>) doLocked(this,code));
	}

	protected String getFileDirectory() {
		return db.getFullIndexDir();
	}
	
	protected String getObjectDirectory() {
		return db.getFullObjectDir();
	}

	protected void readAll() {
		lockMe(this);
		List<IndexElement> elements = new ArrayList<IndexElement>();
		for (IndexElement element: elementsById.values()) {
			if (element.obj==null) {
				elements.add(element.copy());
			}
		}
		unlockMe(this);
		forceReadObjects(elements);
	}

	protected void setKey(StringBuilder key) {
		db.setKey(key);
	}

	protected void writeAll() {
		writeAll(true);
	}

	protected void writeAll(boolean includeObjects) {
		lockMe(this);
		List<IndexElement> elements = new ArrayList<IndexElement>(elementsById.values());
		for (IndexElement element: elements) {
			changedIndexFileNums.add(element.indexFileNum);
			if (includeObjects && element.obj!=null) {
				changedDataFileNums.add(element.dataFileNum);
			}
		}
		unlockMe(this);
	}

	protected void setOpen(boolean open) {
		boolean rebuild = false;
		if (open && indexConfig.isRebuild()) {
			rebuild = true;
			readAll();
		}
		lockMe(this);
		if (rebuild) {
			indexConfig.clear();
			for (IndexElement element: elementsById.values()) {
				element.idxValues = indexConfig.getIndexValuesForObject(element.name,element.obj);
				indexConfig.addObject(element.copy());
			}
		}
		if (open) {
			if (elementsByIndexFileNum.size()>0) {
				for (int i = elementsByIndexFileNum.lastKey(); i>=0; i--) {
					if (elementsByIndexFileNum.get(i)==null || elementsByIndexFileNum.get(i).size()<indexBlockSize) {
						availableIndexFileNums.add(i);
					}
				}
			}
			if (elementsByDataFileNum.size()>0) {
				for (int i = elementsByDataFileNum.lastKey(); i>=0; i--) {
					if (elementsByDataFileNum.get(i)==null || elementsByDataFileNum.get(i).size()<dataBlockSize) {
						availableDataFileNums.add(i);
					}
				}
			}
		}
		this.open = open;
		unlockMe(this);
		db.stateChanged(open);
		if (rebuild) {
			writeAll(false);
		}
	}
	
	protected boolean isOpen() {
		boolean r = false;
		lockMe(this);
		r = open;
		unlockMe(this);
		return r;
	}
	
	protected void clear() {
		lockMe(this);
		clearNoLock();
		unlockMe(this);
	}

	protected void destroy() {
		lockMe(this);
		if (!open) {
			clearNoLock();
			db = null;
			objectReader.destroy();
		}
		unlockMe(this);
	}
	
	protected void addFileElements(Integer fileNum,List<IndexElement> elements) {
		lockMe(this);
		List<IndexElement> added = new ArrayList<IndexElement>();
		elementsByIndexFileNum.put(fileNum,elements);
		for (IndexElement elem: elements) {
			elementsById.put(elem.id,elem);
			List<IndexElement> list = elementsByDataFileNum.get(elem.dataFileNum);
			if (list==null) {
				list = new ArrayList<IndexElement>();
				elementsByDataFileNum.put(elem.dataFileNum,list);
			}
			list.add(elem);
			added.add(elem.copy());
		}
		unlockMe(this);
		indexConfig.addObjects(added);
	}

	protected SortedMap<Integer,List<IndexElement>> getChangedIndexFiles(int max,Set<Integer> exclude) {
		SortedMap<Integer,List<IndexElement>> r = new TreeMap<Integer,List<IndexElement>>();
		lockMe(this);
		List<Integer> fileNums = new ArrayList<Integer>(changedIndexFileNums);
		for (Integer num: fileNums) {
			if (!exclude.contains(num)) {
				List<IndexElement> list = new ArrayList<IndexElement>();
				List<IndexElement> elements = elementsByIndexFileNum.get(num);
				if (elements!=null) {
					for (IndexElement elem: elements) {
						if (!elem.removed) {
							list.add(elem.copy());
						}
					}
				}
				r.put(num,list);
				changedIndexFileNums.remove(num);
				if (max>0 && r.size()>=max) {
					break;
				}
			}
		}
		unlockMe(this);
		return r;
	}

	protected SortedMap<Integer,List<IndexElement>> getChangedDataFiles(int max,Set<Integer> exclude) {
		SortedMap<Integer,List<IndexElement>> r = new TreeMap<Integer,List<IndexElement>>();
		List<IndexElement> read = new ArrayList<IndexElement>();
		lockMe(this);
		List<Integer> fileNums = new ArrayList<Integer>(changedDataFileNums);
		for (Integer num: fileNums) {
			if (!exclude.contains(num)) {
				List<IndexElement> list = new ArrayList<IndexElement>();
				List<IndexElement> elements = elementsByDataFileNum.get(num);
				if (elements!=null) {
					for (IndexElement elem: elements) {
						if (!elem.removed) {
							IndexElement copy = elem.copy();
							list.add(copy);
							if (copy.obj==null) {
								read.add(copy);
							}
						}
					}
				}
				r.put(num,list);
				changedDataFileNums.remove(num);
				if (max>0 && r.size()>=max) {
					break;
				}
			}
		}
		unlockMe(this);
		forceReadObjects(read);
		for (IndexElement element: read) {
			if (element.obj==null) {
				r.get(element.dataFileNum).remove(element);
			}
		}
		return r;
	}

	private List<IndexElement> getObjectsUsingIndex(boolean ascending,String indexName,boolean invert,String operator,ZStringBuilder value,long modAfter,long modBefore,int readTimeOutSeconds,List<ZStringBuilder> errors) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		r = listObjectsUsingIndex(ascending,indexName,invert,operator,value,modAfter,modBefore);
		readObjects(r,readTimeOutSeconds,errors);
		List<IndexElement> read = new ArrayList<IndexElement>(r);
		for (IndexElement element: read) {
			if (element.obj==null) {
				r.remove(element);
			}
		}
		return r;
	}

	private List<IndexElement> listObjectsUsingIndex(int start, int max,boolean ascending,String indexName,boolean invert,String operator,ZStringBuilder value,long modAfter,long modBefore,List<Integer> data) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		if (start<0) {
			start = 0;
		}
		if (max<1) {
			max = 1;
		}
		List<IndexElement> elements = listObjectsUsingIndex(ascending,indexName,invert,operator,value,modAfter,modBefore);
		int end = start + max;
		if (end>=elements.size()) {
			end = elements.size();
		}
		for (int i = start; i < end; i++) {
			r.add(elements.get(i));
		}
		if (data!=null) {
			data.add(elements.size());
		}
		return r;
	}

	private List<IndexElement> listObjectsUsingIndex(boolean ascending,String indexName,boolean invert,String operator,ZStringBuilder value,long modAfter,long modBefore) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		if (isOpen()) {
			SearchIndex index = indexConfig.getListIndex(indexName);
			if (index!=null) {
				List<IndexElement> elements = indexConfig.listObjects(indexName,ascending,invert,operator,value);
				for (IndexElement element: elements) {
					if (
						(modAfter==0L && modBefore==0L) ||
						(modAfter>0L && modBefore>0L && element.modified>modAfter && element.modified<modBefore) || 
						(modAfter>0L && element.modified>modAfter) || 
						(modBefore>0L && element.modified<modBefore)
						) {
						r.add(element);
					}
				}
			}
		}
		return r;
	}
	
	private void clearNoLock() {
		elementsById.clear();
		elementsByIndexFileNum.clear();
		elementsByDataFileNum.clear();
		availableIndexFileNums.clear();
		availableDataFileNums.clear();
		indexConfig.clear();
	}
	
	private IndexElement addObjectNoLock(ZStringBuilder name,JsFile obj,List<ZStringBuilder> errors) {
		Database.removeSpecialCharacters(name);
		IndexElement r = null;
		IndexElement element = new IndexElement();
		element.name = name;
		element.obj = obj;
		element.idxValues = indexConfig.getIndexValuesForObject(element.name,obj);
		if (checkUniqueIndexesForObjectNoLock(element,null,errors)) {
			r = new IndexElement();
			r.id = getNewUidNoLock();
			r.name = name;
			r.obj = obj;
			r.idxValues = element.idxValues;
			r.modified = element.modified;
			r.indexFileNum = getIndexFileNumForNewObjectNoLock();
			r.dataFileNum = getDataFileNumForNewObjectNoLock();
			r.added = true;
			
			elementsById.put(r.id,r);
			List<IndexElement> list = elementsByIndexFileNum.get(r.indexFileNum);
			if (list==null) {
				list = new ArrayList<IndexElement>();
				elementsByIndexFileNum.put(r.indexFileNum,list);
				availableIndexFileNums.add(r.indexFileNum);
			}
			list.add(r);
			if (list.size()>=indexBlockSize) {
				availableIndexFileNums.remove(r.indexFileNum);
			}
				
			list = elementsByDataFileNum.get(r.dataFileNum);
			if (list==null) {
				list = new ArrayList<IndexElement>();
				elementsByDataFileNum.put(r.dataFileNum,list);
				availableDataFileNums.add(r.dataFileNum);
			}
			list.add(r);
			if (list.size()>=dataBlockSize) {
				availableDataFileNums.remove(r.dataFileNum);
			}

			indexConfig.addObject(r.copy());
			
			changedIndexFileNums.add(r.indexFileNum);
			changedDataFileNums.add(r.dataFileNum);
			r = r.copy();
		}
		return r;
	}
		
	private long getNewUidNoLock() {
		long r = 1;
		if (elementsById.size()>0) {
			r = (elementsById.lastKey() + 1L);
		}
		return r;
	}
	
	private int getIndexFileNumForNewObjectNoLock() {
		int r = -1;
		if (availableIndexFileNums.size()==0) {
			if (elementsByIndexFileNum.size()>0) {
				r = (elementsByIndexFileNum.lastKey() + 1);
			}
		} else {
			r = availableIndexFileNums.iterator().next();
		}
		if (r<0) {
			r = 0;
		}
		return r;
	}
	
	private int getDataFileNumForNewObjectNoLock() {
		int r = -1;
		if (availableDataFileNums.size()==0) {
			if (elementsByDataFileNum.size()>0) {
				r = (elementsByDataFileNum.lastKey() + 1);
			}
		} else {
			r = availableDataFileNums.iterator().next();
		}
		if (r<0) {
			r = 0;
		}
		return r;
	}

	private void readObject(IndexElement element,int readTimeOutSeconds,List<ZStringBuilder> errors) {
		List<IndexElement> elements = new ArrayList<IndexElement>();
		elements.add(element);
		readObjects(elements,readTimeOutSeconds,errors);
	}

	private void readObjects(List<IndexElement> elements,int readTimeOutSeconds,List<ZStringBuilder> errors) {
		readObjects(elements,false,readTimeOutSeconds,errors);
	}

	private void forceReadObjects(List<IndexElement> elements) {
		readObjects(elements,true,0,null);
	}
	
	private void readObjects(List<IndexElement> elements, boolean force,int readTimeOutSeconds,List<ZStringBuilder> errors) {
		if (readTimeOutSeconds<=0) {
			readTimeOutSeconds = 1;
		}
		List<Integer> fileNumList = new ArrayList<Integer>();
		List<Long> idList = new ArrayList<Long>();
		for (IndexElement element: elements) {
			lockMe(this);
			IndexElement elem = elementsById.get(element.id);
			if (elem!=null) {
				if (elem.obj!=null) {
					element.obj = elem.obj;
				} else {
					if (!fileNumList.contains(element.dataFileNum)) {
						fileNumList.add(element.dataFileNum);
					}
					idList.add(element.id);
				}
			}
			unlockMe(this);
		}
		if (fileNumList.size()>0) {
			objectReader.addFileNums(fileNumList);
			waitForObjectRead(idList,force,readTimeOutSeconds,errors);
			for (IndexElement element: elements) {
				lockMe(this);
				IndexElement elem = elementsById.get(element.id);
				if (elem!=null && elem.obj!=null) {
					element.obj = elem.obj;
				}
				unlockMe(this);
			}
		}
	}

	private void waitForObjectRead(List<Long> idList,boolean force,int readTimeOutSeconds,List<ZStringBuilder> errors) {
		boolean o = true;
		long sleep = 1;
		long started = System.currentTimeMillis();
		while(idList.size()>0) {
			List<Long> remainingIdList = new ArrayList<Long>();
			for (Long id: idList) {
				lockMe(this);
				IndexElement element = elementsById.get(id);
				if (element!=null && element.obj==null) {
					remainingIdList.add(id);
				}
				unlockMe(this);
			}
			idList = remainingIdList;
			if (!force && idList.size()>0) {
				o = isOpen();
				if (errors!=null) {
					if (!o) {
						errors.add(new ZStringBuilder("Database has been closed while reading object(s)"));
					} else if (System.currentTimeMillis() - started > (long)readTimeOutSeconds * 1000L) {
						errors.add(new ZStringBuilder("Reading object(s) timed out after " + readTimeOutSeconds + " seconds"));
						break;
					}
				} 
			}
			if (!o) {
				break;
			} else if (idList.size()>0) {
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
					getMessenger().error(this,"Waiting for object read was interrupted",e);
					if (errors!=null) {
						errors.add(new ZStringBuilder("Waiting for object read was interrupted"));
					}
					break;
				}
				if (sleep<100) {
					sleep++;
				}
			}
		}
	}
	
	private void setObjectNameNoLock(long id, ZStringBuilder name, List<ZStringBuilder> errors) {
		if (open) {
			IndexElement element = elementsById.get(id);
			if (element!=null && !element.removed) {
				if (!element.name.equals(name)) {
					IndexElement copy = element.copy();
					copy.name = name;
					copy.idxValues = indexConfig.getIndexValuesForObject(copy.name,copy.obj);
					if (checkUniqueIndexesForObjectNoLock(copy,element.name,errors)) {
						indexConfig.removeObject(element);
						element.name = name;
						element.idxValues = copy.idxValues;
						element.updateModified();
						indexConfig.addObject(element.copy());
						changedIndexFileNums.add(element.indexFileNum);
					}
				}
			} else if (errors!=null) {
				errors.add(new ZStringBuilder("Object with id " + id + " does not exist"));
			}
		}
	}

	private void setObjectNoLock(long id, JsFile obj,List<ZStringBuilder> errors) {
		if (elementsById.containsKey(id)) {
			IndexElement element = elementsById.get(id);
			if (!element.removed) {
				IndexElement copy = element.copy();
				copy.idxValues = indexConfig.getIndexValuesForObject(copy.name,obj);
				if (checkUniqueIndexesForObjectNoLock(copy,copy.name,errors)) {
					indexConfig.removeObject(element);
					element.obj = obj;
					element.idxValues = copy.idxValues;
					element.updateModified();
					indexConfig.addObject(element.copy());
					changedIndexFileNums.add(element.indexFileNum);
					changedDataFileNums.add(element.dataFileNum);
				}
			}
		} else if (errors!=null) {
			errors.add(new ZStringBuilder("Object with id " + id + " does not exist"));
		}
	}

	private boolean checkUniqueIndexesForObjectNoLock(IndexElement element,ZStringBuilder oldName,List<ZStringBuilder> errors) {
		boolean ok = true;
		for (SearchIndex index: indexConfig.getUniqueIndexesForObjectName(element.name)) {
			ZStringBuilder strVal = null;
			if (index.getName().equals(IndexConfig.IDX_NAME)) {
				strVal = element.name;
			} else if (index.getName().equals(IndexConfig.IDX_MODIFIED)) {
				strVal = new ZStringBuilder("" + element.modified);
			} else {
				strVal = element.idxValues.get(index.propertyName);
			}
			if (strVal==null || indexConfig.hasObject(index.getName(),element)) {
				if (errors!=null) {
					if (oldName!=null && oldName.length()>0) {
						errors.add(new ZStringBuilder("Index " + index.getName() + " blocks update of object named '" + oldName + "'"));
					} else {
						errors.add(new ZStringBuilder("Index " + index.getName() + " blocks addition of object named '" + element.name + "'"));
					}
				}
				ok = false;
			}
		}
		return ok;
	}
	
	private IndexElement removeObjectNoLock(long id, List<ZStringBuilder> errors) {
		IndexElement r = null;
		if (elementsById.containsKey(id)) {
			r = elementsById.remove(id);
			
			List<IndexElement> list = elementsByIndexFileNum.get(r.indexFileNum);
			list.remove(r);
			if (list.size()==0) {
				elementsByIndexFileNum.remove(r.indexFileNum);
				availableIndexFileNums.add(r.indexFileNum);
			} else if (list.size() < indexBlockSize) {
				availableIndexFileNums.add(r.indexFileNum);
			}
			
			list = elementsByDataFileNum.get(r.dataFileNum);
			list.remove(r);
			if (list.size()==0) {
				elementsByDataFileNum.remove(r.dataFileNum);
				availableDataFileNums.add(r.dataFileNum);
			} else if (list.size() < dataBlockSize) {
				availableDataFileNums.add(r.dataFileNum);
			}
			
			indexConfig.removeObject(r);
			r.removed = true;
			changedIndexFileNums.add(r.indexFileNum);
			changedDataFileNums.add(r.dataFileNum);
			IndexElement ori = r;
			r = r.copy();
			ori.obj = null;
		} else if (errors!=null) {
			errors.add(new ZStringBuilder("Object with id " + id + " does not exist"));
		}
		return r;
	}
}
