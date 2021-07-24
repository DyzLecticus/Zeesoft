package nl.zeesoft.zdk.neural.network.config.type;

import nl.zeesoft.zdk.neural.network.config.ProcessorConfig;
import nl.zeesoft.zdk.neural.processor.InputOutputConfig;
import nl.zeesoft.zdk.neural.processor.Processor;
import nl.zeesoft.zdk.neural.processor.de.DateTimeEncoder;
import nl.zeesoft.zdk.neural.processor.de.DeConfig;

public class DateTimeEncoderConfig extends ProcessorConfig {
	public DeConfig		encoder		= new DeConfig();

	public DateTimeEncoderConfig() {
		
	}
	
	public DateTimeEncoderConfig(int layer, String name) {
		super(layer, name);
	}
	
	@Override
	public Processor getNewInstance() {
		DateTimeEncoder r = new DateTimeEncoder();
		r.encoder = encoder.copy();
		return r;
	}

	@Override
	public InputOutputConfig getInputOutputConfig() {
		return encoder.getInputOutputConfig();
	}
}
