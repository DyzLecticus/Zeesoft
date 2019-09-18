package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.htm.sdr.SDR;

public class Predictor extends Memory implements ProcessableSecondaryOutput {
	private SDR						predictionSDR		= null;
	private Set<MemoryColumnCell>	predictiveCells		= new HashSet<MemoryColumnCell>();
	
	public Predictor(MemoryConfig config) {
		super(config);
	}
	
	public SDR getPredictionSDR() {
		return predictionSDR;
	}

	@Override
	public SDR getSDRForInput(SDR input,boolean learn) {
		SDR r = super.getSDRForInput(input,learn);
		
		predictionSDR =new SDR(config.size);
		if (predictiveCells.size()>0) {
			SortedMap<Integer,List<MemoryColumnCell>> predictionsByColumnIndex = new TreeMap<Integer,List<MemoryColumnCell>>();
			for (MemoryColumnCell cell: predictiveCells) {
				List<MemoryColumnCell> list = predictionsByColumnIndex.get(cell.columnIndex);
				if (list==null) {
					list = new ArrayList<MemoryColumnCell>();
					predictionsByColumnIndex.put(cell.columnIndex,list);
				}
				list.add(cell);
			}
			SortedMap<Integer,List<Integer>> columnIndicesByActivity = new TreeMap<Integer,List<Integer>>();
			for (List<MemoryColumnCell> cells: predictionsByColumnIndex.values()) {
				int index = 0;
				int activity = 0;
				for (MemoryColumnCell cell: cells) {
					index = cell.columnIndex;
					activity += cell.activity;
				}
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
			for (int i = 0; i < input.onBits(); i++) {
				if (i==indices.size()) {
					break;
				}
				predictionSDR.setBit(indices.get(i),true);
			}
		}
		
		return r;
	}

	@Override
	protected void updatePredictions(Set<MemoryColumnCell> predictiveCells,boolean learn) {
		super.updatePredictions(predictiveCells, learn);
		this.predictiveCells = predictiveCells;
	}

	@Override
	public void addSecondarySDRs(List<SDR> outputSDRs) {
		if (predictionSDR!=null) {
			outputSDRs.add(predictionSDR);
		}
	}
}
