package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.neural.processor.InputOutputConfig;

public class NetworkConfigTester {
	protected StringBuilder test(NetworkConfig config) {
		StringBuilder r = new StringBuilder();
		if (config.inputNames.size()==0) {
			Util.appendLine(r, "A network must have at least one input");
		}
		if (config.processorConfigs.size()==0) {
			Util.appendLine(r, "A network must have at least one processor");
		}
		List<String> unused = testProcessorConfigs(config, r);
		for (String name: unused) {
			Util.appendLine(r, name + ": Input is not used");
		}
		return r;
	}
	
	protected static List<String> testProcessorConfigs(NetworkConfig config, StringBuilder errors) {
		List<String> r = new ArrayList<String>(config.inputNames);
		for (ProcessorConfig toConfig: config.processorConfigs) {
			InputOutputConfig toIOConfig = toConfig.getInputOutputConfig();
			if (toConfig.inputLinks.size()==0) {
				Util.appendLine(errors, toConfig.name + ": Processor has no input link(s)");
			} else {
				for (LinkConfig link: toConfig.inputLinks) {
					ProcessorConfig fromConfig = config.getProcessorConfig(link.fromName);
					if (fromConfig!=null) {
						InputOutputConfig fromIOConfig = fromConfig.getInputOutputConfig();
						String err = link.checkLinkIO(fromIOConfig, toConfig.name, toIOConfig);
						if (err.length()>0) {
							Util.appendLine(errors, err);
						}
					} else {
						r.remove(link.fromName);
					}
				}
			}
		}
		return r;
	}
}
