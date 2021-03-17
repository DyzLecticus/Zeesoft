package nl.zeesoft.zdk.neural.processor;

import nl.zeesoft.zdk.matrix.Size;

public class OutputConfig {
	public String	name	= "";
	public Size		size	= new Size(1,1,1);
	
	public OutputConfig(String name, Size size) {
		this.name = name;
		this.size = size;
	}
	
	@Override
	public String toString() {
		return "-> " + name + ": " + size.volume();
	}
}
