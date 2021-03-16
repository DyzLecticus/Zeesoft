package nl.zeesoft.zdk.neural.processor.cl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.SdrHistory;

public class ClBits {
	public ClConfig					config				= null;
	public SdrHistory				activationHistory	= null;
	public HashMap<Integer,ClBit>	bits				= new HashMap<Integer,ClBit>();
	
	public ClBits(Object caller, ClConfig config, SdrHistory activationHistory) {
		this.config = config;
		this.activationHistory = activationHistory;
	}
	
	public void reset() {
		bits.clear();
	}
	
	public boolean associateBits(Object value) {
		boolean divide = false;
		if (value!=null && activationHistory.sdrs.size() > config.predictStep) {
			Sdr associateSDR = activationHistory.sdrs.get(config.predictStep);
			for (Integer onBit: associateSDR.onBits) {
				ClBit bit = bits.get(onBit);
				if (bit==null) {
					bit = new ClBit();
					bit.config = config;
					bit.index = onBit;
					bits.put(onBit,bit);
				}
				if (bit.associate(value)) {
					divide = true;
				}
			}
			if (divide) {
				List<Integer> remove = new ArrayList<Integer>();
				for (ClBit bit: bits.values()) {
					bit.divideValueCountsBy(2);
					if (bit.valueCounts.size()==0) {
						remove.add(bit.index);
					}
				}
				for (Integer index: remove) {
					bits.remove(index);
				}
			}
		}
		return divide;
	}

	public Classification generatePrediction(Sdr input) {
		Classification r = new Classification();
		HashMap<Object,Integer> valueCounts = new HashMap<Object,Integer>();
		if (bits.size()>0) {
			for (Integer onBit: input.onBits) {
				ClBit bit = bits.get(onBit);
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
		r.name = config.valueName;
		r.step = config.predictStep;
		r.valueCounts = valueCounts;
		return r;
	}
}
