package nl.zeesoft.zdk.htm.pool;

import java.util.List;

import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRSet;

public class MemoryProcessor extends PoolerProcessor {
	protected SDRSet						burstSDRSet			= null;

	public MemoryProcessor(Pooler pooler,Memory memory) {
		super(pooler);
		processors.add(memory);
	}

	@Override
	public void setIntputSDRSet(SDRSet inputSDRSet) {
		super.setIntputSDRSet(inputSDRSet);
		burstSDRSet = new SDRSet(((Memory)processors.get(1)).config.size);
	}
	
	public SDRSet getBurstSDRSet() {
		return burstSDRSet;
	}

	@Override
	protected void processedSDR(SDR inputSDR, List<SDR> outputSDRs) {
		super.processedSDR(inputSDR, outputSDRs);
		burstSDRSet.add(outputSDRs.get(1));
	}
}
