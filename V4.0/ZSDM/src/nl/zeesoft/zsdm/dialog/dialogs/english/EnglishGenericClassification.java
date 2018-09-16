package nl.zeesoft.zsdm.dialog.dialogs.english;

import nl.zeesoft.znlb.context.ContextConfig;

public class EnglishGenericClassification extends EnglishGeneric {
	private static final String	EXAMPLE_LANGUAGE_OUTPUT_1	= "I do not understand what language you are speaking.";
	private static final String	EXAMPLE_LANGUAGE_OUTPUT_2	= "I do not understand what you are saying.";
	private static final String	EXAMPLE_CONTEXT_OUTPUT_1	= "I do not understand what you mean.";
	private static final String	EXAMPLE_CONTEXT_OUTPUT_2	= "I do not understand what you are saying.";
	
	public EnglishGenericClassification() {
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
