package nl.zeesoft.zsd.dialog.dialogs;

import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.dialog.DialogInstanceHandler;
import nl.zeesoft.zsd.dialog.DialogResponse;
import nl.zeesoft.zsd.dialog.DialogVariableValue;
import nl.zeesoft.zsd.entity.EntityObject;
import nl.zeesoft.zsd.entity.UniversalMathematic;

public class GenericMathHandler extends DialogInstanceHandler {
	@Override
	protected ZStringSymbolParser updatedValues(DialogResponse r,List<DialogVariableValue> updatedValues,String promptVariable) {
		ZStringSymbolParser expression = new ZStringSymbolParser();
		
		for (int i = 1; i <= 5; i++) {
			String numVal = getValues().get("number" + i).internalValue;
			String opVal = "";
			if (i < 5) {
				opVal = getValues().get("operator" + i).internalValue;
			}
			if (numVal.length()>0) {
				if (expression.length()>0) {
					expression.append(" ");
				}
				numVal = numVal.split(":")[1];
				expression.append(numVal);
				if (opVal.length()>0) {
					opVal = opVal.split(":")[1];
					expression.append(" ");
					expression.append(opVal);
				}
			}
		}
		
		r.addDebugLogLine("    Calculate expression: ",expression);
		UniversalMathematic math = (UniversalMathematic) getConfig().getEntityValueTranslator().getEntityObject(BaseConfiguration.LANG_UNI,BaseConfiguration.TYPE_MATHEMATIC);
		float f = math.calculate(expression);
		r.addDebugLogLine("    Calculated expression: ","" + f);
		
		String exact = getExactly();
		String result = "";
		if (f == UniversalMathematic.RESULT_INFINITY) {
			result = getInfinity();
		} else {
			String[] str = ("" + f).split("\\.");
			int i1 = Integer.parseInt(str[0]);
			int i2 = Integer.parseInt(str[1]);
			if (i2>0) {
				result = getAbout();
				if (i2>=5) {
					i1++;
				}
			}
			String eVal = "";
			EntityObject eo = getNumericEntity();
			if (eo!=null) {
				eVal = eo.getExternalValueForInternalValue(eo.getInternalValuePrefix() + i1);
			}
			if (eVal.length()>0) {
				result = eVal;
			} else {
				result = "" + i1;
			}
		}

		setDialogVariableValue(r,GenericMath.VARIABLE_EXACT,exact);
		setDialogVariableValue(r,GenericMath.VARIABLE_RESULT,result);
		
		return super.updatedValues(r, updatedValues, promptVariable);
	}
	
	protected EntityObject getNumericEntity() {
		return null;
	}
	
	protected String getExactly() {
		return "=";
	}
	
	protected String getAbout() {
		return "+-";
	}

	protected String getInfinity() {
		return "9@INF";
	}
}
