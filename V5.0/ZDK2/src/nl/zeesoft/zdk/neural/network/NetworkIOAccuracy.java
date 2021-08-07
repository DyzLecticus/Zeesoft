package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.HistoricalFloat;
import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;
import nl.zeesoft.zdk.neural.processor.cl.Classification;
import nl.zeesoft.zdk.str.StrUtil;

public class NetworkIOAccuracy {
	public SortedMap<String, Float>		classifierAverages		= new TreeMap<String, Float>();
	public float						average					= 0.0F;

	public SortedMap<String, Float>		classifierAverageRmse	= new TreeMap<String, Float>();
	public float						averageRmse				= 0.0F;

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
		calculateAverageAccuracies(analyzer, start, end, capacity);
		calculateAverageRmses(analyzer, start, end);
	}
	
	@Override
	public String toString() {
		StringBuilder r = new StringBuilder();
		StrUtil.appendLine(r, "Average: ");
		r.append(average);
		r.append(" (");
		r.append(averageRmse);
		r.append(")");
		for (Entry<String,Float> entry: classifierAverages.entrySet()) {
			StrUtil.appendLine(r, entry.getKey());
			r.append(": ");
			r.append(entry.getValue());
			r.append(" (");
			r.append(classifierAverageRmse.get(entry.getKey()));
			r.append(")");
		}
		return r.toString();
	}
	
	protected void calculateAverageAccuracies(NetworkIOAnalyzer analyzer, int start, int end, int capacity) {
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
	
	protected void calculateAverageRmses(NetworkIOAnalyzer analyzer, int start, int end) {
		SortedMap<String,List<Float>> absoluteErrors = getAbsoluteErrors(analyzer, start, end);
		averageRmse = 0.0F;
		for (Entry<String,List<Float>> entry: absoluteErrors.entrySet()) {
			float err = Util.getRootMeanSquaredError(entry.getValue());
			averageRmse += err;
			classifierAverageRmse.put(entry.getKey(), err);
		}
		if (absoluteErrors.size()>0) {
			averageRmse = averageRmse / absoluteErrors.size();
		}
	}

	protected SortedMap<String,List<Float>> getAbsoluteErrors(NetworkIOAnalyzer analyzer, int start, int end) {
		SortedMap<String,List<Float>> r = new TreeMap<String,List<Float>>();
		for (int i = start; i < end; i++) {
			NetworkIO io = analyzer.networkIO.get(i);
			for (String name: io.getProcessorNames()) {
				ProcessorIO pio = io.getProcessorIO(name);
				if (pio.outputValue instanceof Classification) {
					Classification c = (Classification)pio.outputValue;
					Classification p = analyzer.getPrediction(i - c.step, name);
					if (p!=null && p.getMostCountedValues().size()==1) {
						float error = determineError(c.value, p.getMostCountedValues().get(0));
						logError(r, name, error);
					}
				}
			}
		}
		return r;
	}
	
	protected float determineError(Object inputValue, Object predictedValue) {
		float input = Util.getFloatValue(inputValue);
		float predicted = Util.getFloatValue(predictedValue);
		float error = input - predicted;
		if (error < 0) {
			error = error * -1.0F;
		}
		return error;
	}
	
	protected void logError(SortedMap<String,List<Float>> absoluteErrors, String name, float error) {
		List<Float> errors = absoluteErrors.get(name);
		if (errors==null) {
			errors = new ArrayList<Float>();
			absoluteErrors.put(name, errors);
		}
		errors.add(error);
	}
}
