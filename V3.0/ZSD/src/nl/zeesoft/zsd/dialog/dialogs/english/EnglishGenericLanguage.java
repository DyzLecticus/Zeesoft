package nl.zeesoft.zsd.dialog.dialogs.english;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericLanguage;

public class EnglishGenericLanguage extends GenericLanguage {
	public static final String	EXAMPLE_OUTPUT_DEFAULT		= "I speak {languages}.";
	public static final String	EXAMPLE_OUTPUT_CONFIRMATION	= "{confirmation}, I speak {languages}.";
	
	public EnglishGenericLanguage() {
		setLanguage(BaseConfiguration.LANG_ENG);
		setHandlerClassName(EnglishGenericLanguageHandler.class.getName());
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		addExample("Which languages can you speak?",getOutputDefault());
		addExample("What languages do you know?",getOutputDefault());

		addExample("How many languages can you speak?",getOutputDefault());
		addExample("How many languages do you know?",getOutputDefault());

		addExample("Do you speak [" + BaseConfiguration.TYPE_LANGUAGE + "]?",getOutputConfirmation());
		addExample("Can you speak [" + BaseConfiguration.TYPE_LANGUAGE + "]?",getOutputConfirmation());

		addVariable(VARIABLE_LANGUAGE,BaseConfiguration.TYPE_LANGUAGE);
	}
	
	protected String getOutputDefault() {
		return EXAMPLE_OUTPUT_DEFAULT;
	}
	
	protected String getOutputConfirmation() {
		return EXAMPLE_OUTPUT_CONFIRMATION;
	}
}
