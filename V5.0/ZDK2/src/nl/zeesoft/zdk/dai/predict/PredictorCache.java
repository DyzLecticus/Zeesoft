package nl.zeesoft.zdk.dai.predict;

import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;
import nl.zeesoft.zdk.dai.cache.Cache;

public class PredictorCache {
	protected float					mergeSimilarity = 1F;
	protected int					maxSize			= 10000;		
	
	private Cache					cache			= new Cache();
	private PredictorCacheWorker	worker			= null;
	
	private int						requests		= 0;
	private float					requestMs		= 0F;
	
	public synchronized void hitCache(ObjMapList history, ObjMapComparator comparator) {
		ObjMapList baseList = history.getSubList(1,cache.indexes);
		ObjMap nextMap = history.list.get(0);
		worker = new PredictorCacheWorker(this, baseList, nextMap, mergeSimilarity, comparator);
		worker.start();
	}

	public synchronized void hitCache(ObjMapList baseList, ObjMap nextMap, ObjMapComparator comparator) {
		cache.hitCache(baseList,nextMap,mergeSimilarity,comparator);
		while(cache.elements.size()>maxSize) {
			cache.elements.remove(cache.elements.size() - 1);
		}
		worker = null;
	}
	
	public synchronized boolean isUpdatingCache() {
		return worker!=null;
	}
	
	public synchronized PredictorCacheResult getCacheResult(ObjMapList baseList, ObjMapComparator comparator, float minSimilarity) {
		PredictorCacheResult r = new PredictorCacheResult(mergeSimilarity);
		r.setResult(cache.getCacheResult(baseList, comparator, minSimilarity));
		requests++;
		requestMs+=r.getTimeMs();
		return r;
	}
	
	public synchronized void setCache(Cache cache) {
		this.cache = cache;
	}
	
	public synchronized int getCacheSize() {
		return cache.elements.size();
	}
	
	public synchronized float getAverageRequestMs() {
		return requestMs / (float)requests;
	}
}
