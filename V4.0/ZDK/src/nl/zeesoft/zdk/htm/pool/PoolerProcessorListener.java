package nl.zeesoft.zdk.htm.pool;

import nl.zeesoft.zdk.htm.sdr.SDR;

public interface PoolerProcessorListener {
	public void processedSDR(PoolerProcessor processor,SDR inputSDR,SDR outputSDR);
}
