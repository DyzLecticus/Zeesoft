package nl.zeesoft.zid.session;

import nl.zeesoft.zdk.ZStringSymbolParser;

/**
 * Session dialog controllers are used to transfer dialog variable updates into program calls.
 * 
 * Use setCompleted(true) to finish the current dialog.
 */
public abstract class SessionDialogController {
	private boolean	completed	= false;

	public abstract ZStringSymbolParser updatedSessionDialogVariables(Session session);

	public boolean isCompleted() {
		return completed;
	}

	protected void setCompleted(boolean completed) {
		this.completed = completed;
	}
}
