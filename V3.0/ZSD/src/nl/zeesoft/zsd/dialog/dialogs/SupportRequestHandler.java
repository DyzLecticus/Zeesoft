package nl.zeesoft.zsd.dialog.dialogs;

import java.util.List;

import nl.zeesoft.zsd.dialog.DialogInstance;
import nl.zeesoft.zsd.dialog.DialogInstanceHandler;
import nl.zeesoft.zsd.dialog.DialogResponse;
import nl.zeesoft.zsd.dialog.DialogResponseOutput;
import nl.zeesoft.zsd.dialog.DialogVariableValue;

public abstract class SupportRequestHandler extends DialogInstanceHandler {
	@Override
	protected void setPrompt(DialogResponse r,DialogResponseOutput dro,List<DialogVariableValue> updatedValues,String promptVariable) {
		boolean confirmed = false;
		if (promptVariable.equals(SupportRequest.VARIABLE_SUPPORT_CONFIRMATION)) {
			if (dro.output.length()==0) {
				promptVariable = DialogInstance.VARIABLE_NEXT_DIALOG;
				confirmed = true;
			}
		}
		if (promptVariable.equals(DialogInstance.VARIABLE_NEXT_DIALOG)) {
			if (!confirmed) {
				String confirmation = dro.values.get(SupportRequest.VARIABLE_SUPPORT_CONFIRMATION).internalValue;
				confirmed = getConfig().getEntityValueTranslator().getBooleanTypeValueForInternalValue(confirmation,false);
			}
			if (confirmed) {
				dro.output.append(getConnectResponse());
				promptVariable = "";
			} else {
				dro.output.append(getOkayResponse());
			}
		}
		super.setPrompt(r,dro,updatedValues,promptVariable);
	}
	protected abstract String getConnectResponse();
	protected abstract String getOkayResponse();
}
