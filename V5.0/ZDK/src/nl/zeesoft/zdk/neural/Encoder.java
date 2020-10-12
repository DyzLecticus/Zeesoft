package nl.zeesoft.zdk.neural;

public class Encoder {
	public Object 	value			= null;
	public int		encodeLength	= 256;
	
	public SDR getEncodedValue() {
		return new SDR(encodeLength,1);
	}
}
