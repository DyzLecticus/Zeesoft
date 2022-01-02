package nl.zeesoft.zdk.dai.cache;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.dai.MapPrediction;
import nl.zeesoft.zdk.dai.Prediction;

public class CacheResult {
	public List<CacheElement>	results		= new ArrayList<CacheElement>();
	public float				similarity	= 0F;
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Similarity: " + similarity);
		for (CacheElement cache: results) {
			str.append("\n");
			str.append(cache.nextMap + ", count: " + cache.count);
		}
		return str.toString();
	}

	public Prediction getPrediction() {
		Prediction r = new Prediction();
		addMapPredictions(r);
		r.calculatePredictedMap();
		return r;
	}
	
	public void addMapPredictions(Prediction prediction) {
		int total = 0;
		for (CacheElement cache: results) {
			MapPrediction mp = prediction.getOrAddMapPrediction(cache.nextMap);
			mp.votes += cache.count;
			total += cache.count;
		}
		if (total>0) {
			for (MapPrediction mp: prediction.mapPredictions) {
				mp.confidence = ((float)mp.votes / (float)total) * similarity;
			}
		}
	}
}
