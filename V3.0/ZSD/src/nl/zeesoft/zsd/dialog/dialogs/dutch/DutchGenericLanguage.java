package nl.zeesoft.zsd.dialog.dialogs.dutch;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericLanguage;
import nl.zeesoft.zsd.entity.EntityObject;

public class DutchGenericLanguage extends GenericLanguage {
	public static final String	EXAMPLE_OUTPUT_DEFAULT		= "Ik spreek {languages}.";
	public static final String	EXAMPLE_OUTPUT_CONFIRMATION	= "{confirmation}, ik spreek {languages}.";
	
	public DutchGenericLanguage() {
		setLanguage(BaseConfiguration.LANG_NLD);
		setHandlerClassName(DutchGenericLanguageHandler.class.getName());
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		EntityObject lang = t.getEntityObject(BaseConfiguration.LANG_NLD,BaseConfiguration.TYPE_LANGUAGE);
		
		addExample("Welke talen spreek je?",getOutputDefault());
		addExample("Welke talen spreek jij?",getOutputDefault());
		addExample("Welke talen spreekt u?",getOutputDefault());

		addExample("Hoeveel talen spreek je?",getOutputDefault());
		addExample("Hoeveel talen spreek jij?",getOutputDefault());
		addExample("Hoeveel talen spreekt u?",getOutputDefault());

		for (String l: lang.getExternalValues().keySet()) {
			addExample("Spreek je " + l + "?",getOutputConfirmation());
			addExample("Spreek jij " + l + "?",getOutputConfirmation());
			addExample("Spreekt u " + l + "?",getOutputConfirmation());
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
