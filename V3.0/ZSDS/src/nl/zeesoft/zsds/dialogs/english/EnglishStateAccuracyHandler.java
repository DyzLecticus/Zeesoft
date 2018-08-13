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
		return "I am currently unable to obtain my test results.";
	}

	@Override
	protected String getJsonRequestFailedResponse() {
		return "I failed to obtain my test results.";
	}

	@Override
	protected String getAccuracyIncreasedResponse() {
		return "My accuracy has increased compared to my base line.";
	}

	@Override
	protected String getAccuracyUnchangedResponse() {
		return "My accuracy has not changed compared to my base line.";
	}

	@Override
	protected String getAccuracyDecreasedResponse() {
		return "My accuracy has decreased compared to my base line.";
	}
}
