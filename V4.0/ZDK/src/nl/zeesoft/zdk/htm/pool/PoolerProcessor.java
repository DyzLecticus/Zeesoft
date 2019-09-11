package nl.zeesoft.zdk.htm.pool;

import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRSet;

public class PoolerProcessor {
	protected Pooler 	pooler			= null;
	
	protected SDRSet	inputSDRSet		= null;
	protected SDRSet	outputSDRSet	= null;
	
	public PoolerProcessor(Pooler pooler) {
		this.pooler = pooler;
	}
	
	public void setIntputSDRSet(SDRSet inputSDRSet) {
		this.inputSDRSet = inputSDRSet;
	}

	public void process(boolean learn) {
		if (inputSDRSet!=null) {
			outputSDRSet = new SDRSet(pooler.config.outputSize);
			for (int i = 0; i < inputSDRSet.size(); i++) {
				processSDR(inputSDRSet.get(i),learn);
			}
		}
	}
	
	public SDRSet getOutputSDRSet() {
		return outputSDRSet;
	}
	
	protected void processSDR(SDR inputSDR,boolean learn) {
		outputSDRSet.add(pooler.getSDRForInput(inputSDR,learn));
	}
}
