package nl.zeesoft.zdk.blackbox;

public class Blackbox {
	public BoxElementValue[]	inputs		= null;
	public BoxElementValue[]	memory		= null;
	public BoxElementValue[]	outputs		= null;
	
	public BoxRules				inputRules	= new BoxRules();
	public BoxRules				memoryRules	= new BoxRules();
	public BoxRules				outputRules	= new BoxRules();
	
	public Blackbox(BoxConfig config, GeneticCode code) {
		config.initializeBox(this, code);
	}
	
	public void processInput() {
		inputRules.apply(inputs, memory);
		memoryRules.apply(memory, memory);
		outputRules.apply(memory, outputs);
	}
}
