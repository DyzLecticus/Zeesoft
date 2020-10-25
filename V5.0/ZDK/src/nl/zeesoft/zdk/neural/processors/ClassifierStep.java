package nl.zeesoft.zdk.neural.processors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.StrAble;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.SDRHistory;

public class ClassifierStep implements StrAble {
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

	@Override
	public Str toStr() {
		Str r = new Str();
		r.sb().append(step);
		for (ClassifierBit bit: bits.values()) {
			r.sb().append("@");
			r.sb().append(bit.toStr().sb());
		}
		return r;
	}

	@Override
	public void fromStr(Str str) {
		List<Str> elems = str.split("@");
		int i = 0;
		bits.clear();
		for (Str elem: elems) {
			if (i==0) {
				step = Integer.parseInt(elem.toString());
			} else {
				ClassifierBit bit = new ClassifierBit(0,maxCount);
				bit.fromStr(elem);
				bits.put(bit.index, bit);
			}
			i++;
		}
	}
	
	protected void associateBits(Object value) {
		if (value!=null && activationHistory.size()>step) {
			SDR histActivationSDR = activationHistory.get(step);
			if (histActivationSDR!=null) {
				boolean divide = false;;
				for (Integer onBit: histActivationSDR.getOnBits()) {
					ClassifierBit bit = bits.get(onBit);
					if (bit==null) {
						bit = new ClassifierBit(onBit,maxCount);
						bits.put(onBit,bit);
					}
					if (bit.associate(value)) {
						divide = true;
					}
				}
				if (divide) {
					Str msg = new Str();
					msg.sb().append("Dividing step "); 
					msg.sb().append(step); 
					msg.sb().append(" classifier value counts by two ...");
					Logger.dbg(this, msg);
					List<Integer> remove = new ArrayList<Integer>();
					for (ClassifierBit bit: bits.values()) {
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
