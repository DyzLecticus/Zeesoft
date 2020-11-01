package nl.zeesoft.zdk.neural.model;

import java.util.List;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.StrAble;
import nl.zeesoft.zdk.grid.ColumnFunction;
import nl.zeesoft.zdk.grid.Grid;
import nl.zeesoft.zdk.grid.GridColumn;
import nl.zeesoft.zdk.grid.Position;
import nl.zeesoft.zdk.neural.processors.ProcessorFactory;
import nl.zeesoft.zdk.thread.CodeRunnerList;

public class CellGrid extends Grid implements StrAble {
	public CellGrid() {
		
	}
	
	public CellGrid(CellGrid grid) {
		super(grid);
	}
	
	public void initializeCells(CodeRunnerList runnerList, int threads) {
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, int posZ, Object value) {
				return new Cell(new Position(column.posX(), column.posY(), posZ));
			}
		};
		applyFunction(function, runnerList, threads);
	}
	
	public void resetConnections(CodeRunnerList runnerList, int threads) {
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, int posZ, Object value) {
				Cell cell = (Cell) value;
				cell.clear();
				return value;
			}
		};
		applyFunction(function, runnerList, threads);
	}
	
	public void resetState(CodeRunnerList runnerList, int threads) {
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, int posZ, Object value) {
				Cell cell = (Cell) value;
				cell.reset();
				return value;
			}
		};
		applyFunction(function, runnerList, threads);
	}
	
	@Override
	public void initialize(int sizeX, int sizeY, int sizeZ, Object value) {
		if (value==null || value instanceof CodeRunnerList) {
			super.initialize(sizeX, sizeY, sizeZ, value);
			CodeRunnerList runnerList = null;
			if (value!=null) {
				runnerList = (CodeRunnerList) value;
			}
			initializeCells(runnerList, ProcessorFactory.THREADS);
		}
	}

	@Override
	public Str toStr() {
		Str r = new Str();
		r.sb().append(getDimensions());
		for (GridColumn column: columns) {
			for (int z = 0; z < sizeZ; z++) {
				Cell cell = (Cell) column.getValue(z);
				if (cell.proximalSegments.size()>0 ||
					cell.distalSegments.size()>0 ||
					cell.apicalSegments.size()>0
					) {
					r.sb().append("\n");
					r.sb().append(cell.position.toStr().sb());
					r.sb().append("=");
					r.sb().append(cell.toStr().sb());
				}
			}
		}
		return r;
	}

	@Override
	public void fromStr(Str str) {
		List<Str> elems = str.split("\n");
		if (elems.size()>1) {
			List<Str> dimensions = elems.get(0).split("*");
			int sX = Integer.parseInt(dimensions.get(0).toString());
			int sY = Integer.parseInt(dimensions.get(1).toString());
			int sZ = Integer.parseInt(dimensions.get(2).toString());
			initialize(sX, sY, sZ);
			for (int i = 1; i < elems.size(); i++) {
				List<Str> posCell = elems.get(i).split("=");
				Position pos = new Position();
				pos.fromStr(posCell.get(0));
				Cell cell = (Cell) getValue(pos.x, pos.y, pos.z);
				cell.fromStr(posCell.get(1));
			}
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
