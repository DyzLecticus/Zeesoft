package nl.zeesoft.zsd.dialog.dialogs;

import java.util.List;

import nl.zeesoft.zsd.dialog.DialogInstance;
import nl.zeesoft.zsd.dialog.DialogInstanceHandler;
import nl.zeesoft.zsd.dialog.DialogResponse;
import nl.zeesoft.zsd.dialog.DialogResponseOutput;
import nl.zeesoft.zsd.dialog.DialogVariableValue;

public abstract class RoomBookingHandler extends DialogInstanceHandler {
	@Override
	protected void setPrompt(DialogResponse r,DialogResponseOutput dro,List<DialogVariableValue> updatedValues,String promptVariable) {
		if (promptVariable.equals(DialogInstance.VARIABLE_NEXT_DIALOG)) {
			String confirmation = dro.values.get(RoomBooking.VARIABLE_BOOK_CONFIRMATION).internalValue;
			if (getConfig().getEntityValueTranslator().getBooleanTypeValueForInternalValue(confirmation,false)) {
				dro.output.append(getSuccesResponse());
			}
		}
		super.setPrompt(r,dro,updatedValues,promptVariable);
	}
	protected abstract String getSuccesResponse();
}
