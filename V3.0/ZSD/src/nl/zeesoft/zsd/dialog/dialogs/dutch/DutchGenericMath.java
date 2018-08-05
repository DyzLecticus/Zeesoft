package nl.zeesoft.zsd.dialog.dialogs.dutch;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericMath;

public class DutchGenericMath extends GenericMath {
	public DutchGenericMath() {
		setLanguage(BaseConfiguration.LANG_NLD);
		setHandlerClassName(DutchGenericMathHandler.class.getName());
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		addExample("Hoeveel is [NUM] [MTH] [NUM]?","");
		addExample("Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM]?","");
		addExample("Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?","");
		addExample("Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?","");
		
		addVariable(VARIABLE_NEXT_DIALOG,BaseConfiguration.TYPE_ALPHABETIC);
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"{exact} {result}. Kan ik nog meer voor je doen?");
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"{exact} {result}. Is er nog iets waar ik je mee kan helpen?");
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"Dat is {exact} {result}. Kan ik nog meer voor je doen?");
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"Dat is {exact} {result}. Is er nog iets waar ik je mee kan helpen?");

		addVariable(VARIABLE_NUMBER1,BaseConfiguration.TYPE_NUMERIC);
		addVariable(VARIABLE_NUMBER2,BaseConfiguration.TYPE_NUMERIC);
		addVariable(VARIABLE_NUMBER3,BaseConfiguration.TYPE_NUMERIC);
		addVariable(VARIABLE_NUMBER4,BaseConfiguration.TYPE_NUMERIC);
		addVariable(VARIABLE_NUMBER5,BaseConfiguration.TYPE_NUMERIC);

		addVariable(VARIABLE_OPERATOR1,BaseConfiguration.TYPE_MATHEMATIC);
		addVariable(VARIABLE_OPERATOR2,BaseConfiguration.TYPE_MATHEMATIC);
		addVariable(VARIABLE_OPERATOR3,BaseConfiguration.TYPE_MATHEMATIC);
		addVariable(VARIABLE_OPERATOR4,BaseConfiguration.TYPE_MATHEMATIC);
	}
}
