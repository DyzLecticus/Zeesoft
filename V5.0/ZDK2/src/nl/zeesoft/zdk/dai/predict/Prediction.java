package nl.zeesoft.zdk.dai.predict;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.recognize.ListPatternRecognizer;
import nl.zeesoft.zdk.dai.recognize.PatternRecognizer;

public class Prediction {
	public PatternRecognizer		patternRecognizer		= null;
	public List<String>				keys					= null;
	public List<ObjMapPrediction>	objMapPredictions 		= new ArrayList<ObjMapPrediction>();
	public List<KeyPrediction>		keyPredictions			= new ArrayList<KeyPrediction>();
	
	public Prediction() {
		
	}
	
	public Prediction(PatternRecognizer pr, List<String> keys) {
		this.patternRecognizer = pr;
		this.keys = keys;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (ListPatternRecognizer lpr: patternRecognizer.patternRecognizers) {
			if (str.length()>0) {
				str.append("\n");
			}
			str.append(lpr.toString());
			for (ObjMapPrediction omp: objMapPredictions) {
				if (omp.patternRecognizer == lpr) {
					str.append("\n -> " + omp);
				}
			}
		}
		appendKeyPredictions(str);
		return str.toString();
	}
	
	public void appendKeyPredictions(StringBuilder str) {
		for (String key: keys) {
			str.append("\n- key: " + key);
			for (KeyPrediction kp: getPredictions(key)) {
				str.append(" [" + kp.predictedValue + " = " + kp.confidence + "]");
			}
		}
	}

	public ObjMap getPredictedMap() {
		ObjMap r = new ObjMap();
		for (String key: keys) {
			List<KeyPrediction> list = getPredictions(key);
			if (list.size()>0) {
				r.values.put(key, list.get(0).predictedValue);
			}
		}
		return r;
	}

	public ObjMap getPredictedMapConfidences() {
		ObjMap r = new ObjMap();
		for (String key: keys) {
			List<KeyPrediction> list = getPredictions(key);
			if (list.size()>0) {
				r.values.put(key, list.get(0).confidence);
			}
		}
		return r;
	}

	public List<KeyPrediction> getPredictions(String key) {
		List<KeyPrediction> r = new ArrayList<KeyPrediction>();
		for (KeyPrediction kp: keyPredictions) {
			if (kp.key.equals(key)) {
				r.add(kp);
			}
		}
		return r;
	}
	
	public KeyPrediction getOrAddPrediction(String key, Object value) {
		KeyPrediction r = getPrediction(key, value);
		if (r==null) {
			r = new KeyPrediction(key, value);
			keyPredictions.add(r);
		}
		return r;
	}

	public KeyPrediction getPrediction(String key, Object value) {
		KeyPrediction r = null;
		for (KeyPrediction kp: keyPredictions) {
			if (kp.key.equals(key) && (kp.predictedValue==value || (kp.predictedValue != null && kp.predictedValue.equals(value)))) {
				r = kp;
				break;
			}
		}
		return r;
	}
}
