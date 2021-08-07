package nl.zeesoft.zdk.neural.network.analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.MathUtil;
import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;
import nl.zeesoft.zdk.neural.processor.cl.Classification;

public class IOAccuracyRmseCalculator {
	protected void calculateAverageRmses(NetworkIOAnalyzer analyzer, int start, int end, NetworkIOAccuracy accuracy) {
		SortedMap<String,List<Float>> absoluteErrors = getAbsoluteErrors(analyzer, start, end);
		float averageRmse = 0.0F;
		for (Entry<String,List<Float>> entry: absoluteErrors.entrySet()) {
			float rmse = MathUtil.getRootMeanSquaredError(entry.getValue());
			averageRmse += rmse;
			IOAccuracy acc = accuracy.getOrCreateIOAccuracy(entry.getKey());
			acc.rootMeanSquaredError = rmse;
		}
		if (absoluteErrors.size()>0) {
			averageRmse = averageRmse / absoluteErrors.size();
			accuracy.getAverage().rootMeanSquaredError = averageRmse;
		}
	}

	protected static SortedMap<String,List<Float>> getAbsoluteErrors(NetworkIOAnalyzer analyzer, int start, int end) {
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
	
	protected static float determineError(Object inputValue, Object predictedValue) {
		float input = Util.getFloatValue(inputValue);
		float predicted = Util.getFloatValue(predictedValue);
		float error = input - predicted;
		if (error < 0) {
			error = error * -1.0F;
		}
		return error;
	}
	
	protected static void logError(SortedMap<String,List<Float>> absoluteErrors, String name, float error) {
		List<Float> errors = absoluteErrors.get(name);
		if (errors==null) {
			errors = new ArrayList<Float>();
			absoluteErrors.put(name, errors);
		}
		errors.add(error);
	}
}
