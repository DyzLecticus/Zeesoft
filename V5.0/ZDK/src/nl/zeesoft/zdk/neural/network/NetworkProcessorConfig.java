package nl.zeesoft.zdk.neural.network;

import nl.zeesoft.zdk.neural.processors.SDRProcessorConfig;

public class NetworkProcessorConfig {
	public int						layer				= 0;
	public String					name				= "";
	public SDRProcessorConfig		processorConfig		= null;
	public int						threads				= 4;
	
	public NetworkProcessorConfig() {
		
	}
	
	public NetworkProcessorConfig(int layer, String name, SDRProcessorConfig config) {
		this.layer = layer;
		this.name = name;
		this.processorConfig = config;
	}
}
