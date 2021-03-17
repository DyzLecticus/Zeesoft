package nl.zeesoft.zdk.neural.processor.se;

import nl.zeesoft.zdk.neural.Sdr;

public class ScalarSdrEncoder {
	public int			encodeLength	= 256;
	public int			onBits			= 16;
	public float		minValue		= 0;
	public float		maxValue		= 200;
	public float		resolution		= 1;
	public boolean		periodic		= false;

	public Sdr getEncodedValue(Object value) {
		Sdr r = new Sdr(encodeLength);
		float percentage = getCorrectedInputValue(value) / getCorrectedMaxValue();
		int sBit = (int) (percentage * getBuckets());
		int eBit = sBit + onBits;
		for (int bit = sBit; bit < eBit; bit++) {
			int b = bit;
			if (bit >= encodeLength) {
				b = b % encodeLength;
			}
			r.setBit(b, true);
		}
		return r;
	}
	
	public float getBuckets() {
		float r = 0;
		if (periodic) {
			r = encodeLength;
		} else {
			r = encodeLength - onBits;
		}
		return r;
	}

	public StringBuilder testOnBits() {
		StringBuilder r = new StringBuilder();
		for (float val = minValue; val <= maxValue; val+=resolution) {
			Sdr sdr = getEncodedValue(val);
			r = checkOnBits(sdr, onBits, val);
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
	
	public static StringBuilder checkOnBits(Sdr sdr, int onBits, Object value) {
		StringBuilder r = new StringBuilder();
		if (sdr.onBits.size()!=onBits) {
			r.append("Invalid on bits for value: ");
			r.append(value);
			r.append(", on bits: ");
			r.append(sdr.onBits.size());
			r.append(", required: ");
			r.append(onBits);
		}
		return r;
	}
	
	protected float getCorrectedInputValue(Object value) {
		float r = 0;
		if (value instanceof Float) {
			r = (float) value;
		} else if (value instanceof Integer) {
			r = (int) value;
		}
		
		if (periodic) {
			r = getCorrectedValue(r) % (getCorrectedMaxValue() + resolution);
		} else {
			if (r < minValue) {
				r = minValue;
			}
			if (r > maxValue) {
				r = maxValue;
			}
			r = getCorrectedValue(r);
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
