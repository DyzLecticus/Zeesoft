package nl.zeesoft.zsdm.dialog.dialogs.dutch;

import nl.zeesoft.zevt.type.Types;
import nl.zeesoft.znlb.context.ContextConfig;

public class DutchGenericMath extends DutchGeneric {
	public DutchGenericMath() {
		setContext(ContextConfig.CONTEXT_GENERIC_MATH);
		// TODO: setHandlerClassName(DutchGenericMathHandler.class.getName());
	}
	
	@Override
	public void initialize() {
		addExample("Hoeveel is [NUM] [MTH] [NUM]?","{exact} {result}.");
		addExample("Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM]?","{exact} {result}.");
		addExample("Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?","{exact} {result}.");
		addExample("Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?","{exact} {result}.");
		addExample("Hoeveel is [NUM] [MTH] [NUM]?","Dat is {exact} {result}.");
		addExample("Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM]?","Dat is {exact} {result}.");
		addExample("Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?","Dat is {exact} {result}.");
		addExample("Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?","Dat is {exact} {result}.");
		
		addNextDialogVariable();
		addVariablePrompt(VARIABLE_NAME_NEXT_DIALOG,"Kan ik nog meer voor je doen?");
		addVariablePrompt(VARIABLE_NAME_NEXT_DIALOG,"Is er nog iets waar ik je mee kan helpen?");

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
