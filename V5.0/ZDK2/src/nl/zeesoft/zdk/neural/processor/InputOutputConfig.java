package nl.zeesoft.zdk.neural.processor;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.matrix.Size;

public class InputOutputConfig {
	public List<InputConfig>	inputs		= new ArrayList<InputConfig>();
	public List<OutputConfig>	outputs		= new ArrayList<OutputConfig>();
	
	public void addInput(String name, int maxVolume) {
		inputs.add(new InputConfig(name, maxVolume));
	}
	
	public void addOutput(String name, Size size) {
		outputs.add(new OutputConfig(name, size));
	}
	
	@Override
	public String toString() {
		StringBuilder r = new StringBuilder();
		for (InputConfig input: inputs) {
			Util.appendLine(r, input.toString());
		}
		for (OutputConfig output: outputs) {
			Util.appendLine(r, output.toString());
		}
		return r.toString();
	}
}
