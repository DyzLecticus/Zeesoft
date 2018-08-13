package nl.zeesoft.zsd.dialog.dialogs.english;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericThanks;
import nl.zeesoft.zsd.sequence.Analyzer;

public class EnglishGenericThanks extends GenericThanks {
	public static final String	EXAMPLE_OUTPUT_THANKS_1	= "Thanks.";
	public static final String	EXAMPLE_OUTPUT_THANKS_2	= "Thank you.";

	public static final String	EXAMPLE_OUTPUT_1		= "You're welcome.";
	public static final String	EXAMPLE_OUTPUT_2		= "You are very welcome.";
	
	public EnglishGenericThanks() {
		setLanguage(BaseConfiguration.LANG_ENG);
		setHandlerClassName(EnglishGenericThanksHandler.class.getName());
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		addExample("Is there anything else I can do for you? [" + BaseConfiguration.TYPE_CONFIRMATION + "].","");
		addExample("Is there another way I can be of service to you? [" + BaseConfiguration.TYPE_CONFIRMATION + "].","");
		addExample("Is there anything else I can help you with? [" + BaseConfiguration.TYPE_CONFIRMATION + "].","");
		
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
		addVariablePrompt(VARIABLE_THANKS_ELSE,"Is there anything else I can do for you?");
		addVariablePrompt(VARIABLE_THANKS_ELSE,"Is there another way I can be of service to you?");
		addVariablePrompt(VARIABLE_THANKS_ELSE,"Is there anything else I can help you with?");

		addVariable(VARIABLE_THANKS_HELPFUL,BaseConfiguration.TYPE_CONFIRMATION);
		addVariablePrompt(VARIABLE_THANKS_HELPFUL,"Was I helpful to you?");
		addVariablePrompt(VARIABLE_THANKS_HELPFUL,"Was I able to help you?");
		
		addNextDialogVariable();
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"What can I do for you?");
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"How may I help you?");
	}
	
	protected void addComplimentExamples(String append,String output) {
		addExample("Nice" + append,output);
		addExample("Excellent" + append,output);
		addExample("Perfect" + append,output);
		addExample("Great" + append,output);
		addExample("Awesome" + append,output);
		addExample("Cool" + append,output);
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
		return answers;
	}
	
	protected String getOutput1() {
		return EXAMPLE_OUTPUT_1;
	}
	
	protected String getOutput2() {
		return EXAMPLE_OUTPUT_2;
	}
}
