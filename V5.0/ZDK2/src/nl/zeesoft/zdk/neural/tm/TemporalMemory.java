package nl.zeesoft.zdk.neural.tm;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.AbstractProcessor;
import nl.zeesoft.zdk.neural.ProcessorIO;

public class TemporalMemory extends AbstractProcessor {
	public TmConfig		config		= null;
	public TmColumns	columns		= null;
	public TmCells		cells		= null;

	public void initialize(TmConfig config) {
		this.config = config.copy();

		cells = new TmCells(this, config);
		columns = new TmColumns(this, config, cells);
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
		
		addOutput(io, new ArrayList<Position>());
	}

	@Override
	protected int getMaxInputVolume() {
		return (config.size.x * config.size.y);
	}
	
	@Override
	protected Size getOutputSize() {
		return config.size;
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
