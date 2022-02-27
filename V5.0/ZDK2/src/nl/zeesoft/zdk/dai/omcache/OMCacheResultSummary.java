package nl.zeesoft.zdk.dai.omcache;

import nl.zeesoft.zdk.dai.MapPrediction;
import nl.zeesoft.zdk.dai.Prediction;

public class OMCacheResultSummary {
	public OMCacheResult	top				= null;
	public int 				winnerLevel		= 0;
	public OMCacheResult	winner			= null;
		
	public OMCacheResultSummary(OMCacheResult top) {
		this.top = top;
		determineWinner(top, 0);
		if (winner.secondary==null) {
			determineWinnerSecondary(top, 0);
		}
	}
	
	public void determineWinner(OMCacheResult result, int level) {
		if (winner==null || (result.similarity > winner.similarity && level >= winnerLevel)) {
			winnerLevel = level;
			winner = result;
		}
		for (OMCacheResult res: result.subResults) {
			determineWinner(res, (level + 1));
		}
	}
	
	public void determineWinnerSecondary(OMCacheResult result, int level) {
		if (level >= winnerLevel && result!=winner && (winner.secondary==null || (result.similarity > winner.secondary.similarity))) {
			winner.secondary = result;
		}
		for (OMCacheResult res: result.subResults) {
			determineWinnerSecondary(res, (level + 1));
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
			MapPrediction mp = prediction.getOrAddMapPrediction(cache.value);
			mp.votes++;
			mp.support += ((float)cache.count / (float)total) * res.similarity;
		}
	}
}
