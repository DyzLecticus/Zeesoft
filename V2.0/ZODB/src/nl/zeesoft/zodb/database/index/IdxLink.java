package nl.zeesoft.zodb.database.index;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.model.MdlLink;

public class IdxLink extends IdxObject {
	private MdlLink link = null;
	
	private SortedMap<Long,List<Long>> parentChildrenMap = new TreeMap<Long,List<Long>>();
	
	public IdxLink(MdlLink lnk) {
		link = lnk;
		setDirName(link.getIndexDirName());
	}
	
	public void addParentId(long id) {
		// No need to save parent without children
	}
	public void removeParentId(long id) {
		lockMe(this);
		List<Long> removed = parentChildrenMap.remove(id);
		if (removed!=null) {
			removedIndexKey("" + id);
		}
		unlockMe(this);
	}
	
	public void addChildValues(long childId,List<Long> linkValues) {
		lockMe(this);
		List<Long> addValues = null;
		if (linkValues==null) {
			addValues = new ArrayList<Long>();
		} else {
			addValues = new ArrayList<Long>(linkValues);
		}
		if (addValues.size()==0) {
			addValues.add(0L);
		}
		for (long value: addValues) {
			List<Long> idList = parentChildrenMap.get(value);
			if (idList==null) {
				idList = new ArrayList<Long>();
				idList.add(childId);
				parentChildrenMap.put(value, idList);
				addedIndexKey("" + value);
			} else if (!idList.contains(childId)) {
				idList.add(childId);
				updatedIndexKey("" + value);
			}
		}
		unlockMe(this);
	}

	public void removeChildValues(long childId,List<Long> linkValues) {
		lockMe(this);
		List<Long> removeValues = null;
		if (linkValues==null) {
			removeValues = new ArrayList<Long>();
		} else {
			removeValues = new ArrayList<Long>(linkValues);
		}
		if (removeValues.size()==0) {
			removeValues.add(0L);
		}
		for (long value: removeValues) {
			List<Long> idList = parentChildrenMap.get(value);
			if (idList!=null) {
				boolean removed = idList.remove(childId);
				if (removed) {
					if (idList.size()<=0) {
						parentChildrenMap.remove(value);
						removedIndexKey("" + value);
					} else {
						updatedIndexKey("" + value);
					}
				}
			}
		}
		unlockMe(this);
	}
	
	public void removeChildId(long childId) {
		lockMe(this);
		List<Long> removeKeys = new ArrayList<Long>();
		for (Entry<Long,List<Long>> entry: parentChildrenMap.entrySet()) {
			if (entry.getValue().contains(childId)) {
				entry.getValue().remove(childId);
				if (entry.getValue().size()==0) {
					removeKeys.add(entry.getKey());
				} else {
					updatedIndexKey("" + entry.getKey());
				}
			}
		}
		for (long key: removeKeys) {
			parentChildrenMap.remove(key);
			removedIndexKey("" + key);
		}
		unlockMe(this);
	}

	public List<Long> getChildIdListForParentId(long id) {
		List<Long> idList = null;
		lockMe(this);
		List<Long> originalIdList = parentChildrenMap.get(id);
		if (originalIdList!=null) {
			idList = new ArrayList<Long>(originalIdList);
		} else {
			idList = new ArrayList<Long>();
		}
		unlockMe(this);
		return idList;
	}

	public int getSize() {
		return parentChildrenMap.size();
	}
	
	/**
	 * @return the link
	 */
	public MdlLink getLink() {
		return link;
	}

	@Override
	protected StringBuilder getFileContentForKeys(List<String> keys) {
		StringBuilder sb = new StringBuilder();
		if (keys!=null) {
			for (String key: keys) {
				Long keyVal = Long.parseLong(key);
				List<Long> value = parentChildrenMap.get(keyVal);
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
			long parentId = Long.parseLong(key); 
			String val = objects.get(1);
			String[] ids = Generic.getValuesFromString(val);
			List<Long> idList = new ArrayList<Long>();
			for (String strId: ids) {
				idList.add(Long.parseLong(strId));
			}
			parentChildrenMap.put(parentId, idList);
			keys.add(key);
		}
		return keys;
	}
}
