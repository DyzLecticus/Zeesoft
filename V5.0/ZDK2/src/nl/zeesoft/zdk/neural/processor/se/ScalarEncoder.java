package nl.zeesoft.zdk.neural.processor.se;

import nl.zeesoft.zdk.neural.processor.EncoderProcessor;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;

public class ScalarEncoder extends EncoderProcessor {
	public ScalarEncoder() {
		encoder = new SeConfig();
	}
	
	@Override
	protected boolean isValidIO(ProcessorIO io) {
		super.isValidIO(io);
		if (io.error.length() == 0 &&	
			!(io.inputValue instanceof Integer) &&
			!(io.inputValue instanceof Float)
			) {
			io.error = this.getClass().getSimpleName() + " requires an integer or float input value";
		}
		return io.error.length() == 0;
	}
}
