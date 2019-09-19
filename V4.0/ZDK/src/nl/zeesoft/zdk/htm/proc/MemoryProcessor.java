package nl.zeesoft.zdk.htm.proc;

import java.util.List;

import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRMap;

public class MemoryProcessor extends PoolerProcessor {
	protected SDRMap	burstSDRMap			= null;

	public MemoryProcessor(Pooler pooler,Memory memory) {
		super(pooler);
		processors.add(memory);
	}

	@Override
	public void setIntputSDRMap(SDRMap inputSDRMap) {
		super.setIntputSDRMap(inputSDRMap);
		burstSDRMap = new SDRMap(((Memory)processors.get(1)).config.length);
	}
	
	public SDRMap getBurstSDRMap() {
		return burstSDRMap;
	}

	public void resetMemoryStats() {
		((Memory)processors.get(1)).resetStats();
	}

	public MemoryStats getMemoryStats() {
		return ((Memory)processors.get(1)).getStats();
	}

	@Override
	protected void processedSDR(SDR inputSDR, List<SDR> outputSDRs) {
		super.processedSDR(inputSDR, outputSDRs);
		burstSDRMap.add(outputSDRs.get(1));
	}
}
