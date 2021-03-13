package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.model.Cell;
import nl.zeesoft.zdk.neural.model.CellConfig;
import nl.zeesoft.zdk.neural.model.Cells;

public class TestCells {
	private static TestCells	self	= new TestCells();
	
	public static void main(String[] args) {
		CellConfig config = new CellConfig();
		
		Cells cells = new Cells();
		cells.initialize(new Size(4,4,4));
		cells.initializeCells(self, config);
		
		Position position = new Position(0,0,0);
		Cell cell = cells.getCell(position);
		assert cell != null;
		assert cell.position.equals(position);
		assert cell.config == config;
	}
}
