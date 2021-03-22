package nl.zeesoft.zdk.neural.processor;

import java.util.List;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.Sdr;

public abstract class Processor implements ConfigurableIO {
	public final void processIO(ProcessorIO io) {
		if (isInitialized(io) && isValidIO(io)) {
			processValidIO(io);
		}
	}
	
	@Override
	public InputOutputConfig getInputOutputConfig() {
		return new InputOutputConfig();
	}

	public int getMaxInputVolume(int index) {
		return getInputOutputConfig().inputs.get(index).maxVolume;
	}

	public Size getOutputSize(int index) {
		return getInputOutputConfig().outputs.get(index).size;
	}
	
	@Override
	public String toString() {
		StringBuilder r = new StringBuilder();
		r.append(this.getClass().getSimpleName());
		Util.appendLine(r, getInputOutputConfig().toString());
		return r.toString();
	}
	
	protected boolean isInitialized(ProcessorIO io) {
		return true;
	}
	
	protected boolean isValidIO(ProcessorIO io) {
		Util.removeNullValuesFromList(io.inputs);
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
	
	protected abstract void processValidIO(ProcessorIO io);
	
	protected void addOutput(ProcessorIO io, List<Position> positions) {
		Size size = getInputOutputConfig().outputs.get(io.outputs.size()).size;
		Sdr output = new Sdr(size.volume());
		output.setOnPositions(size, positions);
		io.outputs.add(output);
	}
}
