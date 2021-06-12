package nl.zeesoft.zdk.neural.network.config;

import nl.zeesoft.zdk.neural.processor.InputOutputConfig;

public class LinkConfig {
	public String		fromName	= "";
	public int			fromOutput	= 0;
	public int			toInput		= 0;
	
	public LinkConfig() {
		
	}
	
	protected LinkConfig(String fromName, int fromOutput, int toInput) {
		this.fromName = fromName;
		this.fromOutput = fromOutput;
		this.toInput = toInput;
	}
	
	public LinkConfig copy() {
		return new LinkConfig(fromName, fromOutput, toInput);
	}
	
	protected String checkLinkIO(InputOutputConfig fromConfig, String toName, InputOutputConfig toConfig) {
		String r = "";
		if (toInput >= toConfig.inputs.size()) {
			r = toName + ": link can not connect to input index " + toInput;
		} else if (fromOutput >= fromConfig.outputs.size()) {
			r = toName + ": link from " + fromName + " can not connect to output index " + fromOutput;
		} else {
			int outVolume = fromConfig.outputs.get(fromOutput).size.volume();
			int maxVolume = toConfig.inputs.get(toInput).maxVolume;
			if (outVolume>maxVolume) {
				r = toName + ": link from " + fromName + "/" + fromOutput +
					" output volume is greater than input volume " +
					" (" + outVolume + " > " + maxVolume + ")";
			}
		}
		return r;
	}
}
