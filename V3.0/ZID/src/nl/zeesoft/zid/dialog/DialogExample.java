package nl.zeesoft.zid.dialog;

public class DialogExample {
	private StringBuilder	input	= null; 
	private StringBuilder	output	= null;
	
	protected DialogExample(StringBuilder input, StringBuilder output) {
		this.input = input;
		this.output = output;
	}
	
	public StringBuilder getInput() {
		return input;
	}

	public StringBuilder getOutput() {
		return output;
	}
}
