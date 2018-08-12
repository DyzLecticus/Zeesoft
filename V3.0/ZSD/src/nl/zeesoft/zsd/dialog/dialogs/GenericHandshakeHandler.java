package nl.zeesoft.zsd.dialog.dialogs;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.dialog.DialogInstanceHandler;

public class GenericHandshakeHandler extends DialogInstanceHandler {
	@Override
	protected void setPrompt(String promptVariable) {
		ZStringSymbolParser fullName = new ZStringSymbolParser();
		String firstName = getResponseOutput().values.get(GenericHandshake.VARIABLE_FIRSTNAME).externalValue;
		String preposition = getResponseOutput().values.get(GenericHandshake.VARIABLE_PREPOSITION).externalValue;
		String lastName = getResponseOutput().values.get(GenericHandshake.VARIABLE_LASTNAME).externalValue;
		if (firstName.length()>0 && lastName.length()>0) {
			fullName.append(firstName);
			if (preposition.length()>0) {
				fullName.append(" ");
				fullName.append(preposition);
			}
			fullName.append(" ");
			fullName.append(lastName);
			setDialogVariableValue(GenericHandshake.VARIABLE_FULLNAME,fullName.toString());
		}
		super.setPrompt(promptVariable);
	}	
}
