package nl.zeesoft.zsd.dialog.dialogs.english;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericProfanity;

public class EnglishGenericProfanity extends GenericProfanity {
	private static final String EXAMPLE_OUTPUT_1 = "I do not appreciate that kind of language.";
	private static final String EXAMPLE_OUTPUT_2 = "I will pretend I did not read that.";
	
	public EnglishGenericProfanity() {
		setLanguage(BaseConfiguration.LANG_ENG);
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
