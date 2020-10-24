package nl.zeesoft.zdk.neural.processors;

import java.util.HashMap;

import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.SDRHistory;

public class ClassifierStep {
	// Configuration
	protected int								step				= 0;
	protected String							valueKey			= "";
	protected int								maxCount			= 0;
	
	// State
	protected SDRHistory						activationHistory	= null;
	protected HashMap<Integer,ClassifierBit>	bits				= new HashMap<Integer,ClassifierBit>();

	public ClassifierStep(int step, String valueKey, int maxCount, SDRHistory activationHistory) {
		this.step = step;
		this.valueKey = valueKey;
		this.maxCount = maxCount;
		this.activationHistory = activationHistory;
	}
	
	protected void associateBits(Object value) {
		if (value!=null && activationHistory.size()>step) {
			SDR histActivationSDR = activationHistory.get(step);
			if (histActivationSDR!=null) {
				boolean divide = false;;
				for (Integer onBit: histActivationSDR.getOnBits()) {
					ClassifierBit bit = bits.get(onBit);
					if (bit==null) {
						bit = new ClassifierBit(onBit,valueKey,maxCount);
						bits.put(onBit,bit);
					}
					if (bit.associate(value)) {
						divide = true;
					}
				}
				if (divide) {
					for (ClassifierBit bit: bits.values()) {
						bit.divideValueCountsBy(2);
					}
				}
			}
		}
	}

	protected Classification generatePrediction(SDR input) {
		Classification r = new Classification();
		HashMap<Object,Integer> valueCounts = new HashMap<Object,Integer>();
		if (bits.size()>0) {
			for (Integer onBit: input.getOnBits()) {
				ClassifierBit bit = bits.get(onBit);
				if (bit!=null) {
					for (Object value: bit.valueCounts.keySet()) {
						Integer count = valueCounts.get(value);
						if (count==null) {
							count = new Integer(0);
						}
						count += bit.valueCounts.get(value);
						valueCounts.put(value,count);
					}
				}
			}
		}
		r.valueKey = valueKey;
		r.step = step;
		r.valueCounts = valueCounts;
		return r;
	}
}
