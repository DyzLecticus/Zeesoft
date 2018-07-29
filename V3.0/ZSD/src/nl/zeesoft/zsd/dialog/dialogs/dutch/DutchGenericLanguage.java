package nl.zeesoft.zsd.dialog.dialogs.dutch;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericLanguage;

public class DutchGenericLanguage extends GenericLanguage {
	public static final String	EXAMPLE_OUTPUT_DEFAULT		= "Ik spreek {languages}.";
	public static final String	EXAMPLE_OUTPUT_CONFIRMATION	= "{confirmation}, ik spreek {languages}.";
	
	public DutchGenericLanguage() {
		setLanguage(BaseConfiguration.LANG_NLD);
		setHandlerClassName(DutchGenericLanguageHandler.class.getName());
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		addExample("Welke talen spreek je?",getOutputDefault());
		addExample("Welke talen spreek jij?",getOutputDefault());
		addExample("Welke talen spreekt u?",getOutputDefault());

		addExample("Hoeveel talen spreek je?",getOutputDefault());
		addExample("Hoeveel talen spreek jij?",getOutputDefault());
		addExample("Hoeveel talen spreekt u?",getOutputDefault());

		addExample("Spreek je {language}?",getOutputConfirmation());
		addExample("Spreek jij {language}?",getOutputConfirmation());
		addExample("Spreekt u {language}?",getOutputConfirmation());

		addVariable(VARIABLE_LANGUAGE,BaseConfiguration.TYPE_LANGUAGE);
	}
	
	protected String getOutputDefault() {
		return EXAMPLE_OUTPUT_DEFAULT;
	}
	
	protected String getOutputConfirmation() {
		return EXAMPLE_OUTPUT_CONFIRMATION;
	}
}
