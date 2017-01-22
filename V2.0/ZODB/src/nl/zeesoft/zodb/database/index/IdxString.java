package nl.zeesoft.zodb.database.index;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.MdlString;

public class IdxString extends IdxObject {
	private MdlString string = null;

	private SortedMap<String,List<Long>> stringIdListMap	= new TreeMap<String,List<Long>>();
	
	public IdxString(MdlString prop) {
		string = prop;
		setDirName(string.getIndexDirName());
	}
	
	public void addObject(DbDataObject obj) {
		String key = getKeyForObject(obj);
		if (!stringIdListMap.containsKey(key)) {
			List<Long> idList = new ArrayList<Long>();
			idList.add(obj.getId());
			stringIdListMap.put(key, idList);
			addedIndexKey(key);
		} else {
			List<Long> idList = stringIdListMap.get(key);
			if (!idList.contains(obj.getId())) {
				idList.add(obj.getId());
				updatedIndexKey(key);
			}
		}
	}

	private String getKeyForObject(DbDataObject obj) {
		StringBuilder val = obj.getPropertyValue(string.getName());
		String r = "";
		if (val!=null) {
			r = Generic.getSerializableStringValue(val.toString());
		}
		return r;
	}
	
	public void removeObject(DbDataObject obj) {
		String key = getKeyForObject(obj);
		if (stringIdListMap.containsKey(key)) {
			List<Long> idList = stringIdListMap.get(key);
			if (idList!=null) {
				boolean removed = idList.remove(obj.getId());
				if (removed) {
					if (idList.size()==0) {
						stringIdListMap.remove(key);
						removedIndexKey(key);
					} else {
						updatedIndexKey(key);
					}
				}
			}
		}
	}

	public void removeObjectId(long id) {
		List<String> removeKeys = new ArrayList<String>();
		for (Entry<String,List<Long>> entry: stringIdListMap.entrySet()) {
			boolean removed = entry.getValue().remove(id);
			if (removed) {
				if (entry.getValue().size()==0) {
					removeKeys.add(entry.getKey());
				} else {
					updatedIndexKey(entry.getKey().toString());
				}
			}
		}
		for (String key: removeKeys) {
			stringIdListMap.remove(key);
			removedIndexKey(key);
		}
	}

	public List<Long> getIdListForObject(DbDataObject obj,boolean invert) {
		List<Long> idList = null;
		String key = getKeyForObject(obj);
		if (invert) {
			idList = new ArrayList<Long>();
			for (Entry<String,List<Long>> entry: stringIdListMap.entrySet()) {
				if (!entry.getKey().equals(key)) {
					for (long id: entry.getValue()) {
						idList.add(id);
					}
				}
			}
		} else {
			List<Long> originalIdList = stringIdListMap.get(key);
			if (originalIdList!=null) {
				idList = new ArrayList<Long>(originalIdList);
			} else {
				idList = new ArrayList<Long>();
			}
		}
		return idList;
	}

	public List<Long> getIdListForObjectContains(DbDataObject obj,boolean invert) {
		List<Long> idList = new ArrayList<Long>();
		String containsKey = getKeyForObject(obj);
		if (invert) {
			for (Entry<String,List<Long>> entry: stringIdListMap.entrySet()) {
				if (!entry.getKey().contains(containsKey)) {
					for (long id: entry.getValue()) {
						idList.add(id);
					}
				}
			}
		} else {
			for (Entry<String,List<Long>> entry: stringIdListMap.entrySet()) {
				if (entry.getKey().contains(containsKey)) {
					for (long id: entry.getValue()) {
						idList.add(id);
					}
				}
			}
		}
		return idList;
	}

	public int getSize() {
		return stringIdListMap.size();
	}

	public int getSizeValues() {
		int r = 0;
		for (Entry<String,List<Long>> entry: stringIdListMap.entrySet()) {
			r = r + entry.getValue().size();
		}
		return r;
	}

	/**
	 * @return the string
	 */
	public MdlString getString() {
		return string;
	}

	@Override
	protected StringBuilder getFileContentForKeys(List<String> keys) {
		StringBuilder sb = new StringBuilder();
		if (keys!=null) {
			for (String key: keys) {
				List<Long> value = stringIdListMap.get(key);
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
			String key = objects.get(0);
			String val = objects.get(1);
			String[] ids = Generic.getValuesFromString(val);
			List<Long> idList = new ArrayList<Long>();
			for (String strId: ids) {
				idList.add(Long.parseLong(strId));
			}
			stringIdListMap.put(key, idList);
			keys.add(key);
		}
		return keys;
	}
}
