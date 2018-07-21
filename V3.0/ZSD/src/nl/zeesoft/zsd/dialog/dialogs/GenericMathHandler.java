package nl.zeesoft.zsd.dialog.dialogs;

import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.dialog.DialogInstanceHandler;
import nl.zeesoft.zsd.dialog.DialogResponse;
import nl.zeesoft.zsd.dialog.DialogVariableValue;
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
		
		UniversalMathematic math = (UniversalMathematic) getConfig().getEntityValueTranslator().getEntityObject(BaseConfiguration.LANG_UNI,BaseConfiguration.TYPE_MATHEMATIC);
		return getOutputForValue(math.calculate(expression));
	}
	
	protected ZStringSymbolParser getOutputForValue(float f) {
		ZStringBuilder result = new ZStringBuilder();
		if (f == UniversalMathematic.RESULT_INFINITY) {
			result.append("9@INF");
		} else {
			result.append("" + f);
		}
		return new ZStringSymbolParser(result);
	}
}
