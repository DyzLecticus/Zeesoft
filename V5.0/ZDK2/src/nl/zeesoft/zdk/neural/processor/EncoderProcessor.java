package nl.zeesoft.zdk.neural.processor;

import nl.zeesoft.zdk.neural.encoder.AbstractEncoder;

public abstract class EncoderProcessor extends Processor {
	public static final int		SENSOR_VALUE_INPUT		= 0;
	public static final int		ENCODED_SENSOR_OUTPUT	= 0;
	
	public AbstractEncoder		encoder					= null;
	
	@Override
	public InputOutputConfig getInputOutputConfig() {
		InputOutputConfig r = super.getInputOutputConfig();
		if (encoder!=null) {
			r = ((ConfigurableIO)encoder).getInputOutputConfig();
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
		}
		return io.error.length() == 0;
	}
}
