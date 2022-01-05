package nl.zeesoft.zdk.dai.analyze;

import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.cache.CacheResult;

public class Analyzer {
	public PredictionList getUntrainedPredictions(History history, ObjMapComparator comparator) {
		PredictionList r = new PredictionList(history, comparator);
		History workingHistory = new History(history.maxSize);
		for (int i = history.list.size() - 1; i >= 0; i--) {
			workingHistory.add(history.list.get(i));
			CacheResult result = workingHistory.getCacheResult(comparator, 0F);
			r.add(history.list.get(i), result.getPrediction());
		}
		return r;
	}
	
	public PredictionList getPredictions(History history, ObjMapComparator comparator) {
		PredictionList r = new PredictionList(history, comparator);
		r.setComparator(comparator);
		for (int i = history.list.size() - 1; i >= 0; i--) {
			CacheResult result = history.getCacheResult(comparator, 0F, i);
			r.add(history.list.get(i), result.getPrediction());
		}
		return r;
	}
}
