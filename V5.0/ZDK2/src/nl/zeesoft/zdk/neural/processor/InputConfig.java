package nl.zeesoft.zdk.neural.processor;

public class InputConfig {
	public String	name		= "";
	public int		maxVolume	= Integer.MAX_VALUE;
	
	public InputConfig(String name, int maxVolume) {
		this.name = name;
		this.maxVolume = maxVolume;
	}
	
	@Override
	public String toString() {
		return "<- " + name + ": " + maxVolume;
	}
}
