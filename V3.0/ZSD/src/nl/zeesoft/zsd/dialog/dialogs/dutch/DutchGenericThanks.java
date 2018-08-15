package nl.zeesoft.zsd.dialog.dialogs.dutch;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericThanks;
import nl.zeesoft.zsd.sequence.Analyzer;

public class DutchGenericThanks extends GenericThanks {
	public static final String	EXAMPLE_OUTPUT_THANKS_1	= "Bedankt.";
	public static final String	EXAMPLE_OUTPUT_THANKS_2	= "Dankuwel.";

	public static final String	EXAMPLE_OUTPUT_1		= "Graag gedaan.";
	public static final String	EXAMPLE_OUTPUT_2		= "Heel graag gedaan.";
	
	public DutchGenericThanks() {
		setLanguage(BaseConfiguration.LANG_NLD);
		setHandlerClassName(DutchGenericThanksHandler.class.getName());
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		addExample("Kan ik iets anders voor u doen? [" + BaseConfiguration.TYPE_CONFIRMATION + "].","");
		addExample("Kan ik u ergens anders mee van dienst zijn? [" + BaseConfiguration.TYPE_CONFIRMATION + "].","");
		addExample("Kan ik u ergens anders mee helpen? [" + BaseConfiguration.TYPE_CONFIRMATION + "].","");
		
		addExample("[" + BaseConfiguration.TYPE_SMILEY + "].","{smiley}.");

		addComplimentExamples(".",EXAMPLE_OUTPUT_THANKS_1);
		addComplimentExamples(".",EXAMPLE_OUTPUT_THANKS_2);
		addComplimentExamples("!",EXAMPLE_OUTPUT_THANKS_1);
		addComplimentExamples("!",EXAMPLE_OUTPUT_THANKS_2);
		
		for (String thanks: getThanks()) {
			for (String answer: getAnswers()) {
				addComplimentExamples(", " + thanks + ".",answer);
				addExample(Analyzer.upperCaseFirst(thanks) + ".",answer);
			}
		}
		
		addVariable(VARIABLE_THANKS_ELSE,BaseConfiguration.TYPE_CONFIRMATION,false);
		addVariablePrompt(VARIABLE_THANKS_ELSE,"Kan ik iets anders voor u doen?");
		addVariablePrompt(VARIABLE_THANKS_ELSE,"Kan ik u ergens anders mee van dienst zijn?");
		addVariablePrompt(VARIABLE_THANKS_ELSE,"Kan ik u ergens anders mee helpen?");

		addVariable(VARIABLE_THANKS_HELPFUL,BaseConfiguration.TYPE_CONFIRMATION);
		addVariablePrompt(VARIABLE_THANKS_HELPFUL,"Was ik behulpzaam?");
		addVariablePrompt(VARIABLE_THANKS_HELPFUL,"Heb ik u kunnen helpen?");
		
		addNextDialogVariable();
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"Wat kan ik voor u doen?");
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"Hoe kan ik u van dienst zijn?");
	}
	
	protected void addComplimentExamples(String append,String output) {
		addExample("Fijn" + append,"{smiley}.");
		addExample("Prima" + append,"{smiley}.");
		addExample("Cool" + append,"{smiley}.");
		addExample("Uitstekend" + append,output);
		addExample("Perfect" + append,output);
		addExample("Geweldig" + append,output);
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
		return answers;
	}
	
	protected String getOutput1() {
		return EXAMPLE_OUTPUT_1;
	}
	
	protected String getOutput2() {
		return EXAMPLE_OUTPUT_2;
	}
}
