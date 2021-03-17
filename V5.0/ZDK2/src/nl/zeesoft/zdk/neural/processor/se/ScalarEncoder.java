package nl.zeesoft.zdk.neural.processor.se;

import nl.zeesoft.zdk.neural.processor.InputOutputConfig;
import nl.zeesoft.zdk.neural.processor.Processor;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;

public class ScalarEncoder extends Processor {
	public static final int		SENSOR_VALUE_INPUT		= 0;
	public static final int		ENCODED_SENSOR_OUTPUT	= 0;
	
	public ScConfig				encoder					= new ScConfig();
	
	@Override
	public InputOutputConfig getInputOutputConfig() {
		InputOutputConfig r = super.getInputOutputConfig();
		if (encoder!=null) {
			r = encoder.getInputOutputConfig();
		}
		return r;
	}

	@Override
	protected void processValidIO(ProcessorIO io) {
		io.outputs.add(encoder.getEncodedValue(io.inputValue));
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
}
