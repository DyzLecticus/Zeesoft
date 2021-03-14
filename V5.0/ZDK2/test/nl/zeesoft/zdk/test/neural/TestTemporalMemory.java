package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.ProcessorIO;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.model.Cell;
import nl.zeesoft.zdk.neural.model.Cells;
import nl.zeesoft.zdk.neural.model.Segment;
import nl.zeesoft.zdk.neural.tm.TemporalMemory;
import nl.zeesoft.zdk.neural.tm.TmConfig;

public class TestTemporalMemory {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		TmConfig config = new TmConfig();
		config.size = new Size(10,10,4);
		TemporalMemory tm = new TemporalMemory();
		
		ProcessorIO io = new ProcessorIO();
		tm.processIO(io);
		assert io.error.length() > 0;
		assert io.error.equals("TemporalMemory is not initialized");

		tm.initialize(config);
		assert tm.cells.size.volume() == 400;

		tm.cells.size = null;
		io = new ProcessorIO(new Sdr(100));
		tm.processIO(io);
		assert io.error.length() > 0;
		assert io.error.equals("TemporalMemory cells are not initialized");
		tm.cells.size = config.size;

		Cells cells = tm.cells;
		tm.cells = null;
		io = new ProcessorIO(new Sdr(100));
		tm.processIO(io);
		assert io.error.length() > 0;
		assert io.error.equals("TemporalMemory cells are not initialized");
		tm.cells = cells;

		io = new ProcessorIO(new Sdr(100));
		tm.processIO(io);
		assert io.error.length() == 0;
		
		Cell cell = tm.cells.getCell(new Position(0,0,0));
		cell.distalSegments.add(new Segment());
		tm.reset();
		assert cell.distalSegments.size() == 0;
	}
}
