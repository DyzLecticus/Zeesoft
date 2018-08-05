package nl.zeesoft.zsd.dialog.dialogs.dutch;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericProfanity;

public class DutchGenericProfanity extends GenericProfanity {
	private static final String EXAMPLE_OUTPUT_1 = "Van zulk taalgebruik ben ik niet gediend.";
	private static final String EXAMPLE_OUTPUT_2 = "Ik doe alsof ik dat niet heb gelezen.";
	
	public DutchGenericProfanity() {
		setLanguage(BaseConfiguration.LANG_NLD);
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		addExample("[PRF]!",getOutput1());
		addExample("[PRF].",getOutput1());
		addExample("[PRF]?",getOutput1());
		addExample("[PRF]!",getOutput2());
		addExample("[PRF].",getOutput2());
		addExample("[PRF]?",getOutput2());
	}
	
	protected String getOutput1() {
		return EXAMPLE_OUTPUT_1;
	}
	
	protected String getOutput2() {
		return EXAMPLE_OUTPUT_2;
	}
}
