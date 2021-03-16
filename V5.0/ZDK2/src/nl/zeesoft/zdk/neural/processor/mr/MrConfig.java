package nl.zeesoft.zdk.neural.processor.mr;

import nl.zeesoft.zdk.matrix.Size;

public class MrConfig {
	public Size		size			= new Size(48,48,16);

	public boolean	concatenate		= false;
	public int		maxOnBits		= 256;
	public float	distortion		= 0F;
	
	public MrConfig copy() {
		MrConfig r = new MrConfig();
		r.size = size.copy();
		r.concatenate = concatenate;
		r.maxOnBits = maxOnBits;
		r.distortion = distortion;
		return r;
	}
}
