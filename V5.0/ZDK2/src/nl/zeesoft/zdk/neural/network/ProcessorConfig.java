package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.neural.processor.Processor;

public abstract class ProcessorConfig {
	public int					layer		= 0;
	public String				name		= "";
	public Processor			processor	= null;
	public List<LinkConfig>		inputLinks	= new ArrayList<LinkConfig>();
	
	protected ProcessorConfig(int layer, String name) {
		this.layer = layer;
		this.name = name;
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
