package nl.zeesoft.zsd.dialog.dialogs;

public abstract class GenericLanguage extends Generic {
	public static final String	CONTEXT_GENERIC_LANGUAGE	= "Language";

	public static final String	VARIABLE_LANGUAGE			= "language";

	public static final String	VARIABLE_CONFIRMATION		= "confirmation";
	public static final String	VARIABLE_LANGUAGES			= "languages";

	public GenericLanguage() {
		setContext(CONTEXT_GENERIC_LANGUAGE);
		setHandlerClassName(GenericLanguageHandler.class.getName());
	}
}
