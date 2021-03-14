package nl.zeesoft.zdk.neural.model;

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
	
	protected Function getInitializeFunction() {
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
