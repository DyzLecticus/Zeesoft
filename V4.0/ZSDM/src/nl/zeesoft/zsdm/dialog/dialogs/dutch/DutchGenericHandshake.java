package nl.zeesoft.zsdm.dialog.dialogs.dutch;

import nl.zeesoft.zevt.type.Types;
import nl.zeesoft.znlb.context.ContextConfig;

public class DutchGenericHandshake extends DutchGeneric {
	private static final String EXAMPLE_OUTPUT = "Mijn naam is {selfName}.";
	
	public DutchGenericHandshake() {
		setContext(ContextConfig.CONTEXT_GENERIC_HANDSHAKE);
		// TODO: Create and set handler
	}
	
	@Override
	public void initialize() {
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

		addVariable(VARIABLE_FIRSTNAME,Types.ALPHABETIC);
		addVariablePrompt(VARIABLE_FIRSTNAME,"Wat is uw naam?");
		addVariablePrompt(VARIABLE_FIRSTNAME,"Wat is uw voornaam?");

		addVariable(VARIABLE_LASTNAME,Types.ALPHABETIC);
		addVariablePrompt(VARIABLE_LASTNAME,"Wat is uw achternaam?");

		addNextDialogVariable();
		addVariablePrompt(VARIABLE_NAME_NEXT_DIALOG,"Wat kan ik voor u doen {fullName}?");
		addVariablePrompt(VARIABLE_NAME_NEXT_DIALOG,"Hoe kan ik u helpen {fullName}?");

		addVariable(VARIABLE_PREPOSITION,Types.PREPOSITION);
	}
	
	protected String getOutput() {
		return EXAMPLE_OUTPUT;
	}
}
