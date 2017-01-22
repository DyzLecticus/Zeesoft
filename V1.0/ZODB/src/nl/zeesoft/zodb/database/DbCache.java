package nl.zeesoft.zodb.database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.file.FileObject;
import nl.zeesoft.zodb.file.XMLElem;
import nl.zeesoft.zodb.file.XMLFile;
import nl.zeesoft.zodb.model.MdlCollection;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.MdlObjectRefList;

/**
 * This class is used by the database index to store fetch results for cache-able collections
 */
public final class DbCache {
	private final static int						MAX_WEIGHT				= 9999;
	private final static int						MIN_WEIGHT				= 1;
	
	private static DbCache							cache					= null;
	
	private	List<String>							cachedCollections		= new ArrayList<String>();
	
	private SortedMap<String,Long>					collectionChangedMap	= new TreeMap<String,Long>();
	private SortedMap<String,List<String>>			collectionKeyMap		= new TreeMap<String,List<String>>();

	private SortedMap<String,QryFetch>				keyFetchMap				= new TreeMap<String,QryFetch>();
	private SortedMap<String,Long>					keyWeightMap			= new TreeMap<String,Long>();
	private SortedMap<String,Long>					keyWeightMapSave		= new TreeMap<String,Long>();
	private SortedMap<Long,List<String>>			weightKeyMap			= new TreeMap<Long,List<String>>();

	private Object									cacheIsLockedBy			= null;
	
	private boolean									changed					= false;

	private SortedMap<Long,MdlDataObject>			preloadedObjects		= new TreeMap<Long,MdlDataObject>();
	
	private DbCache() {
		// Singleton
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}

	public static DbCache getInstance() {
		if (cache==null) {
			cache = new DbCache();
		}
		return cache;
	}

	/**
	 * @return the preloadedObjects
	 */
	public SortedMap<Long, MdlDataObject> getPreloadedObjects() {
		return preloadedObjects;
	}

	public List<String> getCachedCollections(Object source) {
		List<String> collections = new ArrayList<String>();
		lockCache(source);
		for (Entry<String,Long> entry: collectionChangedMap.entrySet()) {
			collections.add(entry.getKey());
		}
		unlockCache(source);
		return collections;
	}
	
	public void updateCollectionChanged(String collectionName, long changed, Object source) {
		if (cachedCollections.contains(collectionName)) {
			lockCache(source);
			long lastTimeChanged = collectionChangedMap.get(collectionName);
			if (changed > lastTimeChanged) {
				// Invalidate cached fetches for collection
				List<String> oriList = collectionKeyMap.get(collectionName);
				if (oriList!=null) {
					List<String> keyList = new ArrayList<String>(oriList);
					for (String key: keyList) {
						removeCachedFetchNoLock(key);
					}
				}
				collectionChangedMap.put(collectionName,changed);
			}
			unlockCache(source);
		}
	}

	public void addFetchToCache(QryFetch f,Object source) {
		if (
			(checkFetchForCache(f)) &&
			(f.getTime()>=DbConfig.getInstance().getCacheThreshold())
			) {
			lockCache(source);
			addFetchToCacheNoLock(f);
			unlockCache(source);
		}
	}

	public QryFetch getFetchFromCache(QryFetch f,Object source) {
		QryFetch fetch = null;
		if (checkFetchForCache(f)) {
			String key = getKeyForFetch(f);
			lockCache(source);
			fetch = getCachedFetchNoLock(key);
			unlockCache(source);
		}
		return fetch;
	}

	public void clearCache(Object source) {
		lockCache(source);
		clearCacheNoLock();
		unlockCache(source);
	}

	public int getSize(Object source) {
		int s = 0;
		lockCache(source);
		truncateCacheNoLock(false);
		s = keyFetchMap.size();
		unlockCache(source);
		return s;
	}
	
	protected int serializeCacheIfChanged(Object source) {
		int size = 0;
		boolean chg = false;
		lockCache(source);
		chg = changed;
		unlockCache(source);
		if (chg) {
			size = serializeCache(source);
		}
		return size;
	}
		
