package nl.zeesoft.zsd.dialog.dialogs.english;

import nl.zeesoft.zsd.dialog.dialogs.SupportRequestHandler;

public class EnglishSupportRequestHandler extends SupportRequestHandler {
	@Override
	protected String getConnectResponse() {
 		return "You are beeing connected to a human. Thank you for the conversation.";
	}
	@Override
	protected String getOkayResponse() {
 		return "Okay.";
	}
}
