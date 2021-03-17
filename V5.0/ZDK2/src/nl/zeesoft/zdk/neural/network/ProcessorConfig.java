package nl.zeesoft.zdk.neural.network;

import nl.zeesoft.zdk.neural.processor.ConfigurableIO;
import nl.zeesoft.zdk.neural.processor.Processor;

public abstract class ProcessorConfig extends AbstractNetworkProcessor implements ConfigurableIO {
	protected ProcessorConfig(int layer, String name) {
		this.layer = layer;
		this.name = name;
	}
	
	protected NetworkProcessor getNewNetworkProcessor() {
		return new NetworkProcessor(layer, name, getNewInstance(), inputLinks);
	}
	
	protected abstract Processor getNewInstance();
	
	protected void addLink(String fromName, int fromOutput, int toInput) {
		if (getLinkForInput(toInput)==null) {
			inputLinks.add(new LinkConfig(fromName,fromOutput,toInput));
		}
	}
	
	protected LinkConfig getLinkForInput(int toInput) {
		LinkConfig r = null;
		for (LinkConfig lc: inputLinks) {
			if (lc.toInput==toInput) {
				r = lc;
				break;
			}
		}
		return r;
	}
}
