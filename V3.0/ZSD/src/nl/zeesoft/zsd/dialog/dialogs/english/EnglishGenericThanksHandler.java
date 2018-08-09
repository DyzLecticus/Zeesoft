package nl.zeesoft.zsd.dialog.dialogs.english;

import nl.zeesoft.zsd.dialog.dialogs.GenericThanksHandler;

public class EnglishGenericThanksHandler extends GenericThanksHandler {
	@Override
	protected String getHelpfulResponse() {
 		return "Excellent!";
	}
	@Override
	protected String getNotHelpfulResponse() {
 		return "I'm sorry. Thank you for the learning experience.";
	}
}
