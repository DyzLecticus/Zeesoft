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
		return "Ik kan op dit moment mijn test resultaten niet verkrijgen.";
	}

	@Override
	protected String getJsonRequestFailedResponse() {
		return "Het is me niet gelukt mijn test resultaten te verkrijgen.";
	}

	@Override
	protected String getAccuracyIncreasedResponse() {
		return "Mijn nauwkeurigheid is toegenomen ten opzichte van mijn base line.";
	}

	@Override
	protected String getAccuracyUnchangedResponse() {
		return "Mijn nauwkeurigheid is ongewijzigd ten opzichte van mijn base line.";
	}

	@Override
	protected String getAccuracyDecreasedResponse() {
		return "Mijn nauwkeurigheid is afgenomen ten opzichte van mijn base line.";
	}
}
