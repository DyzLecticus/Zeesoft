package nl.zeesoft.zdk.neural.tm;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.Processor;
import nl.zeesoft.zdk.neural.ProcessorIO;

public class TemporalMemory extends Processor {
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
		
		List<Position> activeInputPositions = io.inputs.get(0).getOnPositions(columns.size);
		columns.activate(this, activeInputPositions);
		
		addOutput(io, cells.activeCellPositions);
		addOutput(io, columns.getPositionsForValue(this, true));
		addOutput(io, cells.predictiveCellPositions);
		addOutput(io, cells.winnerCellPositions);
	}

	@Override
	protected int getMaxInputVolume(int index) {
		int r = 0;
		if (index==0) {
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
	
	protected List<Position> getNewActiveApicalCellPositions(ProcessorIO io) {
		List<Position> r = new ArrayList<Position>();
		if (io.inputs.size()>1) {
			r = io.inputs.get(1).getOnPositions(config.size);
		}
		return r;
	}
}
