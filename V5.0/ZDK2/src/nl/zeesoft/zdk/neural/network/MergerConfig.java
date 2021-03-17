package nl.zeesoft.zdk.neural.network;

import nl.zeesoft.zdk.neural.processor.InputOutputConfig;
import nl.zeesoft.zdk.neural.processor.Processor;
import nl.zeesoft.zdk.neural.processor.mr.Merger;
import nl.zeesoft.zdk.neural.processor.mr.MrConfig;

public class MergerConfig extends ProcessorConfig {
	public MrConfig		config	= new MrConfig();

	public MergerConfig(int layer, String name) {
		super(layer, name);
	}
	
	@Override
	public Processor getNewInstance() {
		Merger r = new Merger();
		r.initialize(config);
		return r;
	}

	@Override
	public InputOutputConfig getInputOutputConfig() {
		return config.getInputOutputConfig();
	}
}
