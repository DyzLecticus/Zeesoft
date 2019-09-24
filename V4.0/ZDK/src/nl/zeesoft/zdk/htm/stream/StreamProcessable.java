package nl.zeesoft.zdk.htm.stream;

import java.util.List;

import nl.zeesoft.zdk.htm.sdr.SDR;

public interface StreamProcessable {
	public List<SDR> getOutputSDRsForInputSDRs(List<SDR> inputSDRs,boolean learn);
}
