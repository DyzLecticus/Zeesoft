package nl.zeesoft.zdk.neural.network;

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.HistoricalFloat;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;
import nl.zeesoft.zdk.neural.processor.cl.Classification;
import nl.zeesoft.zdk.str.StrUtil;

public class NetworkIOAccuracy {
	public SortedMap<String, Float>		classifierAverages	= new TreeMap<String, Float>();
	public float						average				= 0.0F;
	
	public NetworkIOAccuracy() {
		
	}
	
	public NetworkIOAccuracy(NetworkIOAnalyzer analyzer, int max) {
		int start = 0;
		int end = analyzer.networkIO.size();
		int capacity = end;
		if (max>0) {
			capacity = max;
			start = end - max;
			if (start<0) {
				start = 0;
			}
		}
		SortedMap<String,HistoricalFloat> acccuracies = getClassifierAccuracies(analyzer, start, end, capacity);
		average = 0.0F;
		for (Entry<String,HistoricalFloat> entry: acccuracies.entrySet()) {
			float avg = entry.getValue().getAverage();
			average += avg;
			classifierAverages.put(entry.getKey(), avg);
		}
		if (acccuracies.size()>0) {
			average = average / acccuracies.size();
		}
	}
	
	@Override
	public String toString() {
		StringBuilder r = new StringBuilder();
		StrUtil.appendLine(r, "Average: ");
		r.append(average);
		for (Entry<String,Float> entry: classifierAverages.entrySet()) {
			StrUtil.appendLine(r, entry.getKey());
			r.append(": ");
			r.append(entry.getValue());
		}
		return r.toString();
	}
	
	protected SortedMap<String,HistoricalFloat> getClassifierAccuracies(NetworkIOAnalyzer analyzer, int start, int end, int capacity) {
		SortedMap<String,HistoricalFloat> r = new TreeMap<String,HistoricalFloat>();
		for (int i = start; i < end; i++) {
			NetworkIO io = analyzer.networkIO.get(i);
			for (String name: io.getProcessorNames()) {
				ProcessorIO pio = io.getProcessorIO(name);
				if (pio.outputValue instanceof Classification) {
					Classification c = (Classification)pio.outputValue;
					Classification p = analyzer.getPrediction(i - c.step, name);
					float accuracy = determineAccuracy(c.value, p);
					logAccuracy(r, name, capacity, accuracy);
				}
			}
		}
		return r;
	}
	
	protected float determineAccuracy(Object inputValue, Classification prediction) {
		float accuracy = 0.0F;
		if (prediction!=null && prediction.getMostCountedValues().size()==1 && prediction.getMostCountedValues().get(0).equals(inputValue)) {
			accuracy = 1.0F;
		}
		return accuracy;
	}
	
	protected void logAccuracy(SortedMap<String,HistoricalFloat> inputAccuracies, String name, int capacity, float accuracy) {
		HistoricalFloat inputAccuracy = inputAccuracies.get(name);
		if (inputAccuracy==null) {
			inputAccuracy = new HistoricalFloat();
			inputAccuracy.capacity = capacity;
			inputAccuracies.put(name, inputAccuracy);
		}
		inputAccuracy.push(accuracy);
	}
}
