package nl.zeesoft.zdk.test.neural;

import java.util.ArrayList;

import nl.zeesoft.zdk.Console;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.ProcessorIO;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.model.Cell;
import nl.zeesoft.zdk.neural.model.Segment;
import nl.zeesoft.zdk.neural.tm.TemporalMemory;
import nl.zeesoft.zdk.neural.tm.TmCells;
import nl.zeesoft.zdk.neural.tm.TmConfig;

public class TestTemporalMemory {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		TmConfig config = new TmConfig();
		config.size = new Size(10,10,4);
		config.segmentCreationSubsample = 1F;
		config.activationThreshold = 2;
		config.matchingThreshold = 1;
		
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

		TmCells cells = tm.cells;
		tm.cells = null;
		io = new ProcessorIO(new Sdr(100));
		tm.processIO(io);
		assert io.error.length() > 0;
		assert io.error.equals("TemporalMemory cells are not initialized");
		tm.cells = cells;
		
		Cell cell = tm.cells.getCell(new Position(0,0,0));
		cell.distalSegments.add(new Segment());
		tm.reset();
		assert cell.distalSegments.size() == 0;

		tm.cells.activeCellPositions.add(new Position(0,0,0));
		tm.cells.winnerCellPositions.add(new Position(0,0,1));
		tm.cells.activeApicalCellPositions.add(new Position(0,0,2));
		tm.cells.cycleState(new ArrayList<Position>());
		assert tm.cells.activeCellPositions.size() == 0;
		assert tm.cells.winnerCellPositions.size() == 0;
		assert tm.cells.activeApicalCellPositions.size() == 0;
		assert tm.cells.prevActiveCellPositions.size() == 1;
		assert tm.cells.prevWinnerCellPositions.size() == 1;
		assert tm.cells.prevActiveApicalCellPositions.size() == 1;
		
		io = new ProcessorIO(new Sdr(100));
		tm.processIO(io);
		assert io.error.length() == 0;

		io = new ProcessorIO(new Sdr(100), new Sdr(400));
		tm.processIO(io);
		assert io.error.length() == 0;

		tm.reset();
		assert tm.cells.size.volume() == 400;
		ProcessorIO io1 = getIO(0);
		ProcessorIO io2 = getIO(1);
		
		tm.processIO(io1);
		checkIO(io1,8,2,0,2,false);

		tm.processIO(io2);
		checkIO(io2,8,2,0,2,false);
	}
	
	protected static ProcessorIO getIO(int index) {
		int i = index * 2;
		Sdr activeColumnInput = new Sdr(100);
		activeColumnInput.setBit(i, true);
		activeColumnInput.setBit(i+1, true);
		i = index * 3;
		Sdr apicalInput = new Sdr(400);
		apicalInput.setBit(i, true);
		apicalInput.setBit(i+1, true);
		apicalInput.setBit(i+2, true);
		return new ProcessorIO(activeColumnInput, apicalInput);
	}
	
	protected static void checkIO(ProcessorIO io, int expectActive, int expectBursting, int expectPredictive, int expectWinners, boolean log) {
		assert io.outputs.size() == 4;
		Sdr active = io.outputs.get(TemporalMemory.ACTIVE_CELLS_OUTPUT);
		Sdr bursting = io.outputs.get(TemporalMemory.BURSTING_COLUMNS_OUTPUT);
		Sdr predictive = io.outputs.get(TemporalMemory.PREDICTIVE_CELLS_OUTPUT);
		Sdr winners = io.outputs.get(TemporalMemory.WINNER_CELLS_OUTPUT);
		
		if (log) {
			Console.log(active.onBits.size());
			Console.log(bursting.onBits.size());
			Console.log(predictive.onBits.size());
			Console.log(winners.onBits.size());
		}

		assert active.length == 400;
		assert active.onBits.size() == expectActive;
		assert bursting.length == 100;
		assert bursting.onBits.size() == expectBursting;
		assert predictive.length == 400;
		assert predictive.onBits.size() == expectPredictive;
		assert winners.length == 400;
		assert winners.onBits.size() == expectWinners;
	}
}
