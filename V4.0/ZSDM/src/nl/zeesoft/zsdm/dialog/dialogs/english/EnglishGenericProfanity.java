package nl.zeesoft.zsdm.dialog.dialogs.english;

import nl.zeesoft.zevt.type.Types;
import nl.zeesoft.znlb.context.ContextConfig;

public class EnglishGenericProfanity extends EnglishGeneric {
	private static final String	EXAMPLE_OUTPUT_1	= "I do not appreciate that kind of language.";
	private static final String	EXAMPLE_OUTPUT_2	= "I will pretend I did not hear that.";
	
	public EnglishGenericProfanity() {
		setContext(ContextConfig.CONTEXT_GENERIC_PROFANITY);
	}
	
	@Override
	public void initialize() {
		addExample("[" + Types.PROFANITY + "].",EXAMPLE_OUTPUT_1);
		addExample("[" + Types.PROFANITY + "].",EXAMPLE_OUTPUT_2);
		addExample("[" + Types.PROFANITY + "]!",EXAMPLE_OUTPUT_1);
		addExample("[" + Types.PROFANITY + "]!",EXAMPLE_OUTPUT_2);
		addExample("[" + Types.PROFANITY + "]?",EXAMPLE_OUTPUT_1);
		addExample("[" + Types.PROFANITY + "]?",EXAMPLE_OUTPUT_2);
	}
	
	protected String getOutput1() {
		return EXAMPLE_OUTPUT_1;
	}
	
	protected String getOutput2() {
		return EXAMPLE_OUTPUT_2;
	}
}
