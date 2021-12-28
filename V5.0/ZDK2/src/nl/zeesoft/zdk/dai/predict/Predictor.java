package nl.zeesoft.zdk.dai.predict;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;
import nl.zeesoft.zdk.dai.recognize.ListPatternRecognizer;
import nl.zeesoft.zdk.dai.recognize.PatternRecognizer;

public class Predictor {
	public PredictionList getPredictions(ObjMapList history, PatternRecognizer patternRecognizer, ObjMapComparator comparator) {
		return getPredictions(history, patternRecognizer, comparator, 0);
	}
	
	public PredictionList getPredictions(ObjMapList history, PatternRecognizer patternRecognizer, ObjMapComparator comparator, int maxDepth) {
		PredictionList r = new PredictionList(history.maxSize);
		ObjMapList workingHistory = new ObjMapList(history.maxSize);
		for (int i = history.list.size() - 1; i >= 0; i--) {
			workingHistory.add(history.list.get(i));
			patternRecognizer.detectPatterns(workingHistory, comparator, maxDepth);
			r.add(generatePrediction(workingHistory, patternRecognizer));
		}
		return r;
	}
	
	public Prediction generatePrediction(ObjMapList history, PatternRecognizer patternRecognizer) {
		Prediction r = new Prediction(patternRecognizer, history.keys);
		addObjMapPredictions(r, history, patternRecognizer);
		addKeyPredictions(r);
		orderKeyPredictions(r);
		calculateKeyPredictionConfidences(r);
		return r;
	}

	public void addObjMapPredictions(Prediction prediction, ObjMapList history, PatternRecognizer patternRecognizer) {
		if (history.list.size()==1) {
			for (String key: prediction.keys) {
				prediction.keyPredictions.add(new KeyPrediction(key, history.list.get(0).values.get(key)));
			}
		} else {
			for (ListPatternRecognizer lpr: patternRecognizer.patternRecognizers) {
				prediction.objMapPredictions.addAll(getPredictions(history, lpr));
			}
		}
	}

	public List<ObjMapPrediction> getPredictions(ObjMapList history, ListPatternRecognizer lpr) {
		List<ObjMapPrediction> r = new ArrayList<ObjMapPrediction>();
		int total = 0;
		for (Integer index: lpr.startIndexes) {
			ObjMap predictedMap = history.list.get(index - 1);
			ObjMapPrediction prediction = getOrAddPrediction(lpr,r,predictedMap);
			prediction.votes++;
			total++;
		}
		if (total>0) {
			for (ObjMapPrediction p: r) {
				p.confidence = ((float)p.votes / (float)total) * lpr.similarity * lpr.weight;
			}
		}
		return r;
	}

	public ObjMapPrediction getOrAddPrediction(ListPatternRecognizer lpr, List<ObjMapPrediction> predictions, ObjMap predictedMap) {
		ObjMapPrediction r = getPrediction(predictions, predictedMap);
		if (r==null) {
			r = new ObjMapPrediction(lpr, predictedMap);
			predictions.add(r);
		}
		return r;
	}

	public ObjMapPrediction getPrediction(List<ObjMapPrediction> predictions, ObjMap predictedMap) {
		ObjMapPrediction r = null;
		for (ObjMapPrediction omp: predictions) {
			if (omp.predictedMap.equals(predictedMap)) {
				r = omp;
				break;
			}
		}
		return r;
	}

	public void addKeyPredictions(Prediction prediction) {
		for (ObjMapPrediction objMapPrediction: prediction.objMapPredictions) {
			for (String key: objMapPrediction.predictedMap.values.keySet()) {
				KeyPrediction kp = prediction.getOrAddPrediction(key, objMapPrediction.predictedMap.values.get(key));
				kp.support += (objMapPrediction.confidence / (float) prediction.patternRecognizer.patternRecognizers.size());
				kp.patternRecognizers.add(objMapPrediction.patternRecognizer);
			}
		}
	}
	
	public void orderKeyPredictions(Prediction prediction) {
		List<KeyPrediction> list = new ArrayList<KeyPrediction>();
		for (KeyPrediction keyPrediction: prediction.keyPredictions) {
			boolean added = false;
			for (KeyPrediction listPrediction: list) {
				if (keyPrediction.support >= listPrediction.support) {
					list.add(list.indexOf(listPrediction), keyPrediction);
					added = true;
					break;
				}
			}
			if (!added) {
				list.add(keyPrediction);
			}
		}
		prediction.keyPredictions = list;
	}
	
	public void calculateKeyPredictionConfidences(Prediction prediction) {
		for (String key: prediction.keys) {
			float total = 0F;
			List<KeyPrediction> list = prediction.getPredictions(key);
			if (list.size()>0) {
				for (KeyPrediction keyPrediction: list) {
					total += keyPrediction.support;
				}
				int i = 0;
				for (KeyPrediction keyPrediction: list) {
					float nextSupport = 0F;
					if (list.size()>(i+1)) {
						nextSupport = list.get(i+1).support;
					}
					if (keyPrediction.support > nextSupport && total > 0F) {
						keyPrediction.confidence = (keyPrediction.support - nextSupport) / total;
					}
					i++;
				}
			}
		}
	}
}
