package nl.zeesoft.zdk.dai.supercache;

import nl.zeesoft.zdk.dai.MapPrediction;
import nl.zeesoft.zdk.dai.Prediction;

public class OMCacheResultSummary {
	public OMCacheResult	top				= null;
	public int 				parentCount		= 0;
	public OMCacheResult	winner			= null;
		
	public OMCacheResultSummary(OMCacheResult top) {
		this.top = top;
		checkResult(top, 0);
	}
	
	public void checkResult(OMCacheResult result, int count) {
		if (winner==null || (result.similarity >= winner.similarity && count >= parentCount)) {
			parentCount = count;
			winner = result;
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
		addMapPredictions(r, winner);
		if (winner.secondary!=null) {
			addMapPredictions(r, winner.secondary);
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