	protected int serializeCache(Object source) {
		Date start = new Date();
		lockCache(source);
		if (collectionChangedMap.size()==0) {
			Date now = new Date();
			for (MdlCollection c: DbConfig.getInstance().getModel().getCollections()) {
				if (c.isCacheable()) {
					cachedCollections.add(c.getName());
					collectionChangedMap.put(c.getName(),now.getTime());
					collectionKeyMap.put(c.getName(),new ArrayList<String>());
				}
			}
		}
		SortedMap<String,Long>			tCollectionChangedMap	= new TreeMap<String,Long>(collectionChangedMap);
		SortedMap<String,List<String>>	tCollectionKeyMap		= new TreeMap<String,List<String>>();
		for (Entry<String,List<String>> entry: collectionKeyMap.entrySet()) {
			List<String> newList = new ArrayList<String>();
			for (String str: entry.getValue()) {
				newList.add(str);
			}
			tCollectionKeyMap.put(entry.getKey(), newList);
		}
		SortedMap<String,QryFetch>		tKeyFetchMap			= new TreeMap<String,QryFetch>(keyFetchMap);
		SortedMap<String,Long>			tKeyWeightMap			= new TreeMap<String,Long>(keyWeightMap);
		SortedMap<Long,List<String>>	tWeightKeyMap			= new TreeMap<Long,List<String>>();
		for (Entry<Long,List<String>> entry: weightKeyMap.entrySet()) {
			List<String> newList = new ArrayList<String>();
			for (String str: entry.getValue()) {
				newList.add(str);
			}
			tWeightKeyMap.put(new Long(entry.getKey()), newList);
		}
		changed = false;
		unlockCache(source);
		Messenger.getInstance().debug(this, "Copy cache took: " + (new Date().getTime() - start.getTime()) + " ms");
		
		XMLFile file = new XMLFile();
		file.setRootElement(new XMLElem("cache",null,null));

		List<String> persistKeyList = getPersistKeyListNoLock(tWeightKeyMap);

		XMLElem colsElem = new XMLElem("collections",null,file.getRootElement());
		for (Entry<String,Long> entry: tCollectionChangedMap.entrySet()) {
			XMLElem colElem = new XMLElem("collection",null,colsElem);
			new XMLElem("name",new StringBuffer(entry.getKey()),colElem);
			StringBuffer sb = new StringBuffer();
			sb.append(entry.getValue());
			new XMLElem("changed",sb,colElem);
			List<String> keyList = tCollectionKeyMap.get(entry.getKey());
			if ((keyList!=null) && (keyList.size()>0)) {
				XMLElem keysElem = new XMLElem("keys",null,colElem);
				for (String key: keyList) {
					if (persistKeyList.contains(key)) {
						new XMLElem("key",new StringBuffer(key),keysElem);
					}
				}
			}
		}
		file.writeFile(DbConfig.getInstance().getFullCacheDir() + "DbCache.xml", file.toStringCompressed());
		file.cleanUp();

		int size = persistKeyList.size();
		if (size>0) {
			int num = 0;
			for (String key: persistKeyList) {
				XMLFile fetchFile = new XMLFile();
				fetchFile.setRootElement(new XMLElem("fetch",null,null));
				long weight = tKeyWeightMap.get(key);
				new XMLElem("key",new StringBuffer(key),fetchFile.getRootElement());
				StringBuffer sb = new StringBuffer();
				sb.append(weight);
				new XMLElem("weight",sb,fetchFile.getRootElement());
				QryObject.toXml(tKeyFetchMap.get(key),true).getRootElement().setParent(fetchFile.getRootElement());
				fetchFile.writeFile(DbConfig.getInstance().getFullCacheDir() + num, fetchFile.toStringCompressed());
				fetchFile.cleanUp();
				num++;
				if (num>=DbConfig.getInstance().getCachePersistSize()) {
					break;
				}
			}
		}
		
		return size;
	}

