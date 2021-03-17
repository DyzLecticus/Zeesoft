package nl.zeesoft.zdk.neural.network;

import nl.zeesoft.zdk.neural.ScalarSdrEncoder;
import nl.zeesoft.zdk.neural.processor.Processor;
import nl.zeesoft.zdk.neural.processor.se.ScalarEncoder;

public class ScalarEncoderConfig extends ProcessorConfig {
	public ScalarSdrEncoder		encoder		= new ScalarSdrEncoder();

	public ScalarEncoderConfig(int layer, String name) {
		super(layer, name);
	}
	
	@Override
	public Processor getNewInstance() {
		ScalarEncoder r = new ScalarEncoder();
		r.encoder = encoder.copy();
		return r;
	}
}
