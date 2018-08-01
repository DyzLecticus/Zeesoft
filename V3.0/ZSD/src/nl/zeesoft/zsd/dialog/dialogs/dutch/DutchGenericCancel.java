package nl.zeesoft.zsd.dialog.dialogs.dutch;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericCancel;

public class DutchGenericCancel extends GenericCancel {
	public static final String	EXAMPLE_OUTPUT_1	= "Okee.";
	public static final String	EXAMPLE_OUTPUT_2	= "Prima.";
	
	public DutchGenericCancel() {
		setLanguage(BaseConfiguration.LANG_NLD);
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		for (String answer: getAnswers()) {
			addExample("Niets.",answer);
			addExample("Laat maar zitten.",answer);
		}
	}

	protected List<String> getAnswers() {
		List<String> answers = new ArrayList<String>();
		answers.add(getOutput1());
		answers.add(getOutput2());
		return answers;
	}
	
	protected String getOutput1() {
		return EXAMPLE_OUTPUT_1;
	}
	
	protected String getOutput2() {
		return EXAMPLE_OUTPUT_2;
	}
}
