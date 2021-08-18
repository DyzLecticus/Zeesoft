package nl.zeesoft.zdk.neural.network.analyzer;

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.HistoricalFloat;
import nl.zeesoft.zdk.json.JsonTransient;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;
import nl.zeesoft.zdk.neural.processor.cl.Classification;

@JsonTransient
public class IOAccuracyCalculator {
	protected void calculateAverageAccuracies(IOAccuracyCalc calc, int capacity) {
		SortedMap<String,HistoricalFloat> acccuracies = getClassifierAccuracies(calc, capacity);
		float average = 0.0F;
		for (Entry<String,HistoricalFloat> entry: acccuracies.entrySet()) {
			float avg = entry.getValue().getAverage();
			average += avg;
			IOAccuracy acc = calc.accuracy.getOrCreateIOAccuracy(entry.getKey());
			acc.accuracy = avg;
		}
		if (acccuracies.size()>0) {
			average = average / acccuracies.size();
			calc.accuracy.getAverage().accuracy = average;
		}
	}
	
	protected SortedMap<String,HistoricalFloat> getClassifierAccuracies(IOAccuracyCalc calc, int capacity) {
		SortedMap<String,HistoricalFloat> r = new TreeMap<String,HistoricalFloat>();
		for (int i = calc.start; i < calc.end; i++) {
			NetworkIO io = calc.analyzer.networkIO.get(i);
			for (String name: io.getProcessorNames()) {
				ProcessorIO pio = io.getProcessorIO(name);
				if (pio.outputValue instanceof Classification) {
					Classification c = (Classification)pio.outputValue;
					Classification p = calc.analyzer.getPrediction(i - c.step, name);
					float accuracy = determineAccuracy(c.value, p, calc.useAvgPrediction);
					logAccuracy(r, name, capacity, accuracy);
				}
			}
		}
		return r;
	}
	
	protected float determineAccuracy(Object inputValue, Classification prediction, boolean useAvgPrediction) {
		float accuracy = 0.0F;
		if (prediction!=null && 
			(
				(prediction.prediction!=null && prediction.prediction.value.equals(inputValue)) ||
				(useAvgPrediction && prediction.averagePrediction != null && prediction.averagePrediction.value.equals(inputValue))
			)
			) {
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
