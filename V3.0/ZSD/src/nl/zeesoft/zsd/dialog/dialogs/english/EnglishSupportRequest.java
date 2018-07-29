package nl.zeesoft.zsd.dialog.dialogs.english;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.SupportRequest;

public class EnglishSupportRequest extends SupportRequest {
	private static final String		EXAMPLE_OUTPUT_LEARNING		= "I am still learning.";
	private static final String		EXAMPLE_OUTPUT_NO_WORRIES	= "Don't worry.";
	
	public EnglishSupportRequest() {
		setLanguage(BaseConfiguration.LANG_ENG);
		setHandlerClassName(EnglishSupportRequestHandler.class.getName());
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		addExample("I want to talk to a human","");
		addExample("I want to speak to a human.","");
		addExample("I want to be put through to a human.","");
		addExample("I want to be connected to a human.","");
		addExample("Can you put me throug to a human?","");
		addExample("Can you connect me to a human?","");
		
		addExample("You do not understand me.",getOutputLearning());
		addExample("You misunderstand me.",getOutputLearning());
		addExample("You do not understand anything.",getOutputLearning());
		addExample("You are unable to help me.",getOutputLearning());
		addExample("You can not help me.",getOutputLearning());
		
		addExample("Help.",getOutputNoWorries());
		addExample("Help!",getOutputNoWorries());

		addVariable(VARIABLE_SUPPORT_CONFIRMATION,BaseConfiguration.TYPE_CONFIRMATION);
		addVariablePrompt(VARIABLE_SUPPORT_CONFIRMATION,"Shall I connect you to a human?");
		addVariablePrompt(VARIABLE_SUPPORT_CONFIRMATION,"Do you want to be connected to a human?");
		
		addVariable(VARIABLE_NEXT_DIALOG,BaseConfiguration.TYPE_ALPHABETIC);
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"Is there something else I can try to help you with?");
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"Is there another way I can be of service to you?");
	}
	
	protected String getOutputLearning() {
		return EXAMPLE_OUTPUT_LEARNING;
	}
	
	protected String getOutputNoWorries() {
		return EXAMPLE_OUTPUT_NO_WORRIES;
	}
}
