package nl.zeesoft.zdk.htm.proc;

import nl.zeesoft.zdk.ZStringBuilder;

public class PredictorStats extends MemoryStats {
	public long	generatingPredictionsNs	= 0;
	
	public StatsObject copy() {
		PredictorStats r = new PredictorStats();
		copyTo(r);
		return r;
	}
	
	public ZStringBuilder getDescription() {
		ZStringBuilder r = super.getDescription();
		r.append("\n");
		appendValue(r,"- Generating prediction SDRs took: ",generatingPredictionsNs);
		return r;
	}
	
	@Override
	protected void copyTo(StatsObject copy) {
		super.copyTo(copy);
		if (copy instanceof PredictorStats) {
			PredictorStats stats = (PredictorStats) copy;
			stats.generatingPredictionsNs = this.generatingPredictionsNs;
		}
	}
}
