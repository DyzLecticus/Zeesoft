package nl.zeesoft.zdk.neural.tm;

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
	
	public void activate(Object caller, List<Position> activeInputPositions) {
		applyFunction(caller, getActivateFunction(caller, activeInputPositions));
	}
	
	protected Function getActivateFunction(Object myCaller, List<Position> activeInputPositions) {
		Function r = new Function() {
			@Override
			protected Object exec() {
				boolean burst = false;
				Position column = (Position) param1;
				if (column.isIn(activeInputPositions)) {
					boolean predicted = false;
					for (Position pos: cells.predictiveCellPositions) {
						if (pos.x==column.x && pos.y==column.y) {
							cells.activatePredictedCell(pos);
							predicted = true;
							break;
						}
					}
					if (!predicted) {
						burst = true;
						cells.burstColumn(column);
					}
				}
				return burst;
			}
		};
		return r;
	}
}
