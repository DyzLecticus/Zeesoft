package nl.zeesoft.zdk.test.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Console;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.ProcessorIO;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.model.Cell;
import nl.zeesoft.zdk.neural.model.CellStats;
import nl.zeesoft.zdk.neural.model.Segment;
import nl.zeesoft.zdk.neural.model.Synapse;
import nl.zeesoft.zdk.neural.tm.TemporalMemory;
import nl.zeesoft.zdk.neural.tm.TmCells;
import nl.zeesoft.zdk.neural.tm.TmConfig;

public class TestTemporalMemory {
	private static TestTemporalMemory	self	= new TestTemporalMemory();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		TmConfig config = new TmConfig();
		config.size = new Size(10,10,4);
		config.segmentCreationSubsample = 1F;
		config.activationThreshold = 2;
		config.matchingThreshold = 1;
		
		TemporalMemory tm = new TemporalMemory();
		assert tm.getInputNames().size() == 2;
		assert tm.getInputNames().get(0).equals("ActiveColumns");
		assert tm.getInputNames().get(1).equals("ActiveApicalCells");
		assert tm.getOutputNames().size() == 4;
		assert tm.getOutputNames().get(0).equals("ActiveCells");
		assert tm.getOutputNames().get(1).equals("BurstingColumns");
		assert tm.getOutputNames().get(2).equals("PredictiveCells");
		assert tm.getOutputNames().get(3).equals("WinnerCells");
		
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
		CellStats stats = new CellStats(tm);
		assert stats.distalSegments == 0;
		assert stats.apicalSegments == 0;
		
		tm.processIO(io2);
		checkIO(io2,8,2,0,2,false);
		stats = new CellStats(tm);
		assert stats.distalSegments == 2;
		assert stats.apicalSegments == 2;

		io1.outputs.clear();
		tm.processIO(io1);
		checkIO(io1,8,2,0,2,false);
		io1.outputs.clear();
		stats = new CellStats(tm);
		assert stats.distalSegments == 4;
		assert stats.apicalSegments == 4;
		
		io2.outputs.clear();
		tm.processIO(io2);
		checkIO(io2,8,2,0,2,false);
		stats = new CellStats(tm);
		assert stats.distalSegments == 4;
		assert stats.apicalSegments == 4;
		int distal = stats.distalSynapses;
		int apical = stats.apicalSynapses;
		
		tm.config.learn = false;
		io1.outputs.clear();
		tm.processIO(io1);
		checkIO(io1,8,2,0,2,false);
		assert stats.distalSegments == 4;
		assert stats.apicalSegments == 4;
		assert stats.distalSynapses == distal;
		assert stats.apicalSynapses == apical;
		
		tm.cells.predictiveCellPositions.add(cell.position);
		boolean burst = tm.cells.activateColumn(new Position(0,0));
		assert !burst;
		assert tm.cells.activeCellPositions.contains(cell.position);
		assert tm.cells.winnerCellPositions.contains(cell.position);
		burst = tm.cells.activateColumn(new Position(0,1));
		assert burst;
		burst = tm.cells.activateColumn(new Position(1,0));
		assert burst;

		tm.cells.activeCellPositions.clear();
		tm.cells.winnerCellPositions.clear();
		List<Segment> segments = new ArrayList<Segment>(); 
		Function function = new Function() {
			@Override
			protected Object exec() {
				Cell cell = (Cell) param2;
				segments.addAll(cell.distalSegments);
				cell.matchingDistalSegments.clear();
				cell.matchingDistalSegments.addAll(cell.distalSegments);
				return param2;
			}
		};
		tm.cells.applyFunction(self, function);
		Synapse synapse = segments.get(0).synapses.get(0);
		assert synapse.permanence == 0.21F;
		tm.cells.adaptColumn(new Position(0,0),false);
		assert synapse.permanence == 0.00999999F;
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
			Console.log("Expected: " +
				expectActive + ", " +
				expectBursting + ", " +
				expectPredictive + ", " +
				expectActive
			);
			Console.log("Received: " +
				active.onBits.size() + ", " +
				bursting.onBits.size() + ", " +
				predictive.onBits.size() + ", " +
				active.onBits.size());
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
