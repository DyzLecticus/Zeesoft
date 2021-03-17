package nl.zeesoft.zdk.neural.network.config;

import nl.zeesoft.zdk.neural.network.ProcessorConfig;
import nl.zeesoft.zdk.neural.processor.InputOutputConfig;
import nl.zeesoft.zdk.neural.processor.Processor;
import nl.zeesoft.zdk.neural.processor.sp.SpConfig;
import nl.zeesoft.zdk.neural.processor.sp.SpatialPooler;

public class SpatialPoolerConfig extends ProcessorConfig {
	public SpConfig		config	= new SpConfig();

	public SpatialPoolerConfig(int layer, String name) {
		super(layer, name);
	}

	@Override
	public Processor getNewInstance() {
		SpatialPooler r = new SpatialPooler();
		r.initialize(config);
		return r;
	}

	@Override
	public InputOutputConfig getInputOutputConfig() {
		return config.getInputOutputConfig();
	}
}
