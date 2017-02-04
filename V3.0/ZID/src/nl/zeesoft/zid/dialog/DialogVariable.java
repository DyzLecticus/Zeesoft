package nl.zeesoft.zid.dialog;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;

public class DialogVariable {
	private String						name		= "";
	private String						type		= "";
	private List<DialogVariableExample>	examples	= new ArrayList<DialogVariableExample>();
	
	protected DialogVariable(String name, String type) {
		this.name = name;
		this.type = type;
	}

	protected void addExample(String question, String answer) {
		DialogVariableExample example = new DialogVariableExample(new ZStringSymbolParser(question),new ZStringSymbolParser(answer));
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
