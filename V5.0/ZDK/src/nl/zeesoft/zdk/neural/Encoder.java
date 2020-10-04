package nl.zeesoft.zdk.neural;

import nl.zeesoft.zdk.grid.SDR;

public class Encoder {
	public Object 	value			= null;
	public int		encodeLength	= 256;
	
	public SDR getEncodedValue() {
		SDR r = new SDR();
		r.initialize(encodeLength,1);
		return r;
	}
}
