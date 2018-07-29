package nl.zeesoft.zsd.dialog.dialogs.dutch;

import nl.zeesoft.zsd.dialog.dialogs.SupportRequestHandler;

public class DutchSupportRequestHandler extends SupportRequestHandler {
	@Override
	protected String getConnectResponse() {
 		return "U wordt doorverbonden met een mens. Bedankt voor het gesprek.";
	}
	@Override
	protected String getOkayResponse() {
 		return "Okee.";
	}
}
