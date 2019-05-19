package nl.zeesoft.zodb.db;

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

public class Index extends Locker {
	private int										blockSize			= 100;
	
	private Database								db					= null;
	
	private SortedMap<Long,IndexElement>			elementsById		= new TreeMap<Long,IndexElement>();
	private SortedMap<String,IndexElement>			elementsByName		= new TreeMap<String,IndexElement>();
	private SortedMap<Integer,List<IndexElement>>	elementsByFileNum	= new TreeMap<Integer,List<IndexElement>>();
	
	private List<Integer>							changedFileNums		= new ArrayList<Integer>();
	private List<IndexElement>						changedElements		= new ArrayList<IndexElement>();
	
	private boolean									open				= false;
	
	public Index(Messenger msgr,Database db) {
		super(msgr);
		this.db = db;
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
	
	protected List<IndexElement> listObjects(int start, int max,long modAfter,long modBefore,List<Integer> data) {
		return listObjects(start,max,null,null,null,modAfter,modBefore,data);
	}

	protected List<IndexElement> listObjectsThatStartWith(String startWith,int start, int max,long modAfter,long modBefore,List<Integer> data) {
		return listObjects(start,max,startWith,null,null,modAfter,modBefore,data);
	}

	protected List<IndexElement> listObjectsThatContain(String contains,int start, int max,long modAfter,long modBefore,List<Integer> data) {
		return listObjects(start,max,null,contains,null,modAfter,modBefore,data);
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
		lockMe(this);
		if (open) {
			IndexElement element = elementsById.get(id);
			if (element!=null && !element.removed) {
				if (!element.name.equals(name)) {
					if (elementsByName.get(name)==null) {
						elementsByName.remove(element.name);
						element.name = name;
						elementsByName.put(element.name,element);
						element.updateModified();
						if (!changedFileNums.contains(element.fileNum)) {
							changedFileNums.add(element.fileNum);
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
			elements.add(element.copy());
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
		lockMe(this);
		List<IndexElement> elements = new ArrayList<IndexElement>(elementsById.values());
		for (IndexElement element: elements) {
			if (!changedElements.contains(element)) {
				changedElements.add(element);
			}
		}
		unlockMe(this);
	}
	
	protected void setOpen(boolean open) {
		lockMe(this);
		this.open = open;
		unlockMe(this);
		db.stateChanged(open);
		if (!open) {
			db = null;
		}
	}
	
	protected boolean isOpen() {
		boolean r = false;
		lockMe(this);
		r = open;
		unlockMe(this);
		return r;
	}
	
	protected void addFileElements(Integer fileNum,List<IndexElement> elements) {
		lockMe(this);
		for (IndexElement elem: elements) {
			elementsById.put(elem.id,elem);
			elementsByName.put(elem.name,elem);
			elementsByFileNum.put(fileNum,elements);
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
			for (IndexElement element: r) {
				if (element.obj==null) {
					read.add(element);
				}
			}
		}
		unlockMe(this);
		if (read.size()>0) {
			for (IndexElement element: read) {
				if (!readObject(element)) {
					break;
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
		IndexElement r = null;
		if (!elementsByName.containsKey(name)) {
			r = new IndexElement();
			r.id = getNewUidNoLock();
			r.name = name;
			r.obj = obj;
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
				element.obj = obj;
				element.updateModified();
				if (!changedElements.contains(element)) {
					changedElements.add(element);
				}
			}
		} else if (errors!=null) {
			errors.add(new ZStringBuilder("Object with id " + id + " does not exist"));
		}
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
