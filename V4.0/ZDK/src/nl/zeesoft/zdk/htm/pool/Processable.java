package nl.zeesoft.zdk.htm.pool;

import nl.zeesoft.zdk.htm.sdr.SDR;

public interface Processable {
	public SDR getSDRForInput(SDR input,boolean learn);
}
