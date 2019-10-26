package nl.zeesoft.zdk.htm.enc;

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
}