	protected void unserializeCache(Object source) {
		if (!FileObject.fileExists(DbConfig.getInstance().getFullCacheDir() + "DbCache.xml")) {
			serializeCache(source);
		} else {
			lockCache(source);
			cachedCollections.clear();
			collectionChangedMap.clear();
			collectionKeyMap.clear();
			keyFetchMap.clear();
			keyWeightMap.clear();
			weightKeyMap.clear();
			preloadedObjects.clear();
			
			File dir = new File(DbConfig.getInstance().getFullCacheDir());
			String[] cacheFiles = dir.list();
			List<Integer> allFileNums = new ArrayList<Integer>();
			for (int i = 0; i < cacheFiles.length; i++) {
				if (Generic.isNumeric(cacheFiles[i])) {
					allFileNums.add(Integer.parseInt(cacheFiles[i]));
				}
			}

			int size = allFileNums.size();
			List<DbCacheLoadWorker> loaders = new ArrayList<DbCacheLoadWorker>();
			int filesPerLoader = (size / 10) + 1;
			int doneLoader = 0;
			DbCacheLoadWorker loader = new DbCacheLoadWorker();
			for (int fileNum: allFileNums) {
				loader.getFileNums().add(fileNum);
				doneLoader++;
				if (doneLoader>=filesPerLoader) {
					loaders.add(loader);
					loader.start();
					doneLoader = 0;
					loader = new DbCacheLoadWorker();
				}
			}
			if (doneLoader>0) {
				loaders.add(loader);
				loader.start();
			}

			SortedMap<Long,List<QryFetch>> idFetchMap = new TreeMap<Long,List<QryFetch>>();
			MdlObjectRefList refs = new MdlObjectRefList();
			for (DbCacheLoadWorker cacheLoader: loaders) {
				while (!cacheLoader.isDone()) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						Messenger.getInstance().error(this, "A load worker was interrupted");
					}
				}
				for (Entry<String,QryFetch> entry: cacheLoader.getKeyFetchMap().entrySet()) {
					long weight = cacheLoader.getKeyWeightMap().get(entry.getKey());
					keyFetchMap.put(entry.getKey(), entry.getValue());
					keyWeightMap.put(entry.getKey(), weight);
					addWeightKeyToMapNoLock(weight,entry.getKey());
					if (
						(!entry.getValue().getType().equals(QryFetch.TYPE_COUNT_REFERENCES)) &&
						(!entry.getValue().getType().equals(QryFetch.TYPE_FETCH_REFERENCES))
						) {
						for (long id: entry.getValue().getResults().getIdList()) {
							MdlObjectRef ref = entry.getValue().getResults().getMdlObjectRefById(id);
							List<QryFetch> fetchList = idFetchMap.get(ref.getId().getValue());
							if (fetchList==null) {
								fetchList = new ArrayList<QryFetch>();
								idFetchMap.put(ref.getId().getValue(), fetchList);
							}
							fetchList.add(entry.getValue());
							if ((ref.getDataObject()==null) && (refs.getMdlObjectRefById(ref.getId().getValue())==null)) {
								refs.getReferences().add(ref);
							}
						}
					}
				}
			}

			size = refs.getReferences().size();
			int refsPerLoader = (size / DbIndexLoadObjectWorker.MAX_FETCH_WORKERS) + 1;
			doneLoader = 0;
			List<DbIndexLoadObjectWorker> workers = new ArrayList<DbIndexLoadObjectWorker>();
			DbIndexLoadObjectWorker worker = new DbIndexLoadObjectWorker();
			for (int i = 0; i < size; i ++) {
				worker.getLoadObjectList().getReferences().add(refs.getReferences().get(i));
				doneLoader++;
				if (doneLoader>=refsPerLoader) {
					doneLoader = 0;
					workers.add(worker);
					worker.start();
					worker = new DbIndexLoadObjectWorker();
				}
			}
			if (doneLoader>0) {
				workers.add(worker);
				worker.start();
			}

			XMLFile file = new XMLFile();
			file.parseFile(DbConfig.getInstance().getFullCacheDir() + "DbCache.xml");
			for (XMLElem cElem: file.getRootElement().getChildren()) {
				
				if (cElem.getName().equals("collections")) {
					for (XMLElem colElem: cElem.getChildren()) {
						String name = colElem.getChildByName("name").getValue().toString();
						long changed = Long.parseLong(colElem.getChildByName("changed").getValue().toString());
						cachedCollections.add(name);
						collectionChangedMap.put(name,changed);
						List<String> keyList = new ArrayList<String>();
						XMLElem keysElem = colElem.getChildByName("keys");
						if (keysElem!=null) {
							for (XMLElem keyElem: keysElem.getChildren()) {
								keyList.add(keyElem.getValue().toString());
							}
						}
						collectionKeyMap.put(name,keyList);
					}
				}
				
			}
			
