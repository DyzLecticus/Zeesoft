package nl.zeesoft.zdk.neural;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.grid.ColumnFunction;
import nl.zeesoft.zdk.grid.Grid;
import nl.zeesoft.zdk.grid.GridColumn;
import nl.zeesoft.zdk.grid.Position;
import nl.zeesoft.zdk.grid.SDR;
import nl.zeesoft.zdk.neural.cell.Cell;
import nl.zeesoft.zdk.neural.cell.DistalSegment;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerList;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;

public class TemporalMemory extends SDRProcessor {
	public Lock					lock							= new Lock();
	
	public int					sizeX							= 48;
	public int					sizeY							= 48;
	public int					sizeZ							= 16;

	public int					maxSegmentsPerCell				= 256;
	public int					maxSynapsesPerSegment			= 256;
	
	public float				initialPermanence				= 0.21F;
	public float				permanenceThreshold				= 0.5F;
	public float				permanenceIncrement				= 0.1F;
	public float				permanenceDecrement				= 0.1F;

	public int					activationThreshold				= 13;
	public int					matchingThreshold				= 10;
	public int					maxNewSynapseCount				= 20;
	
	protected List<Position>	activeInputColumnPositions		= new ArrayList<Position>();
	protected SDR				burstingColumns					= null;
	protected Grid				cells							= null;
	protected List<Position>	activeCellPositions				= new ArrayList<Position>();
	protected List<Position>	winnerCellPositions				= new ArrayList<Position>();
	protected List<Position>	prevActiveCellPositions			= new ArrayList<Position>();
	protected List<Position>	prevWinnerCellPositions			= new ArrayList<Position>();
	protected List<Position>	predictiveCellPositions			= new ArrayList<Position>();

