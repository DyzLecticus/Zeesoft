package nl.zeesoft.zsdm.dialog;

import nl.zeesoft.znlb.context.ContextConfig;

public class Generic extends Dialog {
	public static final String	TRIGGER_INPUT_LANGUAGE			= "[FAILED_TO_CLASSIFY_LANGUAGE].";
	public static final String	TRIGGER_INPUT_CONTEXT			= "[FAILED_TO_CLASSIFY_CONTEXT].";
	
	public static final String	VARIABLE_FIRSTNAME				= "firstName";
	public static final String	VARIABLE_PREPOSITION			= "preposition";
	public static final String	VARIABLE_LASTNAME				= "lastName";
	public static final String	VARIABLE_FULLNAME				= "fullName";
	
	public static final String	VARIABLE_LANGUAGE				= "language";
	
	public static final String	VARIABLE_NUMBER1				= "number1";
	public static final String	VARIABLE_NUMBER2				= "number2";
	public static final String	VARIABLE_NUMBER3				= "number3";
	public static final String	VARIABLE_NUMBER4				= "number4";
	public static final String	VARIABLE_NUMBER5				= "number5";

	public static final String	VARIABLE_OPERATOR1				= "operator1";
	public static final String	VARIABLE_OPERATOR2				= "operator2";
	public static final String	VARIABLE_OPERATOR3				= "operator3";
	public static final String	VARIABLE_OPERATOR4				= "operator4";

	public static final String	VARIABLE_SUPPORT_CONFIRMATION	= "supportConfirmation";
	
	public static final String	VARIABLE_THANKS_ELSE			= "thanksElse";
	public static final String	VARIABLE_THANKS_HELPFUL			= "thanksHelpful";
	
	public static final String	FILTER_CONTEXT_NO_TRANSFER		= "noTransferToHuman";
	public static final String	FILTER_CONTEXT_TRANSFER			= "transferToHuman";
	
	public Generic() {
		setMasterContext(ContextConfig.MASTER_CONTEXT_GENERIC);
	}
}
