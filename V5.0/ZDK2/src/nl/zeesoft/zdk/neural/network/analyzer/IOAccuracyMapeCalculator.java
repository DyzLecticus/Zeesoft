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

@JsonTransient
public class IOAccuracyMapeCalculator {
	protected void calculateAverageMapes(IOAccuracyCalc calc) {
		SortedMap<String,List<Float>> inputValues = getInputValues(calc);
		SortedMap<String,List<Float>> absoluteErrors = IOAccuracyRmseCalculator.getAbsoluteErrors(calc);
		float averageMape = 0.0F;
		for (Entry<String,List<Float>> entry: inputValues.entrySet()) {
			List<Float> errors = absoluteErrors.get(entry.getKey());
			float mape = MathUtil.getMeanAveragePercentageError(entry.getValue(), errors);
			averageMape += mape;
			IOAccuracy acc = calc.accuracy.getOrCreateIOAccuracy(entry.getKey());
			acc.meanAveragePercentageError = mape;
		}
		if (absoluteErrors.size()>0) {
			averageMape = averageMape / absoluteErrors.size();
			calc.accuracy.getAverage().meanAveragePercentageError = averageMape;
		}
	}

	protected static SortedMap<String,List<Float>> getInputValues(IOAccuracyCalc calc) {
		SortedMap<String,List<Float>> r = new TreeMap<String,List<Float>>();
		for (int i = calc.start; i < calc.end; i++) {
			NetworkIO io = calc.analyzer.networkIO.get(i);
			for (String name: io.getProcessorNames()) {
				ProcessorIO pio = io.getProcessorIO(name);
				if (pio.outputValue instanceof Classification) {
					Classification c = (Classification)pio.outputValue;
					Classification p = calc.analyzer.getPrediction(i - c.step, name);
					if (p!=null && (p.prediction!=null || (calc.useAvgPrediction && p.averagePrediction!=null))) {
						logValue(r, name, c.value);
					}
				}
			}
		}
		return r;
	}
	
	protected static void logValue(SortedMap<String,List<Float>> inputValues, String name, Object inputValue) {
		List<Float> values = inputValues.get(name);
		if (values==null) {
			values = new ArrayList<Float>();
			inputValues.put(name, values);
		}
		values.add(Util.getFloatValue(inputValue));
	}
}
