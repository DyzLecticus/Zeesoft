package nl.zeesoft.zdk.neural;

import nl.zeesoft.zdk.grid.SDR;

public class ScalarEncoder extends Encoder {
	public int buckets	= 20;
	public int onBits	= 16;
	
	@Override
	public SDR getEncodedValue() {
		SDR r = super.getEncodedValue();
		if (value!=null) {
			float stepsPerBucket = (float)encodeLength / (float)buckets;
			float val = 0;
			if (value instanceof Float) {
				val = (float) value;
			} else if (value instanceof Integer) {
				val = (int) value;
			}
			int s = (int)((val % (float)buckets) * stepsPerBucket);
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
}
