package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.functions.StaticFunctions;
import nl.zeesoft.zdk.functions.ZRandomize;
import nl.zeesoft.zdk.htm.util.SDR;

public class Memor extends ProcessorObject {
	private MemorColumn[]							columns				= null;
	private SortedMap<String,MemorCell>				cellsById			= new TreeMap<String,MemorCell>();
	
	private HashSet<MemorCell>						winnerCells			= new HashSet<MemorCell>();

	private HashSet<MemorCell>						prevWinnerCells		= new HashSet<MemorCell>();

	private HashSet<MemorCell>						predictiveCells		= new HashSet<MemorCell>();
	private HashSet<MemorDistalSegment>				predictiveSegments	= new HashSet<MemorDistalSegment>();
	
	private SDR										burstSDR			= null;
	
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
				
				start = System.nanoTime();
				unlearnIncorrectPredictions();
				logStatsValue("unlearnIncorrectPredictions",System.nanoTime() - start);
			}
			
			start = System.nanoTime();
			predictActiveCells();
			logStatsValue("predictActiveCells",System.nanoTime() - start);
		}
		return r;
	}
	
	protected void cycleActiveState() {
		prevWinnerCells.clear();
		for (MemorCell cell: winnerCells) {
			prevWinnerCells.add(cell);
		}
		winnerCells.clear();
	}

	protected void activateColumnCells(SDR input,SDR output,SDR burstSDR,boolean learn) {
		for (int onBit: input.getOnBits()) {
			MemorColumn column = columns[onBit];
			boolean activated = false;
			List<MemorCell> columnCells = new ArrayList<MemorCell>();
			for (int z = 0; z < column.cells.length; z++) {
				columnCells.add(column.cells[z]);
			}
			for (int z = 0; z < column.cells.length; z++) {
				MemorCell cell = columnCells.remove(ZRandomize.getRandomInt(0,columnCells.size() - 1));
				if (predictiveCells.contains(cell)) {
					winnerCells.add(cell);
					output.setBit(cell.index,true);
					activated = true;
					if (learn) {
						for (MemorDistalSegment winnerSegment: cell.segments) {
							if (predictiveSegments.contains(winnerSegment)) {
								learnWinnerSegment(winnerSegment);
							}
						}
					}
					break;
				}
			}
			
			// Burst
			if (!activated) {
				for (int z = 0; z < column.cells.length; z++) {
					output.setBit(column.cells[z].index,true);
				}
				burstSDR.setBit(onBit,true);
				// Select random winner
				if (prevWinnerCells.size()==0 || learn==false) {
					winnerCells.add(column.cells[ZRandomize.getRandomInt(0,column.cells.length - 1)]);
				}
			}
		}
	}
	
	protected void learnFromBurstingColumns(SDR burstSDR) {
		if (burstSDR.onBits()>0 && prevWinnerCells.size()>0) {
			//System.out.println("Failed to predict: " + burstSDR.onBits() + "/" + getConfig().bits + ", predictive cells: " + predictiveCells.size() + ", winners: " + winnerCells.size() + ", previous winners: " + prevWinnerCells.size());
			
			for (Integer onBit: burstSDR.getOnBits()) {
				MemorColumn column = columns[onBit];

				MemorDistalSegment winnerSegment = null;
				
				// Map of segments connected to previous winners by number of connections
				SortedMap<Integer,List<MemorDistalSegment>> winnerLinksPerSegment = new TreeMap<Integer,List<MemorDistalSegment>>();
				// Map of open segments arranged by number of links
				SortedMap<Integer,List<MemorDistalSegment>> activeLinksPerSegment = new TreeMap<Integer,List<MemorDistalSegment>>();
				
				for (int z = 0; z < column.cells.length; z++) {
					MemorCell cell = column.cells[z];

					boolean hasFreeSegment = false;
					for (MemorDistalSegment segment: cell.segments) {
						int numWinnerLinks = 0;
						
						for (MemorDistalLink link: segment.links) {
							if (prevWinnerCells.contains(link.toCell)) {
								numWinnerLinks++;
							}
						}
						
						if (numWinnerLinks>=getConfig().minActiveDistalConnections) {
							List<MemorDistalSegment> list = winnerLinksPerSegment.get(numWinnerLinks);
							if (list==null) {
								list = new ArrayList<MemorDistalSegment>();
								winnerLinksPerSegment.put(numWinnerLinks,list);
							}
							list.add(segment);
						}
						
						if (segment.links.size()<getConfig().maxDistalConnectionsPerSegment - getConfig().maxAddDistalConnections) {
							hasFreeSegment = true;
							List<MemorDistalSegment> list = activeLinksPerSegment.get(segment.links.size());
							if (list==null) {
								list = new ArrayList<MemorDistalSegment>();
								activeLinksPerSegment.put(segment.links.size(),list);
							}
							list.add(segment);
						}
					}
					if (!hasFreeSegment) {
						MemorDistalSegment segment = new MemorDistalSegment(cell.getId(),cell.segments.size());
						cell.segments.add(segment);
						List<MemorDistalSegment> list = activeLinksPerSegment.get(0);
						if (list==null) {
							list = new ArrayList<MemorDistalSegment>();
							activeLinksPerSegment.put(0,list);
						}
						list.add(segment);
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
				
				// If no almost active winner segments, get least connected segment
				if (winnerSegment==null && activeLinksPerSegment.size()>0) {
					List<MemorDistalSegment> list = activeLinksPerSegment.get(activeLinksPerSegment.firstKey());
					if (list.size()==1) {
						winnerSegment = list.get(0);
					} else {
						winnerSegment = list.get(ZRandomize.getRandomInt(0,list.size() - 1));
					}
				}
				
				learnWinnerSegment(winnerSegment);
				
				MemorCell winnerCell = cellsById.get(winnerSegment.cellId);
				if (!winnerCells.contains(winnerCell)) {
					winnerCells.add(winnerCell);
				}
			}
		}
	}
	
	protected void learnWinnerSegment(MemorDistalSegment winnerSegment) {
		// Strengthen and grow connections to previous winner cells
		int added = 0;
		int maxAdd = getConfig().maxDistalConnectionsPerSegment - winnerSegment.links.size();
		if (maxAdd>getConfig().maxAddDistalConnections) {
			maxAdd=getConfig().maxAddDistalConnections;
		}
		List<MemorCell> prevWinners = new ArrayList<MemorCell>(prevWinnerCells);
		for (int i = 0; i < prevWinnerCells.size(); i++) {
			MemorCell toCell = prevWinners.remove(ZRandomize.getRandomInt(0,prevWinners.size() - 1));
			boolean found = false;
			for (MemorDistalLink link: winnerSegment.links) {
				if (link.toCell==toCell) {
					found = true;
					link.connection += getConfig().distalConnectionIncrement;
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
				toCell.toSegments.add(winnerSegment);
				added++;
			}
		}
		//System.out.println(onBit + ": " + winnerSegment.getId() + ", added: " + added);
	}
	
	protected void unlearnIncorrectPredictions() {
		for (MemorCell cell: predictiveCells) {
			if (!winnerCells.contains(cell)) {
				for (MemorDistalSegment segment: cell.segments) {
					if (predictiveSegments.contains(segment)) {
						List<MemorDistalLink> list = new ArrayList<MemorDistalLink>(segment.links);
						for (MemorDistalLink link: list) {
							if (prevWinnerCells.contains(link.toCell)) {
								link.connection -= getConfig().distalConnectionDecrement;
								if (link.connection<=0) {
									segment.links.remove(link);
									link.toCell.toSegments.remove(segment);
								}
							}
						}
					}
				}
			}
		}
	}

	protected void predictActiveCells() {
		predictiveCells.clear();
		predictiveSegments.clear();
		List<MemorDistalSegment> checkedSegments = new ArrayList<MemorDistalSegment>();
		for (MemorCell cell: winnerCells) {
			//System.out.println(cell.getId() + ", toSegments: " + cell.toSegments.size());
			for (MemorDistalSegment segment: cell.toSegments) {
				if (!checkedSegments.contains(segment)) {
					int active = 0;
					for (MemorDistalLink link: segment.links) {
						if (link.connection>getConfig().distalConnectionThreshold && winnerCells.contains(link.toCell)) {
							active++;
						}
					}
					if (active>=getConfig().minActiveDistalConnections) {
						predictiveSegments.add(segment);
					}
				}
				checkedSegments.add(segment);
			}
		}
		for (MemorDistalSegment segment: predictiveSegments) {
			MemorCell fromCell = cellsById.get(segment.cellId);
			if (!predictiveCells.contains(fromCell)) {
				predictiveCells.add(fromCell);
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
