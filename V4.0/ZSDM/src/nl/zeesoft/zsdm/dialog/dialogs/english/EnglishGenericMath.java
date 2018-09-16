package nl.zeesoft.zsdm.dialog.dialogs.english;

import nl.zeesoft.zevt.type.Types;
import nl.zeesoft.znlb.context.ContextConfig;

public class EnglishGenericMath extends EnglishGeneric {
	public EnglishGenericMath() {
		setContext(ContextConfig.CONTEXT_GENERIC_MATH);
		// TODO: setHandlerClassName(DutchGenericMathHandler.class.getName());
	}
	
	@Override
	public void initialize() {
		addExample("How much is [NUM] [MTH] [NUM]?","{exact} {result}.");
		addExample("How much is [NUM] [MTH] [NUM] [MTH] [NUM]?","{exact} {result}.");
		addExample("How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?","{exact} {result}.");
		addExample("How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?","{exact} {result}.");
		addExample("How much is [NUM] [MTH] [NUM]?","That is {exact} {result}.");
		addExample("How much is [NUM] [MTH] [NUM] [MTH] [NUM]?","That is {exact} {result}. ");
		addExample("How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?","That is {exact} {result}. ");
		addExample("How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?","That is {exact} {result}. ");
		
		addNextDialogVariable();
		addVariablePrompt(VARIABLE_NAME_NEXT_DIALOG,"What else can I do for you?");
		addVariablePrompt(VARIABLE_NAME_NEXT_DIALOG,"Is there anything else I can help you with?");

		addVariable(VARIABLE_NUMBER1,Types.NUMERIC);
		addVariable(VARIABLE_NUMBER2,Types.NUMERIC);
		addVariable(VARIABLE_NUMBER3,Types.NUMERIC);
		addVariable(VARIABLE_NUMBER4,Types.NUMERIC);
		addVariable(VARIABLE_NUMBER5,Types.NUMERIC);

		addVariable(VARIABLE_OPERATOR1,Types.MATHEMATIC);
		addVariable(VARIABLE_OPERATOR2,Types.MATHEMATIC);
		addVariable(VARIABLE_OPERATOR3,Types.MATHEMATIC);
		addVariable(VARIABLE_OPERATOR4,Types.MATHEMATIC);
	}
}
