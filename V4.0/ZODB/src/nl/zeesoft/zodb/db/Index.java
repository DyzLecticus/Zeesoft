package nl.zeesoft.zodb.db;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
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
				readObjectNoLock(r);
				r = r.copy();
			}
		}
		unlockMe(this);
		return r;
	}
	
	protected IndexElement getObjectByName(String name) {
		IndexElement r = null;
		lockMe(this);
		if (name.length()>0 && open) {
			r = elementsByName.get(name);
			if (r!=null) {
				readObjectNoLock(r);
				r = r.copy();
			}
		}
		unlockMe(this);
		return r;
	}
	
	protected SortedMap<String,Long> listObjects(int start, int max) {
		return listObjects(start,max,null,null,null);
	}

	protected SortedMap<String,Long> listObjectsThatStartWith(String startWith,int start, int max) {
		return listObjects(start,max,startWith,null,null);
	}

	protected SortedMap<String,Long> listObjectsThatMatch(String contains,int start, int max) {
		return listObjects(start,max,null,contains,null);
	}
	
	protected List<IndexElement> getObjectsByNameStartsWith(String startsWith) {
		return getObjectsByName(startsWith,null,null);
	}
	
	protected List<IndexElement> getObjectsByNameContains(String contains) {
		return getObjectsByName(null,contains,null);
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
			}
		}
		unlockMe(this);
		return r;
	}
	
	protected String getFileDirectory() {
		return db.getFullIndexDir();
	}
	
	protected String getObjectDirectory() {
		return db.getFullObjectDir();
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

	private SortedMap<String,Long> listObjects(int start, int max,String startsWith,String contains,String endsWith) {
		SortedMap<String,Long> r = new TreeMap<String,Long>();
		if (start<0) {
			start = 0;
		}
		if (max<1) {
			max = 1;
		}
		List<IndexElement> elements = null;
		lockMe(this);
		if (open) {
			if (
				(startsWith!=null && startsWith.length()>0) ||
				(contains!=null && contains.length()>0) ||
				(endsWith!=null && endsWith.length()>0)
				) {
				elements = listObjectsByNameNoLock(startsWith,contains,endsWith);
			} else {
				elements = new ArrayList<IndexElement>(elementsByName.values());
			}
		}
		if (elements!=null && start<=(elements.size() - 1)) {
			int end = start + max;
			if (end>elements.size()) {
				end = elements.size();
			}
			for (int i = start; i < end; i++) {
				IndexElement element = elements.get(i);
				r.put(element.name,element.id);
			}
		}
		unlockMe(this);
		return r;
	}
	
	private List<IndexElement> getObjectsByName(String startsWith,String contains,String endsWith) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		List<IndexElement> read = new ArrayList<IndexElement>();
		lockMe(this);
		if (open) {
			r = listObjectsByNameNoLock(startsWith,contains,endsWith);
			for (IndexElement element: r) {
				if (element.obj==null) {
					read.add(element);
				}
			}
		}
		unlockMe(this);
		if (read.size()>0) {
			for (IndexElement element: read) {
				lockMe(this);
				readObjectNoLock(element);
				unlockMe(this);
			}
		}
		return r;
	}

	private List<IndexElement> listObjectsByNameNoLock(String startsWith,String contains,String endsWith) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		for (String name: elementsByName.keySet()) {
			if (
				(startsWith==null || startsWith.length()==0 || name.startsWith(startsWith)) &&
				(contains==null || contains.length()==0 || name.contains(contains)) &&
				(endsWith==null || endsWith.length()==0 || name.endsWith(endsWith))
				) {
				IndexElement element = elementsByName.get(name);
				r.add(element.copy());
			}
		}
		return r;
	}
	
	private void readObjectNoLock(IndexElement element) {
		if (element.obj==null) {
			String fileName = getObjectDirectory() + element.id + ".json";
			JsFile obj = new JsFile();
			ZStringBuilder err = obj.fromFile(fileName);
			if (err.length()>0) {
				getMessenger().error(this,"Failed to read object: " + err);
			} else {
				if (obj.rootElement==null) {
					obj.rootElement = new JsElem();
				}
				element.obj = obj;
			}
		}
	}
	
	private IndexElement addObjectNoLock(String name,JsFile obj,List<ZStringBuilder> errors) {
		IndexElement r = null;
		if (!elementsByName.containsKey(name)) {
			r = new IndexElement();
			r.id = getNewUid();
			r.name = name;
			r.obj = obj;
			r.fileNum = getFileNumForNewObject();
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
		
	private long getNewUid() {
		long r = 1;
		if (elementsById.size()>0) {
			r = (elementsById.lastKey() + 1L);
		}
		return r;
	}
	
	private int getFileNumForNewObject() {
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
}
