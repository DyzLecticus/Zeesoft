package nl.zeesoft.zdk.neural.network.analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.MathUtil;
import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.json.JsonTransient;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;
import nl.zeesoft.zdk.neural.processor.cl.Classification;
import nl.zeesoft.zdk.neural.processor.cl.ValueLikelyhood;

@JsonTransient
public class IOAccuracyRmseCalculator {
	protected void calculateAverageRmses(IOAccuracyCalc calc) {
		SortedMap<String,List<Float>> absoluteErrors = getAbsoluteErrors(calc);
		float averageRmse = 0.0F;
		for (Entry<String,List<Float>> entry: absoluteErrors.entrySet()) {
			float rmse = MathUtil.getRootMeanSquaredError(entry.getValue());
			averageRmse += rmse;
			IOAccuracy acc = calc.accuracy.getOrCreateIOAccuracy(entry.getKey());
			acc.rootMeanSquaredError = rmse;
		}
		if (absoluteErrors.size()>0) {
			averageRmse = averageRmse / absoluteErrors.size();
			calc.accuracy.getAverage().rootMeanSquaredError = averageRmse;
		}
	}

	protected static SortedMap<String,List<Float>> getAbsoluteErrors(IOAccuracyCalc calc) {
		SortedMap<String,List<Float>> r = new TreeMap<String,List<Float>>();
		for (int i = calc.start; i < calc.end; i++) {
			NetworkIO io = calc.analyzer.networkIO.get(i);
			for (String name: io.getProcessorNames()) {
				ProcessorIO pio = io.getProcessorIO(name);
				if (pio.outputValue instanceof Classification) {
					Classification c = (Classification)pio.outputValue;
					Classification p = calc.analyzer.getPrediction(i - c.step, name);
					if (p!=null && (p.prediction!=null || (calc.useAvgPrediction && p.averagePrediction!=null))) {
						float error = determineError(c.value, p, calc.useAvgPrediction);
						logError(r, name, error);
					}
				}
			}
		}
		return r;
	}
	
	protected static float determineError(Object inputValue, Classification prediction, boolean useAvgPrediction) {
		float input = Util.getFloatValue(inputValue);
		float error = input;
		ValueLikelyhood vl = prediction.prediction;
		if (useAvgPrediction && prediction.averagePrediction!=null) {
			vl = prediction.averagePrediction;
		}
		if (vl!=null) {
			float predicted = Util.getFloatValue(vl.value);
			error = input - predicted;
			if (error < 0F) {
				error = error * -1.0F;
			}
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
