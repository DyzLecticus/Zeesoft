package nl.zeesoft.zodb.db.idx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.IndexElement;

public class StringIndex extends IndexObject {
	private SortedMap<ZStringBuilder,List<IndexElement>>	map		= new TreeMap<ZStringBuilder,List<IndexElement>>();
	
	protected StringIndex(Messenger msgr,SearchIndex idx) {
		super(msgr,idx);
	}

	@Override
	protected List<IndexElement> getObjectsNoLock(IndexElement element) {
		return map.get(getPropertyValue(element));
	}
	
	@Override
	protected void addObjectNoLock(IndexElement element) {
		ZStringBuilder key = getPropertyValue(element);
		List<IndexElement> elems = map.get(key);
		if (elems==null) {
			elems = new ArrayList<IndexElement>();
			map.put(key, elems);
		}
		elems.add(element);
	}

	@Override
	protected void setObjectNoLock(IndexElement element) {
		List<IndexElement> elems = map.get(getPropertyValue(element));
		if (elems!=null) {
			IndexElement set = getListObjectNoLock(elems,element);
			if (set!=null) {
				set.obj = element.obj;
			}
		}
	}

	@Override
	protected void removeObjectNoLock(IndexElement element) {
		ZStringBuilder key = getPropertyValue(element);
		List<IndexElement> elems = map.get(key);
		if (elems!=null) {
			IndexElement remove = getListObjectNoLock(elems,element);
			if (remove!=null) {
				elems.remove(remove);
				if (elems.size()==0) {
					map.remove(key);
				}
			}
		}
	}

	@Override
	protected List<IndexElement> listObjectsNoLock(boolean ascending, boolean invert, String operator, ZStringBuilder indexValue) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		ZStringBuilder key = getZStringBuilderValue(indexValue);
		if (operator.equals(DatabaseRequest.OP_EQUALS)) {
			if (invert) {
				for (Entry<ZStringBuilder,List<IndexElement>> entry: map.entrySet()) {
					if (!entry.getKey().equals(key)) {
						addElementsToList(r,entry.getValue(),ascending);
					}
				}
			} else if (map.containsKey(key)) {
				addElementsToList(r,map.get(key),ascending);
			}
		} else {
			for (Entry<ZStringBuilder,List<IndexElement>> entry: map.entrySet()) {
				if (checkStringPropertyValueNoLock(entry.getKey(),invert,operator,key)) {
					addElementsToList(r,entry.getValue(),ascending);
				}
			}
		}
		return r;
	}

	@Override
	protected void clearNoLock() {
		map.clear();
	}

	private boolean checkStringPropertyValueNoLock(ZStringBuilder propertyValue, boolean invert, String operator, ZStringBuilder checkValue) {
		boolean r = (propertyValue!=null);
		if (operator!=null && operator.length()>0 && propertyValue!=null && checkValue!=null && checkValue.length()>0) {
			if (operator.equals(DatabaseRequest.OP_CONTAINS)) {
				if ((!invert && !propertyValue.contains(checkValue)) || 
					(invert && propertyValue.contains(checkValue))
					) {
					r = false;
				}
			} else if (operator.equals(DatabaseRequest.OP_STARTS_WITH)) {
				if ((!invert && !propertyValue.startsWith(checkValue)) || 
					(invert && propertyValue.startsWith(checkValue))
					) {
					r = false;
				}
			} else if (operator.equals(DatabaseRequest.OP_ENDS_WITH)) {
				if ((!invert && !propertyValue.endsWith(checkValue)) || 
					(invert && propertyValue.endsWith(checkValue))
					) {
					r = false;
				}
			}
		}
		return r;
	}
	
	private ZStringBuilder getPropertyValue(IndexElement element) {
		ZStringBuilder r = null;
		if (getIndex().getName().equals(IndexConfig.IDX_NAME)) {
			r = element.name;
		} else {
			r = getZStringBuilderValue(element.idxValues.get(getIndex().propertyName));
		}
		return r;
	}

	private ZStringBuilder getZStringBuilderValue(ZStringBuilder value) {
		ZStringBuilder r = null;
		if (value!=null) {
			r = value;
		} else {
			r = new ZStringBuilder();
		}
		return r;
	}
}
