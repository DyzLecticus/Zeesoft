package nl.zeesoft.zdk.htm.enc;

import nl.zeesoft.zdk.htm.util.SDR;

public abstract class EncoderObject {
	protected int		length		= 0;
	protected int		bits		= 0;
	protected float		resolution	= 1;
	
	public EncoderObject(int length,int bits) {
		if (length < 10) {
			length = 10;
		}
		if (bits < 1) {
			length = 1;
		}
		this.length = length;
		this.bits = bits;
	}
	
	public abstract SDR getSDRForValue(float value);
	
	public void setResolution(float resolution) {
		this.resolution = resolution;
	}
	
	public int length() {
		return length;
	}
	
	public int bits() {
		return bits;
	}
}
