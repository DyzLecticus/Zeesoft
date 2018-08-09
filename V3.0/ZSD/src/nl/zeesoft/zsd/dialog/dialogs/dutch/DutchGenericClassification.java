package nl.zeesoft.zsd.dialog.dialogs.dutch;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericClassification;

public class DutchGenericClassification extends GenericClassification {
	public static final String	EXAMPLE_LANGUAGE_OUTPUT_1	= "Ik begrijp niet welke taal u spreekt.";
	public static final String	EXAMPLE_LANGUAGE_OUTPUT_2	= "Ik begrijp niet wat u zegt.";
	public static final String	EXAMPLE_CONTEXT_OUTPUT_1	= "Ik begrijp niet wat u bedoelt.";
	public static final String	EXAMPLE_CONTEXT_OUTPUT_2	= "Ik begrijp niet wat u zegt.";
	
	public DutchGenericClassification() {
		setLanguage(BaseConfiguration.LANG_NLD);
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		addExample(GenericClassification.TRIGGER_INPUT_LANGUAGE,getLanguageOutput1());
		addExample(GenericClassification.TRIGGER_INPUT_LANGUAGE,getLanguageOutput2());
		addExample(GenericClassification.TRIGGER_INPUT_CONTEXT,getContextOutput1());
		addExample(GenericClassification.TRIGGER_INPUT_CONTEXT,getContextOutput2());
	}
	
	protected String getLanguageOutput1() {
		return EXAMPLE_LANGUAGE_OUTPUT_1;
	}
	
	protected String getLanguageOutput2() {
		return EXAMPLE_LANGUAGE_OUTPUT_2;
	}
	
	protected String getContextOutput1() {
		return EXAMPLE_CONTEXT_OUTPUT_1;
	}
	
	protected String getContextOutput2() {
		return EXAMPLE_CONTEXT_OUTPUT_2;
	}
}
