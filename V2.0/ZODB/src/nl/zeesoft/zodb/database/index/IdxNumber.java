package nl.zeesoft.zodb.database.index;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.MdlNumber;

public class IdxNumber extends IdxObject {
	private MdlNumber number = null;

	private SortedMap<BigDecimal,List<Long>> numberIdListMap	= new TreeMap<BigDecimal,List<Long>>();
	
	public IdxNumber(MdlNumber num) {
		number = num;
		setDirName(number.getIndexDirName());
	}
	
	public void addObject(DbDataObject obj) {
		BigDecimal key = getKeyForObject(obj);
		if (!numberIdListMap.containsKey(key)) {
			List<Long> idList = new ArrayList<Long>();
			idList.add(obj.getId());
			numberIdListMap.put(key, idList);
			addedIndexKey(key.toString());
		} else {
			List<Long> idList = numberIdListMap.get(key);
			if (!idList.contains(obj.getId())) {
				idList.add(obj.getId());
				updatedIndexKey(key.toString());
			}
		}
	}

	private BigDecimal getKeyForObject(DbDataObject obj) {
		StringBuilder val = obj.getPropertyValue(number.getName());
		if (val==null||val.length()==0) {
			val = new StringBuilder("0");
		}
		return new BigDecimal(val.toString());
	}
	
	public void removeObject(DbDataObject obj) {
		BigDecimal key = getKeyForObject(obj);
		if (numberIdListMap.containsKey(key)) {
			List<Long> idList = numberIdListMap.get(key);
			if (idList!=null) {
				boolean removed = idList.remove(obj.getId());
				if (removed) {
					if (idList.size()==0) {
						numberIdListMap.remove(key);
						removedIndexKey(key.toString());
					} else {
						updatedIndexKey(key.toString());				
					}
				}
			}
		}
	}

	public void removeObjectId(long id) {
		List<BigDecimal> removeKeys = new ArrayList<BigDecimal>();
		for (Entry<BigDecimal,List<Long>> entry: numberIdListMap.entrySet()) {
			boolean removed = entry.getValue().remove(id);
			if (removed) {
				if (entry.getValue().size()==0) {
					removeKeys.add(entry.getKey());
				} else {
					updatedIndexKey(entry.getKey().toString());
				}
			}
		}
		for (BigDecimal key: removeKeys) {
			numberIdListMap.remove(key);
			removedIndexKey(key.toString());
		}
	}

	public List<Long> getIdListForObject(DbDataObject obj) {
		List<Long> idList = null;
		List<Long> originalIdList = numberIdListMap.get(getKeyForObject(obj));
		if (originalIdList!=null) {
			idList = new ArrayList<Long>(originalIdList);
		} else {
			idList = new ArrayList<Long>();
		}
		return idList;
	}

	public List<Long> getIdListForObjectFromTo(DbDataObject greater,boolean greaterOrEqual,DbDataObject less,boolean lessOrEqual) {
		List<Long> idList = new ArrayList<Long>();
		BigDecimal fromKey = getKeyForObject(greater);
		BigDecimal toKey = getKeyForObject(less);
		if (greaterOrEqual) {
			List<Long> idL = numberIdListMap.get(fromKey);
			if (idL!=null) {
				for (long id: idL) {
					idList.add(id);
				}
			}
		}
		SortedMap<BigDecimal,List<Long>> subset = numberIdListMap.subMap(fromKey,toKey);
		//Messenger.getInstance().debug(this,"From key: " + fromKey + " (equals: " + greaterOrEqual + "), to key: " + toKey + " (equals: " + lessOrEqual + "), subset size: " + subset.size());
		for (Entry<BigDecimal,List<Long>> entry: subset.entrySet()) {
			//Messenger.getInstance().debug(this,"Entry key: " + entry.getKey());
			if (
				(entry.getKey().compareTo(fromKey)!=0 || greaterOrEqual) &&
				(entry.getKey().compareTo(toKey)!=0 || lessOrEqual)
				) {
				for (long id: entry.getValue()) {
					idList.add(id);
				}
			}
		}
		if (lessOrEqual && (!fromKey.equals(toKey))) {
			List<Long> idL = numberIdListMap.get(toKey);
			if (idL!=null) {
				for (long id: idL) {
					idList.add(id);
				}
			}
		}
		return idList;
	}

	public int getSize() {
		return numberIdListMap.size();
	}

	/**
	 * @return the number
	 */
	public MdlNumber getNumber() {
		return number;
	}

	@Override
	protected StringBuilder getFileContentForKeys(List<String> keys) {
		StringBuilder sb = new StringBuilder();
		if (keys!=null) {
			for (String strKey: keys) {
				BigDecimal key = new BigDecimal(strKey);
				List<Long> value = numberIdListMap.get(key);
				sb.append(key);
				sb.append(Generic.SEP_OBJ);
				boolean first = true;
				for (long id: value) {
					if (!first) {
						sb.append(Generic.SEP_STR);
					}
					sb.append(id);
					first = false;
				}
				sb.append("\n");
			}
		}
		return sb;
	}

	@Override
	protected List<String> getKeysFromFileContent(StringBuilder content) {
		String[] lines = content.toString().split("\n");
		List<String> keys = new ArrayList<String>();
		for (String line: lines) {
			List<String> objects = Generic.getObjectsFromString(line);
			String strKey = objects.get(0);
			String val = objects.get(1);
			String[] ids = Generic.getValuesFromString(val);
			if (strKey.length()==0) {
				strKey = "0";
			}
			BigDecimal key = new BigDecimal(strKey);
			List<Long> idList = new ArrayList<Long>();
			for (String strId: ids) {
				idList.add(Long.parseLong(strId));
			}
			numberIdListMap.put(key, idList);
			keys.add(strKey);
		}
		return keys;
	}
}
