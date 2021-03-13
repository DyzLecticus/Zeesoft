package nl.zeesoft.zdk.neural.model;

import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.Position;

public class Cells extends Matrix {
	public void initializeCells(Object caller, CellConfig config) {
		applyFunction(caller, getInitializeFunction(config));
	}
	
	public Cell getCell(Position position) {
		return (Cell) getValue(position);
	}
	
	protected Function getInitializeFunction(CellConfig config) {
		Function r = new Function() {
			@Override
			protected Object exec() {
				Cell cell = new Cell();
				cell.position = (Position) param1;
				cell.config = config;
				return cell;
			}
		};
		return r;
	}
}
