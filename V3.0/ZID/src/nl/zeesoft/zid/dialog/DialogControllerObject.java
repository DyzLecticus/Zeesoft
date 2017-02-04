package nl.zeesoft.zid.dialog;

import nl.zeesoft.zdk.ZStringSymbolParser;

public abstract class DialogControllerObject {
	private ZStringSymbolParser	output					= new ZStringSymbolParser();
	private String				promptForDialogVariable = "";
	private boolean				completed				= false;
	
	public abstract void updatedVariables(DialogHandler handler,Dialog dialog);
	
	protected final ZStringSymbolParser updatedDialogVariables(DialogHandler handler,Dialog dialog) {
		output = new ZStringSymbolParser();
		promptForDialogVariable = "";
		completed = false;
		updatedVariables(handler,dialog);
		return output;
	}

	protected ZStringSymbolParser getOutput() {
		return output;
	}

	protected void setOutput(ZStringSymbolParser output) {
		this.output = output;
	}
	
	protected String getPromptForDialogVariable() {
		return promptForDialogVariable;
	}
	
	protected void setPromptForDialogVariable(String name) {
		this.promptForDialogVariable = name;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	protected final Object getVariable(DialogHandler handler,String name) {
		return handler.getVariable(name);
	}

	protected final void setVariable(DialogHandler handler,String name,Object value) {
		handler.setVariable(name,value);
	}

	protected final String getDialogVariableValue(DialogHandler handler,String name) {
		return handler.getDialogVariable(name);
	}

	protected final String getDialogVariableValueString(DialogHandler handler,DialogVariable variable) {
		return handler.getPatternStringForDialogVariableValue(variable,handler.getDialogVariable(variable.getName()));
	}

	protected final void setDialogVariable(DialogHandler handler,String name,String value) {
		handler.setDialogVariable(name,value);
	}
}
