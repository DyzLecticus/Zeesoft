package nl.zeesoft.zsd.dialog.dialogs.english;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericMath;
import nl.zeesoft.zsd.entity.EntityObject;

public class EnglishGenericMath extends GenericMath {
	public EnglishGenericMath() {
		setLanguage(BaseConfiguration.LANG_ENG);
		setHandlerClassName(EnglishGenericMathHandler.class.getName());
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		List<String> ops = new ArrayList<String>();
		EntityObject math = t.getEntityObject(BaseConfiguration.LANG_UNI,BaseConfiguration.TYPE_MATHEMATIC);
		for (String op: math.getExternalValues().keySet()) {
			ops.add(op);
		}
		math = t.getEntityObject(BaseConfiguration.LANG_ENG,BaseConfiguration.TYPE_MATHEMATIC);
		for (String op: math.getExternalValues().keySet()) {
			ops.add(op);
		}
		for (String op1: ops) {
			addExample("How much is {number1} " + op1 + " {number2}?","");
			for (String op2: ops) {
				addExample("How much is {number1} " + op1 + " {number2} " + op2 + " {number3}?","");
			}
		}

		addVariable(VARIABLE_NEXT_DIALOG,BaseConfiguration.TYPE_ALPHABETIC);
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"{exact} {result}. What else can I do for you?");
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"{exact} {result}. Is there anything else I can help you with?");
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"That is {exact} {result}. What else can I do for you?");
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"That is {exact} {result}. Is there anything else I can help you with?");

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