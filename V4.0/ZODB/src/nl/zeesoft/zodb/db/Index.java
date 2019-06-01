package nl.zeesoft.zodb.db;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.idx.IndexConfig;
import nl.zeesoft.zodb.db.idx.SearchIndex;

public class Index extends Locker {
	private int										blockSize			= 100;
	
	private Database								db					= null;
	private IndexConfig								indexConfig			= null;
	private IndexObjectReaderWorker					objectReader		= null;
	
	private SortedMap<Long,IndexElement>			elementsById		= new TreeMap<Long,IndexElement>();
	private SortedMap<Integer,List<IndexElement>>	elementsByFileNum	= new TreeMap<Integer,List<IndexElement>>();
	
	private List<Integer>							changedFileNums		= new ArrayList<Integer>();
	private List<IndexElement>						changedElements		= new ArrayList<IndexElement>();
	
	private boolean									open				= false;
	
	protected Index(Config config,Database db,IndexConfig indexConfig) {
		super(config.getMessenger());
		this.db = db;
		this.indexConfig = indexConfig;
		objectReader = new IndexObjectReaderWorker(config.getMessenger(),config.getUnion(),this);
	}
	
	protected StringBuilder getKey() {
		return db.getKey();
	}
	
	protected IndexObjectReaderWorker getObjectReader() {
		return objectReader;
	}
	
	protected IndexElement addObject(ZStringBuilder name,JsFile obj,List<ZStringBuilder> errors) {
		IndexElement r = null;
		lockMe(this);
		if (name.length()>0 && open) {
			r = addObjectNoLock(name,obj,errors);
		}
		unlockMe(this);
		return r;
	}
	
	protected IndexElement getObjectById(long id) {
		IndexElement r = null;
		lockMe(this);
		if (id>0 && open) {
			r = elementsById.get(id);
			if (r!=null) {
				r = r.copy();
			}
		}
		unlockMe(this);
		if (r!=null) {
			readObject(r);
		}
		return r;
	}
	
	protected IndexElement getObjectByName(ZStringBuilder name) {
		IndexElement r = null;
		List<IndexElement> elements = indexConfig.listObjects(IndexConfig.IDX_NAME,true,false,DatabaseRequest.OP_EQUALS,name);
		if (elements.size()>0) {
			r = elements.get(0);
		}
		if (r!=null) {
			readObject(r);
		}
		return r;
	}

	protected List<IndexElement> listObjectsUseIndex(int start, int max,boolean ascending,String indexName,boolean invert,String operator,ZStringBuilder value,long modAfter,long modBefore,List<Integer> data) {
		return listObjectsUseIndexOutsideLock(start,max,ascending,indexName,invert,operator,value,modAfter,modBefore,data);
	}
	
	protected List<IndexElement> listObjects(int start, int max,long modAfter,long modBefore,List<Integer> data) {
		return listObjects(start,max,null,null,null,modAfter,modBefore,data);
	}

	protected List<IndexElement> listObjectsThatStartWith(ZStringBuilder startWith,int start, int max,long modAfter,long modBefore,List<Integer> data) {
		return listObjects(start,max,startWith,null,null,modAfter,modBefore,data);
	}

	protected List<IndexElement> listObjectsThatContain(ZStringBuilder contains,int start, int max,long modAfter,long modBefore,List<Integer> data) {
		return listObjects(start,max,null,contains,null,modAfter,modBefore,data);
	}
	
	protected List<IndexElement> getObjectsUseIndex(boolean ascending,String indexName,boolean invert,String operator,ZStringBuilder value,long modAfter,long modBefore) {
		return getObjectsUsingIndex(ascending,indexName,invert,operator,value,modAfter,modBefore);
	}
	
	protected List<IndexElement> getObjectsByNameStartsWith(ZStringBuilder startsWith,long modAfter,long modBefore) {
		return getObjectsByName(startsWith,null,null,modAfter,modBefore);
	}
	
	protected List<IndexElement> getObjectsByNameContains(ZStringBuilder contains,long modAfter,long modBefore) {
		return getObjectsByName(null,contains,null,modAfter,modBefore);
	}
	
	protected void setObject(long id, JsFile obj, List<ZStringBuilder> errors) {
		lockMe(this);
		if (id>0 && open) {
			setObjectNoLock(id,obj,errors);
		}
		unlockMe(this);
	}

