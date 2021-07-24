package nl.zeesoft.zdk.neural.encoder;

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

	public ScalarSdrEncoder copy() {
		ScalarSdrEncoder r = new ScalarSdrEncoder();
		r.copyFrom(this);
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
	
	protected void copyFrom(ScalarSdrEncoder other) {
		this.encodeLength = other.encodeLength;
		this.onBits = other.onBits;
		this.minValue = other.minValue;
		this.maxValue = other.maxValue;
		this.resolution = other.resolution;
		this.periodic = other.periodic;
	}
}
