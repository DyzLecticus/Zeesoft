package nl.zeesoft.zsd.dialog.dialogs;

import nl.zeesoft.zsd.dialog.DialogInstance;
import nl.zeesoft.zsd.dialog.DialogInstanceHandler;

public abstract class RoomBookingHandler extends DialogInstanceHandler {
	@Override
	protected void setPrompt(String promptVariable) {
		if (promptVariable.equals(RoomBooking.VARIABLE_BOOK_CONFIRMATION)) {
			if (!checkPeople()) {
				getResponseOutput().appendOutput(getMinimumPeopleResponse());
				promptVariable = RoomBooking.VARIABLE_BOOK_PEOPLE;
			}
		} else if (promptVariable.equals(DialogInstance.VARIABLE_NEXT_DIALOG)) {
			String confirmation = getResponseOutput().values.get(RoomBooking.VARIABLE_BOOK_CONFIRMATION).internalValue;
			if (getConfig().getEntityValueTranslator().getBooleanTypeValueForInternalValue(confirmation,false)) {
				getResponseOutput().appendOutput(getSuccesResponse());
			} else {
				getResponseOutput().clearDialogVariableValues();
				promptVariable = getDialog().getVariables().get(0).name;
			}
		} else {
			checkPeople();
		}
		super.setPrompt(promptVariable);
	}
	
	protected abstract String getMinimumPeopleResponse();
	
	protected abstract String getSuccesResponse();
	
	private boolean checkPeople() {
		boolean valid = true;
		String people = getResponseOutput().values.get(RoomBooking.VARIABLE_BOOK_PEOPLE).internalValue;
		if (getConfig().getEntityValueTranslator().getIntegerTypeValueForInternalValue(people,0)==1) {
			setDialogVariableValue(RoomBooking.VARIABLE_BOOK_PEOPLE,"","");
			valid = false;
		}
		return valid;
	}
}
