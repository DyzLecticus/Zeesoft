package nl.zeesoft.zdk.neural.model;

import java.util.List;

import nl.zeesoft.zdk.matrix.Position;

public class Cell {
	public Position			position				= null;
	public CellConfig		config					= null;
	
	public CellSegments		proximalSegments		= null;
	public CellSegments		distalSegments			= null;
	public CellSegments		apicalSegments			= null;
	
	public Cell(Position position, CellConfig config) {
		this.position = position;
		this.config = config;
		proximalSegments = new CellSegments(position, config);
		distalSegments = new CellSegments(position, config);
		apicalSegments = new CellSegments(position, config);
	}
	
	public void clear() {
		proximalSegments.clear();
		distalSegments.clear();
		apicalSegments.clear();
		reset();
	}

	public void reset() {
		proximalSegments.reset();
		distalSegments.reset();
		apicalSegments.reset();
	}

	public CellSegments getSegments(String type) {
		CellSegments r = null;
		if (type.equals(CellSegments.PROXIMAL)) {
			r = proximalSegments;
		} else if (type.equals(CellSegments.DISTAL)) {
			r = distalSegments;
		} else if (type.equals(CellSegments.APICAL)) {
			r = apicalSegments;
		}
		return r;
	}

	public void calculateSegmentActivity(
		List<Position> activeCellPositions,
		List<Position> activeApicalCellPositions
		) {
		distalSegments.calculateSegmentActivity(activeCellPositions);
		apicalSegments.calculateSegmentActivity(activeApicalCellPositions);
	}
	
	public void classifySegmentActivity() {
		distalSegments.classifySegmentActivity();
		apicalSegments.classifySegmentActivity();
	}

	public boolean isPredictive(List<Position> activeApicalCellPositions) {
		boolean r = false;
		if ((activeApicalCellPositions.size()==0 && distalSegments.activeSegments.size()>0) ||
			(activeApicalCellPositions.size()>0 && distalSegments.activeSegments.size()>0 && apicalSegments.activeSegments.size()>0)
			) {
			r = true;
		}
		return r;
	}
	
	public void adaptActiveSegments(
		List<Position> prevActiveCellPositions,
		List<Position> prevWinnerCellPositions,
		List<Position> prevActiveApicalCellPositions
		) {
		distalSegments.adaptActiveSegments(prevActiveCellPositions, prevWinnerCellPositions, config.distalPotentialRadius);
		if (prevActiveApicalCellPositions.size()>0) {
			apicalSegments.adaptActiveSegments(prevActiveApicalCellPositions, prevActiveApicalCellPositions, config.apicalPotentialRadius);
		}
	}
	
	public void adaptMatchingSegments(
		List<Position> prevActiveCellPositions,
		List<Position> prevWinnerCellPositions,
		List<Position> prevActiveApicalCellPositions
		) {
		if (distalSegments.matchingSegment!=null) {
			distalSegments.adaptMatchingSegment(prevActiveCellPositions, prevWinnerCellPositions, config.distalPotentialRadius);
			if (apicalSegments.matchingSegment!=null) {
				apicalSegments.adaptMatchingSegment(prevActiveApicalCellPositions, prevActiveApicalCellPositions, config.apicalPotentialRadius);
			}
		}
	}

	public void createSegments(
		List<Position> prevActiveCellPositions,
		List<Position> prevWinnerCellPositions,
		List<Position> prevActiveApicalCellPositions
		) {
		Segment distalSegment = distalSegments.createSegment(prevWinnerCellPositions, config.distalPotentialRadius);
		if (distalSegment!=null) {
			apicalSegments.createSegment(prevActiveApicalCellPositions, config.apicalPotentialRadius);
		}
	}
}
