package nl.zeesoft.zdk.htm.proc;

import nl.zeesoft.zdk.htm.sdr.SDR;

public abstract class ProcessableObject implements Processable {
	protected StatsObject							stats				= null;
	
	@Override
	public SDR getSDRForInput(SDR input,boolean learn) {
		SDR r = null;
		long total = System.nanoTime();
		r = getSDRForInputSDR(input,learn);
		stats.total++;
		stats.totalNs += System.nanoTime() - total;
		return r;
	}
	
	protected abstract SDR getSDRForInputSDR(SDR input,boolean learn);
}
