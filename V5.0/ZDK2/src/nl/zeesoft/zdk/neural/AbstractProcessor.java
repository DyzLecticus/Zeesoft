package nl.zeesoft.zdk.neural;

import java.util.List;

import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;

public abstract class AbstractProcessor implements Processor {
	public void reset() {
		
	}

	@Override
	public final void processIO(ProcessorIO io) {
		if (isInitialized(io) && isValidIO(io)) {
			processValidIO(io);
		}
	}

	protected boolean isInitialized(ProcessorIO io) {
		return true;
	}
	
	protected boolean isValidIO(ProcessorIO io) {
		if (io.inputs.size()==0) {
			io.error = this.getClass().getSimpleName() + " requires at least one input SDR";
		} else {
			Sdr input = io.inputs.get(0);
			int max = getMaxInputVolume();
			if (input.length > max) {
				io.error = this.getClass().getSimpleName() + " input SDR may not be longer than " + max;
			}
		}
		return io.error.length() == 0;
	}

	protected int getMaxInputVolume() {
		return Integer.MAX_VALUE;
	}
	
	protected abstract void processValidIO(ProcessorIO io);
	
	protected void addOutput(ProcessorIO io, List<Position> activePositions) {
		Size size = getOutputSize();
		Sdr output = new Sdr(size.volume());
		output.setOnPositions(size, activePositions);
		io.outputs.add(output);
	}
	
	protected Size getOutputSize() {
		return new Size(1,1,1);
	}
}
