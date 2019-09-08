package nl.zeesoft.zdk.htm.enc;

import nl.zeesoft.zdk.htm.sdr.SDR;

public abstract class EncoderObject {
	protected int		size		= 0;
	protected int		bits		= 0;
	protected float		resolution	= 1;
	
	public EncoderObject(int size,int bits) {
		if (size < 10) {
			size = 10;
		}
		if (bits < 1) {
			size = 1;
		}
		this.size = size;
		this.bits = bits;
	}
	
	public abstract SDR getSDRForValue(float value);
	
	public void setResolution(float resolution) {
		this.resolution = resolution;
	}
}
