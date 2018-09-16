package nl.zeesoft.zsdm.dialog.dialogs.english;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zevt.type.Types;
import nl.zeesoft.znlb.context.ContextConfig;

public class EnglishGenericThanks extends EnglishGeneric {
	private static final String	EXAMPLE_OUTPUT_THANKS_1	= "Thanks.";
	private static final String	EXAMPLE_OUTPUT_THANKS_2	= "Thank you.";

	private static final String	EXAMPLE_OUTPUT_1		= "You're welcome.";
	private static final String	EXAMPLE_OUTPUT_2		= "You are very welcome.";
	
	public EnglishGenericThanks() {
		setContext(ContextConfig.CONTEXT_GENERIC_THANKS);
		// TODO: setHandlerClassName(DutchGenericThanksHandler.class.getName());
	}
	
	@Override
	public void initialize() {
		addExample("Is there anything else I can do for you? [" + Types.CONFIRMATION + "].","");
		addExample("Is there another way I can be of service to you? [" + Types.CONFIRMATION + "].","");
		addExample("Is there anything else I can help you with? [" + Types.CONFIRMATION + "].","");
		
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
		addVariablePrompt(VARIABLE_THANKS_ELSE,"Is there anything else I can do for you?");
		addVariablePrompt(VARIABLE_THANKS_ELSE,"Is there another way I can be of service to you?");
		addVariablePrompt(VARIABLE_THANKS_ELSE,"Is there anything else I can help you with?");

		addVariable(VARIABLE_THANKS_HELPFUL,Types.CONFIRMATION);
		addVariablePrompt(VARIABLE_THANKS_HELPFUL,"Was I helpful to you?");
		addVariablePrompt(VARIABLE_THANKS_HELPFUL,"Was I able to help you?");
		
		addNextDialogVariable();
		addVariablePrompt(VARIABLE_NAME_NEXT_DIALOG,"What can I do for you?");
		addVariablePrompt(VARIABLE_NAME_NEXT_DIALOG,"How may I help you?");
	}
	
	protected void addComplimentExamples(String append,String output) {
		addExample("Nice" + append,"{smiley}.");
		addExample("Cool" + append,"{smiley}.");
		addExample("Excellent" + append,output);
		addExample("Perfect" + append,output);
		addExample("Great" + append,output);
		addExample("Awesome" + append,output);
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
