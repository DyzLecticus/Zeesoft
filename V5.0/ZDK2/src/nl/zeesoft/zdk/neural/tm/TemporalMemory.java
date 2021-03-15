package nl.zeesoft.zdk.neural.tm;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.Processor;
import nl.zeesoft.zdk.neural.ProcessorIO;

public class TemporalMemory extends Processor {
	public static final int		ACTIVE_COLUMNS_INPUT		= 0;
	public static final int		ACTIVE_APICAL_INPUT			= 1;
	
	public static final int		ACTIVE_CELLS_OUTPUT			= 0;
	public static final int		BURSTING_COLUMNS_OUTPUT		= 1;
	public static final int		PREDICTIVE_CELLS_OUTPUT		= 2;
	public static final int		WINNER_CELLS_OUTPUT			= 3;

	public TmConfig				config						= null;
	public TmColumns			columns						= null;
	public TmCells				cells						= null;

	public void initialize(TmConfig config) {
		this.config = config.copy();

		cells = new TmCells(this, this.config);
		columns = new TmColumns(this, this.config, cells);
	}
	
	@Override
	public void reset() {
		cells.reset(this);
	}
	
	@Override
	protected void processValidIO(ProcessorIO io) {
		cells.cycleState(getNewActiveApicalCellPositions(io));
		columns.activate(this, getNewActiveColumnPositions(io));
		if (config.learn) {
			columns.adapt(this);
		}
		cells.predictActiveCells(this);
		addOutput(io, cells.activeCellPositions);
		addOutput(io, columns.getPositionsForValue(this, true));
		addOutput(io, cells.predictiveCellPositions);
		addOutput(io, cells.winnerCellPositions);
	}

	@Override
	protected int getMaxInputVolume(int index) {
		int r = 0;
		if (index==ACTIVE_COLUMNS_INPUT) {
			r = (config.size.x * config.size.y);
		} else {
			r = config.size.volume();
		}
		return r;
	}
	
	@Override
	protected Size getOutputSize(int index) {
		Size r = config.size;
		if (index==BURSTING_COLUMNS_OUTPUT) {
			r = new Size(config.size.x,config.size.y);
		}
		return r;
	}
	
	@Override
	protected boolean isInitialized(ProcessorIO io) {
		if (config==null) {
			io.error = this.getClass().getSimpleName() + " is not initialized";
		} else if (cells==null || !cells.isInitialized()) {
			io.error = this.getClass().getSimpleName() + " cells are not initialized";
		}
		return io.error.length() == 0;
	}

	@Override
	protected int getNumberOfInputs() {
		return 2;
	}

	@Override
	protected String getInputName(int index) {
		String r = "ActiveApicalCells";
		if (index == ACTIVE_COLUMNS_INPUT) {
			r = "ActiveColumns";
		}
		return r;
	}

	@Override
	public int getNumberOfOutputs() {
		return 4;
	}

	@Override
	protected String getOutputName(int index) {
		String r = "WinnerCells";
		if (index == ACTIVE_CELLS_OUTPUT) {
			r = "ActiveCells";
		} else if (index == BURSTING_COLUMNS_OUTPUT) {
			r = "BurstingColumns";
		} else if (index == PREDICTIVE_CELLS_OUTPUT) {
			r = "PredictiveCells";
		}
		return r;
	}
	
	protected List<Position> getNewActiveApicalCellPositions(ProcessorIO io) {
		List<Position> r = new ArrayList<Position>();
		if (io.inputs.size()>ACTIVE_APICAL_INPUT) {
			r = io.inputs.get(ACTIVE_APICAL_INPUT).getOnPositions(cells.size);
		}
		return r;
	}
	
	protected List<Position> getNewActiveColumnPositions(ProcessorIO io) {
		return io.inputs.get(ACTIVE_COLUMNS_INPUT).getOnPositions(columns.size);
	}
}
