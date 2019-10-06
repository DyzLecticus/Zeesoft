package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.htm.util.SDR;

public class StepsClassifier {
	protected ClassifierConfig						config					= null;
	protected int									steps					= 0;
	
	protected List<SDR>								activationHistory		= null;
	protected HashMap<Integer,StepsClassifierBit>	bits					= new HashMap<Integer,StepsClassifierBit>();
	
	protected StepsClassifier(ClassifierConfig config,List<SDR> activationHistory,int steps) {
		this.config = config;
		this.activationHistory = activationHistory;
		this.steps = steps;
	}

	protected ZStringBuilder toStringBuilder() {
		ZStringBuilder r = new ZStringBuilder("" + steps);
		for (StepsClassifierBit bit: bits.values()) {
			r.append("#");
			r.append(bit.toStringBuilder());
		}
		return r;
	}
	
	protected void fromStringBuilder(ZStringBuilder str) {
		List<ZStringBuilder> elems = str.split("#");
		if (elems.size()>1) {
			steps = Integer.parseInt(elems.get(0).toString());
			bits.clear();
			for (ZStringBuilder elem: elems) {
				if (elem.contains(";")) {
					StepsClassifierBit bit = new StepsClassifierBit(config,0);
					bit.fromStringBuilder(elem);
					bits.put(bit.index,bit);
				}
			}
		}
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
		List<Object> mostCountedValues = new ArrayList<Object>();
		List<String> mostCountedLabels = new ArrayList<String>();
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
							mostCountedValues.clear();
						}
						if (count == maxValueCounts) {
							mostCountedValues.add(value);
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
							mostCountedLabels.clear();
						}
						if (count == maxLabelCounts) {
							mostCountedLabels.add(label);
						}
					}
				}
			}
		}
		Classification classification = new Classification();
		classification.steps = steps;
		classification.valueCounts = valueCounts;
		classification.labelCounts = labelCounts;
		classification.mostCountedValues = mostCountedValues;
		classification.mostCountedLabels = mostCountedLabels;
		outputSDR.keyValues.put(Classifier.CLASSIFICATION_KEY,classification);
	}
}
