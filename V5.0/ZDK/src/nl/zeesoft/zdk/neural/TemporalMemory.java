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
import nl.zeesoft.zdk.neural.model.ApicalSegment;
import nl.zeesoft.zdk.neural.model.Cell;
import nl.zeesoft.zdk.neural.model.DistalSegment;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerList;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;

public class TemporalMemory extends SDRProcessor {
	private Lock				lock							= new Lock();
	
	public int					sizeX							= 48;
	public int					sizeY							= 48;
	public int					sizeZ							= 16;
	
	public int					maxSegmentsPerCell				= 256;
	public int					maxSynapsesPerSegment			= 256;
	
	public float				initialPermanence				= 0.21F;
	public float				permanenceThreshold				= 0.5F;
	public float				permanenceIncrement				= 0.1F;
	public float				permanenceDecrement				= 0.1F;
	
	public float				distalSegmentDecrement			= 0.2F;
	public float				apicalSegmentDecrement			= 0.2F;

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

	protected List<Position>	activeApicalCellPositions		= new ArrayList<Position>();
	protected List<Position>	prevActiveApicalCellPositions	= new ArrayList<Position>();
	
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
		
		burstingColumns = new SDR();
		burstingColumns.initialize(sizeX, sizeY);
		
		cells = new Grid();
		cells.initialize(sizeX, sizeY, sizeZ);
		
