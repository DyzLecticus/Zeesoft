package nl.zeesoft.zdk.neural;

import nl.zeesoft.zdk.Str;

public abstract class AbstractScalarEncoder extends SDREncoder {
	protected float		minValue	= 0;
	protected float		maxValue	= 200;
	protected float		resolution	= 1;
	
	protected boolean	periodic	= false;

	public float getMinValue() {
		return minValue;
	}

	public float getMaxValue() {
		return maxValue;
	}

	public float getResolution() {
		return resolution;
	}

	public boolean isPeriodic() {
		return periodic;
	}
	
	@Override
	public SDR getEncodedValue(Object value) {
		SDR r = super.getEncodedValue(value);
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
			if (bit >= getEncodeLength()) {
				if (periodic) {
					b = b % getEncodeLength();
				} else {
					break;
				}
			}
			r.setBit(b, true);
		}
		return r;
	}
	
	public Str testOnBits() {
		Str r = new Str();
		for (float val = minValue; val <= maxValue; val+=resolution) {
			SDR sdr = getEncodedValue(val);
			if (sdr.onBits()!=onBits) {
				r.sb().append("Invalid on bits for value: ");
				r.sb().append(val);
				r.sb().append(", on bits: ");
				r.sb().append(sdr.onBits());
				r.sb().append(", required: ");
				r.sb().append(onBits);
				break;
			}
		}
		return r;
	}
	
	public Str testMinimalOverlap() {
		return testOverlap(1, (onBits - 1));
	}
	
	public Str testNoOverlap() {
		return testOverlap(0, 0);
	}
	
	public Str testOverlap(int minOverlap, int maxOverlap) {
		Str r = new Str();
		SDR curr = getEncodedValue(minValue);
		for (float val = (minValue + resolution); val <= maxValue; val+=resolution) {
			SDR next = getEncodedValue(val);
			next.flatten();
			int overlap = curr.getOverlap(next);
			if (minOverlap > 0 && overlap < minOverlap) {
				r.sb().append("Invalid bucket value overlap for value: ");
				r.sb().append(val);
				r.sb().append(", overlap: ");
				r.sb().append(overlap);
				r.sb().append(", minimum: ");
				r.sb().append(minOverlap);
				break;
			} else if (maxOverlap >= 0 && overlap > maxOverlap) {
				r.sb().append("Invalid bucket value overlap for value: ");
				r.sb().append(val);
				r.sb().append(", overlap: ");
				r.sb().append(overlap);
				r.sb().append(", maximum: ");
				r.sb().append(maxOverlap);
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
