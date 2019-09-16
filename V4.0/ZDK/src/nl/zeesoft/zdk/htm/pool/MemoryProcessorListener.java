package nl.zeesoft.zdk.htm.pool;

import nl.zeesoft.zdk.htm.sdr.SDR;

public interface MemoryProcessorListener {
	public void processedSDR(MemoryProcessor processor,SDR inputSDR,SDR outputSDR,SDR burstSDR);
}
