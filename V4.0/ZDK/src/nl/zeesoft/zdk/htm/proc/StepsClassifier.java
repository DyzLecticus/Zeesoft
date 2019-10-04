package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import nl.zeesoft.zdk.htm.sdr.DateTimeSDR;
import nl.zeesoft.zdk.htm.sdr.SDR;

public class StepsClassifier {
	protected ClassifierConfig						config					= null;
	protected int									steps					= 0;
	
	protected Queue<SDR>							activationHistory		= new LinkedList<SDR>();
	protected HashMap<Integer,StepsClassifierBit>	bits					= new HashMap<Integer,StepsClassifierBit>();
	
	public StepsClassifier(ClassifierConfig config,int steps) {
		this.config = config;
		this.steps = steps;
	}

	protected DateTimeSDR getClassificationSDRForActivationSDR(SDR activationSDR,DateTimeSDR inputSDR) {
		DateTimeSDR r = null;
		if (inputSDR!=null) {
			r = new DateTimeSDR(inputSDR.length());
			associateBits(inputSDR);
			generatePrediction(activationSDR,r);
			activationHistory.add(activationSDR);
			while (activationHistory.size()>steps) {
				activationHistory.remove();
			}
		} else {
			r = new DateTimeSDR(activationSDR.length());
		}
		return r;
	}

	protected void associateBits(DateTimeSDR inputSDR) {
		if (activationHistory.size()==steps) {
			Object value = inputSDR.keyValues.get(config.valueKey);
			String label = (String) inputSDR.keyValues.get(config.labelKey);
			if (value!=null || label!=null) {
				SDR histActivationSDR = activationHistory.remove();
				for (Integer onBit: histActivationSDR.getOnBits()) {
					StepsClassifierBit bit = bits.get(onBit);
					if (bit==null) {
						bit = new StepsClassifierBit(config,onBit);
						bits.put(onBit,bit);
					}
					bit.associate(histActivationSDR,inputSDR);
				}
			}
		}
	}

	protected void generatePrediction(SDR activationSDR,DateTimeSDR outputSDR) {
		HashMap<Float,Integer> valueCounts = new HashMap<Float,Integer>();
		HashMap<String,Integer> labelCounts = new HashMap<String,Integer>();
		List<Float> maxCountedValues = new ArrayList<Float>();
		List<String> maxCountedLabels = new ArrayList<String>();
		if (bits.size()>0) {
			int maxValueCounts = 0;
			int maxLabelCounts = 0;
			for (Integer onBit: activationSDR.getOnBits()) {
				StepsClassifierBit bit = bits.get(onBit);
				if (bit!=null) {
					for (Float value: bit.valueCounts.keySet()) {
						Integer count = valueCounts.get(value);
						if (count==null) {
							count = new Integer(0);
						}
						count += bit.valueCounts.get(value);
						valueCounts.put(value,count);
						if (count > maxValueCounts) {
							maxValueCounts = count;
							maxCountedValues.clear();
						}
						if (count == maxValueCounts) {
							maxCountedValues.add(value);
						}
					}
					for (String label: bit.labelCounts.keySet()) {
						Integer count = labelCounts.get(label);
						if (count==null) {
							count = new Integer(0);
						}
						count += bit.labelCounts.get(label);
						labelCounts.put(label,count);
						if (count > maxLabelCounts) {
							maxLabelCounts = count;
							maxCountedLabels.clear();
						}
						if (count == maxLabelCounts) {
							maxCountedLabels.add(label);
						}
					}
				}
			}
		}
		setPredictionSDR(outputSDR,valueCounts,labelCounts,maxCountedValues,maxCountedLabels);
	}
	
	protected void setPredictionSDR(DateTimeSDR outputSDR,HashMap<Float,Integer> valueCounts,HashMap<String,Integer> labelCounts,List<Float> maxCountedValues,List<String> maxCountedLabels) {
		int i = 0;
		for (Float value: maxCountedValues) {
			i++;
			outputSDR.keyValues.put(config.valueKey + i,value);
		}
		i = 0;
		for (String label: maxCountedLabels) {
			i++;
			outputSDR.keyValues.put(config.labelKey + i,label);
		}
	}
}
