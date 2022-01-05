package nl.zeesoft.zdk.dai.predict;

import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;

public class PredictorRequestWorker implements Runnable {
	protected PredictorRequest	predictorRequest	= null;
	protected PredictorCache	predictorCache		= null;
	protected ObjMapList		baseList			= null;
	protected ObjMapComparator	comparator			= null;
	protected float				minSimilarity		= 0F;
	
	protected PredictorRequestWorker(PredictorRequest predictorRequest, PredictorCache predictorCache, ObjMapList baseList, ObjMapComparator comparator, float minSimilarity) {
		this.predictorRequest = predictorRequest;
		this.predictorCache = predictorCache;
		this.baseList = baseList;
		this.comparator = comparator;
		this.minSimilarity = minSimilarity;
	}

	@Override
	public void run() {
		PredictorCacheResult result = predictorCache.getCacheResult(baseList, comparator, minSimilarity);
		predictorRequest.workerIsDone(this, result);
	}
	
	protected void start() {
		Thread thread = new Thread(this);
		thread.start();
	}
}
