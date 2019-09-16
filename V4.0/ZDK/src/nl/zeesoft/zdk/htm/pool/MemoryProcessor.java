package nl.zeesoft.zdk.htm.pool;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRSet;

public class MemoryProcessor extends PoolerProcessor implements PoolerProcessorListener {
	protected Memory						memory				= null;
	
	protected List<MemoryProcessorListener>	memoryListeners		= new ArrayList<MemoryProcessorListener>();

	protected SDRSet						burstSDRSet			= null;

	public MemoryProcessor(Pooler pooler,Memory memory) {
		super(pooler);
		this.memory = memory;
		listeners.add(this);
	}
	
	public List<MemoryProcessorListener> getMemoryListeners() {
		return memoryListeners;
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
		for (MemoryProcessorListener listener: memoryListeners) {
			listener.processedSDR(this, inputSDR, outputSDR, burstSDR);
		}
	}
}
