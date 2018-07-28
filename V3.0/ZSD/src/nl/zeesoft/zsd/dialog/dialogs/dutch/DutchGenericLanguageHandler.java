package nl.zeesoft.zsd.dialog.dialogs.dutch;

import nl.zeesoft.zsd.dialog.dialogs.GenericLanguageHandler;

public class DutchGenericLanguageHandler extends GenericLanguageHandler {
	@Override
	protected String getAnd() {
		return "en";
	}
	
	@Override
	protected String getYes() {
		return "Ja";
	}

	@Override
	protected String getNo() {
		return "Nee";
	}
}
