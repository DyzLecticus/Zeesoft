package nl.zeesoft.zdk.neural.network;

import nl.zeesoft.zdk.neural.processor.InputOutputConfig;
import nl.zeesoft.zdk.neural.processor.Processor;
import nl.zeesoft.zdk.neural.processor.se.ScConfig;
import nl.zeesoft.zdk.neural.processor.se.ScalarEncoder;

public class ScalarEncoderConfig extends ProcessorConfig {
	public ScConfig		encoder		= new ScConfig();

	public ScalarEncoderConfig(int layer, String name) {
		super(layer, name);
	}
	
	@Override
	public Processor getNewInstance() {
		ScalarEncoder r = new ScalarEncoder();
		r.encoder = encoder.copy();
		return r;
	}

	@Override
	public InputOutputConfig getInputOutputConfig() {
		return encoder.getInputOutputConfig();
	}
}
