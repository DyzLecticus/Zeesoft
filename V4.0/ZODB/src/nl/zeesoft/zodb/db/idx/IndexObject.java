package nl.zeesoft.zodb.db.idx;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.LockedCode;
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
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				boolean r = false;
				List<IndexElement> elements = getObjectsSafeNoLock(element);
				for (IndexElement elem: elements) {
					if (elem.id!=element.id) {
						r = true;
					}
				}
				return r;
			}
		};
		return (boolean) doLocked(this,code);
	}

	protected void addObjects(List<IndexElement> elements) {
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				if (!index.added) {
					for (IndexElement element: elements) {
						if (index.objectNamePrefix.equals(IndexConfig.PFX_OBJ) || element.name.startsWith(index.objectNamePrefix)) {
							addObjectNoLock(element);
						}
					}
				}
				return null;
			}
		};
		doLocked(this,code);
	}

	protected void addObject(IndexElement element) {
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				addObjectNoLock(element);
				return null;
			}
		};
		doLocked(this,code);
	}

	protected void setObject(IndexElement element) {
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				setObjectNoLock(element);
				return null;
			}
		};
		doLocked(this,code);
	}
	
	protected void removeObject(IndexElement element) {
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				removeObjectNoLock(element);
				return null;
			}
		};
		doLocked(this,code);
	}

	@SuppressWarnings("unchecked")
	protected List<IndexElement> listObjects(boolean ascending,boolean invert,String operator,ZStringBuilder indexValue) {
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				return listObjectsNoLock(ascending, invert, operator, indexValue);
			}
		};
		return (List<IndexElement>) doLocked(this,code);
	}
	
	protected void clear() {
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				clearNoLock();
				return null;
			}
		};
		doLocked(this,code);
	}

	protected abstract List<IndexElement> getObjectsNoLock(IndexElement element); 
	protected abstract void addObjectNoLock(IndexElement element);
	protected abstract void setObjectNoLock(IndexElement element);
	protected abstract void removeObjectNoLock(IndexElement element);
	protected abstract List<IndexElement> listObjectsNoLock(boolean ascending,boolean invert,String operator,ZStringBuilder indexValue);
	protected abstract void clearNoLock();

	protected void addElementsToList(List<IndexElement> list,List<IndexElement> elements,boolean ascending) {
		for (IndexElement element: elements) {
			if (ascending) {
				list.add(element.copy());
			} else {
				list.add(0,element.copy());
			}
		}
	}
	
	protected void destroy() {
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				clearNoLock();
				index = null;
				return null;
			}
		};
		doLocked(this,code);
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
	
	private List<IndexElement> getObjectsSafeNoLock(IndexElement element) {
		List<IndexElement> r = getObjectsNoLock(element);
		if (r==null) {
			r = new ArrayList<IndexElement>();
		}
		return r;
	}

}
