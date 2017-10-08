package nl.zeesoft.zid.test;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zid.session.Session;
import nl.zeesoft.zid.session.SessionDialogController;

public class HandshakeDialogController extends SessionDialogController {	
	boolean promptFirstNameExplicitly = false;
	
	@Override
	public ZStringSymbolParser updatedSessionDialogVariables(Session session) {
		ZStringSymbolParser output = new ZStringSymbolParser();
		
		String fName = session.getDialogVariableValueString("firstName");
		String prepo = session.getDialogVariableValueString("preposition");
		String lName = session.getDialogVariableValueString("lastName");

		Object fullName = session.getVariables().get("fullName");
		
		if (!promptFirstNameExplicitly) {
			if (fName.toLowerCase().equals(prepo) || fName.toLowerCase().equals(lName.toLowerCase())) {
				session.getDialogVariables().put("firstName","");
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
				session.getVariables().put("fullName","");
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
				if (session.getDialog().isEnglish()) {
					output.append("Nice to interact with you again {fullName}.");
				} else if (session.getDialog().isDutch()) {
					output.append("Leuk om weer een interactie met je aan te gaan {fullName}.");
				}
			} else if (name.toString().equals("Dyz Lecticus")) {
				if (session.getDialog().isEnglish()) {
					output.append("This is going to be very confusing {fullName}.");
				} else if (session.getDialog().isDutch()) {
					output.append("Dit wordt heel verwarrend {fullName}.");
				}
			}
			session.getVariables().put("fullName",name);
			setPromptForDialogVariable("nextDialog");
		} else {
			if (fName.length()==0) {
				if (promptFirstNameExplicitly) {
					if (session.getDialog().isEnglish()) {
						output.append("What is your firstname?");
					} else if (session.getDialog().isDutch()) {
						output.append("Wat is jouw voornaam?");
					}
				}
				setPromptForDialogVariable("firstName");
			} else if (lName.length()==0) {
				setPromptForDialogVariable("lastName");
			}
		}
		return output;
	}
}
