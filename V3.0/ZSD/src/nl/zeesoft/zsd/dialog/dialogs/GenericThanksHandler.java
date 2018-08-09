package nl.zeesoft.zsd.dialog.dialogs;

import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.dialog.DialogInstance;
import nl.zeesoft.zsd.dialog.DialogInstanceHandler;
import nl.zeesoft.zsd.dialog.DialogResponse;
import nl.zeesoft.zsd.dialog.DialogResponseOutput;
import nl.zeesoft.zsd.dialog.DialogVariableValue;

public class GenericThanksHandler extends DialogInstanceHandler {
	@Override
	protected void setPrompt(DialogResponse r,DialogResponseOutput dro,List<DialogVariableValue> updatedValues,String promptVariable) {
		ZStringSymbolParser output = new ZStringSymbolParser();
		if (promptVariable.equals(GenericThanks.VARIABLE_THANKS_HELPFUL)) {
			String anythingElse = dro.values.get(GenericThanks.VARIABLE_THANKS_ELSE).internalValue;
			if (getConfig().getEntityValueTranslator().getBooleanTypeValueForInternalValue(anythingElse,true)) {
				promptVariable = DialogInstance.VARIABLE_NEXT_DIALOG;
			}
		} else if (promptVariable.equals(DialogInstance.VARIABLE_NEXT_DIALOG)) {
			String anythingElse = dro.values.get(GenericThanks.VARIABLE_THANKS_ELSE).internalValue;
			if (!getConfig().getEntityValueTranslator().getBooleanTypeValueForInternalValue(anythingElse,true)) {
				String helpful = dro.values.get(GenericThanks.VARIABLE_THANKS_HELPFUL).internalValue;
				if (getConfig().getEntityValueTranslator().getBooleanTypeValueForInternalValue(helpful,false)) {
					output.append(getHelpfulResponse());
				} else {
					output.append(getNotHelpfulResponse());
				}
			}
		}
		if (output.length()>0) {
			dro.output = output;
		} else {
			super.setPrompt(r, dro, updatedValues, promptVariable);
		}
	}
	
	protected String getHelpfulResponse() {
		return "!";
	}
	
	protected String getNotHelpfulResponse() {
		return "?";
	}
}
