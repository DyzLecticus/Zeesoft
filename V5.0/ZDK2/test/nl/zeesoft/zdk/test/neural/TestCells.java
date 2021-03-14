package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.model.Cell;
import nl.zeesoft.zdk.neural.model.CellConfig;
import nl.zeesoft.zdk.neural.model.Cells;
import nl.zeesoft.zdk.neural.model.Segment;

public class TestCells {
	private static TestCells	self	= new TestCells();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		CellConfig config = new CellConfig();
		config.size = new Size(4,4,4);
		
		Cells cells = new Cells(self,config);
		
		Position position = new Position(0,0,0);
		Cell cell = cells.getCell(position);
		assert cell != null;
		assert cell.position.equals(position);
		assert cell.config == config;
		
		assert cells.isInitialized();
		cell.config = null;
		assert !cells.isInitialized();
		cell.config = config;
		cells.data[0][0][0] = null;
		assert !cells.isInitialized();
		cells.data[0][0][0] = cell;
		assert cells.isInitialized();
		cells.size = null;
		assert !cells.isInitialized();
		
		cells = new Cells(self,config);
		cell = cells.getCell(position);
		cell.proximalSegments.add(new Segment());
		cell.distalSegments.add(new Segment());
		cell.apicalSegments.add(new Segment());
		cells.reset(self);
		assert cell.proximalSegments.size() == 0; 
		assert cell.distalSegments.size() == 0; 
		assert cell.apicalSegments.size() == 0; 
	}
}
