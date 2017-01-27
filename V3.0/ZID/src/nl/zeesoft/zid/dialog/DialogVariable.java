package nl.zeesoft.zid.dialog;

import java.util.ArrayList;
import java.util.List;

public class DialogVariable {
	private String						name		= "";
	private String						type		= "";
	private List<DialogVariableExample>	examples	= new ArrayList<DialogVariableExample>();
	
	protected DialogVariable(String name, String type) {
		this.name = name;
		this.type = type;
	}

	protected void addExample(String input, String output) {
		DialogVariableExample example = new DialogVariableExample(new StringBuilder(input),new StringBuilder(output));
		examples.add(example);
	}
	
	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public List<DialogVariableExample> getExamples() {
		return new ArrayList<DialogVariableExample>(examples);
	}
}
