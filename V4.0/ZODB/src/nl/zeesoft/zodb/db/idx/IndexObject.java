package nl.zeesoft.zodb.db.idx;

import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zodb.db.IndexElement;

public abstract class IndexObject extends Locker {
	private SearchIndex		index	= null;
	
	protected IndexObject(Messenger msgr,SearchIndex idx) {
		super(msgr);
		index = idx;
	}
	
	protected SearchIndex getIndex() {
		return index;
	}
	
	protected boolean hasObject(IndexElement element) {
		boolean r = false;
		lockMe(this);
		r = hasObjectNoLock(element);
		unlockMe(this);
		return r;
	}

	protected void addObject(IndexElement element) {
		lockMe(this);
		addObjectNoLock(element);
		unlockMe(this);
	}

	protected void setObject(IndexElement element) {
		lockMe(this);
		setObjectNoLock(element);
		unlockMe(this);
	}

	protected void removeObject(IndexElement element) {
		lockMe(this);
		removeObjectNoLock(element);
		unlockMe(this);
	}

	protected List<IndexElement> listObjects(boolean ascending,boolean invert,String operator,ZStringBuilder indexValue) {
		List<IndexElement> r = null;
		lockMe(this);
		r = this.listObjectsNoLock(ascending, invert, operator, indexValue);
		unlockMe(this);
		return r;
	}
	
	protected void clear() {
		lockMe(this);
		clearNoLock();
		unlockMe(this);
	}

	protected abstract boolean hasObjectNoLock(IndexElement element); 
	protected abstract void addObjectNoLock(IndexElement element);
	protected abstract void setObjectNoLock(IndexElement element);
	protected abstract void removeObjectNoLock(IndexElement element);
	protected abstract List<IndexElement> listObjectsNoLock(boolean ascending,boolean invert,String operator,ZStringBuilder indexValue);
	protected abstract void clearNoLock();
	
	protected void destroy() {
		lockMe(this);
		clearNoLock();
		index = null;
		unlockMe(this);
	}
	
	protected boolean listHasObjectNoLock(List<IndexElement> elements,IndexElement element) {
		return getListObjectNoLock(elements,element) != null;
	}

	protected IndexElement getListObjectNoLock(List<IndexElement> elements,IndexElement element) {
		IndexElement r = null;
		for (IndexElement elem: elements) {
			if (elem.id==element.id) {
				r = elem;
				break;
			}
		}
		return r;
	}
}
