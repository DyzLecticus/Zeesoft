package nl.zeesoft.zdk.neural.processor.tm;

import java.util.List;

import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;

public class TmColumns extends Matrix {
	public TmConfig	config	= null;
	public TmCells	cells	= null;
	
	public TmColumns(Object caller, TmConfig config, TmCells cells) {
		this.config = config;
		this.cells = cells;
		initialize(new Size(config.size.x,config.size.y));
	}
	
	public void activate(Object caller, List<Position> activeColumnPositions) {
		applyFunction(caller, getActivateFunction(activeColumnPositions));
	}
	
	public void adapt(Object caller) {
		applyFunction(caller, getAdaptFunction());
	}
	
	protected Function getActivateFunction(List<Position> activeColumnPositions) {
		Function r = new Function() {
			@Override
			protected Object exec() {
				boolean burst = false;
				Position column = (Position) param1;
				if (activeColumnPositions.contains(column)) {
					burst = cells.activateColumn(column);
				}
				return burst;
			}
		};
		return r;
	}

	protected Function getAdaptFunction() {
		Function r = new Function() {
			@Override
			protected Object exec() {
				cells.adaptColumn((Position) param1, (boolean) param2);
				return param2;
			}
		};
		return r;
	}
}
