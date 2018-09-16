package nl.zeesoft.zsdm.dialog.dialogs.english;

import nl.zeesoft.zevt.type.Types;
import nl.zeesoft.znlb.context.ContextConfig;

public class EnglishGenericLanguage extends EnglishGeneric {
	private static final String	EXAMPLE_OUTPUT_DEFAULT		= "I speak {languages}.";
	private static final String	EXAMPLE_OUTPUT_CONFIRMATION	= "{confirmation}, I speak {languages}.";
	
	public EnglishGenericLanguage() {
		setContext(ContextConfig.CONTEXT_GENERIC_LANGUAGE);
		// TODO: setHandlerClassName(DutchGenericLanguageHandler.class.getName());
	}
	
	@Override
	public void initialize() {
		addExample("Which languages can you speak?",getOutputDefault());
		addExample("What languages do you know?",getOutputDefault());

		addExample("How many languages can you speak?",getOutputDefault());
		addExample("How many languages do you know?",getOutputDefault());

		addExample("Do you speak [" + Types.LANGUAGE + "]?",getOutputConfirmation());
		addExample("Can you speak [" + Types.LANGUAGE + "]?",getOutputConfirmation());

		addVariable(VARIABLE_LANGUAGE,Types.LANGUAGE);
	}
	
	protected String getOutputDefault() {
		return EXAMPLE_OUTPUT_DEFAULT;
	}
	
	protected String getOutputConfirmation() {
		return EXAMPLE_OUTPUT_CONFIRMATION;
	}
}
