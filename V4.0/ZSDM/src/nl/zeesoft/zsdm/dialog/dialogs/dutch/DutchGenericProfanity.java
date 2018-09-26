package nl.zeesoft.zsdm.dialog.dialogs.dutch;

import nl.zeesoft.zevt.type.Types;
import nl.zeesoft.znlb.context.ContextConfig;

public class DutchGenericProfanity extends DutchGeneric {
	private static final String	EXAMPLE_OUTPUT_1	= "Van zulk taalgebruik ben ik niet gediend.";
	private static final String	EXAMPLE_OUTPUT_2	= "Ik doe alsof ik dat niet heb gehoord.";
	
	public DutchGenericProfanity() {
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