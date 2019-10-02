package nl.zeesoft.zdk.htm.proc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.zeesoft.zdk.htm.sdr.SDR;

public class CellActivationOutput extends Memory {
	private SDR						cellActivationSDR	= null;
	private Set<MemoryColumnCell>	predictiveCells		= new HashSet<MemoryColumnCell>();
	
	public CellActivationOutput(MemoryConfig config) {
		super(config);
	}
	
	public SDR getCellActivationSDR() {
		return cellActivationSDR;
	}

	@Override
	public List<SDR> getSDRsForInput(SDR input,List<SDR> context,boolean learn) {
		List<SDR> r = super.getSDRsForInput(input,context,learn);
		r.add(cellActivationSDR);
		return r;
	}

	@Override
	protected SDR getSDRForInputSDR(SDR input,boolean learn) {
		SDR r = super.getSDRForInputSDR(input,learn);
		long start = 0;

		start = System.nanoTime();
		generateCellActivationSDR();
		logStatsValue("generateCellActivationSDR",System.nanoTime() - start);
		
		return r;
	}

	@Override
	protected void updatePredictions(Set<MemoryColumnCell> predictiveCells,boolean learn) {
		super.updatePredictions(predictiveCells, learn);
		this.predictiveCells = predictiveCells;
	}
	
	protected void generateCellActivationSDR() {
		cellActivationSDR = new SDR(config.length * config.depth);
		for (MemoryColumnCell cell: predictiveCells) {
			cellActivationSDR.setBit(cell.cellIndex,true);
		}
	}
}
