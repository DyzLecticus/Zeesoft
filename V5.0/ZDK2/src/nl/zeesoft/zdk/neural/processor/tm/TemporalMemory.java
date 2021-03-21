package nl.zeesoft.zdk.neural.processor.tm;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.neural.processor.InputOutputConfig;
import nl.zeesoft.zdk.neural.processor.Processor;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;

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
	public void setLearn(boolean learn) {
		if (config!=null) {
			config.learn = learn;
		}
	}
	
	@Override
	public void reset() {
		if (cells!=null) {
			cells.reset(this);
		}
	}
	
	@Override
	public InputOutputConfig getInputOutputConfig() {
		InputOutputConfig r = super.getInputOutputConfig();
		if (config!=null) {
			r = config.getInputOutputConfig();
		}
		return r;
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
	protected boolean isInitialized(ProcessorIO io) {
		if (config==null) {
			io.error = this.getClass().getSimpleName() + " is not initialized";
		} else if (cells==null || !cells.isInitialized()) {
			io.error = this.getClass().getSimpleName() + " cells are not initialized";
		}
		return io.error.length() == 0;
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
