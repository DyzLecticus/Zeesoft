package nl.zeesoft.zsd.dialog.dialogs;

public abstract class GenericThanks extends Generic {
	public static final String	CONTEXT_GENERIC_THANKS		= "Thanks";

	public static final String	VARIABLE_THANKS_ELSE		= "thanksAnythingElse";
	public static final String	VARIABLE_THANKS_HELPFUL		= "thanksHelpful";
	
	public GenericThanks() {
		setContext(CONTEXT_GENERIC_THANKS);
		setHandlerClassName(GenericThanksHandler.class.getName());
	}
}
