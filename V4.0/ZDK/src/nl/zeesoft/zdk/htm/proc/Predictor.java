package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.htm.sdr.SDR;

public class Predictor extends Memory {
	private	int						maxOnBits			= 0;
	
	private SDR						predictionSDR		= null;
	private Set<MemoryColumnCell>	predictiveCells		= new HashSet<MemoryColumnCell>();
	
	public Predictor(MemoryConfig config) {
		super(config);
	}
	
	public void setMaxOnBits(int maxOnBits) {
		this.maxOnBits = maxOnBits;
	}
	
	public SDR getPredictionSDR() {
		return predictionSDR;
	}

	@Override
	public List<SDR> getSDRsForInput(SDR input,List<SDR> context,boolean learn) {
		List<SDR> r = super.getSDRsForInput(input,context,learn);
		r.add(predictionSDR);
		return r;
	}

	@Override
	protected SDR getSDRForInputSDR(SDR input,boolean learn) {
		SDR r = super.getSDRForInputSDR(input,learn);
		long start = 0;

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
		logStatsValue("generatePredictionSDR",System.nanoTime() - start);
		
		return r;
	}

	@Override
	protected void updatePredictions(Set<MemoryColumnCell> predictiveCells,boolean learn) {
		super.updatePredictions(predictiveCells, learn);
		this.predictiveCells = predictiveCells;
	}
}
