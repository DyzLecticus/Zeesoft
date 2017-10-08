package nl.zeesoft.zid.session;

import nl.zeesoft.zdk.ZStringSymbolParser;

public abstract class SessionDialogController {
	private boolean	completed	= false;

	public abstract ZStringSymbolParser updatedSessionDialogVariables(Session session);

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
}
