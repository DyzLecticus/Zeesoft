package nl.zeesoft.zdk.neural.tm;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.neural.model.Cell;
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
	
	protected void activatePredictedCell(Position position) {
		activeCellPositions.add(position);
		winnerCellPositions.add(position);
	}
	
	protected void burstColumn(Position column) {
		List<Position> columnPositions = getColumnPositions(column);
		activateColumnPositions(columnPositions);
		
		Cell winnerCell = null;
		
		// Find potential match
		SortedMap<Integer,List<Cell>> cellsByPotential = new TreeMap<Integer,List<Cell>>();
		for (Position pos: columnPositions) {
			Cell cell = getCell(pos);
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
			winnerCell = (Cell) Rand.selectRandomFromList(potentialCells);
		} else {
			// Find least connected cell
			SortedMap<Integer,List<Cell>> cellsBySegments = new TreeMap<Integer,List<Cell>>();
			for (Position pos: columnPositions) {
				Cell cell = getCell(pos);
				int segments = cell.distalSegments.size() + cell.apicalSegments.size();
				List<Cell> cells = cellsBySegments.get(segments);
				if (cells==null) {
					cells = new ArrayList<Cell>();
					cellsBySegments.put(segments, cells);
				}
				cells.add(cell);
			}
			List<Cell> leastConnectedCells = cellsBySegments.get(cellsBySegments.firstKey());
			winnerCell = (Cell) Rand.selectRandomFromList(leastConnectedCells);
		}
		
		if (winnerCell!=null) {
			winnerCellPositions.add(winnerCell.position);
		}
	}
	
	protected void activateColumnPositions(List<Position> columnPositions) {
		for (Position pos: columnPositions) {
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
