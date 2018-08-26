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
	
	private SortedMap<Long,IndexElement>			elementsById		= new TreeMap<Long,IndexElement>();
	private SortedMap<String,IndexElement>			elementsByName		= new TreeMap<String,IndexElement>();
	private SortedMap<Integer,List<IndexElement>>	elementsByFileNum	= new TreeMap<Integer,List<IndexElement>>();
	
	private List<Integer>							changedFileNums		= new ArrayList<Integer>();
	private List<IndexElement>						changedElements		= new ArrayList<IndexElement>();
	
	public Index(Messenger msgr) {
		super(msgr);
	}

	public IndexElement addObject(String name,JsFile obj) {
		IndexElement r = null;
		lockMe(this);
		r = addObjectNoLock(name,obj);
		unlockMe(this);
		return r.copy();
	}
	
	public IndexElement getObjectById(long id) {
		IndexElement r = null;
		lockMe(this);
		r = elementsById.get(id);
		unlockMe(this);
		return r.copy();
	}
	
	public IndexElement getObjectByName(String name) {
		IndexElement r = null;
		lockMe(this);
		r = elementsByName.get(name);
		unlockMe(this);
		return r.copy();
	}
	
	public List<IndexElement> getObjectsByNameStartsWith(String start) {
		return null;
	}
	
	public List<IndexElement> getObjectsByNameMatches(String match) {
		return null;
	}
	
	public void setObject(long id, JsFile obj) {
		lockMe(this);
		setObjectNoLock(id,obj);
		unlockMe(this);
	}

	public IndexElement removeObject(long id) {
		IndexElement r = null;
		lockMe(this);
		r = removeObjectNoLock(id);
		unlockMe(this);
		return r.copy();
	}
	
	protected SortedMap<Integer,List<IndexElement>> getChangedFiles() {
		SortedMap<Integer,List<IndexElement>> r = new TreeMap<Integer,List<IndexElement>>();
		for (Integer num: changedFileNums) {
			List<IndexElement> list = new ArrayList<IndexElement>();
			for (IndexElement elem: elementsByFileNum.get(num)) {
				list.add(elem.copy());
			}
			r.put(num,list);
		}
		changedFileNums.clear();
		return r;
	}
	
	protected List<IndexElement> getChangedElements() {
		List<IndexElement> r = new ArrayList<IndexElement>();
		for (IndexElement elem: changedElements) {
			r.add(elem.copy());
		}
		changedElements.clear();
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
		long r = 0;
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
			element.obj = obj;
			element.updateModified();
			if (!changedElements.contains(element)) {
				changedElements.add(element);
			}
		}
	}

	private IndexElement removeObjectNoLock(long id) {
		IndexElement r = new IndexElement();
		if (elementsById.containsKey(id)) {
			r = elementsById.remove(id);
			elementsByName.remove(r.name);
			elementsByFileNum.get(r.fileNum).remove(r);
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
