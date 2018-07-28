package nl.zeesoft.zsd.dialog.dialogs.english;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericLanguage;
import nl.zeesoft.zsd.entity.EntityObject;

public class EnglishGenericLanguage extends GenericLanguage {
	public static final String	EXAMPLE_OUTPUT_DEFAULT		= "I speak {languages}.";
	public static final String	EXAMPLE_OUTPUT_CONFIRMATION	= "{confirmation}, I speak {languages}.";
	
	public EnglishGenericLanguage() {
		setLanguage(BaseConfiguration.LANG_ENG);
		setHandlerClassName(EnglishGenericLanguageHandler.class.getName());
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		EntityObject lang = t.getEntityObject(BaseConfiguration.LANG_ENG,BaseConfiguration.TYPE_LANGUAGE);
		
		addExample("Which languages can you speak?",getOutputDefault());
		addExample("What languages do you know?",getOutputDefault());

		addExample("How many languages can you speak?",getOutputDefault());
		addExample("How many languages do you know?",getOutputDefault());

		for (String l: lang.getExternalValues().keySet()) {
			addExample("Do you speak " + l + "?",getOutputConfirmation());
			addExample("Can you speak " + l + "?",getOutputConfirmation());
		}

		addVariable(VARIABLE_LANGUAGE,BaseConfiguration.TYPE_LANGUAGE);
	}
	
	protected String getOutputDefault() {
		return EXAMPLE_OUTPUT_DEFAULT;
	}
	
	protected String getOutputConfirmation() {
		return EXAMPLE_OUTPUT_CONFIRMATION;
	}
}
