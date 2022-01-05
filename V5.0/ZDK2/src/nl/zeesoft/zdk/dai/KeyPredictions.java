package nl.zeesoft.zdk.dai;

import java.util.ArrayList;
import java.util.List;

public class KeyPredictions {
	public List<String>				keys				= new ArrayList<String>();
	public List<KeyPrediction>		keyPredictions		= new ArrayList<KeyPrediction>();
	public ObjMap					predictedMap		= new ObjMap();
	public ObjMap					confidencesMap		= new ObjMap();
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
				str.append(" [" + kp.predictedValue + " = " + kp.confidence + "]");
			}
		}
		return str.toString();
	}
	
	public void calculatePredictedMap(List<MapPrediction> mapPredictions) {
		keys = addKeyPredictions(mapPredictions);
		orderKeyPredictions();
		calculateKeyPredictionConfidences(keys);
		calculateWeightedPredictions(keys);
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
	
	public void calculateKeyPredictionConfidences(List<String> keys) {
		for (String key: keys) {
			List<KeyPrediction> list = getKeyPredictions(key);
			if (list.size()==1) {
				KeyPrediction prediction = list.get(0);
				prediction.confidence = prediction.support;
				setPredictedMapValueAndConfidence(key, prediction.predictedValue, prediction.confidence);
			} else if (list.size()>1) {
				calculateRelativePredictionConfidences(list, key);
			}
		}
	}
	
	public void calculateRelativePredictionConfidences(List<KeyPrediction> list, String key) {
		float total = 0F;
		for (KeyPrediction prediction: list) {
			total += prediction.support;
		}
		int i = 0;
		for (KeyPrediction prediction: list) {
			float nextSupport = 0F;
			if (list.size()>(i+1)) {
				nextSupport = list.get(i+1).support;
			}
			if (prediction.support > nextSupport && total > 0F) {
				prediction.confidence = (prediction.support - nextSupport) / total;
			}
			if (i==0) {
				setPredictedMapValueAndConfidence(key, prediction.predictedValue, prediction.confidence);
			}
			i++;
		}
	}
	
	public void setPredictedMapValueAndConfidence(String key, Object value, float confidence) {
		predictedMap.values.put(key, value);
		confidencesMap.values.put(key, confidence);
	}
	
	public void calculateWeightedPredictions(List<String> keys) {
		for (String key: keys) {
			List<KeyPrediction> list = getWeighablePredictions(key);
			float total = 0F;
			for (KeyPrediction prediction: list) {
				total += prediction.support;
			}
			if (total>0F) {
				float value = 0;
				for (KeyPrediction prediction: list) {
					value += (prediction.support / total) * (Float)prediction.predictedValue;
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
