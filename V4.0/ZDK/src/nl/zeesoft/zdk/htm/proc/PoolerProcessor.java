package nl.zeesoft.zdk.htm.proc;

import java.util.List;

import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRMap;

public class PoolerProcessor extends ProcessorObject {
	protected SDRMap	outputSDRMap	= null;
	
	public PoolerProcessor(Pooler pooler) {
		super(pooler);
	}
	
	@Override
	public void setIntputSDRMap(SDRMap inputSDRMap) {
		super.setIntputSDRMap(inputSDRMap);
		outputSDRMap = new SDRMap(((Pooler)processors.get(0)).config.outputLength);
	}
	
	public SDRMap getOutputSDRMap() {
		return outputSDRMap;
	}

	public void resetPoolerStats() {
		((Pooler)processors.get(0)).resetStats();
	}

	public PoolerStats getPoolerStats() {
		return ((Pooler)processors.get(0)).getStats();
	}

	@Override
	protected void processedSDR(SDR inputSDR, List<SDR> outputSDRs) {
		outputSDRMap.add(outputSDRs.get(0));
	}
}
