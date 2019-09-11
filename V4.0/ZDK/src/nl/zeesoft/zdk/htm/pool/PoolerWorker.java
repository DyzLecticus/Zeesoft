package nl.zeesoft.zdk.htm.pool;

import java.util.List;

import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRSet;

public class PoolerWorker {
	private		Pooler 			pooler			= null;
	
	protected	SDRSet			inputSDRSet		= null;
	protected	SDRSet			outputSDRSet	= null;
	protected 	List<Integer>	onBits			= null;
	protected	SDR				outputSDR		= null;
	
	public PoolerWorker(Pooler pooler) {
		this.pooler = pooler;
	}
	
	public void setIntputSDRSet(SDRSet inputSDRSet) {
		this.inputSDRSet = inputSDRSet;
		this.outputSDRSet = new SDRSet(pooler.config.inputSize);
	}

	public void work() {
		for (int i = 0; i < inputSDRSet.size(); i++) {
			prepare(inputSDRSet.get(i));
			calculateOverlapScores();
			finalize();
		}
	}
	
	public SDRSet getOutputSDRSet() {
		return outputSDRSet;
	}
	
	protected void prepare(SDR inputSDR) {
		onBits = inputSDR.getOnBits();
		outputSDR = new SDR(pooler.config.outputSize);
	}
	
	protected void calculateOverlapScores() {
		for (PoolerColumn col: pooler.columns) {
			col.calculateOverlapScoreForOnBits(onBits);
		}
	}
	
	protected void finalize() {
		List<PoolerColumn> topColumns = pooler.getTopColumnsByOverlapScore();
		for (PoolerColumn col: topColumns) {
			outputSDR.setBit(col.index,true);
		}
		outputSDRSet.add(outputSDR);
	}
}
