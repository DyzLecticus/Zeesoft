package nl.zeesoft.zdk.dai.optimize;

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Console;
import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;
import nl.zeesoft.zdk.dai.predict.ObjMapPrediction;
import nl.zeesoft.zdk.dai.predict.Prediction;
import nl.zeesoft.zdk.dai.predict.PredictionList;
import nl.zeesoft.zdk.dai.recognize.ListPatternRecognizer;
import nl.zeesoft.zdk.dai.recognize.PatternRecognizer;

public class Optimizer {
	public void calculatePatternRecognizerWeights(PatternRecognizer patternRecognizer, ObjMapList history, PredictionList  predictions, ObjMapComparator comparator) {
		calculatePatternRecognizerAccuracy(patternRecognizer, history, predictions, comparator);
		float max = 0F;
		float min = 1F;
		for (ListPatternRecognizer lpr: patternRecognizer.patternRecognizers) {
			if (lpr.accuracy > max) {
				max = lpr.accuracy;
			}
			if (lpr.accuracy < min) {
				min = lpr.accuracy;
			}
		}
		float diff = max - min;
		calculatePatternRecognizerWeights(patternRecognizer, diff, min);
	}

	public void calculatePatternRecognizerAccuracy(PatternRecognizer patternRecognizer, ObjMapList history, PredictionList  predictions, ObjMapComparator comparator) {
		if (predictions.list.size()>1) {
			calculateObjMapAccuracy(history, predictions, comparator);
			for (ListPatternRecognizer lpr: patternRecognizer.patternRecognizers) {
				float total = 0F;
				for (Prediction prediction: predictions.list) {
					for (ObjMapPrediction omp: prediction.objMapPredictions) {
						if (omp.patternRecognizer == lpr) {
							total += omp.accuracy;
						}
					}
				}
				lpr.accuracy = total / predictions.list.size();
			}
		} else {
			for (ListPatternRecognizer lpr: patternRecognizer.patternRecognizers) {
				lpr.accuracy = 1F;
			}
		}
	}
	
	public void calculatePatternRecognizerWeights(PatternRecognizer patternRecognizer, float diff, float min) {
		if (diff > 0F) {
			for (ListPatternRecognizer lpr: patternRecognizer.patternRecognizers) {
				Console.log(lpr.accuracy + ", min: " + min + ", diff: " + diff);
				lpr.weight = (lpr.accuracy - min) / diff;
				if (lpr.weight < 0.01F) {
					lpr.weight = 0.01F;
				}
			}
		} else {
			for (ListPatternRecognizer lpr: patternRecognizer.patternRecognizers) {
				lpr.weight = 1F;
			}
		}
	}
	
	public void calculateObjMapAccuracy(ObjMapList history, PredictionList predictions, ObjMapComparator comparator) {
		for (int i = history.list.size() - 1; i > 0; i--) {
			ObjMap actualValue = history.list.get(i - 1);
			for (ObjMapPrediction omp: predictions.list.get(i).objMapPredictions) {
				omp.accuracy = comparator.calculateSimilarity(omp.predictedMap, actualValue) * omp.confidence;
			}
		}
	}
	
	public Float getAccuracy(ObjMapList history, PredictionList predictions, ObjMapComparator comparator) {
		float r = 0F;
		SortedMap<String, Float> keyAccuracy = getKeyAccuracies(history, predictions, comparator);
		for (Float accuracy: keyAccuracy.values()) {
			r += accuracy;
		}
		if (keyAccuracy.size()>0) {
			r = r / (float)keyAccuracy.size();
		}
		return r;
	}
	
	public Float getKeyAccuracy(ObjMapList history, PredictionList predictions, ObjMapComparator comparator, String key) {
		return getKeyAccuracy(getErrors(history, predictions, comparator)).get(key);
	}
	
	public SortedMap<String,Float> getKeyAccuracies(ObjMapList history, PredictionList predictions, ObjMapComparator comparator) {
		return getKeyAccuracy(getErrors(history, predictions, comparator));
	}

	public SortedMap<String,Float> getKeyAccuracy(ObjMapList errors) {
		SortedMap<String,Float> r = new TreeMap<String,Float>();
		SortedMap<String,Float> total = new TreeMap<String,Float>();
		SortedMap<String,Integer> count = new TreeMap<String,Integer>();
		for (ObjMap errorMap: errors.list) {
			addErrorMap(errorMap, total, count);
		}
		for (Entry<String,Float> entry: total.entrySet()) {
			Integer num = count.get(entry.getKey());
			r.put(entry.getKey(), entry.getValue() / num.floatValue());
		}
		return r;
	}

	public void addErrorMap(ObjMap errorMap, SortedMap<String,Float> total, SortedMap<String,Integer> count) {
		for (String key: errorMap.values.keySet()) {
			Float acc = total.get(key);
			Integer num = count.get(key);
			if (acc==null) {
				acc = new Float(0F);
				num = new Integer(0);
			}
			acc += 1F - (Float)errorMap.values.get(key);
			num++;
			total.put(key, acc);
			count.put(key, num);
		}
	}
	
	public ObjMapList getErrors(ObjMapList history, PredictionList predictions, ObjMapComparator comparator) {
		ObjMapList r = new ObjMapList(history.maxSize);
		for (int i = history.list.size() - 1; i > 0; i--) {
			addErrorMap(r, comparator, predictions.list.get(i).getPredictedMap(), history.list.get(i - 1));
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
