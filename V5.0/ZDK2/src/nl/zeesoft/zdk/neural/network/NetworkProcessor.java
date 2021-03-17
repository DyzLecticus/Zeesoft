package nl.zeesoft.zdk.neural.network;

import java.util.List;

import nl.zeesoft.zdk.neural.processor.Processor;

public class NetworkProcessor extends AbstractNetworkProcessor {
	protected Processor		processor 	= null;
	
	protected NetworkProcessor(int layer, String name, Processor processor, List<LinkConfig> inputLinks) {
		this.layer = layer;
		this.name = name;
		this.processor = processor;
		for (LinkConfig link: inputLinks) {
			this.inputLinks.add(link.copy());
		}
	}
}
