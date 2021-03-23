package nl.zeesoft.zdk.neural.model;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.Position;

public class Cells extends Matrix {
	public CellConfig	config	= null;
	
	public Cells(Object caller, CellConfig config) {
		this.config = config;
		initialize(config.size);
		applyFunction(caller,getInitializeFunction());
	}
	
	public Cell getCell(Position position) {
		return (Cell) getValue(position);
	}
	
	public boolean isInitialized() {
		return size!=null && data[0][0][0]!=null && ((Cell)data[0][0][0]).config!=null;
	}

	public void reset(Object caller) {
		applyFunction(caller, getResetFunction(caller));
	}

	public void punishPredictedColumn(Position columnPosition, String type, List<Position> prevActivePositions, float segmentDecrement) {
		if (segmentDecrement > 0F) {
			for (int z = 0; z < config.size.z; z++) {
				Cell cell = getCell(new Position(columnPosition.x, columnPosition.y, z));
				List<Segment> segments = cell.getSegments(type).matchingSegments;
				for (Segment segment: segments) {
					segment.adaptSynapses(prevActivePositions, segmentDecrement * -1F, 0F);
				}
			}
		}
	}

	public SortedMap<Integer,List<Cell>> getCellsByPotential(List<Position> columnPositions) {
		SortedMap<Integer,List<Cell>> r = new TreeMap<Integer,List<Cell>>();
		for (Position pos: columnPositions) {
			Cell cell = getCell(pos);
			int potential = 0;
			if (cell.distalSegments.matchingSegment!=null) {
				potential = cell.distalSegments.matchingSegment.potentialSynapses.size();
			}
			if (cell.apicalSegments.matchingSegment!=null) {
				potential = potential + cell.apicalSegments.matchingSegment.potentialSynapses.size();
			}
			if (potential>0) {
				List<Cell> cells = r.get(potential);
				if (cells==null) {
					cells = new ArrayList<Cell>();
					r.put(potential, cells);
				}
				cells.add(cell);
			}
		}
		return r;
	}

	public SortedMap<Integer,List<Cell>> getCellsBySegments(List<Position> columnPositions) {
		SortedMap<Integer,List<Cell>> r = new TreeMap<Integer,List<Cell>>();
		for (Position pos: columnPositions) {
			Cell cell = getCell(pos);
			int segments = cell.distalSegments.size() + cell.apicalSegments.size();
			List<Cell> cells = r.get(segments);
			if (cells==null) {
				cells = new ArrayList<Cell>();
				r.put(segments, cells);
			}
			cells.add(cell);
		}
		return r;
	}
	
	protected Function getInitializeFunction() {
		Function r = new Function() {
			@Override
			protected Object exec() {
				return new Cell((Position) param1, config);
			}
		};
		return r;
	}
	
	protected Function getResetFunction(Object myCaller) {
		Function r = new Function() {
			@Override
			protected Object exec() {
				((Cell) param2).clear();
				return param2;
			}
		};
		return r;
	}
}
