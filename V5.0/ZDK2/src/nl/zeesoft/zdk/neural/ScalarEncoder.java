package nl.zeesoft.zdk.neural;

public class ScalarEncoder {
	public int			encodeLength	= 256;
	public int			onBits			= 16;
	public float		minValue		= 0;
	public float		maxValue		= 200;
	public float		resolution		= 1;
	public boolean		periodic		= false;

	public Sdr getEncodedValue(Object value) {
		Sdr r = new Sdr(encodeLength);
		float val = 0;
		if (value instanceof Float) {
			val = (float) value;
		} else if (value instanceof Integer) {
			val = (int) value;
		}
		
		if (periodic) {
			val = getCorrectedValue(val) % (getCorrectedMaxValue() + resolution);
		} else {
			if (val < minValue) {
				val = minValue;
			}
			if (val > maxValue) {
				val = maxValue;
			}
			val = getCorrectedValue(val);
		}
		
		float percentage = val / getCorrectedMaxValue();
		int sBit = (int) (percentage * getBuckets());
		int eBit = sBit + onBits;
		for (int bit = sBit; bit < eBit; bit++) {
			int b = bit;
			if (bit >= encodeLength) {
				if (periodic) {
					b = b % encodeLength;
				} else {
					break;
				}
			}
			r.setBit(b, true);
		}
		return r;
	}
	
	public float getBuckets() {
		return encodeLength - onBits;
	}

	public StringBuilder testOnBits() {
		StringBuilder r = new StringBuilder();
		for (float val = minValue; val <= maxValue; val+=resolution) {
			Sdr sdr = getEncodedValue(val);
			if (sdr.onBits.size()!=onBits) {
				r.append("Invalid on bits for value: ");
				r.append(val);
				r.append(", on bits: ");
				r.append(sdr.onBits.size());
				r.append(", required: ");
				r.append(onBits);
				break;
			}
		}
		return r;
	}
	
	public StringBuilder testMinimalOverlap() {
		return testOverlap(1, (onBits - 1));
	}
	
	public StringBuilder testNoOverlap() {
		return testOverlap(0, 0);
	}
	
	public StringBuilder testOverlap(int minOverlap, int maxOverlap) {
		StringBuilder r = new StringBuilder();
		Sdr curr = getEncodedValue(minValue);
		for (float val = (minValue + resolution); val <= maxValue; val+=resolution) {
			Sdr next = getEncodedValue(val);
			int overlap = curr.getOverlap(next);
			if (minOverlap > 0 && overlap < minOverlap) {
				r.append("Invalid bucket value overlap for value: ");
				r.append(val);
				r.append(", overlap: ");
				r.append(overlap);
				r.append(", minimum: ");
				r.append(minOverlap);
				break;
			} else if (maxOverlap >= 0 && overlap > maxOverlap) {
				r.append("Invalid bucket value overlap for value: ");
				r.append(val);
				r.append(", overlap: ");
				r.append(overlap);
				r.append(", maximum: ");
				r.append(maxOverlap);
				break;
			}
			curr = next;
		}
		return r;
	}
	
	protected float getCorrectedMaxValue() {
		return getCorrectedValue(maxValue);
	}
	
	protected float getCorrectedValue(float value) {
		float r = value;
		if (minValue!=0) {
			if (minValue > 0) {
				r = r - minValue;
			} else {
				r = r + (minValue * -1);
			}
		}
		return r;
	}
}
