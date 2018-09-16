package nl.zeesoft.zsdm.dialog.dialogs.english;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.znlb.context.ContextConfig;

public class EnglishGenericCancel extends EnglishGeneric {
	private static final String	EXAMPLE_OUTPUT_1	= "Okay.";
	private static final String	EXAMPLE_OUTPUT_2	= "Allright.";
	private static final String	EXAMPLE_OUTPUT_3	= "Fine.";
	
	public EnglishGenericCancel() {
		setContext(ContextConfig.CONTEXT_GENERIC_CANCEL);
	}
	
	@Override
	public void initialize() {
		for (String answer: getAnswers()) {
			addExample("Stop.",answer);
			addExample("Abort.",answer);
			addExample("Cancel.",answer);
			addExample("Nothing.",answer);
			addExample("Nevermind.",answer);
			addExample("Leave it.",answer);
			addExample("I do not want to do this.",answer);
		}
	}

	protected List<String> getAnswers() {
		List<String> answers = new ArrayList<String>();
		answers.add(getOutput1());
		answers.add(getOutput2());
		answers.add(getOutput3());
		return answers;
	}
	
	protected String getOutput1() {
		return EXAMPLE_OUTPUT_1;
	}
	
	protected String getOutput2() {
		return EXAMPLE_OUTPUT_2;
	}
	
	protected String getOutput3() {
		return EXAMPLE_OUTPUT_3;
	}
}
