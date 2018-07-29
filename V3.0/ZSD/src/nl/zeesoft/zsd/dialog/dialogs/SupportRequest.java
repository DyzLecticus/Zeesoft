package nl.zeesoft.zsd.dialog.dialogs;

public abstract class SupportRequest extends Support {
	public static final String	CONTEXT_SUPPORT_REQUEST			= "Request";

	public static final String	VARIABLE_SUPPORT_CONFIRMATION	= "supportConfirmation";

	public SupportRequest() {
		setContext(CONTEXT_SUPPORT_REQUEST);
		setHandlerClassName(RoomBookingHandler.class.getName());
	}
}
