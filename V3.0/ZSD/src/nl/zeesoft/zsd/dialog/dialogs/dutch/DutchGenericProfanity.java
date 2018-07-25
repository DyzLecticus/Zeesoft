package nl.zeesoft.zsd.dialog.dialogs.dutch;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericProfanity;
import nl.zeesoft.zsd.entity.EntityObject;
import nl.zeesoft.zsd.sequence.Analyzer;

public class DutchGenericProfanity extends GenericProfanity {
	private static final String EXAMPLE_OUTPUT_1 = "Van zulk taalgebruik ben ik niet gediend.";
	private static final String EXAMPLE_OUTPUT_2 = "Ik doe alsof ik dat niet heb gelezen.";
	
	public DutchGenericProfanity() {
		setLanguage(BaseConfiguration.LANG_NLD);
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		EntityObject eo = t.getEntityObject(getLanguage(),BaseConfiguration.TYPE_PROFANITY);
		for (String input: eo.getExternalValues().keySet()) {
			addExample(Analyzer.upperCaseFirst(input) + "!",getOutput1());
			addExample(Analyzer.upperCaseFirst(input) + ".",getOutput2());
			addExample(input + ".",getOutput1());
			addExample(input + "!",getOutput2());
		}
	}
	
	protected String getOutput1() {
		return EXAMPLE_OUTPUT_1;
	}
	
	protected String getOutput2() {
		return EXAMPLE_OUTPUT_2;
	}
}
