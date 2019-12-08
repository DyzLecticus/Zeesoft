package nl.zeesoft.zdk.htm.enc;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.util.SDR;

/**
 * Abstract encoder object.
 */
public abstract class EncoderObject {
	protected int		length		= 0;
	protected int		bits		= 0;
	protected float		resolution	= 1;
	
	public EncoderObject(int length,int bits) {
		if (length < 2) {
			length = 2;
		}
		if (bits < 1) {
			length = 1;
		}
		this.length = length;
		this.bits = bits;
	}
	
	/**
	 * Returns an SDR that encodes a certain value.
	 * 
	 * @param value The value to encode
	 * @return The SDR
	 */
	public abstract SDR getSDRForValue(float value);
	
	/**
	 * Sets the resolution of this encoder.
	 * 
	 * @param resolution The resolution to set
	 */
	public void setResolution(float resolution) {
		this.resolution = resolution;
	}
	
	/**
	 * Returns the length of the SDRs this encoder will generate.
	 * 
	 * @return The length
	 */
	public int length() {
		return length;
	}
	
	/**
	 * Returns the number of on bits in the SDRs this encoder will generate.
	 * 
	 * @return The number of bits
	 */
	public int bits() {
		return bits;
	}
	
	/**
	 * Returns the description of the encoder.
	 * 
	 * @return The description
	 */
	public ZStringBuilder getDescription() {
		ZStringBuilder r = new ZStringBuilder(getClass().getSimpleName());
		r.append(" length: ");
		r.append("" + length);
		r.append(", bits: ");
		r.append("" + bits);
		r.append(", resolution: ");
		r.append("" + resolution); 
		return r;
	}

	/**
	 * Iterates through all possible values of this encoder to determine if the SDR values have a certain minimal and maximal overlap.
	 * 
	 * @param minValue The minimum value this encoder can encode
	 * @param maxValue The maximum value this encoder can encode
	 * @param minOverlap The minimal overlap between two sequential values
	 * @param maxOverlap The maximal overlap between two sequential values
	 * @return An empty string builder or a string builder containing an error message
	 */
	protected ZStringBuilder testScalarOverlap(float minValue,float maxValue,int minOverlap,int maxOverlap) {
		if (maxOverlap==0) {
			maxOverlap = bits; 
		}
		ZStringBuilder r = new ZStringBuilder();
		float pv = minValue - resolution;
		SDR prev = null;
		for (float v = minValue; v < maxValue; v += resolution) {
			SDR test = getSDRForValue(v);
			if (prev!=null) {
				int overlap = test.getOverlapScore(prev);
				if (overlap<minOverlap) {
					r.append("Overlap between " + pv + " and " + v + " is less than " + minOverlap);
					break;
				} else if (overlap>maxOverlap) {
					r.append("Overlap between " + pv + " and " + v + " is greater than " + maxOverlap);
					break;
				}
			}
			pv = v;
			prev = test;
		}
		return r;
	}
}
