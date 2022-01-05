package nl.zeesoft.zdk.dai.predict;

import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;
import nl.zeesoft.zdk.dai.cache.Cache;

public class PredictorCache {
	protected float		mergeSimilarity = 1F;
	
	private Cache		cache			= new Cache();
	
	public synchronized void hitCache(ObjMapList history) {
		cache.hitCache(history.getSubList(1,cache.indexes),history.list.get(0));
	}
	
	public synchronized PredictorCacheResult getCacheResult(ObjMapList baseList, ObjMapComparator comparator, float minSimilarity) {
		PredictorCacheResult r = new PredictorCacheResult(mergeSimilarity);
		r.setResult(cache.getCacheResult(baseList, comparator, minSimilarity));
		return r;
	}
	
	public synchronized void setCache(Cache cache) {
		this.cache = cache;
	}
	
	public synchronized Cache getCacheCopy() {
		return cache.copy();
	}
	
	public synchronized int getCacheSize() {
		return cache.elements.size();
	}
}
