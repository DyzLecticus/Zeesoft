package nl.zeesoft.zdk.dai.predict;

import nl.zeesoft.zdk.dai.cache.CacheResult;

public class PredictorCacheResult {
	public float		mergeSimilarity = 1F;
	public CacheResult	cacheResult		= null;
	public long			timeNs			= System.nanoTime();
	
	public PredictorCacheResult() {
		
	}
	
	protected PredictorCacheResult(float mergeSimilarity) {
		this.mergeSimilarity = mergeSimilarity; 
	}
	
	protected void setResult(CacheResult result) {
		cacheResult = result;
		timeNs = System.nanoTime() - timeNs;
	}
	
	public float getTimeMs() {
		return (float)timeNs / 1000000F;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Merge similarity: " + mergeSimilarity);
		str.append("\n");
		str.append(cacheResult);
		str.append("\nTime: " + getTimeMs() + " ms");
		return str.toString();
	}
}
