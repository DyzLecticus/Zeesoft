package nl.zeesoft.zdk.neural.model;

import nl.zeesoft.zdk.grid.ColumnFunction;
import nl.zeesoft.zdk.grid.Grid;
import nl.zeesoft.zdk.grid.GridColumn;
import nl.zeesoft.zdk.grid.Position;
import nl.zeesoft.zdk.thread.CodeRunnerList;

public class CellGrid extends Grid {
	public CellGrid() {
		
	}
	
	public CellGrid(CellGrid grid) {
		super(grid);
	}
	
	public void initializeCells(CodeRunnerList runnerList) {
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, int posZ, Object value) {
				return new Cell(new Position(column.posX(), column.posY(), posZ));
			}
		};
		applyFunction(function, runnerList);
	}
	
	public void resetConnections(CodeRunnerList runnerList) {
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, int posZ, Object value) {
				Cell cell = (Cell) value;
				cell.clear();
				return value;
			}
		};
		applyFunction(function, runnerList);
	}
	
	public void resetState(CodeRunnerList runnerList) {
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, int posZ, Object value) {
				Cell cell = (Cell) value;
				cell.reset();
				return value;
			}
		};
		applyFunction(function, runnerList);
	}
	
	@Override
	public void initialize(int sizeX, int sizeY, int sizeZ, Object value) {
		if (value==null || value instanceof CodeRunnerList) {
			super.initialize(sizeX, sizeY, sizeZ, value);
			CodeRunnerList runnerList = null;
			if (value!=null) {
				runnerList = (CodeRunnerList) value;
			}
			initializeCells(runnerList);
		}
	}
	
	@Override
	protected Object copyValueFrom(GridColumn column, int posZ) {
		Object value = column.getValue(posZ);
		if (value!=null) {
			value = new Cell((Cell) value);
		}
		return value;
	}
}
