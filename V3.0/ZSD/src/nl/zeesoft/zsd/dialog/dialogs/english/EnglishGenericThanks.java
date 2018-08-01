package nl.zeesoft.zsd.dialog.dialogs.english;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericThanks;
import nl.zeesoft.zsd.sequence.Analyzer;

public class EnglishGenericThanks extends GenericThanks {
	public static final String	EXAMPLE_OUTPUT_1	= "You're welcome. Is there anything else I can do for you?";
	public static final String	EXAMPLE_OUTPUT_2	= "You're welcome. Is there another way I can be of serivice to you?";
	public static final String	EXAMPLE_OUTPUT_3	= "You're welcome. Is there anything else I can help you with?";
	
	public EnglishGenericThanks() {
		setLanguage(BaseConfiguration.LANG_ENG);
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		for (String thanks: getThanks()) {
			for (String answer: getAnswers()) {
				addExample(Analyzer.upperCaseFirst(thanks) + "." + ".",answer);
				addExample("Nice, " + thanks + ".",answer);
				addExample("Excellent, " + thanks + ".",answer);
				addExample("Perfect, " + thanks + ".",answer);
				addExample("Great, " + thanks + ".",answer);
				addExample("Nice! " + Analyzer.upperCaseFirst(thanks) + ".",answer);
				addExample("Excellent! " + Analyzer.upperCaseFirst(thanks) + ".",answer);
				addExample("Perfect! " + Analyzer.upperCaseFirst(thanks) + ".",answer);
				addExample("Great! " + Analyzer.upperCaseFirst(thanks) + ".",answer);
			}
		}
	}
		
	protected List<String> getThanks() {
		List<String> thanks = new ArrayList<String>();
		thanks.add("thanks");
		thanks.add("thank you");
		thanks.add("thanks a lot");
		thanks.add("thank you very much");
		return thanks;
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
