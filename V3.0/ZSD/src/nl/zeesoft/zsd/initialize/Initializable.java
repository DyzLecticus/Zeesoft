package nl.zeesoft.zsd.initialize;

import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;

public interface Initializable {
	/**
	 * Initializes the implementing object.
	 * 
	 * @param data The optional data to use for initialization
	 */
	public void initialize(List<ZStringBuilder> data);
}
