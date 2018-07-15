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
		addExample("Hallo.","Hallo. " + getOutput());
		addExample("Hallo!","Hallo. " + getOutput());
		addExample("Hoi.","Hoi. " + getOutput());
		addExample("Hoi!","Hoi. " + getOutput());
		addExample("Goedemorgen.","Goedemorgen. " + getOutput());
		addExample("Goedemorgen!","Goedemorgen. " + getOutput());
		addExample("Goedemiddag.","Goedemiddag. " + getOutput());
		addExample("Goedemiddag!","Goedemiddag. " + getOutput());
		addExample("Goedenavond.","Goedenavond. " + getOutput());
		addExample("Goedenavond!","Goedenavond. " + getOutput());
		
		addExample("Wie bent u?",getOutput());
		addExample("Hoe heet u?",getOutput());
		addExample("Wat is uw naam?",getOutput());
		addExample("Wie ben jij?",getOutput());
		addExample("Hoe heet jij?",getOutput());
		addExample("Hoe heet je?",getOutput());
		addExample("Wat is jouw naam?",getOutput());
		addExample("Wat is je naam?",getOutput());

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
	
	protected String getOutput() {
		return EXAMPLE_OUTPUT;
	}
}
