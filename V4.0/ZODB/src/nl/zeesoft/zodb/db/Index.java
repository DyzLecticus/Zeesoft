package nl.zeesoft.zodb.db;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

public class Index extends Locker {
	private int										blockSize			= 100;
	private String									directory			= "";
	
	private SortedMap<Long,IndexElement>			elementsById		= new TreeMap<Long,IndexElement>();
	private SortedMap<String,IndexElement>			elementsByName		= new TreeMap<String,IndexElement>();
	private SortedMap<Integer,List<IndexElement>>	elementsByFileNum	= new TreeMap<Integer,List<IndexElement>>();
	
	private List<Integer>							changedFileNums		= new ArrayList<Integer>();
	private List<IndexElement>						changedElements		= new ArrayList<IndexElement>();
	
	private boolean									open				= false;
	
	public Index(Messenger msgr,String directory) {
		super(msgr);
		this.directory = directory;
	}

	protected IndexElement addObject(String name,JsFile obj) {
		IndexElement r = null;
		lockMe(this);
		if (name.length()>0 && open) {
			r = addObjectNoLock(name,obj);
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
		return r;
	}

	protected List<IndexElement> getObjectsByNameStartsWith(String start) {
		// TODO: Implement
		return null;
	}
	
	protected List<IndexElement> getObjectsByNameMatches(String match) {
		// TODO: Implement
		return null;
	}
	
	protected void setObject(long id, JsFile obj) {
		lockMe(this);
		if (id>0 && open) {
			setObjectNoLock(id,obj);
		}
		unlockMe(this);
	}

	protected void setObjectName(long id, String name) {
		// TODO: Implement
	}

	protected IndexElement removeObject(long id) {
		IndexElement r = null;
		lockMe(this);
		if (id>0 && open) {
			r = removeObjectNoLock(id);
			if (r!=null) {
				r = r.copy();
			}
		}
		unlockMe(this);
		return r;
	}
	
	protected String getDirectory() {
		return directory;
	}
	
	protected void setOpen(boolean open) {
		lockMe(this);
		this.open = open;
		unlockMe(this);
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
	
	protected List<IndexElement> getChangedElements() {
		List<IndexElement> r = new ArrayList<IndexElement>();
		lockMe(this);
		for (IndexElement elem: changedElements) {
			if (!(elem.added && elem.removed)) {
				r.add(elem.copy());
			}
			elem.added = false;
		}
		changedElements.clear();
		unlockMe(this);
		return r;
	}
	
	private IndexElement addObjectNoLock(String name,JsFile obj) {
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
	
	private void setObjectNoLock(long id, JsFile obj) {
		if (elementsById.containsKey(id)) {
			IndexElement element = elementsById.get(id);
			if (!element.removed) {
				element.obj = obj;
				element.updateModified();
				if (!changedElements.contains(element)) {
					changedElements.add(element);
				}
			}
		}
	}

	private IndexElement removeObjectNoLock(long id) {
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
		}
		return r;
	}
}
