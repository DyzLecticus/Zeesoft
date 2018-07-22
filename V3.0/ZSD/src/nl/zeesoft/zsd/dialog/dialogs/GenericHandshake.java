package nl.zeesoft.zsd.dialog.dialogs;

public abstract class GenericHandshake extends Generic {
	public static final String	CONTEXT_GENERIC_HANDSHAKE	= "Handshake";

	public static final String	VARIABLE_FIRSTNAME			= "firstName";
	public static final String	VARIABLE_LASTNAME			= "lastName";
	public static final String	VARIABLE_PREPOSITION		= "preposition";

	public static final String	VARIABLE_FULLNAME			= "fullName";

	public GenericHandshake() {
		setContext(CONTEXT_GENERIC_HANDSHAKE);
		setHandlerClassName(GenericHandshakeHandler.class.getName());
	}
}
