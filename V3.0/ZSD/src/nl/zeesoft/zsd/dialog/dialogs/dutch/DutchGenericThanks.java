package nl.zeesoft.zsd.dialog.dialogs.dutch;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericThanks;
import nl.zeesoft.zsd.sequence.Analyzer;

public class DutchGenericThanks extends GenericThanks {
	public static final String	EXAMPLE_OUTPUT_1	= "Graag gedaan. Kan ik iets anders voor u doen?";
	public static final String	EXAMPLE_OUTPUT_2	= "Graag gedaan. Kan ik u ergens anders mee van dienst zijn?";
	public static final String	EXAMPLE_OUTPUT_3	= "Graag gedaan. Kan ik u ergens anders mee helpen?";
	
	public DutchGenericThanks() {
		setLanguage(BaseConfiguration.LANG_NLD);
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		for (String thanks: getThanks()) {
			for (String answer: getAnswers()) {
				addExample(Analyzer.upperCaseFirst(thanks) + ".",answer);
				addExample("Fijn, " + thanks + ".",answer);
				addExample("Uitstekend, " + thanks + ".",answer);
				addExample("Perfect, " + thanks + ".",answer);
				addExample("Geweldig, " + thanks + ".",answer);
				addExample("Prima, " + thanks + ".",answer);
				addExample("Fijn! " + Analyzer.upperCaseFirst(thanks) + ".",answer);
				addExample("Uitstekend! " + Analyzer.upperCaseFirst(thanks) + ".",answer);
				addExample("Perfect! " + Analyzer.upperCaseFirst(thanks) + ".",answer);
				addExample("Geweldig! " + Analyzer.upperCaseFirst(thanks) + ".",answer);
				addExample("Prima! " + Analyzer.upperCaseFirst(thanks) + ".",answer);
			}
		}
	}
		
	protected List<String> getThanks() {
		List<String> thanks = new ArrayList<String>();
		thanks.add("dankjewel");
		thanks.add("dank je wel");
		thanks.add("dankuwel");
		thanks.add("dank u wel");
		thanks.add("bedankt");
		thanks.add("mijn dank is groot");
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
