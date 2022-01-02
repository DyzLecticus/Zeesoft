package nl.zeesoft.zdk.dai.predict;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.dai.ObjMap;

public class PrPredictionList {
	public List<PrPrediction>		list	= new ArrayList<PrPrediction>();
	public int					maxSize	= 1000;
	
	public PrPredictionList() {
		
	}
	
	public PrPredictionList(int maxSize) {
		this.maxSize = maxSize;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (PrPrediction prediction: list) {
			if (str.length()>0) {
				str.append("\n");
			}
			str.append(prediction.getPredictedMap());
		}
		return str.toString();
	}
	
	public void add(PrPrediction prediction) {
		this.list.add(0, prediction);
		applyMaxSize();
	}
	
	public void addAll(List<PrPrediction> list) {
		for (PrPrediction prediction: list) {
			this.list.add(0, prediction);
		}
		applyMaxSize();
	}
	
	public void applyMaxSize() {
		while(list.size()>maxSize) {
			list.remove((int)(list.size() - 1));
		}
	}

	public Float getKeyConfidence(String key) {
		return getKeyConfidences().get(key);
	}

	public SortedMap<String,Float> getKeyConfidences() {
		SortedMap<String,Float> r = new TreeMap<String,Float>();
		SortedMap<String,Float> totals = new TreeMap<String,Float>();
		SortedMap<String,Integer> counts = new TreeMap<String,Integer>();
		for (PrPrediction prediction: list) {
			addConfidences(prediction.getPredictedMapConfidences(), totals, counts);
		}
		for (Entry<String,Float> entry: totals.entrySet()) {
			r.put(entry.getKey(), entry.getValue() / counts.get(entry.getKey()).floatValue()); 
		}
		return r;
	}
	
	public void addConfidences(ObjMap confidences, SortedMap<String,Float> totals, SortedMap<String,Integer> counts) {
		for (Entry<String,Object> entry: confidences.values.entrySet()) {
			Float total = totals.get(entry.getKey());
			Integer count = counts.get(entry.getKey());
			if (total==null) {
				total = new Float(0);
				count = new Integer(0);
			}
			total += (Float) entry.getValue();
			count++;
			totals.put(entry.getKey(),total);
			counts.put(entry.getKey(),count);
		}
	}
}
