package nl.zeesoft.zdk.htm.stream;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.proc.StatsObject;

public class StreamStats extends StatsObject {
	public StatsObject copy() {
		StreamStats r = new StreamStats();
		copyTo(r);
		return r;
	}
	
	public ZStringBuilder getDescription() {
		ZStringBuilder r = super.getDescription();
		appendTotal(r,"- Processed SDRs:             ");
		r.append("\n");
		appendNsPerTotal(r,"- Average time per input SDR: ");
		return r;
	}
}
