package nl.zeesoft.zsd.dialog.dialogs.english;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericProfanity;
import nl.zeesoft.zsd.entity.EntityObject;
import nl.zeesoft.zsd.sequence.Analyzer;

public class EnglishGenericProfanity extends GenericProfanity {
	private static final String EXAMPLE_OUTPUT_1 = "I do not appreciate that kind of language.";
	private static final String EXAMPLE_OUTPUT_2 = "I will pretend I did not read that.";
	
	public EnglishGenericProfanity() {
		setLanguage(BaseConfiguration.LANG_ENG);
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		EntityObject eo = t.getEntityObject(getLanguage(),BaseConfiguration.TYPE_PROFANITY);
		for (String input: eo.getExternalValues().keySet()) {
			addExample(Analyzer.upperCaseFirst(input) + "!",getOutput1());
			addExample(Analyzer.upperCaseFirst(input) + "!",getOutput2());
			addExample(input + ".",getOutput1());
			addExample(input + ".",getOutput2());
		}
	}
	
	protected String getOutput1() {
		return EXAMPLE_OUTPUT_1;
	}
	
	protected String getOutput2() {
		return EXAMPLE_OUTPUT_2;
	}
}
