package nl.zeesoft.zsd.dialog.dialogs.english;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericGoodbye;

public class EnglishGenericGoodbye extends GenericGoodbye {
	public static final String	EXAMPLE_OUTPUT_1	= "Goodbye.";
	public static final String	EXAMPLE_OUTPUT_2	= "Bye.";
	public static final String	EXAMPLE_OUTPUT_3	= "Talk to you later.";
	
	public EnglishGenericGoodbye() {
		setLanguage(BaseConfiguration.LANG_ENG);
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		for (String answer: getAnswers()) {
			addExample("Goodbye.",answer);
			addExample("Bye.",answer);
			addExample("See you.",answer);
			addExample("See you later.",answer);
			addExample("Talk to you later.",answer);
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
