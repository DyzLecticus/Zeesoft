package nl.zeesoft.zdk.htm.pool;

import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRSet;

public class MemoryProcessor extends PoolerProcessor implements PoolerProcessorListener {
	protected Memory						memory			= null;
	
	protected SDRSet						burstSDRSet		= null;

	public MemoryProcessor(Pooler pooler,Memory memory) {
		super(pooler);
		this.memory = memory;
		listeners.add(this);
	}

	@Override
	public void setIntputSDRSet(SDRSet inputSDRSet) {
		super.setIntputSDRSet(inputSDRSet);
		burstSDRSet = new SDRSet(pooler.config.outputSize);
	}
	
	public SDRSet getBurstSDRSet() {
		return burstSDRSet;
	}

	@Override
	public void processedSDR(PoolerProcessor processor, SDR inputSDR, SDR outputSDR) {
		SDR burstSDR = memory.getSDRForInput(outputSDR,learn);
		burstSDRSet.add(burstSDR);
		System.out.println("--->>> Processed SDR " + burstSDRSet.size() + ", bursting: " + burstSDR.onBits());
	}
}
