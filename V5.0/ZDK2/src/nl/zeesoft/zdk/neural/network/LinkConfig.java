package nl.zeesoft.zdk.neural.network;

import nl.zeesoft.zdk.neural.processor.InputOutputConfig;

public class LinkConfig {
	public String		fromName	= "";
	public int			fromOutput	= 0;
	public int			toInput		= 0;
	
	protected LinkConfig(String fromName, int fromOutput, int toInput) {
		this.fromName = fromName;
		this.fromOutput = fromOutput;
		this.toInput = toInput;
	}
	
	protected String checkLinkIO(InputOutputConfig fromConfig, String toName, InputOutputConfig toConfig) {
		String err = "";
		if (toInput >= toConfig.inputs.size()) {
			err = toName + ": link can not connect to input index " + toInput;
		} else if (fromOutput >= fromConfig.outputs.size()) {
			err = toName + ": link from " + fromName + " can not connect to output index " + fromOutput;
		} else {
			int outVolume = fromConfig.outputs.get(fromOutput).size.volume();
			int maxVolume = toConfig.inputs.get(toInput).maxVolume;
			if (outVolume>maxVolume) {
				err = toName + ": link from " + fromName + "/" + fromOutput +
					" output volume is greater than input volume " +
					" (" + outVolume + " > " + maxVolume + ")";
			}
		}
		return err;
	}
}
