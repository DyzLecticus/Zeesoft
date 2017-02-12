package nl.zeesoft.zid.test;

import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zid.dialog.DialogControllerObject;
import nl.zeesoft.zid.dialog.DialogHandler;
import nl.zeesoft.zspr.Language;

public class HandshakeController extends DialogControllerObject {	
	boolean promptFirstNameExplicitly = false;

	@Override
	public void updatedVariables(DialogHandler handler, Dialog dialog) {
		String fName = getDialogVariableValueString(handler,dialog.getVariable("firstName"));
		String prepo = getDialogVariableValueString(handler,dialog.getVariable("preposition"));
		String lName = getDialogVariableValueString(handler,dialog.getVariable("lastName"));

		Object fullName = getVariable(handler,"fullName");
		
		if (!promptFirstNameExplicitly) {
			if (fName.toLowerCase().equals(prepo) || fName.toLowerCase().equals(lName.toLowerCase())) {
				setDialogVariable(handler,"firstName","");
				fName = "";
			}
			if (lName.length()>0 && fName.length()==0) {
				 promptFirstNameExplicitly = true;
			}
		} else {
			promptFirstNameExplicitly = false;
		}
		
		if (fName.length()>0) {
			fName = fName.substring(0,1).toUpperCase() + fName.substring(1);
			if (fullName!=null && !fullName.toString().startsWith(fName)) {
				setVariable(handler,"fullName","");
			}
		}
		if (lName.length()>0) {
			lName = lName.substring(0,1).toUpperCase() + lName.substring(1);
		}
		
		if (fName.length()>0 && lName.length()>0) {
			StringBuilder name = new StringBuilder(fName);
			if (prepo.length()>0) {
				name.append(" ");
				name.append(prepo);
			}
			name.append(" ");
			name.append(lName);
			
			if (name.toString().equals("Andre van der Zee")) {
				if (dialog.getLanguage().getCode().equals(Language.ENG)) {
					getOutput().append("Nice to interact with you again {fullName}.");
				} else if (dialog.getLanguage().getCode().equals(Language.NLD)) {
					getOutput().append("Leuk om weer een interactie met je aan te gaan {fullName}.");
				}
			} else if (name.toString().equals("Dyz Lecticus")) {
				if (dialog.getLanguage().getCode().equals(Language.ENG)) {
					getOutput().append("This is going to be very confusing {fullName}.");
				} else if (dialog.getLanguage().getCode().equals(Language.NLD)) {
					getOutput().append("Dit wordt heel verwarrend {fullName}.");
				}
			}
			setVariable(handler,"fullName",name);
			setPromptForDialogVariable("nextDialog");
			setCompleted(true);
		} else {
			if (fName.length()==0) {
				if (promptFirstNameExplicitly) {
					if (dialog.getLanguage().getCode().equals(Language.ENG)) {
						getOutput().append("What is your firstname?");
					} else if (dialog.getLanguage().getCode().equals(Language.NLD)) {
						getOutput().append("Wat is jouw voornaam?");
					}
				}
				setPromptForDialogVariable("firstName");
			} else if (lName.length()==0) {
				setPromptForDialogVariable("lastName");
			}
		}
	}
}
