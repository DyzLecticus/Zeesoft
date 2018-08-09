package nl.zeesoft.zsd.dialog.dialogs.english;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericHandshake;

public class EnglishGenericHandshake extends GenericHandshake {
	private static final String EXAMPLE_OUTPUT = "My name is {selfName}.";
	
	public EnglishGenericHandshake() {
		setLanguage(BaseConfiguration.LANG_ENG);
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
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
		
		addVariable(VARIABLE_FIRSTNAME,BaseConfiguration.TYPE_ALPHABETIC,VARIABLE_FIRSTNAME,BaseConfiguration.TYPE_NAME);
		addVariablePrompt(VARIABLE_FIRSTNAME,"What is your name?");
		addVariablePrompt(VARIABLE_FIRSTNAME,"What is your firstname?");

		addVariable(VARIABLE_LASTNAME,BaseConfiguration.TYPE_ALPHABETIC,VARIABLE_LASTNAME,BaseConfiguration.TYPE_NAME);
		addVariablePrompt(VARIABLE_LASTNAME,"What is your lastname?");

		addNextDialogVariable();
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"What can I do for you {fullName}?");
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"How can I help you {fullName}?");

		addVariable(VARIABLE_PREPOSITION,BaseConfiguration.TYPE_PREPOSITION,VARIABLE_PREPOSITION,BaseConfiguration.TYPE_NAME);
		
		addComplexPatterns(t,BaseConfiguration.TYPE_NAME);
	}
	
	protected String getOutput() {
		return EXAMPLE_OUTPUT;
	}
}
