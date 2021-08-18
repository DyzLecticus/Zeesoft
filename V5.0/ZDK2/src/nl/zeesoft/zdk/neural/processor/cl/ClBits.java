package nl.zeesoft.zdk.neural.processor.cl;

import java.util.ArrayList;
import java.util.Collections;
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
	
	public void associateBits(Object value, int processed) {
		if (value!=null && activationHistory.sdrs.size() > config.predictStep) {
			Sdr associateSDR = activationHistory.sdrs.get(config.predictStep);
			for (Integer onBit: associateSDR.onBits) {
				associateOnBit(onBit, value, processed);
			}
		}
	}

	public Classification generatePrediction(Sdr input, Object value, int processed) {
		Classification r = new Classification();
		r.step = config.predictStep;
		r.valueLikelyhoods = getValueLikelyhoods(input, processed);
		r.value = value;
		r.determinePredictedValue();
		r.determineAveragePredictedValue(config.avgPredictionTop, config.avgPredictionStdDevFactor);
		return r;
	}
	
	protected void associateOnBit(Integer onBit, Object value, int processed) {
		ClBit bit = bits.get(onBit);
		if (bit==null) {
			bit = new ClBit();
			bit.config = config;
			bit.index = onBit;
			bits.put(onBit,bit);
		}
		bit.associate(value, processed);
	}
	
	protected List<ValueLikelyhood> getValueLikelyhoods(Sdr input, int processed) {
		List<ValueLikelyhood> r = new ArrayList<ValueLikelyhood>();
		if (bits.size()>0) {
			HashMap<Object,Float> valueCounts = getValueCounts(input, processed);
			float total = 0F;
			for (Float count: valueCounts.values()) {
				total += count;
			}
			for (Entry<Object,Float> entry: valueCounts.entrySet()) {
				r.add(new ValueLikelyhood(entry.getKey(), entry.getValue() / total));
			}
		}
		Collections.sort(r);
		return r;
	}
	
	protected HashMap<Object,Float> getValueCounts(Sdr input, int processed) {
		HashMap<Object,Float> r = new HashMap<Object,Float>();
		for (Integer onBit: input.onBits) {
			ClBit bit = bits.get(onBit);
			if (bit!=null) {
				for (Entry<Object,ValueCount> entry: bit.valueCounts.entrySet()) {
					countBitValue(r, entry.getKey(), entry.getValue(), processed);
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
