package nl.zeesoft.zdk.neural.processor.se;

import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.processor.Processor;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;

public class ScalarEncoder extends Processor {
	public static final int		ENCODED_SENSOR_OUTPUT	= 0;
	
	public ScalarSdrEncoder		encoder					= new ScalarSdrEncoder();

	@Override
	protected void processValidIO(ProcessorIO io) {
		io.outputs.add(encoder.getEncodedValue(io.inputValue));
	}
	
	@Override
	protected Size getOutputSize(int index) {
		return new Size(encoder.encodeLength,1);
	}
	
	@Override
	protected boolean isValidIO(ProcessorIO io) {
		if (io.inputValue==null) {
			io.error = this.getClass().getSimpleName() + " requires an input value";
		} else if (
			!(io.inputValue instanceof Integer) &&
			!(io.inputValue instanceof Float)
			) {
			io.error = this.getClass().getSimpleName() + " requires an integer or float value";
		}
		return io.error.length() == 0;
	}

	@Override
	protected String getInputName(int index) {
		return "SensorValue";
	}

	@Override
	protected String getOutputName(int index) {
		return "EncodedSensor";
	}
}
