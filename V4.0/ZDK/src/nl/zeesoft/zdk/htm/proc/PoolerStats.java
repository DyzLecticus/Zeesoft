package nl.zeesoft.zdk.htm.proc;

import nl.zeesoft.zdk.ZStringBuilder;

public class PoolerStats extends StatsObject {
	public long	calculateOverlapNs		= 0;
	public long	selectActiveNs			= 0;
	public long	learnActiveNs			= 0;
	public long	logActiveNs				= 0;
	public long	calculateActivityNs		= 0;
	public long	updateBoostNs			= 0;
	
	public StatsObject copy() {
		PoolerStats r = new PoolerStats();
		copyTo(r);
		return r;
	}
	
	public ZStringBuilder getDescription() {
		ZStringBuilder r = super.getDescription();
		appendTotal(r,"- Processed SDRs:                         ");
		r.append("\n");
		appendValue(r,"- Total processing time:                  ",totalNs);
		r.append("\n");
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
	
	@Override
	protected void copyTo(StatsObject copy) {
		super.copyTo(copy);
		if (copy instanceof PoolerStats) {
			PoolerStats stats = (PoolerStats) copy;
			stats.calculateOverlapNs = this.calculateOverlapNs;
			stats.selectActiveNs = this.selectActiveNs;
			stats.learnActiveNs = this.learnActiveNs;
			stats.logActiveNs = this.logActiveNs;
			stats.calculateActivityNs = this.calculateActivityNs;
			stats.updateBoostNs = this.updateBoostNs;
		}
	}
}