			for (DbIndexLoadObjectWorker loadWorker: workers) {
	        	while (!loadWorker.isDone()) {
	        		try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						Messenger.getInstance().error(this, "A fetch worker was interrupted");
					}
	        	}
	        	for (MdlObjectRef ref: loadWorker.getLoadObjectList().getReferences()) {
	    			if (ref.getDataObject()!=null) {
	    				preloadedObjects.put(ref.getId().getValue(),ref.getDataObject());
	    				List<QryFetch> fetchList = idFetchMap.get(ref.getId().getValue());
	    				for (QryFetch fetch: fetchList) {
	    					MdlObjectRef fRef = fetch.getResults().getMdlObjectRefById(ref.getId().getValue());
	    					if ((fRef!=null) && (fRef.getDataObject()==null)) {
	    						fRef.setDataObject(ref.getDataObject());
	    					}
	    				}
	    			} else {
						Messenger.getInstance().error(this, "Failed to unserialize object: " + ref.getId().getValue());
	    			}
	        	}
			}
			
			changed = false;
			unlockCache(source);
			
			file.cleanUp();
		}
	}
	
	/**************************** PRIVATE METHODS **************************/
	private synchronized void lockCache(Object source) {
		int attempt = 0;
		while (cacheIsLocked()) {
		    try {
                wait();
            } catch (InterruptedException e) { 
            	
            }
            attempt ++;
			if (attempt>=1000) {
				Messenger.getInstance().error(this,"Lock failed after " + attempt + " attempts. Source:" + source);
				attempt = 0;
			}
		}
		cacheIsLockedBy = source;
	}

	private synchronized void unlockCache(Object source) {
		if (cacheIsLockedBy==source) {
			cacheIsLockedBy=null;
			notifyAll();
		}
	}
	
	private synchronized boolean cacheIsLocked() {
		return (cacheIsLockedBy!=null);
	}

	private void clearCacheNoLock() {
		File dir = new File(DbConfig.getInstance().getFullCacheDir());
		String[] cacheFiles = dir.list();
		for (int i = 0; i < cacheFiles.length; i++) {
			if (Generic.isNumeric(cacheFiles[i])) {
				try {
					FileObject.deleteFile(DbConfig.getInstance().getFullCacheDir() + cacheFiles[i]);
				} catch (IOException e) {
					Messenger.getInstance().error(this, "Failed to remove file: " + DbConfig.getInstance().getFullCacheDir() + cacheFiles[i]);
				}
			}
		}
		collectionKeyMap.clear();
		keyFetchMap.clear();
		keyWeightMap.clear();
		keyWeightMapSave.clear();
		weightKeyMap.clear();
		changed = true;
	}
	
	private QryFetch getCachedFetchNoLock(String key) {
		truncateCacheNoLock(false);
		QryFetch f = keyFetchMap.get(key);
		if (f!=null) {
			long weight = keyWeightMap.get(key);
			int oldPos = getPersistKeyListNoLock(weightKeyMap).indexOf(key);
			int newPos = 0;
			removeWeightKeyFromMapNoLock(weight,key);
			weight = new Long(weight + 1 + f.getTime());
			keyWeightMap.put(key,new Long(weight));
			addWeightKeyToMapNoLock(weight,key);
			newPos = getPersistKeyListNoLock(weightKeyMap).indexOf(key);
			if (
				(oldPos!=newPos) && 
				(
					(oldPos>=0 && oldPos<100) || 
					(newPos>=0 && newPos<100)
				)
				) {
				changed = true;
			}
			if (weight>MAX_WEIGHT) {
				devideWeightsNoLock(10);
			}
		}
		return f;
	}
	
	private void addFetchToCacheNoLock(QryFetch f) {
		addFetchToCacheNoLock(getKeyForFetch(f),f);
	}
	
	private void addFetchToCacheNoLock(String key, QryFetch f) {
		if (keyFetchMap.get(key)==null) {
			truncateCacheNoLock(true);
			long weight = 0;
			if (keyWeightMapSave.containsKey(key)) {
				weight = keyWeightMapSave.remove(key);
			} else {
				weight = new Long(1 + f.getTime());
			}
			keyFetchMap.put(key, f);
			keyWeightMap.put(key,weight);
			addWeightKeyToMapNoLock(weight,key);
			int pos = getPersistKeyListNoLock(weightKeyMap).indexOf(key);
			if (pos>= 0 && pos<100) {
				changed = true;
			}
			List<String> collections = new ArrayList<String>(f.getEntities());
			collections.add(f.getClassName());
			for (String collectionName: collections) {
				List<String> keyList = collectionKeyMap.get(collectionName);
				if (keyList!=null) {
					keyList.add(key);
				}
			}
		}
	}

	private void removeCachedFetchNoLock(String key) {
		if (keyFetchMap.get(key)!=null) {
			QryFetch f = keyFetchMap.remove(key);
			int pos = getPersistKeyListNoLock(weightKeyMap).indexOf(key);
			if (pos>= 0 && pos<100) {
				changed = true;
			}
			long weight = keyWeightMap.remove(key);
			removeWeightKeyFromMapNoLock(weight,key);
			List<String> collections = new ArrayList<String>(f.getEntities());
			collections.add(f.getClassName());
			for (String collectionName: collections) {
				List<String> keyList = collectionKeyMap.get(collectionName);
				if (keyList!=null) {
					keyList.remove(key);
				}
			}
			keyWeightMapSave.put(key, weight);
		}
	}

	private void addWeightKeyToMapNoLock(long weight, String key) {
		List<String> keyList = weightKeyMap.get(weight);
		if (keyList==null) {
			keyList = new ArrayList<String>();
			weightKeyMap.put(weight, keyList);
		}
		keyList.add(key);
	}

	private void removeWeightKeyFromMapNoLock(long weight, String key) {
		List<String> keyList = weightKeyMap.get(weight);
		if (keyList!=null) {
			keyList.remove(key);
		}
	}

	private void truncateCacheNoLock(boolean oneExtra) {
		int removed = 0;
		int max = DbConfig.getInstance().getCacheSize();
		if (oneExtra) {
			max = (max - 1);
		}
		while (keyFetchMap.size()>max) {
			List<String> keyList = weightKeyMap.get(weightKeyMap.firstKey());
			if ((keyList!=null) && (keyList.size()>0)) {
				removeCachedFetchNoLock(keyList.get(0));
				removed++;
			} else {
				break;
			}
		}
		if (removed>0) {
			Messenger.getInstance().debug(this, "Truncated cache");
		}
	}

	private void devideWeightsNoLock(int divider) {
		if (divider<2) {
			divider=2;
		}
		long weight = 0;
		String key = "";
		long min = 0;
		SortedMap<String,Long> weightMap = new TreeMap<String,Long>(keyWeightMap);
		for (Entry<String,Long> entry: weightMap.entrySet()) {
			weight = entry.getValue();
			key = entry.getKey();
			removeWeightKeyFromMapNoLock(weight,key);
			weight = weight / divider;
			min = (MIN_WEIGHT + keyFetchMap.get(key).getTime()); 
			if (weight<min) {
				weight = min;
			}
			keyWeightMap.put(entry.getKey(), weight);
			addWeightKeyToMapNoLock(weight,key);
			changed = true;
		}
		
	}

	private String getKeyForFetch(QryFetch f) {
		StringBuffer sf = new StringBuffer();
		sf.append(f.getClassName());
		sf.append(Generic.SEP_STR);
		sf.append(f.getType());
		sf.append(Generic.SEP_STR);
		sf.append(f.getStart());
		sf.append(Generic.SEP_STR);
		sf.append(f.getLimit());
		sf.append(Generic.SEP_STR);
		sf.append(f.getOrderBy());
		sf.append(Generic.SEP_STR);
		sf.append(f.isOrderAscending());
		sf.append(Generic.SEP_STR);
		for (QryFetchCondition c: f.getConditions()) {
			sf.append(Generic.SEP_OBJ);
			sf.append(c.getProperty());
			sf.append(Generic.SEP_STR);
			sf.append(c.isInvert());
			sf.append(Generic.SEP_STR);
			sf.append(c.getOperator());
			sf.append(Generic.SEP_STR);
			sf.append(c.getValue());
			sf.append(Generic.SEP_STR);
		}
		for (String entity: f.getEntities()) {
			sf.append(Generic.SEP_OBJ);
			sf.append(entity);
		}
		return sf.toString();
	}
	
	private boolean checkFetchForCache(QryFetch f) {
		boolean cache = (f.isUseCache() && (!f.isResultsIncomplete()));
		if (cache) {
			List<String> collections = new ArrayList<String>(f.getEntities());
			collections.add(f.getClassName());
			for (String collectionName: collections) {
				if (!cachedCollections.contains(collectionName)) {
					cache = false;
				}
			}
		}
		return cache;
	}
	
	private List<String> getPersistKeyListNoLock(SortedMap<Long,List<String>> weightKeyMap) {
		List<String> persistKeyList = new ArrayList<String>();
		ArrayList<Long> keys = new ArrayList<Long>(weightKeyMap.keySet());
		int num = 0;
		for(int i=keys.size()-1; i>=0;i--){
			List<String> keyList = weightKeyMap.get(keys.get(i));
			for (String key: keyList) {
				persistKeyList.add(key);
				num++;
				if (num>=DbConfig.getInstance().getCachePersistSize()) {
					break;
				}
			}
			if (num>=DbConfig.getInstance().getCachePersistSize()) {
				break;
			}
        }
		return persistKeyList;
	}
}	
