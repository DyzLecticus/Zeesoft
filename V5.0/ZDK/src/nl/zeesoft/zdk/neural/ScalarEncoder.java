package nl.zeesoft.zdk.neural;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.grid.SDR;

public class ScalarEncoder extends Encoder {
	public int buckets	= 20;
	public int onBits	= 16;
	
	@Override
	public SDR getEncodedValue() {
		return getSDRForBucket(value);
	}
	
	@Override
	public Str test() {
		Str r = super.test();
		SDR curr = getSDRForBucket(0);
		for (int b = 1; b < buckets; b++) {
			SDR next = getSDRForBucket(b);
			int overlap = curr.getOverlap(next);
			if (overlap==onBits || overlap==0) {
				r.sb().append("Invalid bucket value overlap for value: ");
				r.sb().append(b);
				r.sb().append(", overlap: ");
				r.sb().append(overlap);
			}
			curr = next;
		}
		return r;
	}

	protected SDR getSDRForBucket(Object value) {
		SDR r = super.getEncodedValue();
		if (value!=null) {
			float stepsPerBucket = (float)encodeLength / (float)buckets;
			float bucket = getBucketForValue(value); 
			int s = (int)(bucket * stepsPerBucket);
			int e = s + onBits;
			if (e > encodeLength) {
				e = encodeLength;
			}
			for (int i = s; i < e; i++) {
				r.setValue(i, 0, true);
			}
		}
		return r;
	}
	
	protected float getBucketForValue(Object value) {
		float val = 0;
		if (value instanceof Float) {
			val = (float) value;
		} else if (value instanceof Integer) {
			val = (int) value;
		}
		return (val % (float)buckets);
	}
}
