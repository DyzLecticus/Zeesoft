package nl.zeesoft.zdk.htm.pool;

import java.util.List;

import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRSet;

public class PoolerProcessor {
	private		Pooler 			pooler			= null;
	
	protected	SDRSet			inputSDRSet		= null;
	protected	SDRSet			outputSDRSet	= null;
	protected 	List<Integer>	onBits			= null;
	
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
				List<Integer> onBits = inputSDRSet.get(i).getOnBits();
				calculateOverlapScoresForSDROnBits(onBits);
				List<PoolerColumn> activeColumns = pooler.getActiveColumns();
				recordActiveColumnsInOutputSDRSet(activeColumns);
				if (learn) {
					learnActiveColumns(activeColumns,onBits);
				}
			}
		}
	}
	
	public SDRSet getOutputSDRSet() {
		return outputSDRSet;
	}
	
	protected void calculateOverlapScoresForSDROnBits(List<Integer> onBits) {
		for (PoolerColumn col: pooler.columns) {
			col.calculateOverlapScoreForOnBits(onBits);
		}
	}
	
	protected void recordActiveColumnsInOutputSDRSet(List<PoolerColumn> activeColumns) {
		SDR outputSDR = new SDR(pooler.config.outputSize);
		for (PoolerColumn col: activeColumns) {
			outputSDR.setBit(col.index,true);
		}
		if (outputSDRSet.size() % 100 == 0) {
			System.out.println("--> " + outputSDR.toStringBuilder());
		}
		outputSDRSet.add(outputSDR);
	}
	
	protected void learnActiveColumns(List<PoolerColumn> activeColumns,List<Integer> onBits) {
		for (PoolerColumn col: activeColumns) {
			col.learnOnBits(onBits);
		}
	}
}
