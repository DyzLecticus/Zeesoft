package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.functions.StaticFunctions;
import nl.zeesoft.zdk.functions.ZRandomize;
import nl.zeesoft.zdk.htm.util.SDR;

public class Memor extends ProcessorObject {
	private MemorColumn[]							columns				= null;
	private SortedMap<String,MemorCell>				cellsById			= new TreeMap<String,MemorCell>();
	
	private HashSet<MemorCell>						activeCells			= new HashSet<MemorCell>();
	private HashSet<MemorCell>						prevActiveCells		= new HashSet<MemorCell>();

	private HashSet<MemorCell>						winnerCells			= new HashSet<MemorCell>();
	private HashSet<MemorCell>						prevWinnerCells		= new HashSet<MemorCell>();

	private HashSet<MemorCell>						predictiveCells		= new HashSet<MemorCell>();
	private HashSet<MemorDistalSegment>				predictiveSegments	= new HashSet<MemorDistalSegment>();
	
	private SortedMap<String,List<MemorDistalLink>>	linksByToCell		= new TreeMap<String,List<MemorDistalLink>>();
	
	private SDR										burstSDR			= null;
	
	// TODO: Remove
	private int done = 0;
	private int doneStart = 0;
	
	public Memor(MemorConfig config) {
		super(config);
		this.config = config;
		initialize();
	}

	@Override
	public MemorConfig getConfig() {
		return (MemorConfig) super.getConfig();
	}

	@Override
	public ZStringBuilder getDescription() {
		ZStringBuilder r = getConfig().getDescription();
		int segments = 0;
		int maxLinksPerSegment = 0;
		int links = 0;
		int activeLinks = 0;
		int maxSegmentsPerCell = 0;
		for (int c = 0; c < columns.length; c++) {
			MemorColumn column = columns[c];
			for (int z = 0; z < column.cells.length; z++) {
				MemorCell cell = column.cells[z];
				if (cell.segments.size()>maxSegmentsPerCell) {
					maxSegmentsPerCell = cell.segments.size();
				}
				for (MemorDistalSegment segment: cell.segments) {
					segments++;
					if (segment.links.size() > maxLinksPerSegment) {
						maxLinksPerSegment = segment.links.size();
					}
					for (MemorDistalLink link: segment.links) {
						links++;
						if (link.connection>getConfig().distalConnectionThreshold) {
							activeLinks++;
						}
					}
				}
			}
		}
		r.append("\n");
		r.append("  Segments: " + segments + " (max per cell: " + maxSegmentsPerCell + ")\n"); 
		r.append("  Links/active: " + links + "/" + activeLinks + " (max per segment: " + maxLinksPerSegment + ")");
		return r;
	}

	@Override
	public ZStringBuilder toStringBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fromStringBuilder(ZStringBuilder str) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void destroy() {
		for (int i = 0; i < columns.length; i++) {
			columns[i].destroy();
		}
	}
	
	@Override
	public List<SDR> getSDRsForInput(SDR input,List<SDR> context,boolean learn) {
		List<SDR> r = super.getSDRsForInput(input, context, learn);
		r.add(burstSDR);
		return r;
	}

	@Override
	protected SDR getSDRForInputSDR(SDR input, boolean learn) {
		SDR r = new SDR(getConfig().length * getConfig().depth);
		burstSDR = getConfig().getNewSDR();
		if (input!=null) {
			long start = 0;
			
			if (learn && input.onBits()==0) {
				learn = false;
			}
			
			start = System.nanoTime();
			cycleActiveState();
			logStatsValue("cycleActiveState",System.nanoTime() - start);
			
			start = System.nanoTime();
			activateColumnCells(input,r,burstSDR,learn);
			logStatsValue("activateColumnCells",System.nanoTime() - start);
			
			if (learn) {
				start = System.nanoTime();
				learnFromBurstingColumns(burstSDR);
				logStatsValue("learnFromBurstingColumns",System.nanoTime() - start);
				
				//start = System.nanoTime();
				//unlearnIncorrectPredictions();
				//logStatsValue("unlearnIncorrectPredictions",System.nanoTime() - start);
			}
			
			start = System.nanoTime();
			predictActiveCells(burstSDR);
			logStatsValue("predictActiveCells",System.nanoTime() - start);
		}
		
		done++;
		
		return r;
	}
	
