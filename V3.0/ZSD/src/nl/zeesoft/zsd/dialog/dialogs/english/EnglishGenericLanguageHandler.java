package nl.zeesoft.zsd.dialog.dialogs.english;

import nl.zeesoft.zsd.dialog.dialogs.GenericLanguageHandler;

public class EnglishGenericLanguageHandler extends GenericLanguageHandler {
	@Override
	protected String getAnd() {
		return "and";
	}

	@Override
	protected String getYes() {
		return "Yes";
	}

	@Override
	protected String getNo() {
		return "No";
	}
}
