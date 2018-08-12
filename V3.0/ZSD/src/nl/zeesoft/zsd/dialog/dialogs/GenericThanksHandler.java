package nl.zeesoft.zsd.dialog.dialogs;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.dialog.DialogInstance;
import nl.zeesoft.zsd.dialog.DialogInstanceHandler;

public class GenericThanksHandler extends DialogInstanceHandler {
	@Override
	protected void setPrompt(String promptVariable) {
		ZStringSymbolParser output = new ZStringSymbolParser();
		if (promptVariable.equals(GenericThanks.VARIABLE_THANKS_HELPFUL)) {
			String anythingElse = getResponseOutput().values.get(GenericThanks.VARIABLE_THANKS_ELSE).internalValue;
			if (getConfig().getEntityValueTranslator().getBooleanTypeValueForInternalValue(anythingElse,true)) {
				promptVariable = DialogInstance.VARIABLE_NEXT_DIALOG;
			}
		} else if (promptVariable.equals(DialogInstance.VARIABLE_NEXT_DIALOG)) {
			String anythingElse = getResponseOutput().values.get(GenericThanks.VARIABLE_THANKS_ELSE).internalValue;
			if (!getConfig().getEntityValueTranslator().getBooleanTypeValueForInternalValue(anythingElse,true)) {
				String helpful = getResponseOutput().values.get(GenericThanks.VARIABLE_THANKS_HELPFUL).internalValue;
				if (getConfig().getEntityValueTranslator().getBooleanTypeValueForInternalValue(helpful,false)) {
					output.append(getHelpfulResponse());
				} else {
					output.append(getNotHelpfulResponse());
				}
			}
		}
		if (output.length()>0) {
			getResponseOutput().output = output;
		} else {
			super.setPrompt(promptVariable);
		}
	}
	
	protected String getHelpfulResponse() {
		return "!";
	}
	
	protected String getNotHelpfulResponse() {
		return "?";
	}
}
