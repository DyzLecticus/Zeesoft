package nl.zeesoft.zdk.dai.analyze;

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;
import nl.zeesoft.zdk.dai.predict.PredictionList;

public class Analyzer {	
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
		return getKeyAccuracies(history, predictions, comparator).get(key);
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
