package nl.zeesoft.zdk.dai.predict;

import nl.zeesoft.zdk.dai.cache.CacheResult;

public class PredictorCacheResult extends CacheResult {
	protected PredictorCache	predictorCache	= null;
	protected CacheResult		result			= null;
	protected float				mergeSimilarity = 1F;
	
	public PredictorCacheResult() {
		
	}
	
	public PredictorCacheResult(CacheResult result, float mergeSimilarity) {
		this.result = result;
		this.mergeSimilarity = mergeSimilarity;
	}
}
