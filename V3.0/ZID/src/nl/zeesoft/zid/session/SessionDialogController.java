package nl.zeesoft.zid.session;

import nl.zeesoft.zdk.ZStringSymbolParser;

public abstract class SessionDialogController {
	private String	promptForDialogVariable	= "";

	public abstract ZStringSymbolParser updatedSessionDialogVariables(Session session);

	public String getPromptForDialogVariable() {
		return promptForDialogVariable;
	}
	
	public void setPromptForDialogVariable(String promptForDialogVariable) {
		this.promptForDialogVariable = promptForDialogVariable;
	}
}
