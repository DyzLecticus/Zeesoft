package nl.zeesoft.zsdm.dialog.dialogs.english;

import nl.zeesoft.zevt.type.Types;
import nl.zeesoft.znlb.context.ContextConfig;

public class EnglishGenericHandshake extends EnglishGeneric {
	private static final String EXAMPLE_OUTPUT = "My name is {selfName}.";
	
	public EnglishGenericHandshake() {
		setContext(ContextConfig.CONTEXT_GENERIC_HANDSHAKE);
		// TODO: Create and set handler
	}
	
	@Override
	public void initialize() {
		addExample("Hello.","Hello. " + getOutput());
		addExample("Hello!","Hello. " + getOutput());
		addExample("Hi.","Hi. " + getOutput());
		addExample("Hi!","Hi. " + getOutput());
		addExample("Good morning.","Good morning. " + getOutput());
		addExample("Good morning!","Good morning. " + getOutput());
		addExample("Good afternoon.","Good afternoon. " + getOutput());
		addExample("Good afternoon!","Good afternoon. " + getOutput());
		addExample("Good evening.","Good evening. " + getOutput());
		addExample("Good evening!","Good evening. " + getOutput());
		
		addExample("What is your name?",getOutput());
		addExample("Who are you?",getOutput());
		
		addVariable(VARIABLE_FIRSTNAME,Types.ALPHABETIC);
		addVariablePrompt(VARIABLE_FIRSTNAME,"What is your name?");
		addVariablePrompt(VARIABLE_FIRSTNAME,"What is your firstname?");

		addVariable(VARIABLE_LASTNAME,Types.ALPHABETIC);
		addVariablePrompt(VARIABLE_LASTNAME,"What is your lastname?");

		addNextDialogVariable();
		addVariablePrompt(VARIABLE_NAME_NEXT_DIALOG,"What can I do for you {fullName}?");
		addVariablePrompt(VARIABLE_NAME_NEXT_DIALOG,"How can I help you {fullName}?");
	}
	
	protected String getOutput() {
		return EXAMPLE_OUTPUT;
	}
}
