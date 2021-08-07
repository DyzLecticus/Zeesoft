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
	
	public ClBits(ClConfig config, SdrHistory activationHistory) {
		setConfig(config, activationHistory);
	}
	
	public void setConfig(ClConfig config, SdrHistory activationHistory) {
		this.config = config;
		this.activationHistory = activationHistory;
		for (ClBit bit: bits.values()) {
			bit.config = config;
		}
	}
	
	public void reset() {
		bits.clear();
	}
	
	public boolean associateBits(Object value) {
		boolean divide = false;
		if (value!=null && activationHistory.sdrs.size() > config.predictStep) {
			Sdr associateSDR = activationHistory.sdrs.get(config.predictStep);
			for (Integer onBit: associateSDR.onBits) {
				if (associateOnBit(onBit, value)) {
					divide = true;
				}
			}
			if (divide) {
				divideBits();
			}
		}
		return divide;
	}

	public Classification generatePrediction(Sdr input, Object value) {
		Classification r = new Classification();
		HashMap<Object,Integer> valueCounts = getValueCounts(input);
		r.step = config.predictStep;
		r.valueCounts = valueCounts;
		r.value = value;
		return r;
	}
	
	protected boolean associateOnBit(Integer onBit, Object value) {
		boolean r = false;
		ClBit bit = bits.get(onBit);
		if (bit==null) {
			bit = new ClBit();
			bit.config = config;
			bit.index = onBit;
			bits.put(onBit,bit);
		}
		if (bit.associate(value)) {
			r = true;
		}
		return r;
	}
	
	protected void divideBits() {
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
	
	protected HashMap<Object,Integer> getValueCounts(Sdr input) {
		HashMap<Object,Integer> r = new HashMap<Object,Integer>();
		if (bits.size()>0) {
			for (Integer onBit: input.onBits) {
				ClBit bit = bits.get(onBit);
				if (bit!=null) {
					for (Object value: bit.valueCounts.keySet()) {
						Integer count = r.get(value);
						if (count==null) {
							count = new Integer(0);
						}
						count += bit.valueCounts.get(value);
						r.put(value,count);
					}
				}
			}
		}
		return r;
	}
}
