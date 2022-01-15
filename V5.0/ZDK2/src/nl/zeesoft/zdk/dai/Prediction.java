package nl.zeesoft.zdk.dai;

import java.util.ArrayList;
import java.util.List;

public class Prediction {
	public List<MapPrediction>	mapPredictions	= new ArrayList<MapPrediction>();
	public KeyPredictions		keyPredictions	= new KeyPredictions();
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (MapPrediction mp: mapPredictions) {
			if (str.length()>0) {
				str.append("\n");
			}
			str.append(mp);
		}
		str.append("\n" + keyPredictions);
		return str.toString();
	}
	
	public static Prediction mergePredictions(List<Prediction> predictions) {
		Prediction r = new Prediction();
		for (Prediction p: predictions) {
			for (MapPrediction mp: p.mapPredictions) {
				MapPrediction nmp = r.getOrAddMapPrediction(mp.predictedMap);
				nmp.votes++;
				nmp.support += mp.support;
			}
		}
		r.calculateMapPredictionSupport();
		r.calculatePredictedMap();
		return r;
	}
	
	public static Prediction mergePredictions(Prediction... predictions) {
		List<Prediction> list = new ArrayList<Prediction>();
		for (int i = 0; i < predictions.length; i++) {
			list.add(predictions[i]);
		}
		return mergePredictions(list);
	}
	
	public void calculateMapPredictionSupport() {
		int maxVotes = 0;
		for (MapPrediction mp: mapPredictions) {
			if (mp.votes > maxVotes) {
				maxVotes = mp.votes;
			}
		}
		for (MapPrediction mp: mapPredictions) {
			if (mp.votes>1) {
				mp.support = mp.support / (float)mp.votes;
			}
			if (maxVotes>1) {
				mp.support = mp.support * ((float)mp.votes / (float)maxVotes);
			}
			mp.votes = 0;
		}
	}
	
	public void calculatePredictedMap() {
		keyPredictions.calculatePredictedMap(mapPredictions);
	}

	public ObjMap getPredictedMap() {
		return keyPredictions.predictedMap;
	}

	public ObjMap getWeightsMap() {
		return keyPredictions.weightsMap;
	}

	public ObjMap getWeightedMap() {
		return keyPredictions.weightedMap;
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
}
