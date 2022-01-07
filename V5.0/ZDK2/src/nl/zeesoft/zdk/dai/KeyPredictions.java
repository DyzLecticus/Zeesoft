package nl.zeesoft.zdk.dai;

import java.util.ArrayList;
import java.util.List;

public class KeyPredictions {
	public List<String>				keys				= new ArrayList<String>();
	public List<KeyPrediction>		keyPredictions		= new ArrayList<KeyPrediction>();
	public ObjMap					predictedMap		= new ObjMap();
	public ObjMap					weightsMap			= new ObjMap();
	public ObjMap					weightedMap			= new ObjMap();
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key: keys) {
			if (str.length()>0) {
				str.append("\n");
			}
			str.append("- key: " + key);
			for (KeyPrediction kp: getKeyPredictions(key)) {
				str.append(" [" + kp.predictedValue + " = " + kp.weight + "]");
			}
		}
		return str.toString();
	}
	
	public void calculatePredictedMap(List<MapPrediction> mapPredictions) {
		keys = addKeyPredictions(mapPredictions);
		orderKeyPredictions();
		calculateKeyPredictionWeights();
		calculateWeightedPredictions();
	}
	
	public List<String> addKeyPredictions(List<MapPrediction> mapPredictions) {
		List<String> r = new ArrayList<String>();
		for (MapPrediction prediction: mapPredictions) {
			for (String key: prediction.predictedMap.values.keySet()) {
				KeyPrediction kp = getOrAddKeyPrediction(key, prediction.predictedMap.values.get(key));
				kp.support += (prediction.support / (float) mapPredictions.size());
				if (!r.contains(key)) {
					r.add(key);
				}
			}
		}
		return r;
	}
	
	public void orderKeyPredictions() {
		List<KeyPrediction> list = new ArrayList<KeyPrediction>();
		for (KeyPrediction keyPrediction: keyPredictions) {
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
		keyPredictions = list;
	}
	
	public void calculateKeyPredictionWeights() {
		for (String key: keys) {
			List<KeyPrediction> list = getKeyPredictions(key);
			calculateRelativePredictionWeights(list, key);
		}
	}
	
	public void calculateRelativePredictionWeights(List<KeyPrediction> list, String key) {
		float total = 0F;
		for (KeyPrediction prediction: list) {
			total += prediction.support;
		}
		int i = 0;
		for (KeyPrediction prediction: list) {
			prediction.weight = prediction.support / total;
			if (i==0) {
				setPredictedMapValueAndWeight(key, prediction.predictedValue, prediction.weight);
			}
			i++;
		}
	}
	
	public void setPredictedMapValueAndWeight(String key, Object value, float weight) {
		predictedMap.values.put(key, value);
		weightsMap.values.put(key, weight);
	}
	
	public void calculateWeightedPredictions() {
		for (String key: keys) {
			List<KeyPrediction> list = getWeighablePredictions(key);
			float total = 0F;
			for (KeyPrediction prediction: list) {
				total += prediction.weight;
			}
			if (total>0F) {
				float value = 0;
				for (KeyPrediction prediction: list) {
					value += (prediction.weight / total) * (Float)prediction.predictedValue;
				}
				weightedMap.values.put(key, value);
			}
		}
	}
	
	public KeyPrediction getKeyPrediction(String key, Object predictedValue) {
		KeyPrediction r = null;
		for (KeyPrediction prediction: keyPredictions) {
			if (prediction.key.equals(key) && 
				(prediction.predictedValue==predictedValue || (prediction.predictedValue != null && prediction.predictedValue.equals(predictedValue)))) {
				r = prediction;
				break;
			}
		}
		return r;
	}
	
	public KeyPrediction getOrAddKeyPrediction(String key, Object predictedValue) {
		KeyPrediction r = getKeyPrediction(key, predictedValue);
		if (r==null) {
			r = new KeyPrediction(key, predictedValue);
			keyPredictions.add(r);
		}
		return r;
	}
	
	public List<KeyPrediction> getKeyPredictions(String key) {
		List<KeyPrediction> r = new ArrayList<KeyPrediction>();
		for (KeyPrediction prediction: keyPredictions) {
			if (prediction.key.equals(key)) {
				r.add(prediction);
			}
		}
		return r;
	}
	
	public List<KeyPrediction> getWeighablePredictions(String key) {
		List<KeyPrediction> r = new ArrayList<KeyPrediction>();
		for (KeyPrediction prediction: keyPredictions) {
			if (prediction.key.equals(key) && prediction.predictedValue!=null && prediction.predictedValue instanceof Float) {
				r.add(prediction);
				if (r.size()==2) {
					break;
				}
			}
		}
		return r;
	}
}
