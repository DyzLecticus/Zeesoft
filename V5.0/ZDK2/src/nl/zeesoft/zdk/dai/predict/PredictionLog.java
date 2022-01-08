package nl.zeesoft.zdk.dai.predict;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.MathUtil;
import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;
import nl.zeesoft.zdk.dai.Prediction;

public class PredictionLog {
	protected ObjMapComparator		comparator	= new ObjMapComparator();
	protected ObjMapList			history		= new ObjMapList();
	protected List<Prediction>		list		= new ArrayList<Prediction>();
	protected int					maxSize		= 1000;
	
	public PredictionLog copy() {
		PredictionLog r = new PredictionLog();
		r.setComparator(comparator);
		r.setMaxSize(maxSize);
		r.history.keys.addAll(history.keys);
		for (ObjMap map: history.list) {
			r.history.list.add(map);
		}
		for (Prediction prediction: list) {
			r.list.add(prediction);
		}
		return r;
	}
	
	public void setComparator(ObjMapComparator comparator) {
		this.comparator = comparator;
	}
	
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
		this.history.maxSize = maxSize;
	}
	
	public void add(ObjMap map, Prediction prediction) {
		history.add(map);
		this.list.add(0, prediction);
		applyMaxSize();
	}
	
	public void applyMaxSize() {
		while(list.size()>maxSize) {
			list.remove((int)(list.size() - 1));
		}
	}
	
	public List<ObjMap> getHistory() {
		return new ArrayList<ObjMap>(history.list);
	}
	
	public List<Prediction> getPredictions() {
		return new ArrayList<Prediction>(list);
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
			str.append(prediction.getPredictedMap());
			if (prediction.getWeightedMap().values.size()>0) {
				str.append(", weighted: ");
				str.append(prediction.getWeightedMap());
			}
			i++;
		}
		return str.toString();
	}
	
	public float getKeyAccuracyTrend(String key, boolean weighted) {
		return getKeyAccuracy(key, weighted, 0.1F);
	}

	public float getKeyAccuracy(String key, boolean weighted, float trend) {
		int max = 0;
		if (trend>0F) {
			max = (int)((float)list.size() * trend);
		}
		return MathUtil.getAverage(getKeyAccuracies(key, weighted, max, comparator));
	}

	public float getKeyAccuracy(String key, boolean weighted) {
		return MathUtil.getAverage(getKeyAccuracies(key, weighted, 0, comparator));
	}

	public float getKeyAccuracyStdDev(String key, boolean weighted) {
		return MathUtil.getStandardDeviation(getKeyAccuracies(key, weighted, 0, comparator));
	}

	public List<Float> getKeyAccuracies(String key, boolean weighted, int max, ObjMapComparator comparator) {
		List<Float> r = new ArrayList<Float>();
		if (history.keys.contains(key)) {
			int i = 0;
			for (Prediction prediction: list) {
				if (i > 0) {
					ObjMap actualMap = history.list.get(i - 1);
					Object predictedValue = prediction.getPredictedMap().values.get(key);
					if (weighted && prediction.getWeightedMap().values.containsKey(key)) {
						predictedValue = prediction.getWeightedMap().values.get(key);
					}
					r.add(comparator.calculateSimilarity(predictedValue,actualMap.values.get(key),key));
				}
				i++;
				if (i==max) {
					break;
				}
			}
		}
		return r;
	}

	public Float getKeyWeight(String key) {
		return MathUtil.getAverage(getKeyWeights(key));
	}

	public Float getKeyWeightStdDev(String key) {
		return MathUtil.getStandardDeviation(getKeyWeights(key));
	}

	public List<Float> getKeyWeights(String key) {
		List<Float> r = new ArrayList<Float>();
		for (Prediction prediction: list) {
			Object conf = prediction.getWeightsMap().values.get(key);
			if (conf!=null) {
				r.add((Float)conf);
			}
		}
		return r;
	}
}
