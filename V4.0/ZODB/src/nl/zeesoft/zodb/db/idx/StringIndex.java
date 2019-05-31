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
	protected boolean hasObjectNoLock(IndexElement element) {
		boolean r = false;
		ZStringBuilder key = getPropertyValue(element);
		List<IndexElement> elems = map.get(key);
		if (elems!=null) {
			if (listHasObjectNoLock(elems,element)) {
				r = elems.size()>1;
			} else {
				r = elems.size()>0;
			}
		}
		return r;
	}
	
	@Override
	protected void addObjectNoLock(IndexElement element) {
		ZStringBuilder key = getPropertyValue(element);
		List<IndexElement> elems = map.get(key);
		if (elems==null) {
			elems = new ArrayList<IndexElement>();
			map.put(key, elems);
		}
		if (!listHasObjectNoLock(elems,element)) {
			elems.add(element);
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
		if (!invert && operator.equals(DatabaseRequest.OP_EQUALS)) {
			if (map.containsKey(key)) {
				List<IndexElement> elements = map.get(key);
				for (IndexElement element: elements) {
					if (ascending) {
						r.add(element.copy());
					} else {
						r.add(0,element.copy());
					}
				}
			}
		} else {
			for (Entry<ZStringBuilder,List<IndexElement>> entry: map.entrySet()) {
				if (checkStringPropertyValueNoLock(entry.getKey(),invert,operator,key)) {
					for (IndexElement element: entry.getValue()) {
						if (ascending) {
							r.add(element.copy());
						} else {
							r.add(0,element.copy());
						}
					}
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
			} else if (operator.equals(DatabaseRequest.OP_ENDS_WITH)) {
				if (
					(!invert && !propertyValue.endsWith(checkValue)) || 
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
