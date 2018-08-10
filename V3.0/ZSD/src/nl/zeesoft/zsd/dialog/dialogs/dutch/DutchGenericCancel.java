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
			addExample("Stop.",answer);
			addExample("Niets.",answer);
			addExample("Laat maar zitten.",answer);
			addExample("Ik wil geen actie ondernemen.",answer);
			addExample("Ik wil helemaal geen actie ondernemen.",answer);
			addExample("Dat wil ik helemaal niet.",answer);
			addExample("Dat wil ik niet.",answer);
			addExample("Ik wil dat niet doen.",answer);
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
