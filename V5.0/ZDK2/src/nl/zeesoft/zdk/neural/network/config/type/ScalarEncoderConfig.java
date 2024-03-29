package nl.zeesoft.zdk.neural.network.config.type;

import nl.zeesoft.zdk.neural.network.config.ProcessorConfig;
import nl.zeesoft.zdk.neural.processor.InputOutputConfig;
import nl.zeesoft.zdk.neural.processor.Processor;
import nl.zeesoft.zdk.neural.processor.se.SeConfig;
import nl.zeesoft.zdk.neural.processor.se.ScalarEncoder;

public class ScalarEncoderConfig extends ProcessorConfig {
	public SeConfig		encoder		= new SeConfig();

	public ScalarEncoderConfig() {
		
	}
	
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
