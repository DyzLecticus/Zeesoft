package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.sdr.DateTimeSDR;
import nl.zeesoft.zdk.htm.sdr.SDR;

public class Classifier extends ProcessorObject {
	protected ClassifierConfig					config					= null;
	
	protected DateTimeSDR						inputSDR				= null;
	protected Queue<SDR>						activationHistory		= new LinkedList<SDR>();
	protected HashMap<Integer,ClassifierBit>	bits					= new HashMap<Integer,ClassifierBit>();
	
	public Classifier(ClassifierConfig config) {
		this.config = config;
		config.initialized = true;
	}
	
	@Override
	public ZStringBuilder toStringBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fromStringBuilder(ZStringBuilder str) {
		// TODO Auto-generated method stub
		
	}
	
	public List<SDR> getSDRsForInput(SDR input,List<SDR> context,boolean learn) {
		inputSDR = null;
		if (context.get(0) instanceof DateTimeSDR) {
			inputSDR = (DateTimeSDR) context.get(0);
		}
		List<SDR> r = super.getSDRsForInput(input, context, learn);
		if (inputSDR!=null) {
			activationHistory.add(input);
			while (activationHistory.size()>config.steps) {
				activationHistory.remove();
			}
		}
		return r;
	}

	@Override
	protected SDR getSDRForInputSDR(SDR input, boolean learn) {
		DateTimeSDR r = null;
		long start = 0;
		
		if (inputSDR!=null) {
			start = System.nanoTime();
			associateBits(input);
			logStatsValue("associateBits",System.nanoTime() - start);
			
			start = System.nanoTime();
			r = generatePrediction(input);
			logStatsValue("generatePrediction",System.nanoTime() - start);
		} else {
			r = new DateTimeSDR(input.length());
		}
		
		return r;
	}

	protected void associateBits(SDR input) {
		if (activationHistory.size()==config.steps) {
			Object value = inputSDR.keyValues.get(config.valueKey);
			String label = (String) inputSDR.keyValues.get(config.labelKey);
			if (value!=null || label!=null) {
				SDR activationSDR = activationHistory.remove();
				for (Integer onBit: input.getOnBits()) {
					ClassifierBit bit = bits.get(onBit);
					if (bit==null) {
						bit = new ClassifierBit(config,onBit);
						bits.put(onBit,bit);
					}
					bit.associate(activationSDR,inputSDR);
				}
			}
		}
	}

	protected DateTimeSDR generatePrediction(SDR input) {
		HashMap<Float,Integer> valueCounts = new HashMap<Float,Integer>();
		HashMap<String,Integer> labelCounts = new HashMap<String,Integer>();
		int maxValueCounts = 0;
		int maxLabelCounts = 0;
		List<Float> maxCountedValues = new ArrayList<Float>();
		List<String> maxCountedLabels = new ArrayList<String>();
		for (Integer onBit: input.getOnBits()) {
			ClassifierBit bit = bits.get(onBit);
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
		return getPredictionSDR(valueCounts,labelCounts,maxCountedValues,maxCountedLabels);
	}
	
	protected DateTimeSDR getPredictionSDR(HashMap<Float,Integer> valueCounts,HashMap<String,Integer> labelCounts,List<Float> maxCountedValues,List<String> maxCountedLabels) {
		DateTimeSDR r = new DateTimeSDR(inputSDR.length());
		int i = 0;
		for (Float value: maxCountedValues) {
			i++;
			r.keyValues.put(config.valueKey + i,value);
		}
		i = 0;
		for (String label: maxCountedLabels) {
			i++;
			r.keyValues.put(config.labelKey + i,label);
		}
		return r;
	}
}
