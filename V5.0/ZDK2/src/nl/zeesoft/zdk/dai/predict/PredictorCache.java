package nl.zeesoft.zdk.dai.predict;

import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;
import nl.zeesoft.zdk.dai.cache.Cache;
import nl.zeesoft.zdk.dai.cache.CacheElement;
import nl.zeesoft.zdk.dai.cache.CacheElementsLinked;

public class PredictorCache {
	public MsLogger					hitMsLogger				= new MsLogger();
	public MsLogger					requestMsLogger			= new MsLogger();
	
	protected float					mergeSimilarity 		= 1F;
	protected boolean				linked					= false;
	protected float					linkedMergeSimilarity 	= 1F;
	
	private Cache					cache					= null;
	private PredictorCacheWorker	worker					= null;
	
	public PredictorCache() {
		cache = new Cache();		
	}
	
	public PredictorCache(float mergeSimilarity, int maxSize, boolean linked, float linkedMergeSimilarity, int linkedMaxSize) {
		this.mergeSimilarity = mergeSimilarity;
		this.linked = linked;
		this.linkedMergeSimilarity = linkedMergeSimilarity;
		if (linked) {
			cache = new Cache(maxSize, linkedMergeSimilarity, linkedMaxSize);
		} else {
			cache = new Cache(maxSize);
		}
	}
	
	public synchronized void hitCache(ObjMapList history, ObjMapComparator comparator) {
		ObjMapList baseList = history.getSubList(1,cache.indexes);
		if (baseList.list.size()>0) {
			ObjMap nextMap = history.list.get(0);
			worker = new PredictorCacheWorker(this, baseList, nextMap, comparator);
			worker.start();
		}
	}

	public synchronized void hitCache(ObjMapList baseList, ObjMap nextMap, ObjMapComparator comparator) {
		long start = System.nanoTime();
		cache.hitCache(baseList,nextMap,mergeSimilarity,comparator);
		hitMsLogger.add((float)(System.nanoTime() - start) / 1000000F);
		worker = null;
	}
	
	public synchronized boolean isHittingCache() {
		return worker!=null;
	}
	
	public synchronized PredictorCacheResult getCacheResult(ObjMapList baseList, ObjMapComparator comparator, float minSimilarity) {
		PredictorCacheResult r = null;
		float ms = mergeSimilarity;
		if (cache.elements instanceof CacheElementsLinked) {
			ms = linkedMergeSimilarity;
		}
		r = new PredictorCacheResult(ms);
		r.setResult(cache.getCacheResult(baseList, comparator, minSimilarity));
		requestMsLogger.add(r.getTimeMs());
		return r;
	}
	
	public synchronized int getCacheSize() {
		int r = 0;
		if (cache.elements instanceof CacheElementsLinked) {
			r = 0;
			for (CacheElement element: cache.elements.elements) {
				r += element.elements.elements.size();
			}
		} else {
			r = cache.elements.elements.size();
		}
		return r;
	}
	
	public synchronized int getCacheMaxSize() {
		return cache.elements.maxSize;
	}
	
	public synchronized int getCacheLinkedMaxSize() {
		return ((CacheElementsLinked)cache.elements).linkedMaxSize;
	}
}
