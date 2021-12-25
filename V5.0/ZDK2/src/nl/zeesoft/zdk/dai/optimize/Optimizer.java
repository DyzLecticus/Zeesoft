package nl.zeesoft.zdk.dai.optimize;

import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;
import nl.zeesoft.zdk.dai.predict.Prediction;
import nl.zeesoft.zdk.dai.predict.Predictor;
import nl.zeesoft.zdk.dai.recognize.PatternRecognizer;

public class Optimizer {
	public ObjMapList getErrors(ObjMapList history, PatternRecognizer patternRecognizer, ObjMapComparator comparator, Predictor predictor) {
		ObjMapList r = new ObjMapList();
		r.maxSize = history.maxSize;
		ObjMapList workingHistory = new ObjMapList();
		workingHistory.maxSize = history.maxSize;
		for (int i = history.list.size() - 1; i > 0; i--) {
			workingHistory.add(history.list.get(i));
			patternRecognizer.detectPatterns(workingHistory, comparator);
			Prediction prediction = predictor.generatePrediction(workingHistory, patternRecognizer);
			addErrorMap(r, comparator, prediction.getPredictedMap(), history.list.get(i - 1));
		}
		return r;
	}
	
	public void addErrorMap(ObjMapList errors, ObjMapComparator comparator, ObjMap predictedMap, ObjMap actualMap) {
		ObjMap errorMap = new ObjMap();
		for (String key: actualMap.values.keySet()) {
			errorMap.values.put(key, 1.0F - comparator.calculateSimilarity(actualMap.values.get(key), predictedMap.values.get(key)));
		}
		errors.add(errorMap);
	}
}
