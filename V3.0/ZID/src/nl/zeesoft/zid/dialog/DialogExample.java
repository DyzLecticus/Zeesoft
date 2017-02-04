package nl.zeesoft.zid.dialog;

import nl.zeesoft.zdk.ZStringSymbolParser;

public class DialogExample {
	private ZStringSymbolParser	input	= null; 
	private ZStringSymbolParser	output	= null;
	
	protected DialogExample(ZStringSymbolParser input, ZStringSymbolParser output) {
		this.input = input;
		this.output = output;
	}
	
	public ZStringSymbolParser getInput() {
		return input;
	}

	public ZStringSymbolParser getOutput() {
		return output;
	}
}
