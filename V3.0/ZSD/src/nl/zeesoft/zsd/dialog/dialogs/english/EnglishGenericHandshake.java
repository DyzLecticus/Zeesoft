package nl.zeesoft.zsd.dialog.dialogs.english;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericHandshake;

public class EnglishGenericHandshake extends GenericHandshake {
	private static final String EXAMPLE_OUTPUT = "Mijn naam is {selfName}. Wat is jouw naam?";
	
	public EnglishGenericHandshake() {
		setLanguage(BaseConfiguration.LANG_ENG);
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		addExample("Hello.","Hello. " + EXAMPLE_OUTPUT);
		addExample("Hello!","Hello. " + EXAMPLE_OUTPUT);
		addExample("Hi.","Hi. " + EXAMPLE_OUTPUT);
		addExample("Hi!","Hi. " + EXAMPLE_OUTPUT);
		addExample("Good morning.","Good morning. " + EXAMPLE_OUTPUT);
		addExample("Good morning!","Good morning. " + EXAMPLE_OUTPUT);
		addExample("Good afternoon.","Good afternoon. " + EXAMPLE_OUTPUT);
		addExample("Good afternoon!","Good afternoon. " + EXAMPLE_OUTPUT);
		addExample("Good evening.","Good evening. " + EXAMPLE_OUTPUT);
		addExample("Good evening!","Good evening. " + EXAMPLE_OUTPUT);
		
		addExample("What is your name?",EXAMPLE_OUTPUT);
		addExample("Who are you?",EXAMPLE_OUTPUT);
	
		addComplexPatterns(t,BaseConfiguration.TYPE_NAME);
		
		addVariable(VARIABLE_FIRSTNAME,BaseConfiguration.TYPE_ALPHABETIC,VARIABLE_FIRSTNAME,BaseConfiguration.TYPE_NAME);
		addVariablePrompt(VARIABLE_FIRSTNAME,"What is your name?");
		addVariablePrompt(VARIABLE_FIRSTNAME,"What is your firstname?");

		addVariable(VARIABLE_LASTNAME,BaseConfiguration.TYPE_ALPHABETIC,VARIABLE_LASTNAME,BaseConfiguration.TYPE_NAME);
		addVariablePrompt(VARIABLE_LASTNAME,"What is your lastname?");

		addVariable(VARIABLE_NEXT_DIALOG,BaseConfiguration.TYPE_ALPHABETIC);
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"What can I do for you {fullName}?");

		addVariable(VARIABLE_PREPOSITION,BaseConfiguration.TYPE_PREPOSITION,VARIABLE_PREPOSITION,BaseConfiguration.TYPE_NAME);
	}
}
