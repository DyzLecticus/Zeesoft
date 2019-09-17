package nl.zeesoft.zdk.htm.pool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.sdr.SDR;

public class Memory implements Processable {
	protected	MemoryConfig							config					= null;

	protected	List<MemoryColumn>						columns					= new ArrayList<MemoryColumn>();
	
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
			r.append("\n");
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

	@Override
	public SDR getSDRForInput(SDR input,boolean learn) {
		SDR r = new SDR(config.size);
		
		List<MemoryColumnCell> previouslyActiveCells = cycleActiveState();
		//System.out.println("---> Cycled active state");
		
		List<MemoryColumn> activeColumns = new ArrayList<MemoryColumn>();
		for (Integer onBit: input.getOnBits()) {
			activeColumns.add(columns.get(onBit));
		}
		//System.out.println("---> Active columns: " + activeColumns.size());
		
		activateColumnCells(activeColumns,learn,previouslyActiveCells,r);
		//System.out.println("---> Activated column cells, bursting: " + r.onBits());
		
		calculateActivity();
		//System.out.println("---> Calculated overlap scores");
		
		Set<MemoryColumnCell> predictiveCells = selectPredictiveCells();
		//System.out.println("---> Selected predictive cells: " + predictiveCells.size());
		
		updatePredictions(predictiveCells,learn);
		///System.out.println("---> Set predictions");
		
		return r;
	}

	protected List<MemoryColumnCell> cycleActiveState() {
		List<MemoryColumnCell> r = new ArrayList<MemoryColumnCell>();
		for (MemoryColumn col: columns) {
			col.cycleActiveState(r);
		}
		return r;
	}

	protected void activateColumnCells(List<MemoryColumn> activeColumns,boolean learn,List<MemoryColumnCell> previouslyActiveCells,SDR burstSDR) {
		for (MemoryColumn col: activeColumns) {
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
		for (int i = 0; i < config.size; i++) {
			MemoryColumn col = new MemoryColumn(i);
			columns.add(col);
			for (int d = 0; d < config.depth; d++) {
				MemoryColumnCell cell = new MemoryColumnCell(config,col.index,d);
				col.cells.add(cell);
			}
		}
	}
}
