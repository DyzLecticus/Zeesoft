package nl.zeesoft.zdk.neural;

import java.util.List;

import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;

public abstract class Processor {
	public static final int		DEFAULT_INPUT	= 0;
	
	public void reset() {
		
	}

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
			int i = 0;
			for (Sdr input: io.inputs) {
				int max = getMaxInputVolume(i);
				if (input.length > max) {
					io.error = this.getClass().getSimpleName() + " input SDR " + (i + 1) + " may not be longer than " + max;
					break;
				}
				i++;
			}
		}
		return io.error.length() == 0;
	}

	protected int getMaxInputVolume(int index) {
		return Integer.MAX_VALUE;
	}
	
	protected abstract void processValidIO(ProcessorIO io);
	
	protected void addOutput(ProcessorIO io, List<Position> activePositions) {
		Size size = getOutputSize(io.outputs.size());
		Sdr output = new Sdr(size.volume());
		output.setOnPositions(size, activePositions);
		io.outputs.add(output);
	}
	
	protected Size getOutputSize(int index) {
		return new Size(1,1,1);
	}
}
