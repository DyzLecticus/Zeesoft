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
		if (promptVariable.equals(RoomBooking.VARIABLE_BOOK_CONFIRMATION)) {
			if (!checkPeople(r,dro)) {
				dro.appendOutput(getMinimumPeopleResponse());
				promptVariable = RoomBooking.VARIABLE_BOOK_PEOPLE;
			}
		} else if (promptVariable.equals(DialogInstance.VARIABLE_NEXT_DIALOG)) {
			String confirmation = dro.values.get(RoomBooking.VARIABLE_BOOK_CONFIRMATION).internalValue;
			if (getConfig().getEntityValueTranslator().getBooleanTypeValueForInternalValue(confirmation,false)) {
				dro.appendOutput(getSuccesResponse());
			} else {
				dro.clearDialogVariableValues();
			}
		} else {
			checkPeople(r,dro);
		}
		super.setPrompt(r,dro,updatedValues,promptVariable);
	}
	
	protected abstract String getMinimumPeopleResponse();
	
	protected abstract String getSuccesResponse();
	
	private boolean checkPeople(DialogResponse r,DialogResponseOutput dro) {
		boolean valid = true;
		String people = dro.values.get(RoomBooking.VARIABLE_BOOK_PEOPLE).internalValue;
		if (getConfig().getEntityValueTranslator().getIntegerTypeValueForInternalValue(people,0)==1) {
			dro.setDialogVariableValue(r,RoomBooking.VARIABLE_BOOK_PEOPLE,"","");
			valid = false;
		}
		return valid;
	}
}
