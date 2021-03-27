package nl.zeesoft.zdk.neural.network;

import nl.zeesoft.zdk.neural.network.config.LinkConfig;
import nl.zeesoft.zdk.neural.network.config.ProcessorConfig;
import nl.zeesoft.zdk.neural.processor.Processor;

public class NetworkProcessor extends AbstractNetworkProcessor {
	public Processor	processor 	= null;
	
	protected NetworkProcessor(ProcessorConfig pc) {
		this.layer = pc.layer;
		this.name = pc.name;
		this.processor = pc.getNewInstance();
		for (LinkConfig link: pc.inputLinks) {
			this.inputLinks.add(link.copy());
		}
	}
}
