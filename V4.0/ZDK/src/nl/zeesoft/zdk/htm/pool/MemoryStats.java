package nl.zeesoft.zdk.htm.pool;

import nl.zeesoft.zdk.ZStringBuilder;

public class MemoryStats extends StatsObject {
	public long	cycleStateNs			= 0;
	public long	activateCellsNs			= 0;
	public long	calculateActivityNs		= 0;
	public long	selectPredictiveNs		= 0;
	public long	updatePredictionsNs		= 0;
	
	public ZStringBuilder getDescription() {
		ZStringBuilder r = super.getDescription();
		appendNsPerTotal(r,"- Average time per input SDR:      ");
		r.append("\n");
		appendValue(r,"- Cycling active state took:       ",cycleStateNs);
		r.append("\n");
		appendValue(r,"- Activating column cells took:    ",activateCellsNs);
		r.append("\n");
		appendValue(r,"- Calculating cell activity took:  ",calculateActivityNs);
		r.append("\n");
		appendValue(r,"- Selecting predictive cells took: ",selectPredictiveNs);
		r.append("\n");
		appendValue(r,"- Updating predictions took:       ",updatePredictionsNs);
		return r;
	}
}
