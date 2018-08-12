package nl.zeesoft.zsd.dialog.dialogs;

import nl.zeesoft.zsd.dialog.DialogInstance;
import nl.zeesoft.zsd.dialog.DialogInstanceHandler;

public abstract class SupportRequestHandler extends DialogInstanceHandler {
	@Override
	protected void setPrompt(String promptVariable) {
		boolean confirmed = false;
		if (promptVariable.equals(SupportRequest.VARIABLE_SUPPORT_CONFIRMATION)) {
			if (getResponseOutput().output.length()==0) {
				promptVariable = DialogInstance.VARIABLE_NEXT_DIALOG;
				confirmed = true;
			}
		}
		if (promptVariable.equals(DialogInstance.VARIABLE_NEXT_DIALOG)) {
			if (!confirmed) {
				String confirmation = getResponseOutput().values.get(SupportRequest.VARIABLE_SUPPORT_CONFIRMATION).internalValue;
				confirmed = getConfig().getEntityValueTranslator().getBooleanTypeValueForInternalValue(confirmation,false);
			}
			if (confirmed) {
				getResponseOutput().appendOutput(getConnectResponse());
				promptVariable = "";
			} else {
				getResponseOutput().appendOutput(getOkayResponse());
			}
		}
		super.setPrompt(promptVariable);
	}
	
	protected abstract String getConnectResponse();
	
	protected abstract String getOkayResponse();
}
