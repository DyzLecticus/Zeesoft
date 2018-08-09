package nl.zeesoft.zsd.dialog.dialogs.dutch;

import nl.zeesoft.zsd.dialog.dialogs.GenericThanksHandler;

public class DutchGenericThanksHandler extends GenericThanksHandler {
	@Override
	protected String getHelpfulResponse() {
 		return "Uitstekend!";
	}
	@Override
	protected String getNotHelpfulResponse() {
 		return "Dat spijt me. Bedankt voor de leerzame ervaring.";
	}
}
