package nl.zeesoft.zdk.neural.tm;

import java.util.ArrayList;

import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.AbstractProcessor;
import nl.zeesoft.zdk.neural.ProcessorIO;
import nl.zeesoft.zdk.neural.model.Cells;

public class TemporalMemory extends AbstractProcessor {
	public TmConfig		config		= null;
	public Cells		cells		= null;

	public void initialize(TmConfig config) {
		this.config = config.copy();
		
		cells = new Cells(this, config);
	}
	
	@Override
	public void reset() {
		cells.reset(this);
	}

	@Override
	protected void processValidIO(ProcessorIO io) {
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
}
