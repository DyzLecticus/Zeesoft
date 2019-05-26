package nl.zeesoft.zodb.db;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringEncoder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zodb.db.idx.IndexConfig;
import nl.zeesoft.zodb.db.idx.SearchIndex;

public class Index extends Locker {
	private int										blockSize			= 100;
	
	private Database								db					= null;
	private IndexConfig								indexConfig			= null;
	
	private SortedMap<Long,IndexElement>			elementsById		= new TreeMap<Long,IndexElement>();
	private SortedMap<String,IndexElement>			elementsByName		= new TreeMap<String,IndexElement>();
	private SortedMap<Integer,List<IndexElement>>	elementsByFileNum	= new TreeMap<Integer,List<IndexElement>>();
	
	private List<Integer>							changedFileNums		= new ArrayList<Integer>();
	private List<IndexElement>						changedElements		= new ArrayList<IndexElement>();
	
	private boolean									open				= false;
	
	protected Index(Messenger msgr,Database db,IndexConfig indexConfig) {
		super(msgr);
		this.db = db;
		this.indexConfig = indexConfig;
	}
	
	protected StringBuilder getKey() {
		return db.getKey();
	}
	
	protected IndexElement addObject(String name,JsFile obj,List<ZStringBuilder> errors) {
		IndexElement r = null;
		lockMe(this);
		if (name.length()>0 && open) {
			r = addObjectNoLock(name,obj,errors);
			if (r!=null) {
				r = r.copy();
			}
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
	
	protected IndexElement getObjectByName(String name) {
		IndexElement r = null;
		lockMe(this);
		if (name.length()>0 && open) {
			r = elementsByName.get(name);
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

	protected List<IndexElement> listObjectsUseIndex(int start, int max,boolean ascending,String indexName,boolean invert,String operator,String value,long modAfter,long modBefore,List<Integer> data) {
		List<IndexElement> r = null;
		lockMe(true);
		r = listObjectsUseIndexNoLock(start,max,ascending,indexName,invert,operator,value,modAfter,modBefore,data);
		unlockMe(true);
		return r;
	}
	
	protected List<IndexElement> listObjects(int start, int max,long modAfter,long modBefore,List<Integer> data) {
		return listObjects(start,max,null,null,null,modAfter,modBefore,data);
	}

	protected List<IndexElement> listObjectsThatStartWith(String startWith,int start, int max,long modAfter,long modBefore,List<Integer> data) {
		return listObjects(start,max,startWith,null,null,modAfter,modBefore,data);
	}

	protected List<IndexElement> listObjectsThatContain(String contains,int start, int max,long modAfter,long modBefore,List<Integer> data) {
		return listObjects(start,max,null,contains,null,modAfter,modBefore,data);
	}
	
	protected List<IndexElement> getObjectsUseIndex(boolean ascending,String indexName,boolean invert,String operator,String value,long modAfter,long modBefore) {
		return getObjectsUsingIndex(ascending,indexName,invert,operator,value,modAfter,modBefore);
	}
	
	protected List<IndexElement> getObjectsByNameStartsWith(String startsWith,long modAfter,long modBefore) {
		return getObjectsByName(startsWith,null,null,modAfter,modBefore);
	}
	
	protected List<IndexElement> getObjectsByNameContains(String contains,long modAfter,long modBefore) {
		return getObjectsByName(null,contains,null,modAfter,modBefore);
	}
	
	protected void setObject(long id, JsFile obj, List<ZStringBuilder> errors) {
		lockMe(this);
		if (id>0 && open) {
			setObjectNoLock(id,obj,errors);
		}
		unlockMe(this);
	}

	protected void setObjectName(long id, String name, List<ZStringBuilder> errors) {
		name = Database.removeControlCharacters(name);
		IndexElement element = null;
		if (indexConfig.objectHasIndexes(name)) {
			boolean read = false;
			lockMe(this);
			if (open) {
				element = elementsById.get(id);
				if (element!=null && element.obj==null) {
					element = element.copy();
					read = true;
				}
			}
			unlockMe(this);
			if (read) {
				readObject(element);
			}
		}
		lockMe(this);
		if (open) {
			element = elementsById.get(id);
			if (element!=null && !element.removed) {
				if (!element.name.equals(name)) {
					if (elementsByName.get(name)==null) {
						IndexElement copy = element.copy();
						copy.name = name;
						copy.idxValues = indexConfig.getIndexValuesForObject(copy.name,copy.obj);
						if (checkUniqueIndexForObjectNoLock(copy,element.name,errors)) {
							elementsByName.remove(element.name);
							element.name = name;
							elementsByName.put(element.name,element);
							element.idxValues = copy.idxValues;
							element.updateModified();
							if (!changedFileNums.contains(element.fileNum)) {
								changedFileNums.add(element.fileNum);
							}
						}
					} else if (errors!=null) {
						errors.add(new ZStringBuilder("Object named '" + name + "' already exists"));
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
			if (r!=null) {
				r = r.copy();
				r.obj = null;
			}
		}
		unlockMe(this);
		return r;
	}

	protected List<IndexElement> removeObjectsThatStartWith(String startsWith,long modAfter,long modBefore,List<ZStringBuilder> errors) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		lockMe(this);
		if (startsWith.length()>0 && open) {
			List<IndexElement> elements = listObjectsByNameNoLock(startsWith,"","",modAfter,modBefore);
			for (IndexElement element: elements) {
				removeObjectNoLock(element.id,errors);
				r.add(element.copy());
				element.obj = null;
			}
		}
		unlockMe(this);
		return r;
	}
	
	protected List<IndexElement> removeObjectsThatContain(String contains,long modAfter,long modBefore,List<ZStringBuilder> errors) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		lockMe(this);
		if (contains.length()>0 && open) {
			List<IndexElement> elements = listObjectsByNameNoLock("",contains,"",modAfter,modBefore);
			for (IndexElement element: elements) {
				removeObjectNoLock(element.id,errors);
				r.add(element.copy());
				element.obj = null;
			}
		}
		unlockMe(this);
		return r;
	}
	
	protected boolean readObject(IndexElement element) {
		boolean r = true;
		lockMe(this);
		IndexElement copy = elementsById.get(element.id).copy();
		r = open;
		unlockMe(this);
		if (copy.obj==null) {
			String fileName = getObjectDirectory() + copy.id + ".txt";
			ZStringEncoder decoder = new ZStringEncoder();
			ZStringBuilder err = decoder.fromFile(fileName);
			if (err.length()>0) {
				getMessenger().error(this,"Failed to read object " + element.id + ": " + err);
			} else {
				decoder.decodeKey(db.getKey(),0);
				JsFile obj = new JsFile();
				obj.fromStringBuilder(decoder);
				if (obj.rootElement==null) {
					getMessenger().error(this,"Object " + element.id + " has been corrupted");
					obj.rootElement = new JsElem();
				}
				lockMe(this);
				elementsById.get(element.id).obj = obj;
				element.obj = obj;
				r = open;
				unlockMe(this);
			}
		}
		return r;
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
		for (IndexElement element: elements) {
			readObject(element);
		}
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
				if (!changedElements.contains(element)) {
					changedElements.add(element);
				}
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
			for (IndexElement element: elementsById.values()) {
				element.idxValues = indexConfig.getIndexValuesForObject(element.name,element.obj);
			}
		}
		this.open = open;
		if (!open) {
			elementsById.clear();
			elementsByName.clear();
			elementsByFileNum.clear();
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
		}
		unlockMe(this);
	}

	protected void addFileElements(Integer fileNum,List<IndexElement> elements) {
		lockMe(this);
		if (open) {
			for (IndexElement elem: elements) {
				elementsById.put(elem.id,elem);
				elementsByName.put(elem.name,elem);
				elementsByFileNum.put(fileNum,elements);
			}
		}
		unlockMe(this);
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

	private List<IndexElement> listObjects(int start, int max,String startsWith,String contains,String endsWith,long modAfter,long modBefore,List<Integer> data) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		lockMe(this);
		if (open) {
			SortedMap<String,Long> list = listObjectsNoLock(start, max, startsWith, contains, endsWith, modAfter, modBefore, data);
			for (Entry<String,Long> entry: list.entrySet()) {
				r.add(elementsById.get(entry.getValue()).copy());
			}
		}
		unlockMe(this);
		return r;
	}

	private List<IndexElement> getObjectsByName(String startsWith,String contains,String endsWith,long modAfter,long modBefore) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		List<IndexElement> read = new ArrayList<IndexElement>();
		lockMe(this);
		if (open) {
			r = listObjectsByNameNoLock(startsWith,contains,endsWith,modAfter,modBefore);
		}
		unlockMe(this);
		for (IndexElement element: r) {
			if (element.obj==null) {
				read.add(element);
			}
		}
		if (read.size()>0) {
			for (IndexElement element: read) {
				if (!readObject(element)) {
					break;
				}
			}
		}
		return r;
	}

	private List<IndexElement> getObjectsUsingIndex(boolean ascending,String indexName,boolean invert,String operator,String value,long modAfter,long modBefore) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		List<IndexElement> read = new ArrayList<IndexElement>();
		lockMe(this);
		if (open) {
			r = listObjectsUseIndexNoLock(indexName,invert,operator,value,modAfter,modBefore);
		}
		unlockMe(this);
		if (r.size()>0) {
			if (!ascending) {
				List<IndexElement> temp = new ArrayList<IndexElement>(r);
				r.clear();
				for (IndexElement element: temp) {
					r.add(0,element);
					if (element.obj==null) {
						read.add(element);
					}
				}
			} else {
				for (IndexElement element: r) {
					if (element.obj==null) {
						read.add(element);
					}
				}
			}
		}
		if (read.size()>0) {
			for (IndexElement element: read) {
				if (!readObject(element)) {
					break;
				}
			}
		}
		return r;
	}

	private List<IndexElement> listObjectsUseIndexNoLock(int start, int max,boolean ascending,String indexName,boolean invert,String operator,String value,long modAfter,long modBefore,List<Integer> data) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		if (start<0) {
			start = 0;
		}
		if (max<1) {
			max = 1;
		}
		List<IndexElement> elements = listObjectsUseIndexNoLock(indexName,invert,operator,value,modAfter,modBefore);
		if (!ascending) {
			List<IndexElement> temp = new ArrayList<IndexElement>(elements);
			elements.clear();
			for (IndexElement element: temp) {
				elements.add(0,element);
			}
		}
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

	private List<IndexElement> listObjectsUseIndexNoLock(String indexName,boolean invert,String operator,String value,long modAfter,long modBefore) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		if (open) {
			SearchIndex index = indexConfig.getListIndex(indexName);
			if (index!=null) {
				List<IndexElement> elements = null;
				if (indexName.equals(IndexConfig.IDX_NAME) || indexName.equals(IndexConfig.IDX_MODIFIED)) {
					elements = listObjectsByNameNoLock(null,null,null,modAfter,modBefore);
				} else {
					elements = listObjectsByNameNoLock(index.objectNamePrefix,null,null,modAfter,modBefore);
				}
				if (elements.size()>0) {
					if (index.numeric) {
						BigDecimal numVal = null;
						if (value!=null && value.length()>0) {
							try {
								numVal = new BigDecimal(value);
							} catch (NumberFormatException e) {
								// Ignore
							}
						}
						r = listObjectsUseNumericIndexNoLock(elements,index,invert,operator,numVal);
					} else {
						r = listObjectsUseStringIndexNoLock(elements,index,invert,operator,value);
					}
				}
			}
		}
		return r;
	}

	private List<IndexElement> listObjectsUseNumericIndexNoLock(List<IndexElement> elements,SearchIndex index,boolean invert,String operator,BigDecimal value) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		SortedMap<BigDecimal,List<IndexElement>> map = new TreeMap<BigDecimal,List<IndexElement>>();
		for (IndexElement element: elements) {
			BigDecimal key = null;
			if (index==null) {
				key = new BigDecimal(element.modified);
			} else {
				key = new BigDecimal("0");
				String strVal = element.idxValues.get(index.propertyName);
				if (strVal!=null) {
					try {
						key = new BigDecimal(strVal);
					} catch (NumberFormatException e) {
						key = new BigDecimal("0");
					}
				}
			}
			if (checkNumericPropertyValueNoLock(key,invert,operator,value)) {
				List<IndexElement> v = map.get(key);
				if (v==null) {
					v = new ArrayList<IndexElement>();
					map.put(key,v);
				}
				v.add(element);
			}
		}
		for (Entry<BigDecimal,List<IndexElement>> entry: map.entrySet()) {
			for (IndexElement element: entry.getValue()) {
				r.add(element);
			}
		}
		return r;
	}
	
