package nl.zeesoft.zsdm.dialog.dialogs.dutch;

import nl.zeesoft.zevt.type.Types;
import nl.zeesoft.znlb.context.ContextConfig;

public class DutchGenericLanguage extends DutchGeneric {
	private static final String	EXAMPLE_OUTPUT_DEFAULT		= "Ik spreek {languages}.";
	private static final String	EXAMPLE_OUTPUT_CONFIRMATION	= "{confirmation}, ik spreek {languages}.";
	
	public DutchGenericLanguage() {
		setContext(ContextConfig.CONTEXT_GENERIC_LANGUAGE);
		// TODO: setHandlerClassName(DutchGenericLanguageHandler.class.getName());
	}
	
	@Override
	public void initialize() {
		addExample("Welke talen spreek je?",getOutputDefault());
		addExample("Welke talen spreek jij?",getOutputDefault());
		addExample("Welke talen spreekt u?",getOutputDefault());

		addExample("Hoeveel talen spreek je?",getOutputDefault());
		addExample("Hoeveel talen spreek jij?",getOutputDefault());
		addExample("Hoeveel talen spreekt u?",getOutputDefault());

		addExample("Spreek je [" + Types.LANGUAGE + "]?",getOutputConfirmation());
		addExample("Spreek jij [" + Types.LANGUAGE + "]?",getOutputConfirmation());
		addExample("Spreekt u [" + Types.LANGUAGE + "]?",getOutputConfirmation());

		addVariable(VARIABLE_LANGUAGE,Types.LANGUAGE);
	}
	
	protected String getOutputDefault() {
		return EXAMPLE_OUTPUT_DEFAULT;
	}
	
	protected String getOutputConfirmation() {
		return EXAMPLE_OUTPUT_CONFIRMATION;
	}
}
