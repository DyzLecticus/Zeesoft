package nl.zeesoft.zsd.dialog.dialogs;

import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.dialog.DialogInstanceHandler;
import nl.zeesoft.zsd.dialog.DialogResponse;
import nl.zeesoft.zsd.dialog.DialogVariableValue;

public class GenericHandshakeHandler extends DialogInstanceHandler {
	@Override
	protected ZStringSymbolParser updatedValues(DialogResponse r,List<DialogVariableValue> updatedValues,String promptVariable) {
		ZStringSymbolParser fullName = new ZStringSymbolParser();
		String firstName = getValues().get(GenericHandshake.VARIABLE_FIRSTNAME).externalValue;
		String preposition = getValues().get(GenericHandshake.VARIABLE_PREPOSITION).externalValue;
		String lastName = getValues().get(GenericHandshake.VARIABLE_LASTNAME).externalValue;
		if (firstName.length()>0 && lastName.length()>0) {
			fullName.append(firstName);
			if (preposition.length()>0) {
				fullName.append(" ");
				fullName.append(preposition);
			}
			fullName.append(" ");
			fullName.append(lastName);
			setDialogVariableValue(r,GenericHandshake.VARIABLE_FULLNAME,fullName.toString());
		}
		return super.updatedValues(r,updatedValues,promptVariable);
	}	
}
