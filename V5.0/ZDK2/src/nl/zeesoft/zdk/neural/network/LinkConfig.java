package nl.zeesoft.zdk.neural.network;

import nl.zeesoft.zdk.neural.processor.Processor;

public class LinkConfig {
	public String		fromName	= "";
	public int			fromOutput	= 0;
	public int			toInput		= 0;
	
	protected LinkConfig(String fromName, int fromOutput, int toInput) {
		this.fromName = fromName;
		this.fromOutput = fromOutput;
		this.toInput = toInput;
	}
	
	protected String checkLinkIO(Processor fromProcessor, String toName, Processor toProcessor) {
		String err = "";
		if (fromOutput >= fromProcessor.getOutputNames().size()) {
			err = toName + ": link from " + fromName + " does not provide output index " + fromOutput;
		} else {
			int outVolume = fromProcessor.getOutputSize(fromOutput).volume();
			int maxVolume = toProcessor.getMaxInputVolume(toInput);
			if (outVolume>maxVolume) {
				err = toName + ": link from " + fromName + "/" + fromOutput +
					" output volume is greater than input volume " +
					" (" + outVolume + " > " + maxVolume + ")";
			}
		}
		return err;
	}
}
