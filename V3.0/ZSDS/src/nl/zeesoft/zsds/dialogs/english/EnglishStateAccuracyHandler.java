package nl.zeesoft.zsds.dialogs.english;

import nl.zeesoft.zsds.dialogs.StateAccuracyHandler;

public class EnglishStateAccuracyHandler extends StateAccuracyHandler {
	@Override
	protected String getExactly() {
		return "exactly";
	}
	
	@Override
	protected String getAbout() {
		return "about";
	}

	@Override
	protected String getBusyResponse() {
		return "I am currently unable to obtain my test result summary.";
	}

	@Override
	protected String getJsonRequestFailedResponse() {
		return "I failed to obtain my test result summary.";
	}
}
