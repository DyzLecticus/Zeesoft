package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.htm.sdr.SDR;

public class Predictor extends Memory implements ProcessableSecondaryOutput {
	private	int						maxOnBits			= 0;
	
	private SDR						predictionSDR		= null;
	private Set<MemoryColumnCell>	predictiveCells		= new HashSet<MemoryColumnCell>();
	
	public Predictor(MemoryConfig config) {
		super(config);
		stats = new PredictorStats();
	}
	
	public void setMaxOnBits(int maxOnBits) {
		this.maxOnBits = maxOnBits;
	}
	
	public SDR getPredictionSDR() {
		return predictionSDR;
	}

	@Override
	protected SDR getSDRForInputSDR(SDR input,boolean learn) {
		SDR r = super.getSDRForInputSDR(input,learn);
		long start = 0;

		PredictorStats pStats = (PredictorStats) stats;
		
		start = System.nanoTime();
		
		predictionSDR = new SDR(config.length);
		if (predictiveCells.size()>0) {
			HashMap<Integer,Float> columnActivity = new HashMap<Integer,Float>();
			for (MemoryColumnCell cell: predictiveCells) {
				Float activity = columnActivity.get(cell.columnIndex);
				if (activity==null) {
					activity = new Float(0);
				}
				activity += cell.activity;
				columnActivity.put(cell.columnIndex,activity);
			}
			SortedMap<Float,List<Integer>> columnIndicesByActivity = new TreeMap<Float,List<Integer>>();
			for (Integer index: columnActivity.keySet()) {
				Float activity = columnActivity.get(index);
				List<Integer> list = columnIndicesByActivity.get(activity);
				if (list==null) {
					list = new ArrayList<Integer>();
					columnIndicesByActivity.put(activity,list);
				}
				list.add(0,index);
			}
			List<Integer> indices = new ArrayList<Integer>();
			for (List<Integer> list: columnIndicesByActivity.values()) {
				for (Integer index: list) {
					indices.add(0,index);
				}
			}
			int max = maxOnBits;
			if (max<=0) {
				max = indices.size();
			}
			for (int i = 0; i < max; i++) {
				if (i==indices.size()) {
					break;
				}
				predictionSDR.setBit(indices.get(i),true);
			}
		}
		
		pStats.generatingPredictionsNs += System.nanoTime() - start;
		
		return r;
	}
	
	@Override
	public void resetStats() {
		stats = new PredictorStats();
	}
	
	@Override
	public PredictorStats getStats() {
		return (PredictorStats) stats;
	}

	@Override
	protected void updatePredictions(Set<MemoryColumnCell> predictiveCells,boolean learn) {
		super.updatePredictions(predictiveCells, learn);
		this.predictiveCells = predictiveCells;
	}

	@Override
	public void addSecondarySDRs(List<SDR> outputSDRs) {
		outputSDRs.add(predictionSDR);
	}
}
