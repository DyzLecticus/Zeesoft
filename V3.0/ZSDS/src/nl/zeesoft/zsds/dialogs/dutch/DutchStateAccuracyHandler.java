package nl.zeesoft.zsds.dialogs.dutch;

import nl.zeesoft.zsds.dialogs.StateAccuracyHandler;

public class DutchStateAccuracyHandler extends StateAccuracyHandler {
	@Override
	protected String getExactly() {
		return "precies";
	}
	
	@Override
	protected String getAbout() {
		return "ongeveer";
	}

	@Override
	protected String getBusyResponse() {
		return "Ik kan op dit moment mijn test resultaten overzicht niet verkrijgen.";
	}

	@Override
	protected String getJsonRequestFailedResponse() {
		return "Het is me niet gelukt mijn test resultaten overzicht te verkrijgen.";
	}
}
