package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.util.SDR;

public class Memory extends ProcessorObject {
	protected MemoryConfig			config		= null;

	protected List<MemoryColumn>	columns		= new ArrayList<MemoryColumn>();
	
	protected SDR					burstSDR	= null;	
	
	public Memory(MemoryConfig config) {
		this.config = config;
		config.initialized = true;
		initialize();
	}
	
	public ZStringBuilder getDescription() {
		ZStringBuilder r = new ZStringBuilder();
		int cells = config.length * config.depth;
		int min = Integer.MAX_VALUE; 
		int max = 0;
		int avg = 0;
		
		int minCon = Integer.MAX_VALUE; 
		int maxCon = 0;
		int avgCon = 0;

		for (MemoryColumn col: columns) {
			for (MemoryColumnCell cell: col.cells) {
				int con = 0;
				for (DistalLink lnk: cell.distLinks) {
					if (lnk.connection>config.distalConnectionThreshold) {
						con++;
						avgCon++;
					}
				}
				int size = cell.distLinks.size();
				avg += size;
				if (size<min) {
					min = size;
				}
				if (size>max) {
					max = size;
				}
				if (con<minCon) {
					minCon = con;
				}
				if (con>maxCon) {
					maxCon = con;
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

			avgCon = avgCon / cells;
			r.append("\n");
			r.append("Average connected distal inputs per memory cell: ");
			r.append("" + avgCon);
			if (minCon!=avgCon || maxCon!=avgCon) {
				r.append(" (min: ");
				r.append("" + minCon);
				r.append(", max: ");
				r.append("" + maxCon);
				r.append(")");
			}
		}
		return r;
	}
	
	@Override
	public List<SDR> getSDRsForInput(SDR input,List<SDR> context,boolean learn) {
		List<SDR> r = super.getSDRsForInput(input, context, learn);
		r.add(burstSDR);
		return r;
	}

	@Override
	protected SDR getSDRForInputSDR(SDR input,boolean learn) {
		SDR r = new SDR(config.length * config.depth);
		burstSDR = config.getNewSDR();
		long start = 0;
		
		start = System.nanoTime();
		List<MemoryColumnCell> previouslyActiveCells = cycleActiveState();
		logStatsValue("cycleActiveState",System.nanoTime() - start);
		
		start = System.nanoTime();
		activateColumnCells(input,learn,previouslyActiveCells,r,burstSDR);
		logStatsValue("activateColumnCells",System.nanoTime() - start);
		
		start = System.nanoTime();
		calculateActivity();
		logStatsValue("calculateActivity",System.nanoTime() - start);
		
		start = System.nanoTime();
		Set<MemoryColumnCell> predictiveCells = selectPredictiveCells();
		logStatsValue("selectPredictiveCells",System.nanoTime() - start);
		
		start = System.nanoTime();
		updatePredictions(predictiveCells,learn);
		logStatsValue("updatePredictions",System.nanoTime() - start);
		
		return r;
	}

	@Override
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder r = new ZStringBuilder();
		for (MemoryColumn col: columns) {
			if (r.length()>0) {
				r.append("#");
			}
			boolean firstCell = true;
			for (MemoryColumnCell cell: col.cells) {
				if (!firstCell) {
					r.append("|");
				}
				boolean firstLink = true;
				for (DistalLink link: cell.distLinks) {
					if (!firstLink) {
						r.append(";");
					}
					r.append("" + link.connection);
					r.append(",");
					r.append("" + link.cell.posX);
					r.append("-");
					r.append("" + link.cell.posY);
					r.append("-");
					r.append("" + link.cell.posZ);
					firstLink = false;
				}
				firstCell = false;
			}
		}
		return r;
	}
	
	@Override
	public void fromStringBuilder(ZStringBuilder str) {
		SortedMap<ZStringBuilder,MemoryColumnCell> cellsByPos = new TreeMap<ZStringBuilder,MemoryColumnCell>();
		for (MemoryColumn col: columns) {
			for (MemoryColumnCell cell: col.cells) {
				ZStringBuilder pos = new ZStringBuilder();
				pos.append("" + cell.posX);
				pos.append("-");
				pos.append("" + cell.posY);
				pos.append("-");
				pos.append("" + cell.posZ);
				cellsByPos.put(pos,cell);
			}
		}
		
		List<ZStringBuilder> cols = str.split("#");
		if (cols.size()==columns.size()) {
			for (int i = 0; i < cols.size(); i++) {
				MemoryColumn col = columns.get(i);
				List<ZStringBuilder> cells = cols.get(i).split("|");
				if (cells.size()==col.cells.size()) {
					for (int j = 0; j < cells.size(); j++) {
						MemoryColumnCell cell = col.cells.get(j);
						List<ZStringBuilder> lnks = cells.get(j).split(";");
						for (ZStringBuilder lnk: lnks) {
							List<ZStringBuilder> vals = lnk.split(",");
							if (vals.size()==2) {
								MemoryColumnCell toCell = cellsByPos.get(vals.get(1));
								if (toCell!=null) {
									DistalLink link = new DistalLink();
									link.connection = Float.parseFloat(vals.get(0).toString());
									link.cell = toCell;
									cell.distLinks.add(link);
									toCell.forwardLinks.add(link);
								}
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public void destroy() {
		for (MemoryColumn col: columns) {
			for (MemoryColumnCell cell: col.cells) {
				for (DistalLink link: cell.distLinks) {
					link.origin = null;
				}
				cell.distLinks.clear();
				cell.forwardLinks.clear();
			}
			col.cells.clear();
		}
		columns.clear();
	}
	
	protected List<MemoryColumnCell> cycleActiveState() {
		List<MemoryColumnCell> r = new ArrayList<MemoryColumnCell>();
		for (MemoryColumn col: columns) {
			col.cycleActiveState(r);
		}
		return r;
	}

	protected Set<MemoryColumnCell> activateColumnCells(SDR input,boolean learn,List<MemoryColumnCell> previouslyActiveCells,SDR outputSDR,SDR burstSDR) {
		Set<MemoryColumnCell> r = new HashSet<MemoryColumnCell>(); 
		for (Integer onBit: input.getOnBits()) {
			MemoryColumn col = columns.get(onBit);
			Set<MemoryColumnCell> activatedCells = new HashSet<MemoryColumnCell>();
			if (col.activateCells(learn,previouslyActiveCells,activatedCells)) {
				burstSDR.setBit(col.index,true);
			}
			for (MemoryColumnCell cell: activatedCells) {
				outputSDR.setBit(cell.cellIndex,true);
				r.add(cell);
			}
		}
		return r;
	}
	
	protected void calculateActivity() {
		for (MemoryColumn col: columns) {
			for (MemoryColumnCell cell: col.cells) {
				cell.activity = 0;
			}
		}
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
		int cellIndex = 0;
		for (int i = 0; i < config.length; i++) {
			MemoryColumn col = new MemoryColumn(i);
			columns.add(col);
			for (int d = 0; d < config.depth; d++) {
				MemoryColumnCell cell = new MemoryColumnCell(config,i,cellIndex,posX,posY,d);
				col.cells.add(cell);
				cellIndex++;
			}
			posX++;
			if (posX % config.sizeX == 0) {
				posX = 0;
				posY++;
			}
		}
	}
}
