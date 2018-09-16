package nl.zeesoft.zsdm.dialog.dialogs.dutch;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zevt.type.Types;
import nl.zeesoft.znlb.context.ContextConfig;

public class DutchGenericThanks extends DutchGeneric {
	private static final String	EXAMPLE_OUTPUT_THANKS_1	= "Bedankt.";
	private static final String	EXAMPLE_OUTPUT_THANKS_2	= "Dankuwel.";

	private static final String	EXAMPLE_OUTPUT_1		= "Graag gedaan.";
	private static final String	EXAMPLE_OUTPUT_2		= "Heel graag gedaan.";
	
	public DutchGenericThanks() {
		setContext(ContextConfig.CONTEXT_GENERIC_THANKS);
		// TODO: setHandlerClassName(DutchGenericThanksHandler.class.getName());
	}
	
	@Override
	public void initialize() {
		addExample("Kan ik iets anders voor u doen? [" + Types.CONFIRMATION + "].","");
		addExample("Kan ik u ergens anders mee van dienst zijn? [" + Types.CONFIRMATION + "].","");
		addExample("Kan ik u ergens anders mee helpen? [" + Types.CONFIRMATION + "].","");
		
		addExample("[" + Types.SMILEY + "].","{smiley}.");

		addComplimentExamples(".",EXAMPLE_OUTPUT_THANKS_1);
		addComplimentExamples(".",EXAMPLE_OUTPUT_THANKS_2);
		addComplimentExamples("!",EXAMPLE_OUTPUT_THANKS_1);
		addComplimentExamples("!",EXAMPLE_OUTPUT_THANKS_2);
		
		for (String thanks: getThanks()) {
			for (String answer: getAnswers()) {
				addComplimentExamples(", " + thanks + ".",answer);
				addExample(ZStringBuilder.upperCaseFirst(thanks) + ".",answer);
			}
		}
		
		addVariable(VARIABLE_THANKS_ELSE,Types.CONFIRMATION,false);
		addVariablePrompt(VARIABLE_THANKS_ELSE,"Kan ik iets anders voor u doen?");
		addVariablePrompt(VARIABLE_THANKS_ELSE,"Kan ik u ergens anders mee van dienst zijn?");
		addVariablePrompt(VARIABLE_THANKS_ELSE,"Kan ik u ergens anders mee helpen?");

		addVariable(VARIABLE_THANKS_HELPFUL,Types.CONFIRMATION);
		addVariablePrompt(VARIABLE_THANKS_HELPFUL,"Was ik behulpzaam?");
		addVariablePrompt(VARIABLE_THANKS_HELPFUL,"Heb ik u kunnen helpen?");
		
		addNextDialogVariable();
		addVariablePrompt(VARIABLE_NAME_NEXT_DIALOG,"Wat kan ik voor u doen?");
		addVariablePrompt(VARIABLE_NAME_NEXT_DIALOG,"Hoe kan ik u van dienst zijn?");
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
