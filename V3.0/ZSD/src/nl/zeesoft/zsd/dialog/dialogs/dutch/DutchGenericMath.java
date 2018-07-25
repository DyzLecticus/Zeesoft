package nl.zeesoft.zsd.dialog.dialogs.dutch;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericMath;
import nl.zeesoft.zsd.entity.EntityObject;

public class DutchGenericMath extends GenericMath {
	public DutchGenericMath() {
		setLanguage(BaseConfiguration.LANG_NLD);
		setHandlerClassName(DutchGenericMathHandler.class.getName());
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		List<String> ops = new ArrayList<String>();
		EntityObject math = t.getEntityObject(BaseConfiguration.LANG_UNI,BaseConfiguration.TYPE_MATHEMATIC);
		for (String op: math.getExternalValues().keySet()) {
			ops.add(op);
		}
		math = t.getEntityObject(BaseConfiguration.LANG_NLD,BaseConfiguration.TYPE_MATHEMATIC);
		for (String op: math.getExternalValues().keySet()) {
			ops.add(op);
		}
		for (String op1: ops) {
			addExample("Hoeveel is {number1} " + op1 + " {number2}?","");
			for (String op2: ops) {
				addExample("Hoeveel is {number1} " + op1 + " {number2} " + op2 + " {number3}?","");
			}
		}

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
