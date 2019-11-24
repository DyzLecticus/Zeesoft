package nl.zeesoft.zdk.htm.proc;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.util.SDR;

/**
 * Abstract SDR processor object.
 * 
 * Takes one or more SDRs as input and returns one or more output SDRs.
 * Can log performance statistics for its internal operations.
 */
public abstract class ProcessorObject {
	protected DecimalFormat		df 			= new DecimalFormat("0.000");
	
	public boolean				logStats	= false;						
	public StatsLog				statsLog	= new StatsLog(this);
	
	private Stats				stats		= null;
	
	/**
	 * Returns the output SDR for a certain input SDR.
	 * 
	 * @param input The input SDR
	 * @param learn Indicates the processor should learn this input
	 * @return The output SDR
	 */
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
	
	/**
	 * Returns a description of this processor.
	 * 
	 * @return A description of this processor
	 */
	public abstract ZStringBuilder getDescription();
	
	/**
	 * Returns a string builder that represents the state information of this processor.
	 * 
	 * @return A string builder
	 */
	public abstract ZStringBuilder toStringBuilder();
	
	/**
	 * Initializes the state of this processor using a string builder.
	 * 
	 * @param str The string builder
	 */
	public abstract void fromStringBuilder(ZStringBuilder str);

	/**
	 * Destroys the processor to help garbage collection.
	 */
	public void destroy() {
		// Override to implement
	}
	
	/**
	 * Returns a list of one or more output SDRs for a certain input SDR, in a certain context.
	 * Used by streams to chain processor IO.
	 * 
	 * @param input The input SDR; Different processors expect different SDRs
	 * @param context A list of SDRs that were produced in the previous parts of the processor chain
	 * @param learn Indicates the processor should learn this input
	 * @return A list of one or more output SDRs
	 */
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
