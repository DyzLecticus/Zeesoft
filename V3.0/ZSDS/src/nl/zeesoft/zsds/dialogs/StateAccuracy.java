package nl.zeesoft.zsds.dialogs;

public class StateAccuracy extends State {
	public static final String	CONTEXT_STATE_QNA			= "QuestionAndAnswer";

	public static final String	VARIABLE_EXACT				= "exact";
	public static final String	VARIABLE_PERCENTAGE			= "percentage";
	public static final String	VARIABLE_DIFFERENCE			= "difference";
	public static final String	VARIABLE_RESPONSE_CODE		= "responseCode";
	
	public StateAccuracy() {
		setContext(CONTEXT_STATE_QNA);
	}
}
