package nl.zeesoft.zdk.neural.model;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;

public class Cells extends Matrix {
	private static Size		DEFAULT_SIZE	= new Size(45,45,16);
	
	public CellConfig		config			= null;
	
	protected Cells(Object caller, Matrix matrix) {
		initialize(matrix.size);
		copyDataFrom(caller, matrix);
	}
	
	public Cells(Object caller, CellConfig config) {
		this.config = config;
		initialize(config.size);
		applyFunction(caller,getInitializeFunction());
	}
	
	public static Size getDefaultSize() {
		return DEFAULT_SIZE.copy();
	}
	
	public static int getDefaultOnBits() {
		return (DEFAULT_SIZE.surface().volume() * 2) / 100;
	}
	
	public void setConfig(Object caller, CellConfig config) {
		this.config = config;
		applyFunction(caller,getSetConfigFunction(config));
	}
	
	public Cell getCell(Position position) {
		return (Cell) getValue(position);
	}
	
	public boolean isInitialized() {
		return size!=null && data[0][0][0]!=null && ((Cell)data[0][0][0]).config!=null;
	}

	public void reset(Object caller) {
		applyFunction(caller, getResetFunction());
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
			int potential = cell.getPotential();
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
	
	protected Function getResetFunction() {
		Function r = new Function() {
			@Override
			protected Object exec() {
				((Cell) param2).clear();
				return param2;
			}
		};
		return r;
	}
	
	protected Function getSetConfigFunction(CellConfig config) {
		Function r = new Function() {
			@Override
			protected Object exec() {
				((Cell) param2).setConfig(config);
				return param2;
			}
		};
		return r;
	}
}
