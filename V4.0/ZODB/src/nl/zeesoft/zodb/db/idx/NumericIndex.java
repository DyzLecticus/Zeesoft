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
	protected List<IndexElement> getObjectsNoLock(IndexElement element) {
		return map.get(getPropertyValue(element));
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
		if (operator.equals(DatabaseRequest.OP_EQUALS)) {
			if (invert) {
				for (Entry<BigDecimal,List<IndexElement>> entry: map.entrySet()) {
					if (!entry.getKey().equals(key)) {
						addElementsToList(r,entry.getValue(),ascending);
					}
				}
			} else if (map.containsKey(key)) {
				addElementsToList(r,map.get(key),ascending);
			}
		} else {
			SortedMap<BigDecimal,List<IndexElement>> subMap = getSubmap(key,invert,operator.equals(DatabaseRequest.OP_GREATER_OR_EQUAL));
			for (Entry<BigDecimal,List<IndexElement>> entry: subMap.entrySet()) {
				addElementsToList(r,entry.getValue(),ascending);
			}
		}
		return r;
	}

	@Override
	protected void clearNoLock() {
		map.clear();
	}
	
	private SortedMap<BigDecimal,List<IndexElement>> getSubmap(BigDecimal checkValue, boolean invert, boolean equals) {
		SortedMap<BigDecimal,List<IndexElement>> r = null;
		if (invert) {
			r = new TreeMap<BigDecimal,List<IndexElement>>(map.headMap(checkValue));
			if (!equals && map.containsKey(checkValue)) {
				r.put(checkValue,map.get(checkValue));
			}
		} else {
			r = new TreeMap<BigDecimal,List<IndexElement>>(map.tailMap(checkValue));
			if (!equals) {
				r.remove(checkValue);
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
