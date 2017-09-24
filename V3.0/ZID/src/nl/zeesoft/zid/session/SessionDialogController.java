package nl.zeesoft.zid.session;

import nl.zeesoft.zspr.pattern.PatternManager;

public abstract class SessionDialogController {
	private PatternManager	patternManager = null;

	public SessionDialogController(PatternManager patternManager) {
		this.patternManager = patternManager;
	}
	
	public abstract void updatedSessionDialogVariables();

	protected PatternManager getPatternManager() {
		return patternManager;
	}
}