	protected void setObjectName(long id, ZStringBuilder name, List<ZStringBuilder> errors) {
		Database.removeControlCharacters(name);
		IndexElement element = null;
		if (indexConfig.objectHasUpdateIndexes(name)) {
			lockMe(this);
			element = elementsById.get(id);
			if (element!=null) {
				element = element.copy();
			}
			unlockMe(this);
			if (element!=null) {
				readObject(element);
			}
		}
		lockMe(this);
		if (open) {
			element = elementsById.get(id);
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
						if (!changedFileNums.contains(element.fileNum)) {
							changedFileNums.add(element.fileNum);
						}
					}
				}
			} else if (errors!=null) {
				errors.add(new ZStringBuilder("Object with id " + id + " does not exist"));
			}
		}
		unlockMe(this);
	}

	protected IndexElement removeObject(long id,List<ZStringBuilder> errors) {
		IndexElement r = null;
		lockMe(this);
		if (id>0 && open) {
			r = removeObjectNoLock(id,errors);
		}
		unlockMe(this);
		return r;
	}

	protected List<IndexElement> removeObjectsThatStartWith(ZStringBuilder startsWith,long modAfter,long modBefore,List<ZStringBuilder> errors) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		if (startsWith.length()>0 && isOpen()) {
			List<IndexElement> elements = listObjectsByNameOutsideLock(startsWith,null,null,modAfter,modBefore);
			for (IndexElement element: elements) {
				removeObjectNoLock(element.id,errors);
				r.add(element);
			}
		}
		return r;
	}
	
	protected List<IndexElement> removeObjectsThatContain(ZStringBuilder contains,long modAfter,long modBefore,List<ZStringBuilder> errors) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		if (contains.length()>0 && isOpen()) {
			List<IndexElement> elements = listObjectsByNameOutsideLock(null,contains,null,modAfter,modBefore);
			for (IndexElement element: elements) {
				removeObjectNoLock(element.id,errors);
				r.add(element);
			}
		}
		return r;
	}

	protected List<IndexElement> removeObjectsUseIndex(String indexName,boolean invert,String operator,ZStringBuilder value,long modAfter,long modBefore,List<ZStringBuilder> errors) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		List<IndexElement> elements = listObjectsUseIndexOutsideLock(true,indexName,invert,operator,value,modAfter,modBefore);
		for (IndexElement element: elements) {
			removeObjectNoLock(element.id,errors);
			r.add(element);
		}
		return r;
	}

	protected void readObject(IndexElement element) {
		List<IndexElement> elements = new ArrayList<IndexElement>();
		elements.add(element);
		readObjects(elements);
	}

	protected void readObjects(List<IndexElement> elements) {
		List<Long> idList = new ArrayList<Long>();
		for (IndexElement element: elements) {
			lockMe(this);
			IndexElement elem = elementsById.get(element.id);
			if (elem!=null) {
				if (elem.obj!=null) {
					element.obj = elem.obj;
				} else {
					idList.add(element.id);
				}
			}
			unlockMe(this);
		}
		if (idList.size()>0) {
			objectReader.addIdList(idList);
			waitForObjectRead(idList);
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

	protected void waitForObjectRead(List<Long> idList) {
		boolean o = true;
		long sleep = 1;
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
			if (idList.size()>0) {
				lockMe(this);
				o = open;
				unlockMe(this);
			}
			if (!o) {
				break;
			} else if (idList.size()>0) {
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
					getMessenger().error(this,"Waiting for object read was interrupted",e);
				}
				if (sleep<100) {
					sleep++;
				}
			}
		}
	}
	
	protected void readObject(long id,JsFile obj) {
		IndexElement element = null;
		lockMe(this);
		if (open) {
			element = elementsById.get(id);
			if (element!=null) {
				element.obj = obj;
				element = element.copy();
			}
		}
		unlockMe(this);
		if (element!=null) {
			indexConfig.setObject(element);
		}
	}

	protected String getFileDirectory() {
		return db.getFullIndexDir();
	}
	
	protected String getObjectDirectory() {
		return db.getFullObjectDir();
	}

	protected void readAll() {
		lockMe(this);
		List<IndexElement> elems = new ArrayList<IndexElement>(elementsById.values());
		List<IndexElement> elements = new ArrayList<IndexElement>();
		for (IndexElement element: elems) {
			if (element.obj==null) {
				elements.add(element.copy());
			}
		}
		unlockMe(this);
		readObjects(elements);
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
			if (!changedFileNums.contains(element.fileNum)) {
				changedFileNums.add(element.fileNum);
			}
			if (includeObjects) {
				changedElements.add(element);
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
		this.open = open;
		if (!open) {
			elementsById.clear();
			elementsByFileNum.clear();
			indexConfig.clear();
		}
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

	protected void destroy() {
		lockMe(this);
		if (!open) {
			db = null;
			objectReader.destroy();
		}
		unlockMe(this);
	}

	protected void addFileElements(Integer fileNum,List<IndexElement> elements) {
		lockMe(this);
		elementsByFileNum.put(fileNum,elements);
		for (IndexElement elem: elements) {
			elementsById.put(elem.id,elem);
		}
		unlockMe(this);
		for (IndexElement elem: elements) {
			indexConfig.addObject(elem.copy());
		}
	}

	protected SortedMap<Integer,List<IndexElement>> getChangedFiles() {
		SortedMap<Integer,List<IndexElement>> r = new TreeMap<Integer,List<IndexElement>>();
		lockMe(this);
		for (Integer num: changedFileNums) {
			List<IndexElement> list = new ArrayList<IndexElement>();
			for (IndexElement elem: elementsByFileNum.get(num)) {
				list.add(elem.copy());
			}
			r.put(num,list);
		}
		changedFileNums.clear();
		unlockMe(this);
		return r;
	}
	
	protected List<IndexElement> getChangedElements(int max) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		lockMe(this);
		int added = 0;
		List<IndexElement> list = new ArrayList<IndexElement>(changedElements);
		for (IndexElement elem: list) {
			if (!(elem.added && elem.removed)) {
				r.add(elem.copy());
				added++;
			}
			elem.added = false;
			changedElements.remove(elem);
			if (max>0 && added>=max) {
				break;
			}
		}
		unlockMe(this);
		return r;
	}

	private List<IndexElement> listObjects(int start, int max,ZStringBuilder startsWith,ZStringBuilder contains,ZStringBuilder endsWith,long modAfter,long modBefore,List<Integer> data) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		if (isOpen()) {
			r = listObjectsOutsideLock(start, max, startsWith, contains, endsWith, modAfter, modBefore, data);
		}
		return r;
	}

	private List<IndexElement> getObjectsByName(ZStringBuilder startsWith,ZStringBuilder contains,ZStringBuilder endsWith,long modAfter,long modBefore) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		if (isOpen()) {
			r = listObjectsByNameOutsideLock(startsWith,contains,endsWith,modAfter,modBefore);
		}
		readObjects(r);
		return r;
	}

	private List<IndexElement> getObjectsUsingIndex(boolean ascending,String indexName,boolean invert,String operator,ZStringBuilder value,long modAfter,long modBefore) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		r = listObjectsUseIndexOutsideLock(ascending,indexName,invert,operator,value,modAfter,modBefore);
		readObjects(r);
		return r;
	}

	private List<IndexElement> listObjectsUseIndexOutsideLock(int start, int max,boolean ascending,String indexName,boolean invert,String operator,ZStringBuilder value,long modAfter,long modBefore,List<Integer> data) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		if (start<0) {
			start = 0;
		}
		if (max<1) {
			max = 1;
		}
		List<IndexElement> elements = listObjectsUseIndexOutsideLock(ascending,indexName,invert,operator,value,modAfter,modBefore);
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

	private List<IndexElement> listObjectsUseIndexOutsideLock(boolean ascending,String indexName,boolean invert,String operator,ZStringBuilder value,long modAfter,long modBefore) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		if (isOpen()) {
			SearchIndex index = indexConfig.getListIndex(indexName);
			if (index!=null) {
				List<IndexElement> elements = indexConfig.listObjects(indexName,ascending,invert,operator,value);
				for (IndexElement element: elements) {
					if (checkModifiedNoLock(element, modAfter, modBefore)) {
						r.add(element);
					}
				}
			}
		}
		return r;
	}
	
	private List<IndexElement> listObjectsOutsideLock(int start, int max,ZStringBuilder startsWith,ZStringBuilder contains,ZStringBuilder endsWith,long modAfter,long modBefore,List<Integer> data) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		if (start<0) {
			start = 0;
		}
		if (max<1) {
			max = 1;
		}
		List<IndexElement> elements = null;
		if (isOpen()) {
			if (
				(startsWith!=null && startsWith.length()>0) ||
				(contains!=null && contains.length()>0) ||
				(endsWith!=null && endsWith.length()>0)
				) {
				elements = listObjectsByNameOutsideLock(startsWith,contains,endsWith,modAfter,modBefore);
			} else {
				List<IndexElement> elems = indexConfig.listObjects(IndexConfig.IDX_NAME,true,false,DatabaseRequest.OP_STARTS_WITH,null);
				if (elems.size()>0) {
					elements = new ArrayList<IndexElement>();
					for (IndexElement element: elems) {
						if (checkModifiedNoLock(element,modAfter,modBefore)) {
							elements.add(element);
						}
					}
				}
			}
		}
		if (elements!=null) {
			if (data!=null) {
				data.add(elements.size());
			}
			if (start<=(elements.size() - 1)) {
				int end = start + max;
				if (end>elements.size()) {
					end = elements.size();
				}
				for (int i = start; i < end; i++) {
					r.add(elements.get(i));
				}
			}
		}
		return r;
	}

	private List<IndexElement> listObjectsByNameOutsideLock(ZStringBuilder startsWith,ZStringBuilder contains,ZStringBuilder endsWith,long modAfter,long modBefore) {
		List<IndexElement> r = null;
		List<IndexElement> elements = null;
		if (startsWith!=null && startsWith.length()>0) {
			elements = indexConfig.listObjects(IndexConfig.IDX_NAME,true,false,DatabaseRequest.OP_STARTS_WITH,startsWith);
		} else if (contains!=null && contains.length()>0) {
			elements = indexConfig.listObjects(IndexConfig.IDX_NAME,true,false,DatabaseRequest.OP_CONTAINS,contains);
		} else if (endsWith!=null && endsWith.length()>0) {
			elements = indexConfig.listObjects(IndexConfig.IDX_NAME,true,false,DatabaseRequest.OP_ENDS_WITH,endsWith);
		}
		if (modAfter>0L || modBefore>0L) {
			r = new ArrayList<IndexElement>();
			for (IndexElement element: elements) {
				if (checkModifiedNoLock(element,modAfter,modBefore)) {
					r.add(element);
				}
			}
		} else {
			r = elements;
		}
		return r;
	}
	
	private IndexElement addObjectNoLock(ZStringBuilder name,JsFile obj,List<ZStringBuilder> errors) {
		Database.removeControlCharacters(name);
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
			r.fileNum = getFileNumForNewObjectNoLock();
			r.added = true;
			
			elementsById.put(r.id,r);
			List<IndexElement> list = elementsByFileNum.get(r.fileNum);
			if (list==null) {
				list = new ArrayList<IndexElement>();
				elementsByFileNum.put(r.fileNum,list);
			}
			list.add(r);
			indexConfig.addObject(r.copy());
			
			if (!changedFileNums.contains(r.fileNum)) {
				changedFileNums.add(r.fileNum);
			}
			changedElements.add(r);
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
	
	private int getFileNumForNewObjectNoLock() {
		int r = -1;
		if (elementsByFileNum.size()>0) {
			for (int i = 0; i <= elementsByFileNum.lastKey(); i++) {
				if (elementsByFileNum.get(i).size()<blockSize) {
					r = i;
					break;
				}
			}
			if (r<0) {
				r = (elementsByFileNum.lastKey() + 1);
			}
		}
		if (r<0) {
			r = 0;
		}
		return r;
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
					if (!changedFileNums.contains(element.fileNum)) {
						changedFileNums.add(element.fileNum);
					}
					if (changedElements.contains(element)) {
						changedElements.remove(element);
					}
					changedElements.add(element);
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
			elementsByFileNum.get(r.fileNum).remove(r);
			indexConfig.removeObject(r);
			r.removed = true;
			if (!changedFileNums.contains(r.fileNum)) {
				changedFileNums.add(r.fileNum);
			}
			if (changedElements.contains(r)) {
				changedElements.remove(r);
			}
			changedElements.add(r);
			IndexElement ori = r;
			r = r.copy();
			ori.obj = null;
		} else if (errors!=null) {
			errors.add(new ZStringBuilder("Object with id " + id + " does not exist"));
		}
		return r;
	}
	
	private boolean checkModifiedNoLock(IndexElement element,long modAfter,long modBefore) {
		return
			(modAfter==0L && modBefore==0L) ||
			(modAfter>0L && modBefore>0L && element.modified>modAfter && element.modified<modBefore) || 
			(modAfter>0L && element.modified>modAfter) || 
			(modBefore>0L && element.modified<modBefore);
	}
}
