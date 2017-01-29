package nl.zeesoft.zid.dialog;

import java.util.ArrayList;
import java.util.List;

public class Dialog {
	private String					name				= "";
	private String					controllerClassName	= "";
	private List<DialogExample>		examples			= new ArrayList<DialogExample>();
	private List<DialogVariable>	variables			= new ArrayList<DialogVariable>();
	
	public Dialog(String name,String controllerClassName) {
		this.name = name;
		this.controllerClassName = controllerClassName;
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

	public void addVariableExample(String name,String question, String answer) {
		DialogVariable variable = getVariable(name);
		if (variable!=null) {
			variable.addExample(question, answer);
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
	
	public String getControllerClassName() {
		return controllerClassName;
	}

	public void setControllerClassName(String controllerClassName) {
		this.controllerClassName = controllerClassName;
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
	
	protected DialogControllerObject getNewController()  {
		DialogControllerObject r = null;
		try {
			Class<?> clas = Class.forName(controllerClassName);
			r = (DialogControllerObject) clas.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return r;
	}
	
	protected List<String> getExpectedTypes() {
		List<String> expectedTypes = new ArrayList<String>();
		for (DialogVariable variable: getVariables()) {
			if (!expectedTypes.contains(variable.getType())) {
				expectedTypes.add(variable.getType());
			}
		}
		return expectedTypes;
	}
}
