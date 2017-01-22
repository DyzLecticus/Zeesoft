package nl.zeesoft.zodb.database.index;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.MdlClass;

public class IdxClass extends IdxObject {
	private MdlClass						cls 						= null;
	
	private SortedMap<Long,Object> 			idObjectMap					= new TreeMap<Long,Object>();

	// Used to communicate changes to write worker and provide read access to index while writing
	private List<Long> 						lockedIdList				= new ArrayList<Long>();
	private SortedMap<Long,DbDataObject> 	changedIdDataObjectMap		= new TreeMap<Long,DbDataObject>();
	private SortedMap<Long,DbDataObject> 	writingIdDataObjectMap		= new TreeMap<Long,DbDataObject>();
	private List<Long> 						changedIdList				= new ArrayList<Long>();
	private List<Long> 						removedIdList				= new ArrayList<Long>();
	
	private Object							changeMapsAreLockedBy		= null;
	
	private String							classDirName				= "";
	
	public IdxClass(MdlClass cl) {
		cls = cl;
		setDirName(cls.getIndexDirName());
		classDirName = cls.getClassDirName();
	}

	public boolean idObjectMapContainsId(long id) {
		boolean r = false;
		lockMe(this);
		r = idObjectMap.containsKey(id);
		unlockMe(this);
		return r;
	}

	public SortedMap<Long,Object> getNewIdObjectMap() {
		lockMe(this);
		SortedMap<Long,Object> map = new TreeMap<Long,Object>(idObjectMap);
		unlockMe(this);
		return map;
	}
	
	// Called by index
	public boolean lockObjectId(long id,DbDataObject changedObject,Object source) {
		boolean locked = false;
		lockMaps(source);
		DbConfig.getInstance().getCache().updateClassObject(cls.getFullName(),changedObject,source);
		locked = lockObjectId(id,changedObject);
		unlockMaps(source);
		return locked;
	}

	// Called by index
	public boolean lockObjectIds(List<Long> idList,List<DbDataObject> changedObjectList,Object source) {
		boolean locked = false;
		lockMaps(source);
		boolean lock = true;
		for (Long id: idList) {
			if (lockedIdList.contains(id)) {
				lock = false;
				break;
			}
		}
		if (lock) {
			int i = 0;
			for (Long id: idList) {
				DbDataObject changedObject = null;
				if (changedObjectList!=null && changedObjectList.size()==idList.size()) {
					changedObject = changedObjectList.get(i);
					DbConfig.getInstance().getCache().updateClassObject(cls.getFullName(),changedObject,source);
				}
				lockObjectId(id,changedObject);
				i++;
			}
			locked = true;
		}
		unlockMaps(source);
		return locked;
	}

	private boolean lockObjectId(long id,DbDataObject changedObject) {
		boolean locked = false;
		if (!lockedIdList.contains(id)) {
			lockedIdList.add(id);
			if (changedObject!=null) {
				changedIdDataObjectMap.put(id,changedObject.copy(null));
			}
			locked = true;
		}
		//Messenger.getInstance().debug(this,"Locked object: " + id + ":" + locked);
		return locked;
	}

	// Called by index
	public boolean unlockObjectId(Long id,boolean changed,boolean removed,DbDataObject changedObject,Object source) {
		boolean unlocked = false;
		lockMaps(source);
		if (changed) {
			DbConfig.getInstance().getCache().updateClassObject(cls.getFullName(),changedObject,source);
		} else if (removed) {
			DbConfig.getInstance().getCache().removeClassObjectById(cls.getFullName(),id,source);
		}
		if (lockedIdList.contains(id)) {
			lockedIdList.remove(id);
			if (changed) {
				if (!changedIdList.contains(id)) {
					changedIdList.add(id);
				}
				changedIdDataObjectMap.put(id,changedObject.copy(null));
			} else if (removed) {
				removedIdList.add(id);
				changedIdList.remove(id);
				changedIdDataObjectMap.remove(id);
			} else if (!changedIdList.contains(id)) {
				changedIdDataObjectMap.remove(id);
			}
			if (!removed) {
				removedIdList.remove(id);
			}
			unlocked = true;
		}
		unlockMaps(source);
		//Messenger.getInstance().debug(this,"Unlocked object: " + id + ":" + unlocked);
		return unlocked;
	}
	
