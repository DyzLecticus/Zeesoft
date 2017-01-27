package nl.zeesoft.zid.dialog;

import java.util.ArrayList;
import java.util.List;

public class Dialog {
	private DialogControllerObject	controller 	= null;
	private String					name		= "";
	private List<DialogExample>		examples	= new ArrayList<DialogExample>();
	private List<DialogVariable>	variables	= new ArrayList<DialogVariable>();
	
	public Dialog(String name) {
		this.name = name;
	}
	
	public void addExample(String input, String output) {
		DialogExample example = new DialogExample(new StringBuilder(input),new StringBuilder(output));
		examples.add(example);
	}

	public void addVariable(String name,String type) {
		if (getVariable(name)==null) {
			DialogVariable variable = new DialogVariable(name,type);
			variables.add(variable);
		}
	}

	public void addVariableExample(String name,String input, String output) {
		DialogVariable variable = getVariable(name);
		if (variable!=null) {
			variable.addExample(input, output);
		}
	}

	public DialogVariable getVariable(String name) {
		DialogVariable r = null;
		for (DialogVariable variable: variables) {
			if (variable.getName().equals(name)) {
				r = variable;
				break;
			}
		}
		return r;
	}
	
	public DialogControllerObject getController() {
		return controller;
	}

	public void setController(DialogControllerObject controller) {
		this.controller = controller;
	}

	public List<DialogExample> getExamples() {
		return new ArrayList<DialogExample>(examples);
	}

	public List<DialogVariable> getVariables() {
		return new ArrayList<DialogVariable>(variables);
	}

	public String getName() {
		return name;
	}
}
