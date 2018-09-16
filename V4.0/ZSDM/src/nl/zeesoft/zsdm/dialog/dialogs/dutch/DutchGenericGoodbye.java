package nl.zeesoft.zsdm.dialog.dialogs.dutch;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.znlb.context.ContextConfig;

public class DutchGenericGoodbye extends DutchGeneric {
	private static final String	EXAMPLE_OUTPUT_1	= "Dag.";
	private static final String	EXAMPLE_OUTPUT_2	= "Tot ziens.";
	private static final String	EXAMPLE_OUTPUT_3	= "Tot de volgende keer.";
	
	public DutchGenericGoodbye() {
		setContext(ContextConfig.CONTEXT_GENERIC_GOODBYE);
	}
	
	@Override
	public void initialize() {
		for (String answer: getAnswers()) {
			addExample("Dag.",answer);
			addExample("Daag.",answer);
			addExample("Doei.",answer);
			addExample("Tot ziens.",answer);
			addExample("Tot de volgende keer.",answer);
			addExample("Later.",answer);
			addExample("Laters.",answer);
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
