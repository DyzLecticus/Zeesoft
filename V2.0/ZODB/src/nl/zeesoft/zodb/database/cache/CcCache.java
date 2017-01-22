package nl.zeesoft.zodb.database.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbDataObject;

public final class CcCache {
	private CcConfig						config					= null;
	private List<String>					classNames				= new ArrayList<String>();
	private SortedMap<String,Object>		cacheClassIsLockedBy	= new TreeMap<String,Object>();
	private SortedMap<String,CcCacheClass>	cacheClassMap			= new TreeMap<String,CcCacheClass>();
	
	public void initialize(CcConfig config) {
		this.config = config;
		classNames.clear();
		cacheClassMap.clear();
		cacheClassIsLockedBy.clear();
		if (config.isActive()) {
			for (String className: config.getClassMaxSizeMap().keySet()) {
				classNames.add(className);
				cacheClassMap.put(className,new CcCacheClass(config.isDebug(),config.getClassMaxSizeMap().get(className)));
			}
		}
	}

	public void reinitialize(Object source) {
		for (String className: config.getClassMaxSizeMap().keySet()) {
			lockCacheClass(className,source);
			cacheClassMap.get(className).clear();
			unlockCacheClass(className,source);
		}
		initialize(config);
	}
	
	public void addClassObject(String className, DbDataObject object, Object source) {
		if (config.isActive() && classNames.contains(className)) {
			lockCacheClass(className,source);
			cacheClassMap.get(className).addObject(object);
			unlockCacheClass(className,source);
		}
	}

	public void updateClassObject(String className, DbDataObject object, Object source) {
		if (config.isActive() && classNames.contains(className)) {
			lockCacheClass(className,source);
			cacheClassMap.get(className).updateObject(object);
			unlockCacheClass(className,source);
		}
	}

	public DbDataObject getClassObjectById(String className, long id, Object source) {
		DbDataObject r = null;
		if (config.isActive() && classNames.contains(className)) {
			lockCacheClass(className,source);
			r = cacheClassMap.get(className).getObjectById(id);
			unlockCacheClass(className,source);
		}
		return r;
	}

	public void removeClassObjectById(String className, long id, Object source) {
		if (config.isActive() && classNames.contains(className)) {
			lockCacheClass(className,source);
			cacheClassMap.get(className).removeObjectById(id);
			unlockCacheClass(className,source);
		}
	}

	public void applyHalfLife(Object source) {
		if (config.isActive()) {
			for (String className: config.getClassMaxSizeMap().keySet()) {
				lockCacheClass(className,source);
				cacheClassMap.get(className).applyHalfLife();
				unlockCacheClass(className,source);
			}
		}
	}

	/**
	 * @return the config
	 */
	public CcConfig getConfig() {
		return config;
	}

	/**************************** PRIVATE METHODS **************************/
	private synchronized void lockCacheClass(String className,Object source) {
		int attempt = 0;
		while (cacheClassIsLocked(className)) {
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
		cacheClassIsLockedBy.put(className,source);
	}

	private synchronized void unlockCacheClass(String className,Object source) {
		if (cacheClassIsLockedBy.get(className)==source) {
			cacheClassIsLockedBy.put(className,null);
			notifyAll();
		} else {
			Messenger.getInstance().error(this,"Invalid attempt to unlock cache class by source: " + source);
		}
	}
	
	private synchronized boolean cacheClassIsLocked(String className) {
		return cacheClassIsLockedBy.get(className)!=null;
	}
}
