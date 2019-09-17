package nl.zeesoft.zdk.htm.pool;

import nl.zeesoft.zdk.ZStringBuilder;

public class PoolerStats extends StatsObject {
	public long	calculateOverlapNs		= 0;
	public long	selectActiveNs			= 0;
	public long	learnActiveNs			= 0;
	public long	logActiveNs				= 0;
	public long	calculateActivityNs		= 0;
	public long	updateBoostNs			= 0;
	
	public ZStringBuilder getDescription() {
		ZStringBuilder r = super.getDescription();
		appendNsPerTotal(r,"- Average time per input SDR:             ");
		r.append("\n");
		appendValue(r,"- Calculating input overlaps took:        ",calculateOverlapNs);
		r.append("\n");
		appendValue(r,"- Selecting active columns took:          ",selectActiveNs);
		r.append("\n");
		appendValue(r,"- Learning active columns took:           ",learnActiveNs);
		r.append("\n");
		appendValue(r,"- Logging column activity took:           ",logActiveNs);
		r.append("\n");
		appendValue(r,"- Calculating column group activity took: ",calculateActivityNs);
		r.append("\n");
		appendValue(r,"- Updating boost factors took:            ",updateBoostNs);
		return r;
	}
}
