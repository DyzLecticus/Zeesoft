package nl.zeesoft.zodb.db.idx;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.IndexElement;

public class NumericIndex extends IndexObject {
	private SortedMap<BigDecimal,List<IndexElement>>	map		= new TreeMap<BigDecimal,List<IndexElement>>();
	
	protected NumericIndex(Messenger msgr,SearchIndex idx) {
		super(msgr,idx);
	}

	@Override
	protected boolean hasObjectNoLock(IndexElement element) {
		return map.containsKey(getPropertyValue(element));
	}
	
	@Override
	protected void addObjectNoLock(IndexElement element) {
		BigDecimal key = getPropertyValue(element);
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
		BigDecimal key = getPropertyValue(element);
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
		BigDecimal key = getBigDecimalValue(indexValue);
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
			for (Entry<BigDecimal,List<IndexElement>> entry: map.entrySet()) {
				if (checkNumericPropertyValueNoLock(entry.getKey(),invert,operator,key)) {
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
	
	private BigDecimal getPropertyValue(IndexElement element) {
		BigDecimal r = null;
		if (getIndex().getName().equals(IndexConfig.IDX_MODIFIED)) {
			r = new BigDecimal(element.modified);
		} else {
			r = getBigDecimalValue(element.idxValues.get(getIndex().propertyName));
		}
		return r;
	}
	
	private BigDecimal getBigDecimalValue(ZStringBuilder value) {
		BigDecimal r = null;
		if (value!=null && value.length()>0) {
			r = new BigDecimal(value.toCharArray());
		} else {
			r = new BigDecimal("0");
		}
		return r;
	}
}
