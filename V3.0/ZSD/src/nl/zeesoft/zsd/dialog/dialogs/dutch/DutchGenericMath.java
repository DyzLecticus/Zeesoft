package nl.zeesoft.zsd.dialog.dialogs.dutch;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericMath;
import nl.zeesoft.zsd.entity.entities.dutch.DutchMathematic;

public class DutchGenericMath extends GenericMath {
	public DutchGenericMath() {
		setLanguage(BaseConfiguration.LANG_NLD);
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		DutchMathematic math = (DutchMathematic) t.getEntityObject(BaseConfiguration.LANG_NLD,BaseConfiguration.TYPE_MATHEMATIC);
		for (String op1: math.getExternalValues().keySet()) {
			addExample("Hoeveel is {number1} " + op1 + " {number2}?","");
			for (String op2: math.getExternalValues().keySet()) {
				addExample("Hoeveel is {number1} " + op1 + " {number2} " + op2 + " {number3}?","");
				for (String op3: math.getExternalValues().keySet()) {
					addExample("Hoeveel is {number1} " + op1 + " {number2} " + op2 + " {number3} " + op3 + " {number4}?","");
					for (String op4: math.getExternalValues().keySet()) {
						addExample("Hoeveel is {number1} " + op1 + " {number2} " + op2 + " {number3} " + op3 + " {number4} " + op4 + " {number5}?","");
					}
				}
			}
		}

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
