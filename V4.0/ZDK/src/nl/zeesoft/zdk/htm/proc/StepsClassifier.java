package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.zeesoft.zdk.htm.sdr.DateTimeSDR;
import nl.zeesoft.zdk.htm.sdr.SDR;

public class StepsClassifier {
	protected ClassifierConfig						config					= null;
	protected int									steps					= 0;
	
	protected List<SDR>								activationHistory		= null;
	protected HashMap<Integer,StepsClassifierBit>	bits					= new HashMap<Integer,StepsClassifierBit>();
	
	public StepsClassifier(ClassifierConfig config,List<SDR> activationHistory,int steps) {
		this.config = config;
		this.activationHistory = activationHistory;
		this.steps = steps;
	}

	protected DateTimeSDR getClassificationSDRForActivationSDR(SDR activationSDR,DateTimeSDR inputSDR,boolean learn) {
		DateTimeSDR r = null;
		if (inputSDR!=null) {
			r = new DateTimeSDR(inputSDR.length());
			if (learn) {
				associateBits(inputSDR);
			}
			generatePrediction(activationSDR,r);
		} else {
			r = new DateTimeSDR(activationSDR.length());
		}
		return r;
	}

	protected void associateBits(DateTimeSDR inputSDR) {
		if (activationHistory.size()>steps) {
			int index = activationHistory.size() - (steps + 1);
			Object value = inputSDR.keyValues.get(config.valueKey);
			String label = (String) inputSDR.keyValues.get(config.labelKey);
			if (value!=null || label!=null) {
				SDR histActivationSDR = activationHistory.get(index);
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
		HashMap<Object,Integer> valueCounts = new HashMap<Object,Integer>();
		HashMap<String,Integer> labelCounts = new HashMap<String,Integer>();
		List<Object> maxCountedValues = new ArrayList<Object>();
		List<String> maxCountedLabels = new ArrayList<String>();
		if (bits.size()>0) {
			int maxValueCounts = 0;
			int maxLabelCounts = 0;
			for (Integer onBit: activationSDR.getOnBits()) {
				StepsClassifierBit bit = bits.get(onBit);
				if (bit!=null) {
					for (Object value: bit.valueCounts.keySet()) {
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
		Classification classification = new Classification();
		classification.steps = steps;
		classification.valueCounts = valueCounts;
		classification.labelCounts = labelCounts;
		classification.maxCountedValues = maxCountedValues;
		classification.maxCountedLabels = maxCountedLabels;
		outputSDR.keyValues.put(Classifier.CLASSIFICATION_KEY,classification);
	}
}
