package nl.zeesoft.zdk.dai.predict;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;
import nl.zeesoft.zdk.dai.cache.CacheResult;

public class PredictorRequest {
	private float							minSimilarity	= 0F;
	private int								minCacheIndex	= 0;
	private int								maxCacheIndex	= Integer.MAX_VALUE;

	private List<PredictorRequestWorker>	workers			= new ArrayList<PredictorRequestWorker>();
	
	private List<CacheResult>				cacheResults	= new ArrayList<CacheResult>();
	
	protected synchronized void process(List<PredictorCache> caches, ObjMapList baseList, ObjMapComparator comparator) {
		int max = maxCacheIndex;
		if (max > caches.size() - 1) {
			max = caches.size() - 1;
		}
		int min = minCacheIndex;
		for (int i = max; i >= min; i--) {
			PredictorRequestWorker worker = new PredictorRequestWorker(this, caches.get(i), baseList, comparator, minSimilarity);
			workers.add(worker);
			worker.start();
		}
	}
	
	protected synchronized void workerIsDone(PredictorRequestWorker worker, CacheResult result) {
		workers.remove(worker);
		cacheResults.add(result);
	}
	
	public synchronized boolean isProcessing() {
		return workers.size()>0;
	}
	
	public synchronized List<CacheResult> getResults() {
		return new ArrayList<CacheResult>(cacheResults);
	}
	
	public synchronized float getMinSimilarity() {
		return minSimilarity;
	}

	public synchronized void setMinSimilarity(float minSimilarity) {
		this.minSimilarity = minSimilarity;
	}

	public synchronized int getMinCacheIndex() {
		return minCacheIndex;
	}

	public synchronized void setMinCacheIndex(int minCacheIndex) {
		this.minCacheIndex = minCacheIndex;
	}

	public synchronized int getMaxCacheIndex() {
		return maxCacheIndex;
	}

	public synchronized void setMaxCacheIndex(int maxCacheIndex) {
		this.maxCacheIndex = maxCacheIndex;
	}
}
