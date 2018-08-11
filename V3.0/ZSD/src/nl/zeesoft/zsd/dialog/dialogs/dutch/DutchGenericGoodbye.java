package nl.zeesoft.zsd.dialog.dialogs.dutch;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericGoodbye;

public class DutchGenericGoodbye extends GenericGoodbye {
	public static final String	EXAMPLE_OUTPUT_1	= "Dag.";
	public static final String	EXAMPLE_OUTPUT_2	= "Tot ziens.";
	public static final String	EXAMPLE_OUTPUT_3	= "Tot de volgende keer.";
	
	public DutchGenericGoodbye() {
		setLanguage(BaseConfiguration.LANG_NLD);
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
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
