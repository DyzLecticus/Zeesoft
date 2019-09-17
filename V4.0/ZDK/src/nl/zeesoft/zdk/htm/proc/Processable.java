package nl.zeesoft.zdk.htm.proc;

import nl.zeesoft.zdk.htm.sdr.SDR;

public interface Processable {
	public SDR getSDRForInput(SDR input,boolean learn);
}
