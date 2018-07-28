package nl.zeesoft.zsd.dialog.dialogs;

import java.util.List;

import nl.zeesoft.zsd.dialog.DialogInstanceHandler;
import nl.zeesoft.zsd.dialog.DialogResponse;
import nl.zeesoft.zsd.dialog.DialogResponseOutput;
import nl.zeesoft.zsd.dialog.DialogVariableValue;

public abstract class RoomBookingHandler extends DialogInstanceHandler {
	@Override
	protected void setPrompt(DialogResponse r,DialogResponseOutput dro,List<DialogVariableValue> updatedValues,String promptVariable) {
		if (promptVariable.equals(Generic.VARIABLE_NEXT_DIALOG)) {
			dro.output.append(getSuccesResponse());
		}
		super.setPrompt(r,dro,updatedValues,promptVariable);
	}
	protected abstract String getSuccesResponse();
}
