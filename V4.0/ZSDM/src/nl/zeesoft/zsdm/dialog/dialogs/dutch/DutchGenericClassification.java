package nl.zeesoft.zsdm.dialog.dialogs.dutch;

import nl.zeesoft.znlb.context.ContextConfig;

public class DutchGenericClassification extends DutchGeneric {
	private static final String	EXAMPLE_LANGUAGE_OUTPUT_1	= "Ik begrijp niet welke taal u spreekt.";
	private static final String	EXAMPLE_LANGUAGE_OUTPUT_2	= "Ik begrijp niet wat u zegt.";
	private static final String	EXAMPLE_CONTEXT_OUTPUT_1	= "Ik begrijp niet wat u bedoelt.";
	private static final String	EXAMPLE_CONTEXT_OUTPUT_2	= "Ik begrijp niet wat u zegt.";
	
	public DutchGenericClassification() {
		setContext(ContextConfig.CONTEXT_GENERIC_CLASSIFICATION);
	}
	
	@Override
	public void initialize() {
		addExample(TRIGGER_INPUT_LANGUAGE,getLanguageOutput1());
		addExample(TRIGGER_INPUT_LANGUAGE,getLanguageOutput2());
		addExample(TRIGGER_INPUT_CONTEXT,getContextOutput1());
		addExample(TRIGGER_INPUT_CONTEXT,getContextOutput2());
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
