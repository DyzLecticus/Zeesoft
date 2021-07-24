package nl.zeesoft.zdk.neural.processor.de;

import java.util.Date;

import nl.zeesoft.zdk.neural.processor.EncoderProcessor;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;

public class DateTimeEncoder extends EncoderProcessor {
	public DateTimeEncoder() {
		encoder = new DeConfig();
	}
	
	@Override
	protected boolean isValidIO(ProcessorIO io) {
		super.isValidIO(io);
		if (io.error.length() == 0 &&	
			!(io.inputValue instanceof Long) &&
			!(io.inputValue instanceof Date)
			) {
			io.error = this.getClass().getSimpleName() + " requires a long or date input value";
		}
		return io.error.length() == 0;
	}
}
