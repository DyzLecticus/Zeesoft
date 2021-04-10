package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.HistoricalFloat;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;
import nl.zeesoft.zdk.neural.processor.cl.Classification;

public class NetworkIOAnalyzer {
	protected List<NetworkIO>						networkIO 			= new ArrayList<NetworkIO>();
	protected SortedMap<String,HistoricalFloat>		accuracies			= new TreeMap<String,HistoricalFloat>();
	protected int									accuracyCapacity	= 100;
	
	public void setAccuracyCapacity(int accuracyCapacity) {
		this.accuracyCapacity = accuracyCapacity;
		for (HistoricalFloat accuracy: accuracies.values()) {
			accuracy.capacity = accuracyCapacity;
			accuracy.applyCapacity();
		}
	}
	
	public void add(NetworkIO io) {
		int index = networkIO.size();
		networkIO.add(io);
		for (String name: io.getProcessorNames()) {
			ProcessorIO pio = io.getProcessorIO(name);
			if (pio.outputValue instanceof Classification) {
				checkPrediction(index, name, (Classification) pio.outputValue);
			}
		}
	}
	
	public NetworkIOStats getAverageStats() {
		NetworkIOStats r = new NetworkIOStats();
		r.nsPerLayer = new TreeMap<Integer,Long>();
		int total = 0;
		for (NetworkIO io: networkIO) {
			NetworkIOStats stats = io.getStats();
			if (stats!=null) {
				total++;
				addStats(r, stats);
			}
		}
		divideStats(r, total);
		return r;
	}
	
	public void clearAccuracy() {
		accuracies.clear();
	}
	
	public NetworkIOAccuracy getAccuracy() {
		return new NetworkIOAccuracy(this);
	}
	
	protected void checkPrediction(int index, String name, Classification cls) {
		Classification prediction = getPrediction(index - cls.step, name);
		if (prediction!=null) {
			float accuracy = 0F;
			if (valueWasPredicted(prediction, cls.value)) {
				accuracy = 1F;
			}
			logAccuracy(name, accuracy);
		}
	}
	
	protected Classification getPrediction(int pIndex, String name) {
		Classification r = null;
		if (pIndex>=0) {
			NetworkIO prevIO = networkIO.get(pIndex); 
			ProcessorIO ppio = prevIO.getProcessorIO(name);
			if (ppio!=null && ppio.outputValue instanceof Classification) {
				r = (Classification) ppio.outputValue;
			}
		}
		return r;
	}
	
	protected boolean valueWasPredicted(Classification prediction, Object value) {
		boolean r = false;
		List<Object> mostCountedValues = prediction.getMostCountedValues();
		if (mostCountedValues.size()==1 && mostCountedValues.get(0).equals(value)) {
			r = true;
		}
		return r;
	}
	
	protected void logAccuracy(String name, float accuracy) {
		HistoricalFloat hist = accuracies.get(name);
		if (hist==null) {
			hist = new HistoricalFloat();
			hist.capacity = accuracyCapacity;
			accuracies.put(name, hist);
		}
		hist.push(accuracy);
	}
	
	protected void addStats(NetworkIOStats base, NetworkIOStats add) {
		base.totalNs += add.totalNs;
		List<Integer> keySet = new ArrayList<Integer>(add.nsPerLayer.keySet());
		for (Integer layer: keySet) {
			Long ns = new Long(0);
			if (base.nsPerLayer.containsKey(layer)) {
				ns = base.nsPerLayer.get(layer);
			}
			ns += add.nsPerLayer.get(layer);
			base.nsPerLayer.put(layer, ns);
		}
	}
	
	protected void divideStats(NetworkIOStats stats, int total) {
		if (total>0) {
			stats.totalNs = stats.totalNs / total;
			for (Integer layer: stats.nsPerLayer.keySet()) {
				Long ns = stats.nsPerLayer.get(layer);
				stats.nsPerLayer.put(layer, (ns / total));
			}
		}
	}
}