	@Override
	public void initialize(CodeRunnerList runnerList) {
		if (sizeX < 4) {
			sizeX = 4;
		}
		if (sizeY < 4) {
			sizeY = 4;
		}
		if (sizeZ < 4) {
			sizeZ = 4;
		}
		
		input = new SDR();
		input.initialize(sizeX, sizeY);
		
		output = new SDR();
		output.initialize(sizeX, sizeY * sizeZ);
		
		activeInputColumnPositions.clear();
		activeCellPositions.clear();
		winnerCellPositions.clear();
		prevActiveCellPositions.clear();
		prevWinnerCellPositions.clear();
		predictiveCellPositions.clear();
		
		burstingColumns = new SDR();
		burstingColumns.initialize(sizeX, sizeY);
		
		cells = new Grid();
		cells.initialize(sizeX, sizeY, sizeZ);
		
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, int posZ, Object value) {
				return new Cell(new Position(column.posX(), column.posY(), posZ));
			}
		};
		cells.applyFunction(function, runnerList);
	}
	
	@Override
	public void randomizeConnections(CodeRunnerList runnerList) {
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, int posZ, Object value) {
				Cell cell = (Cell) value;
				cell.clear();
				return value;
			}
		};
		cells.applyFunction(function, runnerList);
	}
	
	@Override
	public void setInput(SDR sdr) {
		super.setInput(sdr);
		activeInputColumnPositions = input.getValuePositions(true);
		burstingColumns.setValue(false);
		
		prevActiveCellPositions = new ArrayList<Position>(activeCellPositions);
		prevWinnerCellPositions = new ArrayList<Position>(winnerCellPositions);
		activeCellPositions.clear();
		winnerCellPositions.clear();
		
		Position.randomizeList(predictiveCellPositions);
	}
	
	@Override
	public void buildProcessorChain(CodeRunnerChain runnerChain, boolean learn) {
		CodeRunnerList activateColumns = new CodeRunnerList();
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, int posZ, Object value) {
				if (Position.posXYIsInList(column.posX(), column.posY(), activeInputColumnPositions)) {
					boolean predicted = false;
					for (Position pos: predictiveCellPositions) {
						if (pos.x==column.posX() && pos.y==column.posY()) {
							lock.lock(this);
							activeCellPositions.add(pos);
							winnerCellPositions.add(pos);
							lock.unlock(this);
							predicted = true;
							break;
						}
					}
					if (!predicted) {
						value = true;
						burstColumn(column);
					}
				}
				return value;
			}
		};
		burstingColumns.applyFunction(function, activateColumns);
		
		// TODO: Remove
		CodeRunnerList debug = new CodeRunnerList(new RunCode() {
			@Override
			protected boolean run() {
				debug();
				return true;
			}
		});
		
		CodeRunnerList growSegmentsAndSynapses = new CodeRunnerList();
		if (learn) {
			function = new ColumnFunction() {
				@Override
				public Object applyFunction(GridColumn column, int posZ, Object value) {
					if (Position.posIsInList(column.posX(), column.posY(), posZ, activeCellPositions)) {
						growSegmentsAndSynapses((Cell) value);
					}
					return value;
				}
			};
			cells.applyFunction(function, growSegmentsAndSynapses);
		}
		
		CodeRunnerList clearPrediction = new CodeRunnerList(new RunCode() {
			@Override
			protected boolean run() {
				predictiveCellPositions.clear();
				return true;
			}
		});
		
		CodeRunnerList predictActiveCells = new CodeRunnerList();
		function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, int posZ, Object value) {
				Cell cell = (Cell) value;
				if (cell.distalSegments.size()>0) {
					cell.calculateSegmentActivity(activeCellPositions, permanenceThreshold);
					cell.classifySegmentActivity(activationThreshold, matchingThreshold);
					if (cell.activeDistalSegments.size()>0) {
						predictiveCellPositions.add(new Position(column.posX(),column.posY(),posZ));
					}
				}
				return value;
			}
		};
		cells.applyFunction(function, predictActiveCells);
		
		runnerChain.add(activateColumns);
		if (learn) {
			//runnerChain.add(debug);
			runnerChain.add(growSegmentsAndSynapses);
		}
		runnerChain.add(clearPrediction);
		runnerChain.add(predictActiveCells);
	}
	
	public void debug() {
		System.out.println(
			"Burst SDR onBits: " + burstingColumns.getActiveColumns().size() + 
			", activeCellPositions: " + activeCellPositions.size() + 
			", winnerCellPositions: " + winnerCellPositions.size() + 
			", predictiveCellPositions: " + predictiveCellPositions.size()
			);
	}
	
	protected void burstColumn(GridColumn column) {
		lock.lock(this);
		activeCellPositions.addAll(Position.getColumnPositions(column.posX(), column.posY(), sizeZ));
		lock.unlock(this);
		Cell winnerCell = null;
		// Find potential match
		SortedMap<Integer,List<Cell>> cellsByPotential = new TreeMap<Integer,List<Cell>>();
		for (int z = 0; z < sizeZ; z++) {
			Cell cell = (Cell) cells.getValue(column.posX(), column.posY(), z);
			if (cell.matchingDistalSegment!=null) {
				int potential = cell.matchingDistalSegment.potentialSynapses.size();
				List<Cell> cells = cellsByPotential.get(potential);
				if (cells==null) {
					cells = new ArrayList<Cell>();
					cellsByPotential.put(potential, cells);
				}
				cells.add(cell);
			}
		}
		if (cellsByPotential.size()>0) {
			List<Cell> potentialCells = cellsByPotential.get(cellsByPotential.lastKey());
			winnerCell = potentialCells.get(Rand.getRandomInt(0, (potentialCells.size() -1)));
			//System.out.println(column + " " + winnerCell.matchingDistalSegment.potentialSynapses.size());
		} else {
			// Find least connected cell
			SortedMap<Integer,List<Cell>> cellsBySegments = new TreeMap<Integer,List<Cell>>();
			for (int z = 0; z < sizeZ; z++) {
				Cell cell = (Cell) cells.getValue(column.posX(), column.posY(), z);
				int segments = cell.distalSegments.size();
				List<Cell> cells = cellsByPotential.get(segments);
				if (cells==null) {
					cells = new ArrayList<Cell>();
					cellsBySegments.put(segments, cells);
				}
				cells.add(cell);
			}
			List<Cell> leastConnectedCells = cellsBySegments.get(cellsBySegments.firstKey());
			winnerCell = leastConnectedCells.get(Rand.getRandomInt(0, (leastConnectedCells.size() -1)));
		}
		if (winnerCell!=null) {
			lock.lock(this);
			winnerCellPositions.add(winnerCell.position);
			lock.unlock(this);
		}
	}
	
	protected void growSegmentsAndSynapses(Cell cell) {
		// increment/decrement permanence for active/inactive synapses
		// grow segments and/or synapses to previous winners
		if (cell.matchingDistalSegment!=null) {
			cell.matchingDistalSegment.adaptSynapses(prevActiveCellPositions, permanenceIncrement, permanenceDecrement);
			int growNum = maxNewSynapseCount - cell.matchingDistalSegment.potentialSynapses.size();
			if (growNum>0) {
				cell.matchingDistalSegment.growSynapses(
					cell.position, growNum, prevWinnerCellPositions, initialPermanence, maxSynapsesPerSegment);								
			}
		} else {
			if (cell.activeDistalSegments.size()>0) {
				for (DistalSegment segment: cell.activeDistalSegments) {
					segment.adaptSynapses(prevActiveCellPositions, permanenceIncrement, permanenceDecrement);
					int growNum = maxNewSynapseCount - segment.activeSynapses.size();
					if (growNum>0) {
						segment.growSynapses(
							cell.position, growNum, prevWinnerCellPositions, initialPermanence, maxSynapsesPerSegment);								
					}
				}
			} else {
				DistalSegment segment = cell.createSegment(maxSegmentsPerCell);
				int growNum = maxNewSynapseCount;
				if (prevWinnerCellPositions.size()<growNum) {
					growNum = prevWinnerCellPositions.size();
				}
				if (growNum>0) {
					segment.growSynapses(
						cell.position, growNum, prevWinnerCellPositions, initialPermanence, maxSynapsesPerSegment);								
				}
			}
		}
	}
}
