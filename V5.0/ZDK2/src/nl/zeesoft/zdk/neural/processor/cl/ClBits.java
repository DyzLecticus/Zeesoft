package nl.zeesoft.zdk.neural.processor.cl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

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
	
	public boolean associateBits(Object value, int processed) {
		boolean divide = false;
		if (value!=null && activationHistory.sdrs.size() > config.predictStep) {
			Sdr associateSDR = activationHistory.sdrs.get(config.predictStep);
			for (Integer onBit: associateSDR.onBits) {
				if (associateOnBit(onBit, value, processed)) {
					divide = true;
				}
			}
			if (divide) {
				divideBits();
			}
		}
		return divide;
	}

	public Classification generatePrediction(Sdr input, Object value, int processed) {
		Classification r = new Classification();
		r.step = config.predictStep;
		r.valueCounts = getValueCounts(input, processed);
		r.value = value;
		return r;
	}
	
	protected boolean associateOnBit(Integer onBit, Object value, int processed) {
		boolean r = false;
		ClBit bit = bits.get(onBit);
		if (bit==null) {
			bit = new ClBit();
			bit.config = config;
			bit.index = onBit;
			bits.put(onBit,bit);
		}
		if (bit.associate(value, processed)) {
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
	
	protected HashMap<Object,Float> getValueCounts(Sdr input, int processed) {
		HashMap<Object,Float> r = new HashMap<Object,Float>();
		if (bits.size()>0) {
			for (Integer onBit: input.onBits) {
				ClBit bit = bits.get(onBit);
				if (bit!=null) {
					for (Entry<Object,ValueCount> entry: bit.valueCounts.entrySet()) {
						countBitValue(r, entry.getKey(), entry.getValue(), processed);
					}
				}
			}
		}
		return r;
	}
	
	protected void countBitValue(HashMap<Object,Float> results, Object value, ValueCount vc, int processed) {
		float factor = 1.0F - (((float)(processed - vc.lastProcessed)) * config.alpha);
		if (factor > 0.0F) {
			Float count = results.get(value);
			if (count==null) {
				count = new Float(0);
			}
			count += ((float)vc.count) * factor;
			results.put(value,count);
		}
	}
}
