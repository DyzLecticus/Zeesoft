package nl.zeesoft.zdk.htm.proc;

import java.util.List;

import nl.zeesoft.zdk.htm.sdr.SDR;

public interface ProcessorListener {
	public void processedSDR(ProcessorObject processor,SDR inputSDR,List<SDR> outputSDRs);
}
