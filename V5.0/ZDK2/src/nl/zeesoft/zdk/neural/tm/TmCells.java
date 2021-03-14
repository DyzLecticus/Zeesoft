package nl.zeesoft.zdk.neural.tm;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.matrix.Position;
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
		
		Util.randomizeList(predictiveCellPositions);
	}
}
