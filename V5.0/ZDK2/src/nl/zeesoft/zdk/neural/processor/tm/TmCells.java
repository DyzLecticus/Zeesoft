package nl.zeesoft.zdk.neural.processor.tm;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.neural.model.Cell;
import nl.zeesoft.zdk.neural.model.CellSegments;
import nl.zeesoft.zdk.neural.model.Cells;

public class TmCells extends Cells {
	public List<Position>	activeCellPositions				= new ArrayList<Position>();
	public List<Position>	winnerCellPositions				= new ArrayList<Position>();
	public List<Position>	prevActiveCellPositions			= new ArrayList<Position>();
	public List<Position>	prevWinnerCellPositions			= new ArrayList<Position>();
	public List<Position>	predictiveCellPositions			= new ArrayList<Position>();
	
	public List<Position>	activeApicalCellPositions		= new ArrayList<Position>();
	public List<Position>	prevActiveApicalCellPositions	= new ArrayList<Position>();

	public TmCells(Object caller, TmConfig config) {
		super(caller, config);
	}
	
	public void cycleState(List<Position> newActiveApicalCellPositions) {
		prevActiveCellPositions = new ArrayList<Position>(activeCellPositions);
		prevWinnerCellPositions = new ArrayList<Position>(winnerCellPositions);
		activeCellPositions.clear();
		winnerCellPositions.clear();
		
		prevActiveApicalCellPositions = new ArrayList<Position>(activeApicalCellPositions);
		activeApicalCellPositions = newActiveApicalCellPositions;
		
		Rand.randomizeList(predictiveCellPositions);
	}
	
	public boolean activateColumn(Position column) {
		boolean r = false;
		boolean predicted = false;
		for (Position pos: predictiveCellPositions) {
			if (pos.x==column.x && pos.y==column.y) {
				activatePredictedCell(pos);
				predicted = true;
				break;
			}
		}
		if (!predicted) {
			r = true;
			burstColumn(column);
		}
		return r;
	}
	
	public void adaptColumn(Position column, boolean bursting) {
		if (column.columnContains(activeCellPositions)) {
			rewardPredictedColumn(column, bursting);
		} else if (column.columnContains(predictiveCellPositions)) {
			punishPredictedColumn(column);
		}
	}
	
	public void predictActiveCells(Object caller) {
		predictiveCellPositions.clear();
		applyFunction(caller, getPredictActiveCellsFunction());
	}
	
	protected Function getPredictActiveCellsFunction() {
		Function function = new Function() {
			@Override
			protected Object exec() {
				Cell cell = (Cell) param2;
				if (cell.distalSegments.size()>0) {
					if (activeCellPositions.size()>0) {
						cell.calculateSegmentActivity(activeCellPositions, activeApicalCellPositions);
						cell.classifySegmentActivity();
						if (cell.isPredictive(activeApicalCellPositions)) {
							predictiveCellPositions.add(cell.position);
						}
					} else {
						cell.reset();
					}
				}
				return param2;
			}
		};
		return function;
	}
	
	protected void rewardPredictedColumn(Position column, boolean bursting) {
		if (prevWinnerCellPositions.size()>0) {
			for (int z = 0; z < config.size.z; z++) {
				Cell cell = getCell(new Position(column.x, column.y, z));
				if (bursting) {
					if (winnerCellPositions.contains(cell.position)) {
						if (cell.distalSegments.matchingSegment!=null) {
							cell.adaptMatchingSegments(
								prevActiveCellPositions, prevWinnerCellPositions, prevActiveApicalCellPositions
							);
						} else {
							cell.createSegments(
								prevActiveCellPositions, prevWinnerCellPositions, prevActiveApicalCellPositions
							);
						}
					}
				} else {
					cell.adaptActiveSegments(
						prevActiveCellPositions, prevWinnerCellPositions, prevActiveApicalCellPositions
					);
				}
			}
		}
	}
	
	protected void punishPredictedColumn(Position column) {
		punishPredictedColumn(column, CellSegments.DISTAL, prevActiveCellPositions, config.distalSegmentDecrement);
		punishPredictedColumn(column, CellSegments.APICAL, prevActiveApicalCellPositions, config.apicalSegmentDecrement);
	}
	
	protected void activatePredictedCell(Position position) {
		activeCellPositions.add(position);
		winnerCellPositions.add(position);
	}
	
	protected void burstColumn(Position column) {
		List<Position> columns = getColumnPositions(column);
		activateColumnPositions(columns);
		
		Cell winnerCell = null;
		
		// Find potential match
		SortedMap<Integer,List<Cell>> cellsByPotential = getCellsByPotential(columns); 
		if (cellsByPotential.size()>0) {
			List<Cell> potentialCells = cellsByPotential.get(cellsByPotential.lastKey());
			winnerCell = (Cell) Rand.selectRandomFromList(potentialCells);
		} else {
			// Get least connected cell
			SortedMap<Integer,List<Cell>> cellsBySegments = getCellsBySegments(columns);
			List<Cell> leastConnectedCells = cellsBySegments.get(cellsBySegments.firstKey());
			winnerCell = (Cell) Rand.selectRandomFromList(leastConnectedCells);
		}
		
		winnerCellPositions.add(winnerCell.position);
	}
	
	protected void activateColumnPositions(List<Position> columns) {
		for (Position pos: columns) {
			activeCellPositions.add(pos);
		}
	}
	
	protected List<Position> getColumnPositions(Position column) {
		List<Position> r = new ArrayList<Position>();
		for (int z = 0; z < config.size.z; z++) {
			r.add(new Position(column.x, column.y, z));
		}
		return r;
	}
}
