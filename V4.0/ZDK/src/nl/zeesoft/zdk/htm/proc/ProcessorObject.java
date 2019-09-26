package nl.zeesoft.zdk.htm.proc;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.sdr.SDR;

public abstract class ProcessorObject {
	protected DecimalFormat		df 			= new DecimalFormat("0.000");
	
	public boolean				logStats	= false;						
	public StatsLog				statsLog	= new StatsLog(this);
	
	private Stats				stats		= null;
	
	public SDR getSDRForInput(SDR input,boolean learn) {
		SDR r = null;
		stats = null;
		long start = 0;
		if (logStats) {
			stats = new Stats();
			start = System.nanoTime();
		}
		r = getSDRForInputSDR(input,learn);
		if (logStats) {
			stats.setValue("total",System.nanoTime() - start);
			statsLog.addStats(stats);
		}
		return r;
	}
	
	public List<SDR> getSDRsForInput(SDR input,List<SDR> context,boolean learn) {
		List<SDR> r = new ArrayList<SDR>();
		r.add(getSDRForInput(input,learn));
		return r;
	}
	
	protected void logStatsValue(String name,long value) {
		if (logStats && stats!=null) {
			stats.setValue(name, value);
		}
	}
	
	protected abstract SDR getSDRForInputSDR(SDR input,boolean learn);
}