		resetLocalState();

		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, int posZ, Object value) {
				return new Cell(new Position(column.posX(), column.posY(), posZ));
			}
		};
		cells.applyFunction(function, runnerList);
	}
	
	@Override
	public void resetConnections(CodeRunnerList runnerList) {
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
	
	public void resetState(CodeRunnerList runnerList) {
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, int posZ, Object value) {
				Cell cell = (Cell) value;
				cell.reset();
				return value;
			}
		};
		cells.applyFunction(function, runnerList);
		if (runnerList!=null) {
			runnerList.add(new RunCode() {
				@Override
				protected boolean run() {
					resetLocalState();
					return true;
				}
			});
		} else {
			resetLocalState();
		}
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
		
		prevActiveApicalCellPositions = new ArrayList<Position>(activeApicalCellPositions);
		activeApicalCellPositions.clear();
		
		Position.randomizeList(predictiveCellPositions);
	}
	
	public void addContext(SDR sdr) {
		activeApicalCellPositions = sdr.getValuePositions(true);
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
							activatePredictedColumn(pos);
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
		
		CodeRunnerList generateOutput = new CodeRunnerList(new RunCode() {
			@Override
			protected boolean run() {
				Grid activations = new Grid();
				activations.initialize(sizeX, sizeY, sizeZ, false);
				activations.setValue(activeCellPositions, true);
				activations.flatten();
				activations.flatten();
				output.copyFrom(activations);
				return true;
			}
		});
		
		CodeRunnerList adaptColumnSegmentsAndSynapses = new CodeRunnerList();
		if (learn) {
			function = new ColumnFunction() {
				@Override
				public Object applyFunction(GridColumn column, int posZ, Object value) {
					if (Position.posXYIsInList(column.posX(), column.posY(), activeCellPositions)) {
						adaptColumn(column);
					} else if (
						(distalSegmentDecrement > 0F || apicalSegmentDecrement > 0F) &&
						Position.posXYIsInList(column.posX(), column.posY(), predictiveCellPositions)
						) {
						if (distalSegmentDecrement > 0F) {
							punishPredictedColumn(column, distalSegmentDecrement, false);
						}
						if (apicalSegmentDecrement > 0F) {
							punishPredictedColumn(column, apicalSegmentDecrement, true);
						}
					}
					return value;
				}
			};
			cells.applyFunction(function, adaptColumnSegmentsAndSynapses);
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
					cell.calculateDistalSegmentActivity(activeCellPositions, permanenceThreshold);
					cell.calculateApicalSegmentActivity(activeApicalCellPositions, permanenceThreshold);
					cell.classifySegmentActivity(activationThreshold, matchingThreshold);
					if ((activeApicalCellPositions.size()==0 && cell.activeDistalSegments.size()>0) ||
						(activeApicalCellPositions.size()>0 && cell.activeDistalSegments.size()>0 && cell.activeApicalSegments.size()>0)
						) {
						lock.lock(this);
						predictiveCellPositions.add(new Position(column.posX(),column.posY(),posZ));
						lock.unlock(this);
					}
				}
				return value;
			}
		};
		cells.applyFunction(function, predictActiveCells);
		
		runnerChain.add(activateColumns);
		runnerChain.add(generateOutput);
		if (learn) {
			runnerChain.add(adaptColumnSegmentsAndSynapses);
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
	
	protected void activatePredictedColumn(Position pos) {
		lock.lock(this);
		activeCellPositions.add(pos);
		winnerCellPositions.add(pos);
		lock.unlock(this);
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
			int potential = 0;
			if (cell.matchingDistalSegment!=null) {
				potential = cell.matchingDistalSegment.potentialSynapses.size();
			}
			if (cell.matchingApicalSegment!=null) {
				potential = potential + cell.matchingApicalSegment.potentialSynapses.size();
			}
			if (potential>0) {
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
		} else {
			// Find least connected cell
			SortedMap<Integer,List<Cell>> cellsBySegments = new TreeMap<Integer,List<Cell>>();
			for (int z = 0; z < sizeZ; z++) {
				Cell cell = (Cell) cells.getValue(column.posX(), column.posY(), z);
				int segments = cell.distalSegments.size() + cell.apicalSegments.size();
				List<Cell> cells = cellsBySegments.get(segments);
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

	protected void adaptColumn(GridColumn column) {
		boolean bursting = (boolean) burstingColumns.getValue(column.posX(), column.posY());
		if (prevWinnerCellPositions.size()>0) {
			for (int z = 0; z < sizeZ; z++) {
				if (bursting) {
					if (Position.posIsInList(column.posX(), column.posY(), z, winnerCellPositions)) {
						Cell winnerCell = (Cell) cells.getValue(column.posX(), column.posY(), z);
						if (winnerCell.matchingDistalSegment!=null) {
							winnerCell.adaptMatchingSegments(
								prevActiveCellPositions, prevWinnerCellPositions, prevActiveApicalCellPositions,
								initialPermanence, permanenceIncrement, permanenceDecrement, maxNewSynapseCount, maxSynapsesPerSegment);
						} else {
							winnerCell.createSegments(
								prevActiveCellPositions, prevWinnerCellPositions, prevActiveApicalCellPositions,
								initialPermanence, permanenceIncrement, permanenceDecrement, maxNewSynapseCount, maxSegmentsPerCell, maxSynapsesPerSegment);
						}
					}
				} else {
					Cell cell = (Cell) cells.getValue(column.posX(), column.posY(), z);
					cell.adaptActiveSegments(
						prevActiveCellPositions, prevWinnerCellPositions, prevActiveApicalCellPositions,
						initialPermanence, permanenceIncrement, permanenceDecrement, maxNewSynapseCount, maxSynapsesPerSegment);
				}
			}
		}
	}

	protected void punishPredictedColumn(GridColumn column, float segmentDecrement, boolean apical) {
		for (int z = 0; z < sizeZ; z++) {
			Cell cell = (Cell) cells.getValue(column.posX(), column.posY(), z);
			if (apical) {
				for (ApicalSegment segment: cell.matchingApicalSegments) {
					segment.adaptSynapses(prevActiveApicalCellPositions, segmentDecrement * -1F, 0F);
				}
			} else {
				for (DistalSegment segment: cell.matchingDistalSegments) {
					segment.adaptSynapses(prevActiveCellPositions, segmentDecrement * -1F, 0F);
				}
			}
		}
	}
	
	protected void resetLocalState() {
		predictiveCellPositions.clear();
		prevActiveCellPositions.clear();
		prevWinnerCellPositions.clear();
		activeCellPositions.clear();
		winnerCellPositions.clear();

		activeApicalCellPositions.clear();
		prevActiveApicalCellPositions.clear();
	}
}
