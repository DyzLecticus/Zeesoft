package nl.zeesoft.zdk.neural.network.config;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.neural.processor.InputOutputConfig;
import nl.zeesoft.zdk.str.StrUtil;

public class NetworkConfigTester {
	public StringBuilder test(NetworkConfig config) {
		StringBuilder r = new StringBuilder();
		if (config.inputNames.size()==0) {
			StrUtil.appendLine(r, "A network must have at least one input");
		}
		if (config.processorConfigs.size()==0) {
			StrUtil.appendLine(r, "A network must have at least one processor");
		}
		List<String> unused = testProcessorConfigs(config, r);
		for (String name: unused) {
			StrUtil.appendLine(r, name + ": Input is not used");
		}
		return r;
	}
	
	protected List<String> testProcessorConfigs(NetworkConfig config, StringBuilder errors) {
		List<String> r = new ArrayList<String>(config.inputNames);
		for (ProcessorConfig toConfig: config.processorConfigs) {
			InputOutputConfig toIOConfig = toConfig.getInputOutputConfig();
			if (toConfig.inputLinks.size()==0) {
				StrUtil.appendLine(errors, toConfig.name + ": Processor has no input link(s)");
			} else {
				testProcessorLinkConfigs(config, toConfig, toIOConfig, errors, r);
			}
		}
		return r;
	}
	
	protected void testProcessorLinkConfigs(
		NetworkConfig config,
		ProcessorConfig toConfig,
		InputOutputConfig toIOConfig,
		StringBuilder errors,
		List<String> unused
		) {
		for (LinkConfig link: toConfig.inputLinks) {
			ProcessorConfig fromConfig = config.getProcessorConfig(link.fromName);
			if (fromConfig!=null) {
				InputOutputConfig fromIOConfig = fromConfig.getInputOutputConfig();
				String err = link.checkLinkIO(fromIOConfig, toConfig.name, toIOConfig);
				if (err.length()>0) {
					StrUtil.appendLine(errors, err);
				}
			} else {
				if (!config.inputNames.contains(link.fromName)) {
					StrUtil.appendLine(errors, toConfig.name + ": link from '" + link.fromName + "' not found");
				}
				unused.remove(link.fromName);
			}
		}
	}
}