	protected void cycleActiveState() {
		prevActiveCells.clear();
		for (MemorCell cell: activeCells) {
			prevActiveCells.add(cell);
		}
		activeCells.clear();
		
		prevWinnerCells.clear();
		for (MemorCell cell: winnerCells) {
			prevWinnerCells.add(cell);
		}
		winnerCells.clear();
	}

	protected void activateColumnCells(SDR input,SDR output,SDR burstSDR,boolean learn) {
		System.out.println("SDR: " + input.toStringBuilder() + ", predictive cells: " + predictiveCells.size());
		for (int onBit: input.getOnBits()) {
			MemorColumn column = columns[onBit];
			boolean activated = false;
			List<MemorCell> columnCells = column.getCellsAsList();
			for (int z = 0; z < column.cells.length; z++) {
				MemorCell cell = columnCells.remove(ZRandomize.getRandomInt(0,columnCells.size() - 1));
				if (predictiveCells.contains(cell)) {
					activeCells.add(cell);
					winnerCells.add(cell);
					output.setBit(cell.index,true);
					activated = true;
					//if (learn) {
					//	for (MemorDistalSegment winnerSegment: cell.segments) {
					//		if (predictiveSegments.contains(winnerSegment)) {
					//			learnWinnerSegment(winnerSegment);
					//		}
					//	}
					//}
					break;
				}
			}
			
			// Burst
			if (!activated) {
				for (int z = 0; z < column.cells.length; z++) {
					output.setBit(column.cells[z].index,true);
					activeCells.add(column.cells[z]);
				}
				burstSDR.setBit(onBit,true);
				// Select random winner
				if (prevActiveCells.size()==0 || learn==false) {
					winnerCells.add(column.cells[ZRandomize.getRandomInt(0,column.cells.length - 1)]);
				}
			}
		}
	}
	
