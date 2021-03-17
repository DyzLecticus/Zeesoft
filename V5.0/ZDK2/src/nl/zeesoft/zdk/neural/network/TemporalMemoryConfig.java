package nl.zeesoft.zdk.neural.network;

import nl.zeesoft.zdk.neural.processor.Processor;
import nl.zeesoft.zdk.neural.processor.tm.TemporalMemory;
import nl.zeesoft.zdk.neural.processor.tm.TmConfig;

public class TemporalMemoryConfig extends ProcessorConfig {
	public TmConfig		config	= new TmConfig();

	public TemporalMemoryConfig(int layer, String name) {
		super(layer, name);
	}
	
	@Override
	public Processor getNewInstance() {
		TemporalMemory r = new TemporalMemory();
		r.initialize(config);
		return null;
	}
}
