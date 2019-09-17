package nl.zeesoft.zdk.htm.proc;

import java.util.List;

import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRSet;

public class PoolerProcessor extends ProcessorObject {
	protected SDRSet	outputSDRSet	= null;
	
	public PoolerProcessor(Pooler pooler) {
		super(pooler);
	}
	
	@Override
	public void setIntputSDRSet(SDRSet inputSDRSet) {
		super.setIntputSDRSet(inputSDRSet);
		outputSDRSet = new SDRSet(((Pooler)processors.get(0)).config.outputSize);
	}
	
	public SDRSet getOutputSDRSet() {
		return outputSDRSet;
	}

	public void resetPoolerStats() {
		((Pooler)processors.get(0)).resetStats();
	}

	public PoolerStats getPoolerStats() {
		return ((Pooler)processors.get(0)).getStats();
	}

	@Override
	protected void processedSDR(SDR inputSDR, List<SDR> outputSDRs) {
		outputSDRSet.add(outputSDRs.get(0));
	}
}
