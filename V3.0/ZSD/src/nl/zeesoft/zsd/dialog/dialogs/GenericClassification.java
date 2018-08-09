package nl.zeesoft.zsd.dialog.dialogs;

public class GenericClassification extends Generic {
	public static final String	CONTEXT_GENERIC_CLASSIFICATION		= "Classification";

	public static final String	TRIGGER_INPUT_LANGUAGE				= "[FAILED_TO_CLASSIFY_LANGUAGE].";
	public static final String	TRIGGER_INPUT_CONTEXT				= "[FAILED_TO_CLASSIFY_CONTEXT].";

	public GenericClassification() {
		setContext(CONTEXT_GENERIC_CLASSIFICATION);
	}
}
