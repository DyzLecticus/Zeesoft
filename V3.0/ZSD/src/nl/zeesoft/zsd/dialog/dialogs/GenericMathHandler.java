package nl.zeesoft.zsd.dialog.dialogs;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.dialog.DialogInstanceHandler;
import nl.zeesoft.zsd.entity.EntityObject;
import nl.zeesoft.zsd.entity.UniversalMathematic;

public class GenericMathHandler extends DialogInstanceHandler {
	@Override
	protected void setPrompt(String promptVariable) {
		ZStringSymbolParser expression = new ZStringSymbolParser();
		
		for (int i = 1; i <= 5; i++) {
			String numVal = getResponseOutput().values.get("number" + i).internalValue;
			String opVal = "";
			if (i < 5) {
				opVal = getResponseOutput().values.get("operator" + i).internalValue;
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
		
		if (expression.length()>0) {
			getResponse().addDebugLogLine("    Calculate expression: ",expression);
			UniversalMathematic math = (UniversalMathematic) getConfig().getEntityValueTranslator().getEntityObject(BaseConfiguration.LANG_UNI,BaseConfiguration.TYPE_MATHEMATIC);
			float f = math.calculate(expression);
			getResponse().addDebugLogLine("    Calculated expression: ","" + f);
			
			String exact = getExactly();
			String result = "";
			if (f == UniversalMathematic.RESULT_INFINITY) {
				result = getInfinity();
			} else {
				boolean minus = false;
				String[] str = ("" + f).split("\\.");
				int i = Integer.parseInt(str[0]);
				float d = Float.parseFloat("0." + str[1]);
				if (i<0) {
					minus = true;
					i = (i * -1);
				}
				if (d>0) {
					exact = getAbout();
					if (d>=0.5F) {
						i++;
					}
				}
				String eVal = getExternalValueForNumber(i);
				if (eVal.length()>0) {
					result = eVal;
					if (minus) {
						result = getMinus() + " " + result;
					}
				} else {
					if (minus) {
						result = "-" + i;
					} else {
						result = "" + i;
					}
				}
			}
	
			setDialogVariableValue(GenericMath.VARIABLE_EXACT,exact);
			setDialogVariableValue(GenericMath.VARIABLE_RESULT,result);
			
			super.setPrompt(promptVariable);
		} else {
			getResponseOutput().output = new ZStringSymbolParser();
		}
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

	protected String getMinus() {
		return "-";
	}

	protected String getInfinity() {
		return "9@INF";
	}
}
