package nl.zeesoft.zsd.dialog.dialogs;

import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.dialog.DialogInstanceHandler;
import nl.zeesoft.zsd.dialog.DialogResponse;
import nl.zeesoft.zsd.dialog.DialogResponseOutput;
import nl.zeesoft.zsd.dialog.DialogVariableValue;

public class GenericHandshakeHandler extends DialogInstanceHandler {
	@Override
	protected void setPrompt(DialogResponse r,DialogResponseOutput dro,List<DialogVariableValue> updatedValues,String promptVariable) {
		ZStringSymbolParser fullName = new ZStringSymbolParser();
		String firstName = dro.values.get(GenericHandshake.VARIABLE_FIRSTNAME).externalValue;
		String preposition = dro.values.get(GenericHandshake.VARIABLE_PREPOSITION).externalValue;
		String lastName = dro.values.get(GenericHandshake.VARIABLE_LASTNAME).externalValue;
		if (firstName.length()>0 && lastName.length()>0) {
			fullName.append(firstName);
			if (preposition.length()>0) {
				fullName.append(" ");
				fullName.append(preposition);
			}
			fullName.append(" ");
			fullName.append(lastName);
			dro.setDialogVariableValue(r,GenericHandshake.VARIABLE_FULLNAME,fullName.toString());
		}
		super.setPrompt(r,dro,updatedValues,promptVariable);
	}	
}
