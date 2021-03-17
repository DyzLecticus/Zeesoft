package nl.zeesoft.zdk.neural.network;

public class LinkConfig {
	public String		fromName	= "";
	public int			fromOutput	= 0;
	public int			toInput		= 0;
	
	public LinkConfig(String fromName, int fromOutput, int toInput) {
		this.fromName = fromName;
		this.fromOutput = fromOutput;
		this.toInput = toInput;
	}
}
