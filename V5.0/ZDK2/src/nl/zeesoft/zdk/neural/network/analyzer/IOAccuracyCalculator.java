package nl.zeesoft.zdk.neural.network.analyzer;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import nl.zeesoft.zdk.HistoricalFloat;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;
import nl.zeesoft.zdk.neural.processor.cl.Classification;

public class IOAccuracyCalculator {
	protected void calculateAverageAccuracies(NetworkIOAnalyzer analyzer, int start, int end, int capacity, NetworkIOAccuracy accuracy) {
		SortedMap<String,HistoricalFloat> acccuracies = getClassifierAccuracies(analyzer, start, end, capacity);
		float average = 0.0F;
		for (Entry<String,HistoricalFloat> entry: acccuracies.entrySet()) {
			float avg = entry.getValue().getAverage();
			average += avg;
			IOAccuracy acc = accuracy.getOrCreateIOAccuracy(entry.getKey());
			acc.accuracy = avg;
		}
		if (acccuracies.size()>0) {
			average = average / acccuracies.size();
			accuracy.getAverage().accuracy = average;
		}
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
	
	protected void logAccuracy(SortedMap<String,HistoricalFloat> classifierAccuracies, String name, int capacity, float accuracy) {
		HistoricalFloat hist = classifierAccuracies.get(name);
		if (hist==null) {
			hist = new HistoricalFloat();
			hist.capacity = capacity;
			classifierAccuracies.put(name, hist);
		}
		hist.push(accuracy);
	}

}
