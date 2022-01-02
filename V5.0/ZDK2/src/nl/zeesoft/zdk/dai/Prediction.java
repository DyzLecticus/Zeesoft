package nl.zeesoft.zdk.dai;

import java.util.ArrayList;
import java.util.List;

public class Prediction {
	public List<MapPrediction>		mapPredictions			= new ArrayList<MapPrediction>();
	public List<KeyPrediction>		keyPredictions			= new ArrayList<KeyPrediction>();
	public ObjMap					predictedMap			= new ObjMap();
	public ObjMap					predictedConfidencesMap	= new ObjMap();
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (MapPrediction mp: mapPredictions) {
			if (str.length()>0) {
				str.append("\n");
			}
			str.append(mp);
		}
		appendKeyPredictions(str);
		return str.toString();
	}
	
	public void appendKeyPredictions(StringBuilder str) {
		for (String key: predictedMap.values.keySet()) {
			str.append("\n- key: " + key);
			for (KeyPrediction kp: getKeyPredictions(key)) {
				str.append(" [" + kp.predictedValue + " = " + kp.confidence + "]");
			}
		}
	}

	public void calculatePredictedMap() {
		List<String> keys = addKeyPredictions();
		orderKeyPredictions();
		calculateKeyPredictionConfidences(keys);
	}
	
	public List<String> addKeyPredictions() {
		List<String> r = new ArrayList<String>();
		for (MapPrediction prediction: mapPredictions) {
			for (String key: prediction.predictedMap.values.keySet()) {
				KeyPrediction kp = getOrAddKeyPrediction(key, prediction.predictedMap.values.get(key));
				kp.support += (prediction.confidence / (float) mapPredictions.size());
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
		predictedConfidencesMap.values.put(key, confidence);
	}

	public MapPrediction getMapPrediction(ObjMap predictedMap) {
		MapPrediction r = null;
		for (MapPrediction prediction: mapPredictions) {
			if (prediction.predictedMap.equals(predictedMap)) {
				r = prediction;
				break;
			}
		}
		return r;
	}
	
	public MapPrediction getOrAddMapPrediction(ObjMap predictedMap) {
		MapPrediction r = getMapPrediction(predictedMap);
		if (r==null) {
			r = new MapPrediction(predictedMap);
			mapPredictions.add(r);
		}
		return r;
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
}
