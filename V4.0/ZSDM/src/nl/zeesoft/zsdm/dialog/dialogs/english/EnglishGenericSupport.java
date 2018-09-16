package nl.zeesoft.zsdm.dialog.dialogs.english;

import nl.zeesoft.zevt.type.Types;
import nl.zeesoft.znlb.context.ContextConfig;

public class EnglishGenericSupport extends EnglishGeneric {
	private static final String		EXAMPLE_OUTPUT_CONTACT		= "Sadly I cannot transfer you to a human right now. You can send an e-mail to {selfEmail}.";
	private static final String		EXAMPLE_OUTPUT_LEARNING		= "I am still learning.";
	private static final String		EXAMPLE_OUTPUT_NO_WORRIES	= "Don't worry.";
	
	public EnglishGenericSupport() {
		setContext(ContextConfig.CONTEXT_GENERIC_SUPPORT);
		// TODO: setHandlerClassName(DutchSupportRequestHandler.class.getName());
	}
	
	@Override
	public void initialize() {
		addExamplesForFilterContext(FILTER_CONTEXT_NO_TRANSFER,getOutputContact());
		addExamplesForFilterContext(FILTER_CONTEXT_TRANSFER,"");

		addVariable(VARIABLE_SUPPORT_CONFIRMATION,Types.CONFIRMATION);
		addVariablePrompt(VARIABLE_SUPPORT_CONFIRMATION,"Shall I transfer you to a human?");
		addVariablePrompt(VARIABLE_SUPPORT_CONFIRMATION,"Do you want to be transferred to a human?");
		
		addNextDialogVariable();
		addVariablePrompt(VARIABLE_NAME_NEXT_DIALOG,"Is there something else I can try to help you with?");
		addVariablePrompt(VARIABLE_NAME_NEXT_DIALOG,"Is there another way I can be of service to you?");
	}
	
	protected void addExamplesForFilterContext(String filterContext,String outputContact) {
		addExample("I want to talk to a human.",outputContact,filterContext);
		addExample("I want to speak to a human.",outputContact,filterContext);
		addExample("I want to be put through to a human.",outputContact,filterContext);
		addExample("I want to be transferred to a human.",outputContact,filterContext);
		addExample("Can you put me through to a human?",outputContact,filterContext);
		addExample("Can you transfer me to a human?",outputContact,filterContext);
		
		String append = "";
		if (outputContact.length()>0) {
			append = " " + outputContact;
		}
		
		addExample("[" + Types.FROWNY + "].",getOutputLearning() + append,filterContext);
		addExample("Sigh.",getOutputLearning() + append,filterContext);
		addExample("You do not understand me.",getOutputLearning() + append,filterContext);
		addExample("You misunderstand me.",getOutputLearning() + append,filterContext);
		addExample("You do not understand anything.",getOutputLearning() + append,filterContext);
		addExample("You are unable to help me.",getOutputLearning() + append,filterContext);
		addExample("You can not help me.",getOutputLearning() + append,filterContext);
		
		addExample("Help.",getOutputNoWorries() + append,filterContext);
		addExample("Help!",getOutputNoWorries() + append,filterContext);
	}
	
	protected String getOutputContact() {
		return EXAMPLE_OUTPUT_CONTACT;
	}
	
	protected String getOutputLearning() {
		return EXAMPLE_OUTPUT_LEARNING;
	}
	
	protected String getOutputNoWorries() {
		return EXAMPLE_OUTPUT_NO_WORRIES;
	}
}
