package nl.zeesoft.zdk.grid;

import nl.zeesoft.zdk.thread.CodeRunnerList;
import nl.zeesoft.zdk.thread.RunCode;

public class HistoricalGrid extends Grid {
	public void update(Grid grid) {
		for (GridColumn other: grid.columns) {
			GridColumn column = getColumn(other.index);
			if (column!=null) {
				column.setValue(other.getValue());
			}
		}
	}
	
	public void update(Grid grid, CodeRunnerList runnerList) {
		if (runnerList!=null) {
			runnerList.add(new RunCode() {
				@Override
				protected boolean run() {
					update(grid);
					return true;
				}
			});
		} else {
			update(grid);
		}
	}
	
	public void cycle() {
		cycle(null);
	}
	
	public void cycle(CodeRunnerList runnerList) {
		if (runnerList!=null) {
			ColumnFunction function = new ColumnFunction() {
				@Override
				public Object[] applyFunction(GridColumn column, Object[] values) {
					for (int z = (values.length - 1); z > 0; z--) {
						int p = z - 1;
						values[z] = values[p];
					}
					return values;
				}
				
			};
			applyFunction(function, runnerList);
		} else {
			for (GridColumn column: columns) {
				for (int z = (sizeZ - 1); z > 0; z--) {
					int p = z - 1;
					column.setValue(z,column.getValue(p));
				}
			}
		}
	}

	@Override
	public void initialize(int sizeX, int sizeY, int sizeZ, Object value) {
		if (sizeZ < 2) {
			sizeZ = 2;
		}
		super.initialize(sizeX, sizeY, sizeZ, value);
	}
}
