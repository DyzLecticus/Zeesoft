package nl.zeesoft.zdk.dai.analyze;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.MathUtil;
import nl.zeesoft.zdk.dai.History;
import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.Prediction;

public class PredictionList {
	public History				history		= null;
	public List<Prediction>		list		= new ArrayList<Prediction>();
	public int					maxSize		= 1000;
	
	public PredictionList() {
		
	}
	
	public PredictionList(History history) {
		this.history = history;
		this.maxSize = history.maxSize;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		int i = 0;
		for (Prediction prediction: list) {
			if (str.length()>0) {
				str.append("\n");
			}
			str.append(history.list.get(i));
			str.append(" -> ");
			str.append(prediction.predictedMap);
			i++;
		}
		return str.toString();
	}

	public Float getKeyAccuracy(String key, ObjMapComparator comparator) {
		return MathUtil.getAverage(getKeyAccuracies(key, comparator));
	}

	public Float getKeyAccuracyStdDev(String key, ObjMapComparator comparator) {
		return MathUtil.getStandardDeviation(getKeyAccuracies(key, comparator));
	}

	public List<Float> getKeyAccuracies(String key, ObjMapComparator comparator) {
		List<Float> r = new ArrayList<Float>();
		if (history.keys.contains(key)) {
			int i = 0;
			for (Prediction prediction: list) {
				if (i > 0) {
					ObjMap actualMap = history.list.get(i - 1);
					r.add(comparator.calculateSimilarity(prediction.predictedMap.values.get(key),actualMap.values.get(key)));
				}
				i++;
			}
		}
		return r;
	}

	public Float getKeyConfidence(String key) {
		return MathUtil.getAverage(getKeyConfidences(key));
	}

	public Float getKeyConfidenceStdDev(String key) {
		return MathUtil.getStandardDeviation(getKeyConfidences(key));
	}

	public List<Float> getKeyConfidences(String key) {
		List<Float> r = new ArrayList<Float>();
		for (Prediction prediction: list) {
			Object conf = prediction.predictedConfidencesMap.values.get(key);
			if (conf!=null) {
				r.add((Float)conf);
			}
		}
		return r;
	}
	
	public void add(Prediction prediction) {
		this.list.add(0, prediction);
		applyMaxSize();
	}
	
	public void addAll(List<Prediction> list) {
		for (Prediction prediction: list) {
			this.list.add(0, prediction);
		}
		applyMaxSize();
	}
	
	public void applyMaxSize() {
		while(list.size()>maxSize) {
			list.remove((int)(list.size() - 1));
		}
	}
}
