package nl.zeesoft.zdk.neural;

import nl.zeesoft.zdk.Str;

public class BasicScalarEncoder extends SDREncoder {
	protected float		minValue	= 0;
	protected float		maxValue	= 200;
	
	protected boolean	periodic	= false;

	public float getMinValue() {
		return minValue;
	}

	public void setMinValue(float minValue) {
		this.minValue = minValue;
	}

	public float getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}

	public boolean isPeriodic() {
		return periodic;
	}

	public void setPeriodic(boolean periodic) {
		this.periodic = periodic;
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
			val = getCorrectedValue(val) % getCorrectedMaxValue();
		} else {
			if (val < minValue) {
				val = minValue;
			}
			if (val > maxValue) {
				val = maxValue;
			}
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
	
	protected float getCorrectedMaxValue() {
		float r = maxValue;
		if (minValue!=0) {
			if (minValue > 0) {
				r = r - minValue;
			} else {
				r = r + (minValue * -1);
			}
		}
		return r;
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
	
	public Str testMinimalOverlap() {
		return testOverlap(1, onBits, 1);
	}
	
	public Str testNoOverlap() {
		return testOverlap(0, 0, 1);
	}
	
	public Str testOverlap(int minOverlap, int maxOverlap, float resolution) {
		Str r = new Str();
		SDR curr = getEncodedValue(minValue);
		for (float val = (minValue + resolution); val < maxValue; val+=resolution) {
			SDR next = getEncodedValue(val);
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
}
