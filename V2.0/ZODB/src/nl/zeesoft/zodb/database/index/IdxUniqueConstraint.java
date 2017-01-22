package nl.zeesoft.zodb.database.index;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.MdlLink;
import nl.zeesoft.zodb.database.model.MdlProperty;
import nl.zeesoft.zodb.database.model.MdlUniqueConstraint;

public class IdxUniqueConstraint extends IdxObject {
	private MdlUniqueConstraint uniqueConstraint = null;

	private SortedMap<String,String> keyClassNameIdMap	= new TreeMap<String,String>();
	
	public IdxUniqueConstraint(MdlUniqueConstraint uc) {
		uniqueConstraint = uc;
		setDirName(uniqueConstraint.getIndexDirName());
	}
	
	public boolean addObject(String className,DbDataObject obj) {
		boolean added = false;
		String key = getKeyForObject(className,obj);
		if (!keyClassNameIdMap.containsKey(key)) {
			keyClassNameIdMap.put(key,className + ":" + obj.getId());
			addedIndexKey(key);
			added = true;
		}
		return added;
	}

	public void removeObject(String className,DbDataObject obj) {
		String key = getKeyForObject(className,obj);
		if (keyClassNameIdMap.containsKey(key)) {
			keyClassNameIdMap.remove(key);
			removedIndexKey(key);
		}
	}

	public void removeObjectId(long id) {
		String removeKey = null;
		for (Entry<String,String> entry: keyClassNameIdMap.entrySet()) {
			long entryId = Long.parseLong(entry.getValue().split(":")[1]);
			if (entryId==id) {
				removeKey = entry.getKey();
				break;
			}
		}
		if (removeKey!=null) {
			keyClassNameIdMap.remove(removeKey);
			removedIndexKey(removeKey);
		}
	}

	public String getClassNameIdForObject(String className,DbDataObject obj) {
		return keyClassNameIdMap.get(getKeyForObject(className,obj));
	}

	private String getKeyForObject(String className,DbDataObject obj) {
		StringBuilder key = new StringBuilder();
		boolean first = true;
		for (MdlProperty ucProp: uniqueConstraint.getPropertiesListForClass(className)) {
			if (!first) {
				key.append(Generic.SEP_STR);
			}
			if (ucProp instanceof MdlLink) {
				List<Long> linkVal = obj.getLinkValue(ucProp.getName());
				if (linkVal==null) {
					linkVal = new ArrayList<Long>();
				}
				if (linkVal.size()==0) {
					linkVal.add(0L);
				}
				for (Long id: linkVal) {
					key.append(id);
					key.append(",");
				}
			} else {
				StringBuilder propVal = obj.getPropertyValue(ucProp.getName());
				if (propVal!=null) {
					String value = ""; 
					if (uniqueConstraint.isCaseSensitive()) {
						value = propVal.toString();
					} else {
						value = propVal.toString().toUpperCase();
					}
					key.append(Generic.getSerializableStringValue(value));
				}
			}
			first = false;
		}
		return key.toString();
	}

	public int getSize() {
		return keyClassNameIdMap.size();
	}

	/**
	 * @return the uniqueConstraint
	 */
	public MdlUniqueConstraint getUniqueConstraint() {
		return uniqueConstraint;
	}

	@Override
	protected StringBuilder getFileContentForKeys(List<String> keys) {
		StringBuilder sb = new StringBuilder();
		if (keys!=null) {
			for (String key: keys) {
				String classNameId = keyClassNameIdMap.get(key);
				sb.append(key);
				sb.append(Generic.SEP_OBJ);
				sb.append(classNameId);
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
			String classNameId = objects.get(1);
			keyClassNameIdMap.put(key,classNameId);
			keys.add(key);
		}
		return keys;
	}
}