	private List<IndexElement> listObjectsUseStringIndexNoLock(List<IndexElement> elements,SearchIndex index,boolean invert,String operator,String value) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		SortedMap<String,List<IndexElement>> map = new TreeMap<String,List<IndexElement>>();
		for (IndexElement element: elements) {
			String key = element.name;
			if (index!=null) {
				key = element.idxValues.get(index.propertyName);
			}
			if (key==null) {
				key = "";
			}
			if (checkStringPropertyValueNoLock(key,invert,operator,value)) {
				List<IndexElement> v = map.get(key);
				if (v==null) {
					v = new ArrayList<IndexElement>();
					map.put(key,v);
				}
				v.add(element);
			}
		}
		for (Entry<String,List<IndexElement>> entry: map.entrySet()) {
			for (IndexElement element: entry.getValue()) {
				r.add(element);
			}
		}
		return r;
	}

	private boolean checkNumericPropertyValueNoLock(BigDecimal propertyValue, boolean invert, String operator, BigDecimal checkValue) {
		boolean r = (propertyValue!=null);
		if (operator!=null && operator.length()>0 && propertyValue!=null && checkValue!=null) {
			if (operator.equals(DatabaseRequest.OP_EQUALS)) {
				if (
					(!invert && propertyValue.compareTo(checkValue)!=0) || 
					(invert && propertyValue.compareTo(checkValue)==0)
					) {
					r = false;
				}
			} else if (operator.equals(DatabaseRequest.OP_GREATER)) {
				if (
					(!invert && propertyValue.compareTo(checkValue)<=0) || 
					(invert && propertyValue.compareTo(checkValue)>0)
					) {
					r = false;
				}
			} else if (operator.equals(DatabaseRequest.OP_GREATER_OR_EQUAL)) {
				if (
					(!invert && propertyValue.compareTo(checkValue)<0) || 
					(invert && propertyValue.compareTo(checkValue)>=0)
					) {
					r = false;
				}
			}
		}
		return r;
	}
	
	private boolean checkStringPropertyValueNoLock(String propertyValue, boolean invert, String operator, String checkValue) {
		boolean r = (propertyValue!=null);
		if (operator!=null && operator.length()>0 && propertyValue!=null && checkValue!=null) {
			if (operator.equals(DatabaseRequest.OP_EQUALS)) {
				if (
					(!invert && !propertyValue.equals(checkValue)) || 
					(invert && propertyValue.equals(checkValue))
					) {
					r = false;
				}
			} else if (operator.equals(DatabaseRequest.OP_CONTAINS)) {
				if (
					(!invert && !propertyValue.contains(checkValue)) || 
					(invert && propertyValue.contains(checkValue))
					) {
					r = false;
				}
			} else if (operator.equals(DatabaseRequest.OP_STARTS_WITH)) {
				if (
					(!invert && !propertyValue.startsWith(checkValue)) || 
					(invert && propertyValue.startsWith(checkValue))
					) {
					r = false;
				}
			}
		}
		return r;
	}
	
	private SortedMap<String,Long> listObjectsNoLock(int start, int max,String startsWith,String contains,String endsWith,long modAfter,long modBefore,List<Integer> data) {
		SortedMap<String,Long> r = new TreeMap<String,Long>();
		if (start<0) {
			start = 0;
		}
		if (max<1) {
			max = 1;
		}
		List<IndexElement> elements = null;
		if (open) {
			if (
				(startsWith!=null && startsWith.length()>0) ||
				(contains!=null && contains.length()>0) ||
				(endsWith!=null && endsWith.length()>0)
				) {
				elements = listObjectsByNameNoLock(startsWith,contains,endsWith,modAfter,modBefore);
			} else {
				elements = new ArrayList<IndexElement>();
				for (IndexElement element: elementsByName.values()) {
					if (checkModifiedNoLock(element,modAfter,modBefore)) {
						elements.add(element);
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
					IndexElement element = elements.get(i);
					r.put(element.name,element.id);
				}
			}
		}
		return r;
	}

	private List<IndexElement> listObjectsByNameNoLock(String startsWith,String contains,String endsWith,long modAfter,long modBefore) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		for (String name: elementsByName.keySet()) {
			if (
				(startsWith==null || startsWith.length()==0 || name.startsWith(startsWith)) &&
				(contains==null || contains.length()==0 || name.contains(contains)) &&
				(endsWith==null || endsWith.length()==0 || name.endsWith(endsWith))
				) {
				IndexElement element = elementsByName.get(name);
				if (checkModifiedNoLock(element,modAfter,modBefore)) {
					r.add(element.copy());
				}
			}
		}
		return r;
	}
	
	private IndexElement addObjectNoLock(String name,JsFile obj,List<ZStringBuilder> errors) {
		name = Database.removeControlCharacters(name);
		IndexElement r = null;
		if (!elementsByName.containsKey(name)) {
			IndexElement element = new IndexElement();
			element.name = name;
			element.obj = obj;
			element.idxValues = indexConfig.getIndexValuesForObject(name,obj);
			if (checkUniqueIndexForObjectNoLock(element,"",errors)) {
				r = new IndexElement();
				r.id = getNewUidNoLock();
				r.name = name;
				r.obj = obj;
				r.idxValues = element.idxValues;
				r.fileNum = getFileNumForNewObjectNoLock();
				r.added = true;
				
				elementsById.put(r.id,r);
				elementsByName.put(r.name,r);
				List<IndexElement> list = elementsByFileNum.get(r.fileNum);
				if (list==null) {
					list = new ArrayList<IndexElement>();
					elementsByFileNum.put(r.fileNum,list);
				}
				list.add(r);
				
				if (!changedFileNums.contains(r.fileNum)) {
					changedFileNums.add(r.fileNum);
				}
				changedElements.add(r);
			}
		} else if (errors!=null) {
			errors.add(new ZStringBuilder("Object named '" + name + "' already exists"));
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
				if (checkUniqueIndexForObjectNoLock(copy,copy.name,errors)) {
					element.obj = obj;
					element.idxValues = copy.idxValues;
					element.updateModified();
					if (!changedFileNums.contains(element.fileNum)) {
						changedFileNums.add(element.fileNum);
					}
					if (!changedElements.contains(element)) {
						changedElements.add(element);
					}
				}
			}
		} else if (errors!=null) {
			errors.add(new ZStringBuilder("Object with id " + id + " does not exist"));
		}
	}

	private boolean checkUniqueIndexForObjectNoLock(IndexElement element,String oldName,List<ZStringBuilder> errors) {
		boolean ok = true;
		for (SearchIndex index: indexConfig.getUniqueIndexesForObjectName(element.name)) {
			String strVal = element.idxValues.get(index.propertyName);
			List<IndexElement> elements = listObjectsUseIndexNoLock(index.getName(),false,DatabaseRequest.OP_EQUALS,strVal,0L,0L);
			IndexElement duplicate = null;
			if (elements.size()==1) {
				duplicate = elements.get(0);
				if (duplicate.id==element.id) {
					duplicate = null;
				}
			} else if (elements.size()>1) {
				for (IndexElement elem: elements) {
					if (elem.id!=element.id) {
						duplicate = elem;
						break;
					}
				}
			}
			if (duplicate!=null) {
				if (errors!=null) {
					if (oldName.length()>0) {
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
			elementsByName.remove(r.name);
			elementsByFileNum.get(r.fileNum).remove(r);
			r.removed = true;
			if (!changedFileNums.contains(r.fileNum)) {
				changedFileNums.add(r.fileNum);
			}
			if (!changedElements.contains(r)) {
				changedElements.add(r);
			}
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
