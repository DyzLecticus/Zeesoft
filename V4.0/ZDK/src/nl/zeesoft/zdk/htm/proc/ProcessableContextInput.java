package nl.zeesoft.zdk.htm.proc;

import java.util.List;

import nl.zeesoft.zdk.htm.sdr.SDR;

public interface ProcessableContextInput {
	public void setContextSDRs(List<SDR> contextSDRs);
}
