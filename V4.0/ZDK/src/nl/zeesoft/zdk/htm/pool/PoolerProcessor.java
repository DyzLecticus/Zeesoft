package nl.zeesoft.zdk.htm.pool;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRSet;

public class PoolerProcessor {
	protected Pooler 						pooler			= null;
	protected List<PoolerProcessorListener>	listeners		= new ArrayList<PoolerProcessorListener>();
	
	protected SDRSet						inputSDRSet		= null;
	protected boolean						learn			= true;
	protected SDRSet						outputSDRSet	= null;
	
	public PoolerProcessor(Pooler pooler) {
		this.pooler = pooler;
	}
	
	public List<PoolerProcessorListener> getListeners() {
		return listeners;
	}
	
	public void setLearn(boolean learn) {
		this.learn = learn;
	}
	
	public void setIntputSDRSet(SDRSet inputSDRSet) {
		this.inputSDRSet = inputSDRSet;
		outputSDRSet = new SDRSet(pooler.config.outputSize);
	}
	
	public void process() {
		process(inputSDRSet.size());
	}

	public void process(int num) {
		if (inputSDRSet!=null) {
			int start = outputSDRSet.size();
			int stop = start + num;
			if (stop > inputSDRSet.size()) {
				stop = inputSDRSet.size();
			}
			for (int i = start; i < stop; i++) {
				processSDR(inputSDRSet.get(i));
			}
		}
	}
	
	public SDRSet getOutputSDRSet() {
		return outputSDRSet;
	}

	public void resetStats() {
		pooler.resetStats();
	}

	public PoolerStats getStats() {
		return pooler.getStats();
	}
	
	protected void processSDR(SDR inputSDR) {
		SDR outputSDR = pooler.getSDRForInput(inputSDR,learn);
		outputSDRSet.add(outputSDR);
		for (PoolerProcessorListener listener: listeners) {
			listener.processedSDR(this, inputSDR, outputSDR);
		}
	}
}
