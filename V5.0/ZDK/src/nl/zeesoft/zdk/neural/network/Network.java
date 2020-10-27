package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.processors.Processor;
import nl.zeesoft.zdk.neural.processors.ProcessorFactory;

public class Network {
	private NetworkConfig		config			= new NetworkConfig();
	private List<Processor>		processors		= new ArrayList<Processor>();
	
	public Network(NetworkConfig config) {
		this.config = config;
	}
	
	public Str initialize(boolean resetConnections) {
		Str err = config.testConfiguration();
		if (err.length()>0) {
			Logger.err(this, err);
		} else {
			for (NetworkProcessorConfig cfg: config.processorConfigs) {
				processors.add(
					ProcessorFactory.getNewProcessor(
						cfg.name, cfg.processorConfig, cfg.threads, resetConnections
					)
				);
			}
		}
		return err;
	}
}
