package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.sdr.SDR;

public class Memory implements Processable {
	protected	MemoryConfig		config		= null;

	protected	MemoryStats			stats		= new MemoryStats();

	protected	List<MemoryColumn>	columns		= new ArrayList<MemoryColumn>();
	
	public Memory(MemoryConfig config) {
		this.config = config;
		initialize();
	}
	
	public ZStringBuilder getDescription() {
		ZStringBuilder r = new ZStringBuilder();
		int cells = config.size * config.depth;
		int min = cells; 
		int max = 0;
		int avg = 0;
		for (MemoryColumn col: columns) {
			for (MemoryColumnCell cell: col.cells) {
				int con = 0;
				for (DistalLink lnk: cell.distLinks) {
					if (lnk.connection>=0) {
						con++;
						avg++;
					}
				}
				if (con<min) {
					min = con;
				}
				if (con>max) {
					max = con;
				}
			}
		}
		if (avg>0) {
			avg = avg / cells;
			r.append("Average distal inputs per memory cell: ");
			r.append("" + avg);
			if (min!=avg || max!=avg) {
				r.append(" (min: ");
				r.append("" + min);
				r.append(", max: ");
				r.append("" + max);
				r.append(")");
			}
		}
		return r;
	}
	
	public void resetStats() {
		stats = new MemoryStats();
	}
	
	public MemoryStats getStats() {
		return stats;
	}

	@Override
	public SDR getSDRForInput(SDR input,boolean learn) {
		long total = System.nanoTime();
		SDR r = new SDR(config.size);
		long start = 0;
		
		start = System.nanoTime();
		List<MemoryColumnCell> previouslyActiveCells = cycleActiveState();
		stats.cycleStateNs += System.nanoTime() - start;
		
		start = System.nanoTime();
		activateColumnCells(input,learn,previouslyActiveCells,r);
		stats.activateCellsNs += System.nanoTime() - start;
		
		start = System.nanoTime();
		calculateActivity();
		stats.calculateActivityNs += System.nanoTime() - start;
		
		start = System.nanoTime();
		Set<MemoryColumnCell> predictiveCells = selectPredictiveCells();
		stats.selectPredictiveNs += System.nanoTime() - start;
		
		start = System.nanoTime();
		updatePredictions(predictiveCells,learn);
		stats.updatePredictionsNs += System.nanoTime() - start;
		
		stats.total++;
		stats.totalNs += System.nanoTime() - total;
		
		return r;
	}

	protected List<MemoryColumnCell> cycleActiveState() {
		List<MemoryColumnCell> r = new ArrayList<MemoryColumnCell>();
		for (MemoryColumn col: columns) {
			col.cycleActiveState(r);
		}
		return r;
	}

	protected void activateColumnCells(SDR input,boolean learn,List<MemoryColumnCell> previouslyActiveCells,SDR burstSDR) {
		for (Integer onBit: input.getOnBits()) {
			MemoryColumn col = columns.get(onBit);
			if (col.activateCells(learn,previouslyActiveCells)) {
				burstSDR.setBit(col.index,true);
			}
		}
	}
	
	protected void calculateActivity() {
		for (MemoryColumn col: columns) {
			col.calculateActivity();
		}
	}

	protected Set<MemoryColumnCell> selectPredictiveCells() {
		Set<MemoryColumnCell> r = new HashSet<MemoryColumnCell>();
		for (MemoryColumn col: columns) {
			for (MemoryColumnCell cell: col.cells) {
				if (cell.activity>0) {
					r.add(cell);
				}
			}
		}
		return r;
	}
	
	protected void updatePredictions(Set<MemoryColumnCell> predictiveCells,boolean learn) {
		for (MemoryColumn col: columns) {
			col.updatePreditions(predictiveCells,learn);
		}
	}
	
	protected void initialize() {
		int posX = 0;
		int posY = 0;
		for (int i = 0; i < config.size; i++) {
			MemoryColumn col = new MemoryColumn(i);
			columns.add(col);
			for (int d = 0; d < config.depth; d++) {
				MemoryColumnCell cell = new MemoryColumnCell(config,i,posX,posY,d);
				col.cells.add(cell);
			}
			posX++;
			if (posX % config.sizeX == 0) {
				posX = 0;
				posY++;
			}
		}
	}
}
