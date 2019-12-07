package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.util.SDR;

/**
 * A (Temporal) Memory is used to learn SDR sequences.
 * 
 * PLEASE NOTE; This implementation differs greatly from the Numenta HTM implementation because it does not model dendrites;
 * Memory cells are directly connected to each other and dendrite activation is not limited.
 * Further more, distal connections do not need to be randomly initialized when the memory is created. 
 */
public class Memory extends ProcessorObject {
	protected List<MemoryColumn>	columns			= new ArrayList<MemoryColumn>();
	protected List<MemoryColumn>	contextColumns	= new ArrayList<MemoryColumn>();
	protected List<MemoryColumn>	allColumns		= new ArrayList<MemoryColumn>();
	
	protected List<SDR>				contextSDRs		= null;
	
	protected SDR					burstSDR		= null;	
	
	public Memory(MemoryConfig config) {
		super(config);
		initialize();
	}
	
	@Override
	public MemoryConfig getConfig() {
		return (MemoryConfig) super.getConfig();
	}

	/**
	 * Returns a description of this temporal memory.
	 * 
	 * @return A description
	 */
	@Override
	public ZStringBuilder getDescription() {
		ZStringBuilder r = getConfig().getDescription();
		int cells = getConfig().length * getConfig().depth;
		
		int min = Integer.MAX_VALUE; 
		int max = 0;
		int avg = 0;
		int act = 0;
		
		int minCon = Integer.MAX_VALUE; 
		int maxCon = 0;
		int avgCon = 0;
		
		int minCtx = Integer.MAX_VALUE;
		int maxCtx = 0;
		int avgCtx = 0;

		for (MemoryColumn col: allColumns) {
			for (MemoryColumnCell cell: col.cells) {
				int con = 0;
				int ctx = 0;
				for (DistalLink lnk: cell.distLinks) {
					if (lnk.connection>getConfig().distalConnectionThreshold) {
						con++;
						avgCon++;
						act++;
					}
					if (lnk.cell.posZ<0 || lnk.cell.posY<0 || lnk.cell.posZ<0) {
						ctx++;
						avgCtx++;
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
				if (ctx<minCtx) {
					minCtx = ctx;
				}
				if (ctx>maxCtx) {
					maxCtx = ctx;
				}
			}
		}
		if (avg>0) {
			r.append("\n");
			r.append("- Total distal links: ");
			r.append("" + avg);
			r.append(", active: ");
			r.append("" + act);
			avg = avg / cells;
			r.append("\n");
			r.append("- Average distal inputs per memory cell: ");
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
			r.append("- Average connected distal inputs per memory cell: ");
			r.append("" + avgCon);
			if (minCon!=avgCon || maxCon!=avgCon) {
				r.append(" (min: ");
				r.append("" + minCon);
				r.append(", max: ");
				r.append("" + maxCon);
				r.append(")");
			}
			
			if (getConfig().contextDimensions.size()>0) {
				avgCtx = avgCtx / cells;
				r.append("\n");
				r.append("- Average connected distal context inputs per memory cell: ");
				r.append("" + avgCtx);
				if (minCtx!=avgCtx || maxCon!=avgCtx) {
					r.append(" (min: ");
					r.append("" + minCtx);
					r.append(", max: ");
					r.append("" + maxCtx);
					r.append(")");
				}
			}
		}
		return r;
	}
	
	@Override
	public List<SDR> getSDRsForInput(SDR input,List<SDR> context,boolean learn) {
		contextSDRs = context;
		List<SDR> r = super.getSDRsForInput(input, context, learn);
		r.add(burstSDR);
		return r;
	}

	@Override
	protected SDR getSDRForInputSDR(SDR input,boolean learn) {
		SDR r = new SDR(getConfig().length * getConfig().depth);
		burstSDR = getConfig().getNewSDR();
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
		for (MemoryColumn col: allColumns) {
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
		for (MemoryColumn col: allColumns) {
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
		if (cols.size()==allColumns.size()) {
			for (int i = 0; i < cols.size(); i++) {
				MemoryColumn col = allColumns.get(i);
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
		for (MemoryColumn col: allColumns) {
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
		contextColumns.clear();
		allColumns.clear();
	}
	
	protected List<MemoryColumnCell> cycleActiveState() {
		List<MemoryColumnCell> r = new ArrayList<MemoryColumnCell>();
		for (MemoryColumn col: allColumns) {
			col.cycleActiveState(r);
		}
		return r;
	}

	protected Set<MemoryColumnCell> activateColumnCells(SDR input,boolean learn,List<MemoryColumnCell> previouslyActiveCells,SDR outputSDR,SDR burstSDR) {
		Set<MemoryColumnCell> r = new HashSet<MemoryColumnCell>(); 
		if (getConfig().contextDimensions.size()>0 && contextSDRs!=null && contextSDRs.size()==getConfig().contextDimensions.size()) {
			int i = 0;
			for (SDR contextSDR: contextSDRs) {
				int size = getConfig().contextDimensions.get(i);
				if (contextSDR.length()<=size) {
					MemoryColumn contextColumn = contextColumns.get(i);
					for (Integer onBit: contextSDR.getOnBits()) {
						MemoryColumnCell cell = contextColumn.cells.get(onBit); 
						cell.active = true;
						r.add(cell);
					}
				}
				i++;
			}
		}
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
		for (int i = 0; i < getConfig().length; i++) {
			MemoryColumn col = new MemoryColumn(i);
			columns.add(col);
			allColumns.add(col);
			
			for (int d = 0; d < getConfig().depth; d++) {
				MemoryColumnCell cell = new MemoryColumnCell(getConfig(),i,cellIndex,posX,posY,d);
				col.cells.add(cell);
				cellIndex++;
			}
			posX++;
			if (posX % getConfig().sizeX == 0) {
				posX = 0;
				posY++;
			}
		}
		int i = -1;
		for (Integer length: getConfig().contextDimensions) {
			MemoryColumn col = new MemoryColumn(i);
			contextColumns.add(col);
			allColumns.add(col);

			for (int d = 0; d < length; d++) {
				MemoryColumnCell cell = new MemoryColumnCell(getConfig(),i,cellIndex,i,-1,d);
				col.cells.add(cell);
				cellIndex++;
			}
			i--;
		}
	}
}
