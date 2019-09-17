package nl.zeesoft.zdk.htm.pool;

import java.util.List;

import nl.zeesoft.zdk.htm.sdr.SDR;

public interface ProcessableSecondaryOutput {
	public void addSecondarySDRs(List<SDR> outputSDRs);
}
