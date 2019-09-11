package nl.zeesoft.zdk.htm.pool;

import java.util.List;

import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRSet;

public class PoolerWorker {
	private		Pooler 			pooler			= null;
	
	protected	SDRSet			inputSDRSet		= null;
	protected	SDRSet			outputSDRSet	= null;
	protected 	List<Integer>	onBits			= null;
	
	public PoolerWorker(Pooler pooler) {
		this.pooler = pooler;
	}
	
	public void setIntputSDRSet(SDRSet inputSDRSet) {
		this.inputSDRSet = inputSDRSet;
		this.outputSDRSet = new SDRSet(pooler.config.outputSize);
	}

	public void work(boolean learn) {
		if (inputSDRSet!=null) {
			for (int i = 0; i < inputSDRSet.size(); i++) {
				List<Integer> onBits = inputSDRSet.get(i).getOnBits();
				calculateOverlapScores(onBits);
				List<PoolerColumn> activeColumns = pooler.getActiveColumns();
				finalize(activeColumns);
				if (learn) {
					learn(activeColumns,onBits);
				}
			}
		}
	}
	
	public SDRSet getOutputSDRSet() {
		return outputSDRSet;
	}
	
	protected void calculateOverlapScores(List<Integer> onBits) {
		for (PoolerColumn col: pooler.columns) {
			col.calculateOverlapScoreForOnBits(onBits);
		}
	}
	
	protected void finalize(List<PoolerColumn> activeColumns) {
		SDR outputSDR = new SDR(pooler.config.outputSize);
		for (PoolerColumn col: activeColumns) {
			outputSDR.setBit(col.index,true);
		}
		if (outputSDRSet.size() % 100 == 0) {
			System.out.println("--> " + outputSDR.toStringBuilder());
		}
		outputSDRSet.add(outputSDR);
	}
	
	protected void learn(List<PoolerColumn> activeColumns,List<Integer> onBits) {
		for (PoolerColumn col: activeColumns) {
			col.learnOnBits(onBits);
		}
	}
}
