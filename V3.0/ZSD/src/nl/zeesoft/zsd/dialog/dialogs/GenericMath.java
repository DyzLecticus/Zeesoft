package nl.zeesoft.zsd.dialog.dialogs;

public abstract class GenericMath extends Generic {
	public static final String	CONTEXT_GENERIC_MATH		= "Math";

	public static final String	VARIABLE_NUMBER1			= "number1";
	public static final String	VARIABLE_NUMBER2			= "number2";
	public static final String	VARIABLE_NUMBER3			= "number3";
	public static final String	VARIABLE_NUMBER4			= "number4";
	public static final String	VARIABLE_NUMBER5			= "number5";

	public static final String	VARIABLE_OPERATOR1			= "operator1";
	public static final String	VARIABLE_OPERATOR2			= "operator2";
	public static final String	VARIABLE_OPERATOR3			= "operator3";
	public static final String	VARIABLE_OPERATOR4			= "operator4";

	public GenericMath() {
		setContext(CONTEXT_GENERIC_MATH);
		setHandlerClassName(GenericMathHandler.class.getName());
	}
}
