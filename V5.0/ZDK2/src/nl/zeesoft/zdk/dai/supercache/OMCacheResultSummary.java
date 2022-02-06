package nl.zeesoft.zdk.dai.supercache;

import nl.zeesoft.zdk.dai.MapPrediction;
import nl.zeesoft.zdk.dai.Prediction;

public class OMCacheResultSummary {
	public OMCacheResult	top				= null;
	
	public int 				addCount		= 0;
	
	public OMCacheResult	primary			= null;
	public int				primaryCount	= 0;
	
	public OMCacheResult	secondary		= null;
	public int				secondaryCount	= 0;
		
	public OMCacheResultSummary(OMCacheResult top) {
		checkResult(top, 0);
	}
	
	public void checkResult(OMCacheResult result, int count) {
		if (primary==null || result.similarity >= primary.similarity) {
			addCount = count;
			primary = result;
			if (result.secondary!=null && (secondary==null || result.secondary.similarity >= secondary.similarity)) {
				secondary = result;
			}
		}
		checkSubResults(result, count);
	}
	
	public void checkSubResults(OMCacheResult result, int count) {
		int index = 0;
		for (OMCacheResult res: result.subResults) {
			int add = 0;
			if (res.elements.size()>index) {
				add = res.elements.get(index).count;
			}
			checkResult(res, (count + add));
			index++;
		}
	}
	
	public Prediction getPrediction() {
		Prediction r = new Prediction();
		addMapPredictions(r, primary);
		if (secondary!=null) {
			addMapPredictions(r, secondary);
		}
		r.calculateMapPredictionSupport();
		r.calculatePredictedMap();
		return r;
	}
	
	public void addMapPredictions(Prediction prediction, OMCacheResult res) {
		int total = 0;
		for (OMCacheElement cache: res.elements) {
			total += cache.count;
		}
		for (OMCacheElement cache: res.elements) {
			MapPrediction mp = prediction.getOrAddMapPrediction(cache.value.list.get(0));
			mp.votes++;
			mp.support += ((float)cache.count / (float)total) * res.similarity;
		}
	}
}
