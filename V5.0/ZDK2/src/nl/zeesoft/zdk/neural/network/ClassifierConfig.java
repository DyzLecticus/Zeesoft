package nl.zeesoft.zdk.neural.network;

import nl.zeesoft.zdk.neural.processor.Processor;
import nl.zeesoft.zdk.neural.processor.cl.ClConfig;
import nl.zeesoft.zdk.neural.processor.cl.Classifier;

public class ClassifierConfig extends ProcessorConfig {
	public ClConfig		config	= new ClConfig();

	public ClassifierConfig(int layer, String name) {
		super(layer, name);
	}
	
	@Override
	public Processor getNewInstance() {
		Classifier r = new Classifier();
		r.initialize(config);
		return null;
	}
}