	protected void learnFromBurstingColumns(SDR burstSDR) {
		if (burstSDR.onBits()>0 && prevActiveCells.size()>0) {
			//System.out.println("Failed to predict: " + burstSDR.onBits() + "/" + getConfig().bits + ", predictive cells: " + predictiveCells.size() + ", winners: " + winnerCells.size() + ", previous winners: " + prevActiveCells.size());
			
			for (Integer onBit: burstSDR.getOnBits()) {
				MemorColumn column = columns[onBit];

				MemorDistalSegment winnerSegment = null;
				
				// Map of segments connected to previous winners by number of connections
				SortedMap<Integer,List<MemorDistalSegment>> winnerLinksPerSegment = new TreeMap<Integer,List<MemorDistalSegment>>();
				SortedMap<Integer,List<MemorDistalSegment>> activeLinksPerSegment = new TreeMap<Integer,List<MemorDistalSegment>>();
				// Map of cells by number of segments
				SortedMap<Integer,List<MemorCell>> cellsBySegment = new TreeMap<Integer,List<MemorCell>>();
				
				for (int z = 0; z < column.cells.length; z++) {
					MemorCell cell = column.cells[z];

					if (cell.segments.size()<getConfig().maxDistalSegmentsPerCell) {
						List<MemorCell> cells = cellsBySegment.get(cell.segments.size());
						if (cells==null) {
							cells = new ArrayList<MemorCell>();
							cellsBySegment.put(cell.segments.size(),cells);
						}
						cells.add(cell);
					}
					
					for (MemorDistalSegment segment: cell.segments) {
						int numWinnerLinks = 0;
						for (MemorDistalLink link: segment.links) {
							if (prevActiveCells.contains(link.toCell)) {
								numWinnerLinks++;
							}
						}
						
						if (numWinnerLinks>0) {
							List<MemorDistalSegment> activeSegments = activeLinksPerSegment.get(numWinnerLinks);
							if (activeSegments==null) {
								activeSegments = new ArrayList<MemorDistalSegment>();
								activeLinksPerSegment.put(numWinnerLinks,activeSegments);
							}
							activeSegments.add(segment);
						}
						
						if (numWinnerLinks>=getConfig().minAlmostActiveDistalConnections) {
							List<MemorDistalSegment> winningSegments = winnerLinksPerSegment.get(numWinnerLinks);
							if (winningSegments==null) {
								winningSegments = new ArrayList<MemorDistalSegment>();
								winnerLinksPerSegment.put(numWinnerLinks,winningSegments);
							}
							winningSegments.add(segment);
						}
					}
				}
				
				// Select almost active winner segment
				if (winnerLinksPerSegment.size()>0) {
					List<MemorDistalSegment> list = winnerLinksPerSegment.get(winnerLinksPerSegment.lastKey());
					if (list.size()==1) {
						winnerSegment = list.get(0);
					} else {
						winnerSegment = list.get(ZRandomize.getRandomInt(0,list.size() - 1));
					}
				}
				
				MemorCell winnerCell = null;
				
				// If no almost active winner segments, add segment to least used cell
				if (winnerSegment==null && cellsBySegment.size()>0) {
					List<MemorCell> list = cellsBySegment.get(cellsBySegment.firstKey());
					if (list.size()==1) {
						winnerCell = list.get(0);
					} else {
						winnerCell = list.get(ZRandomize.getRandomInt(0,list.size() - 1));
					}
					winnerSegment = addSegment(winnerCell);
				}
				
				if (winnerSegment!=null) {
					learnWinnerSegment(winnerSegment);
					if (winnerCell==null) {
						winnerCell = cellsById.get(winnerSegment.cellId);
					}
					if (!winnerCells.contains(winnerCell)) {
						winnerCells.add(winnerCell);
					}
					
					// Weaken connections on losing segments
					for (Integer key: activeLinksPerSegment.keySet()) {
						List<MemorDistalSegment> activeSegments = activeLinksPerSegment.get(key);
						for (MemorDistalSegment segment: activeSegments) {
							if (segment!=winnerSegment) {
								List<MemorDistalLink> links = new ArrayList<MemorDistalLink>(segment.links);
								for (MemorDistalLink link: links) {
									if (prevActiveCells.contains(link.toCell)) {
										decrementLink(segment, link);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	protected MemorDistalSegment addSegment(MemorCell cell) {
		MemorDistalSegment r = null;
		int index = 0;
		if (cell.segments.size()>0) {
			index = cell.segments.get(cell.segments.size() - 1).index + 1;
		}
		r = new MemorDistalSegment(cell.getId(),index);
		cell.segments.add(r);
		return r;
	}
	
	protected void learnWinnerSegment(MemorDistalSegment winnerSegment) {
		// Strengthen and grow connections to previously active cells
		int incremented = 0;
		int added = 0;
		int maxAdd = getConfig().maxDistalConnectionsPerSegment - winnerSegment.links.size();
		if (maxAdd>getConfig().maxAddDistalConnections) {
			maxAdd=getConfig().maxAddDistalConnections;
		}
		List<MemorCell> prevActive = new ArrayList<MemorCell>(prevActiveCells);
		for (int i = 0; i < prevActiveCells.size(); i++) {
			MemorCell toCell = prevActive.remove(ZRandomize.getRandomInt(0,prevActive.size() - 1));
			boolean found = false;
			List<MemorDistalLink> list = new ArrayList<MemorDistalLink>(winnerSegment.links);
			for (MemorDistalLink link: list) {
				if (link.toCell==toCell) {
					found = true;
					link.connection += getConfig().distalConnectionIncrement;
					incremented++;
					if (link.connection>=1) {
						link.connection = 1;
					}
				}
			}
			// TODO: Check distance
			if (!found && added < maxAdd) {
				MemorDistalLink link = new MemorDistalLink();
				link.connection = getConfig().distalConnectionThreshold + getConfig().distalConnectionIncrement;
				link.fromSegment = winnerSegment;
				link.toCell = toCell;
				winnerSegment.links.add(link);
				List<MemorDistalLink> links = linksByToCell.get(toCell.getId());
				if (links==null) {
					links = new ArrayList<MemorDistalLink>();
					linksByToCell.put(toCell.getId(),links);
				}
				links.add(link);
				added++;
				//System.out.println("Added: " + this.cellsById.get(link.fromSegment.cellId).columnIndex + " -> " + toCell.columnIndex);
			}
		}
		if (done>doneStart) {
			System.out.println(winnerSegment.getId() + ", added: " + added + ", incremented: " + incremented);
		}
	}
	
	protected void unlearnIncorrectPredictions() {
		for (MemorCell cell: predictiveCells) {
			if (!winnerCells.contains(cell)) {
				List<MemorDistalSegment> segments = new ArrayList<MemorDistalSegment>(cell.segments);
				for (MemorDistalSegment segment: segments) {
					if (predictiveSegments.contains(segment)) {
						List<MemorDistalLink> links = new ArrayList<MemorDistalLink>(segment.links);
						for (MemorDistalLink link: links) {
							if (prevActiveCells.contains(link.toCell)) {
								decrementLink(segment,link);
							}
						}
					}
				}
			}
		}
	}

	protected void decrementLink(MemorDistalSegment segment,MemorDistalLink link) {
		link.connection -= getConfig().distalConnectionDecrement;
		if (link.connection<=0) {
			segment.links.remove(link);
			List<MemorDistalLink> links = linksByToCell.get(link.toCell.getId());
			if (links!=null) {
				links.remove(link);
			}
		}
		if (segment.links.size()==0) {
			MemorCell cell = cellsById.get(segment.cellId);
			cell.segments.remove(segment);
		}
	}
	
	protected void predictActiveCells(SDR burstSDR) {
		predictiveCells.clear();
		predictiveSegments.clear();
		SortedMap<String,Integer> sourceCellIdActive = new TreeMap<String,Integer>();
		for (MemorCell cell: activeCells) {
			List<MemorDistalLink> links = linksByToCell.get(cell.getId());
			if (links!=null) {
				for (MemorDistalLink link: links) {
					if (link.connection>getConfig().distalConnectionThreshold) {
						Integer active = sourceCellIdActive.get(link.fromSegment.cellId);
						if (active==null) {
							active = new Integer(0);
						}
						active++;
						sourceCellIdActive.put(link.fromSegment.cellId,active);
					}
				}
			}
		}
		for (Entry<String,Integer> entry: sourceCellIdActive.entrySet()) {
			if (done>doneStart) {
				System.out.println(done + ": " + entry.getKey() + ", active: " + entry.getValue());
			}
			if (entry.getValue()>=getConfig().minActiveDistalConnections) {
				MemorCell fromCell = cellsById.get(entry.getKey());
				if (!predictiveCells.contains(fromCell)) {
					predictiveCells.add(fromCell);
				}
			}
		}
	}
	
	protected void initialize() {
		columns = new MemorColumn[getConfig().sizeX * getConfig().sizeY];
		int index = 0;
		int columnIndex = 0;
		for (int x = 0; x < getConfig().sizeX; x++) {
			for (int y = 0; y < getConfig().sizeY; y++) {
				MemorColumn column = new MemorColumn(columnIndex,x,y,getConfig().depth);
				columns[columnIndex] = column;
				for (int z = 0; z < getConfig().depth; z++) {
					column.cells[z] = new MemorCell(getConfig(),index,columnIndex,x,y,z);
					cellsById.put(column.cells[z].getId(),column.cells[z]);
					index++;
				}
				columnIndex++;
			}
		}
	}
	
	protected int getCellDistance(MemorCell cellA, MemorCell cellB) {
		return StaticFunctions.getDistance(cellA.posX,cellA.posY,cellA.posZ,cellB.posX,cellB.posY,cellB.posZ);
	}
}
