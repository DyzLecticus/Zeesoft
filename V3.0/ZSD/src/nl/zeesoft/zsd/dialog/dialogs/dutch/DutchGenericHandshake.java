package nl.zeesoft.zsd.dialog.dialogs.dutch;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericHandshake;

public class DutchGenericHandshake extends GenericHandshake {
	private static final String EXAMPLE_OUTPUT = "Mijn naam is {selfName}. Wat is jouw naam?";
	
	public DutchGenericHandshake() {
		setLanguage(BaseConfiguration.LANG_NLD);
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		addExample("Hallo.","Hallo. " + EXAMPLE_OUTPUT);
		addExample("Hallo!","Hallo. " + EXAMPLE_OUTPUT);
		addExample("Hoi.","Hoi. " + EXAMPLE_OUTPUT);
		addExample("Hoi!","Hoi. " + EXAMPLE_OUTPUT);
		addExample("Goedemorgen.","Goedemorgen. " + EXAMPLE_OUTPUT);
		addExample("Goedemorgen!","Goedemorgen. " + EXAMPLE_OUTPUT);
		addExample("Goedemiddag.","Goedemiddag. " + EXAMPLE_OUTPUT);
		addExample("Goedemiddag!","Goedemiddag. " + EXAMPLE_OUTPUT);
		addExample("Goedenavond.","Goedenavond. " + EXAMPLE_OUTPUT);
		addExample("Goedenavond!","Goedenavond. " + EXAMPLE_OUTPUT);
		
		addExample("Wie bent u?",EXAMPLE_OUTPUT);
		addExample("Hoe heet u?",EXAMPLE_OUTPUT);
		addExample("Wat is uw naam?",EXAMPLE_OUTPUT);
		addExample("Wie ben jij?",EXAMPLE_OUTPUT);
		addExample("Hoe heet jij?",EXAMPLE_OUTPUT);
		addExample("Hoe heet je?",EXAMPLE_OUTPUT);
		addExample("Wat is jouw naam?",EXAMPLE_OUTPUT);
		addExample("Wat is je naam?",EXAMPLE_OUTPUT);

		addComplexPatterns(t,BaseConfiguration.TYPE_NAME);
		
		addVariable(VARIABLE_FIRSTNAME,BaseConfiguration.TYPE_ALPHABETIC,VARIABLE_FIRSTNAME,BaseConfiguration.TYPE_NAME);
		addVariablePrompt(VARIABLE_FIRSTNAME,"Wat is jouw naam?");
		addVariablePrompt(VARIABLE_FIRSTNAME,"Wat is jouw voornaam?");

		addVariable(VARIABLE_LASTNAME,BaseConfiguration.TYPE_ALPHABETIC,VARIABLE_LASTNAME,BaseConfiguration.TYPE_NAME);
		addVariablePrompt(VARIABLE_LASTNAME,"Wat is jouw achternaam?");

		addVariable(VARIABLE_NEXT_DIALOG,BaseConfiguration.TYPE_ALPHABETIC);
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"Wat kan ik voor je doen {fullName}?");
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"Hoe kan ik je helpen {fullName}?");

		addVariable(VARIABLE_PREPOSITION,BaseConfiguration.TYPE_PREPOSITION,VARIABLE_PREPOSITION,BaseConfiguration.TYPE_NAME);
	}
}