	// Called by index
	public DbDataObject getChangedDataObject(long id, Object source) {
		lockMaps(source);
		DbDataObject r = changedIdDataObjectMap.get(id);
		if (r==null) {
			r = writingIdDataObjectMap.get(id);
		}
		unlockMaps(source);
		return r;
	}

	// Called by write worker
	public SortedMap<Long,DbDataObject> getChangedDataObjects(int max, Object source) {
		SortedMap<Long,DbDataObject> r = new TreeMap<Long,DbDataObject>();
		if (max<1) {
			max=1;
		}
		lockMaps(source);
		List<Long> idList = new ArrayList<Long>(changedIdList);
		int added = 0;
		for (Long id: idList) {
			writingIdDataObjectMap.put(id,changedIdDataObjectMap.get(id));
			r.put(id,changedIdDataObjectMap.remove(id));
			changedIdList.remove(id);
			added++;
			if (added>=max) {
				break;
			}
		}
		unlockMaps(source);
		return r;
	}

	// Called by write worker
	public void removeWrittenDataObjects(List<Long> idList, Object source) {
		lockMaps(source);
		for (Long id: idList) {
			writingIdDataObjectMap.remove(id);
		}
		unlockMaps(source);
	}

	// Called by write worker
	public List<Long> getRemovedIdList(int max, Object source) {
		if (max<1) {
			max=1;
		}
		lockMaps(source);
		List<Long> r = new ArrayList<Long>();
		List<Long> idList = new ArrayList<Long>(removedIdList);
		int added = 0;
		for (Long id: idList) {
			r.add(id);
			removedIdList.remove(id);
			added++;
			if (added>=max) {
				break;
			}
		}
		unlockMaps(source);
		return r;
	}

	// Called by write worker
	public void removeRemovedIds(List<Long> idList, Object source) {
		lockMaps(source);
		for (Long id: idList) {
			removedIdList.remove(id);
		}
		unlockMaps(source);
	}
	
	public long getNewId() {
		long id = 1;
		lockMe(this);
		if (idObjectMap.size()>0) {
			id = idObjectMap.lastKey() + 1;
		}
		unlockMe(this);
		return id;
	}

	public void addId(long id) {
		lockMe(this);
		if (!idObjectMap.containsKey(id)) {
			idObjectMap.put(id,null);
			addedIndexKey("" + id);
		}
		unlockMe(this);
	}

	public void removeId(long id) {
		lockMe(this);
		if (idObjectMap.containsKey(id)) {
			idObjectMap.remove(id);
			removedIndexKey("" + id);
		}
		unlockMe(this);
	}

	/**
	 * @return the cls
	 */
	public MdlClass getCls() {
		return cls;
	}

	/**
	 * @return the classDirName
	 */
	public String getClassDirName() {
		return classDirName;
	}

	@Override
	protected StringBuilder getFileContentForKeys(List<String> keys) {
		StringBuilder content = new StringBuilder();
		if (keys!=null) {
			for (String key: keys) {
				content.append(key);
				content.append("\n");
			}
		}
		return content;
	}

	@Override
	protected List<String> getKeysFromFileContent(StringBuilder content) {
		String[] lines = content.toString().split("\n");
		List<String> keys = new ArrayList<String>();
		for (String line: lines) {
			if (line.length()>0) {
				idObjectMap.put(Long.parseLong(line),null);
				keys.add(line);
			}
		}
		return keys;
	}
	
	/**************************** PRIVATE METHODS **************************/
	private synchronized void lockMaps(Object source) {
		int attempt = 0;
		while (mapsAreLocked()) {
		    try {
                wait();
            } catch (InterruptedException e) { 
            	// Ignore
            }
            attempt ++;
			if (attempt>=1000) {
				Messenger.getInstance().warn(this,Locker.getLockFailedMessage(attempt,source));
				attempt = 0;
			}
		}
		changeMapsAreLockedBy = source;
	}

	private synchronized void unlockMaps(Object source) {
		if (changeMapsAreLockedBy==source) {
			changeMapsAreLockedBy = null;
			notifyAll();
		} else {
			Messenger.getInstance().error(this,"Invalid attempt to unlock index by source: " + source);
		}
	}
	
	private synchronized boolean mapsAreLocked() {
		return changeMapsAreLockedBy!=null;
	}	
}
