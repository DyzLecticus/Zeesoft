package nl.zeesoft.zids.dialog.controller;

import nl.zeesoft.zids.database.model.Human;
import nl.zeesoft.zids.database.model.Session;
import nl.zeesoft.zids.database.model.SessionDialogVariable;
import nl.zeesoft.zids.database.model.SessionVariable;
import nl.zeesoft.zids.database.model.ZIDSDataGenerator;
import nl.zeesoft.zids.database.model.ZIDSModel;
import nl.zeesoft.zids.database.pattern.PtnHuman;
import nl.zeesoft.zids.dialog.pattern.PtnManager;
import nl.zeesoft.zids.server.SvrControllerSelf;
import nl.zeesoft.zids.server.SvrControllerSessions;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.event.EvtEvent;

public class CtrHanddruk extends CtrObject {
	private ReqAdd	addHumanRequest = null;
	
	public void validateSessionDialogVariables(Session s, PtnManager m) {
		StringBuilder response = new StringBuilder();
		StringBuilder responseContext = new StringBuilder();
		
		SessionVariable interactingHuman = SvrControllerSessions.getInstance().getSessionVariableByCode(s,ZIDSDataGenerator.VARIABLE_INTERACTING_HUMAN);
		
		SessionDialogVariable human = SvrControllerSessions.getInstance().getSessionDialogVariableByCode(s,ZIDSDataGenerator.NL_HANDSHAKE_VAR_HUMAN);
		if (human!=null && human.getValue()!=null && human.getValue().length()>0) {
			String fullName = m.getPatternByClassName(PtnHuman.class.getName()).getStringForValue(human.getValue().toString());
			if (interactingHuman==null || interactingHuman.getValue().toString().equals(human.getValue().toString())) {
				if (interactingHuman==null) {
					response.append("Leuk om weer met u een interactie aan te gaan, " + human.getValue() + ". Wat kan ik voor u doen?");
					SvrControllerSessions.getInstance().addOrUpdateSessionVariable(s,ZIDSDataGenerator.VARIABLE_INTERACTING_HUMAN,human.getValue());
					SvrControllerSessions.getInstance().addOrUpdateSessionVariable(s,ZIDSDataGenerator.VARIABLE_INTERACTING_HUMAN_FULLNAME,new StringBuilder(fullName));
				} else {
					response.append("Dat weet ik, " + human.getValue() + ". Wat kan ik voor u doen?");
				}
			} else {
				response.append("Leuk om weer met u een interactie aan te gaan, " + human.getValue() + ". Wat kan ik voor u doen?");
				SvrControllerSessions.getInstance().addOrUpdateSessionVariable(s,ZIDSDataGenerator.VARIABLE_INTERACTING_HUMAN,human.getValue());
				SvrControllerSessions.getInstance().addOrUpdateSessionVariable(s,ZIDSDataGenerator.VARIABLE_INTERACTING_HUMAN_FULLNAME,new StringBuilder(fullName));
			}
			SvrControllerSessions.getInstance().removeSessionDialogVariablesFromSession(s);
			responseContext.append(ZIDSDataGenerator.NL_SELF_INTRODUCTION_CONTEXT);
		} else {
			SessionDialogVariable firstName = SvrControllerSessions.getInstance().getSessionDialogVariableByCode(s,ZIDSDataGenerator.NL_HANDSHAKE_VAR_FIRSTNAME);
			SessionDialogVariable lastName = SvrControllerSessions.getInstance().getSessionDialogVariableByCode(s,ZIDSDataGenerator.NL_HANDSHAKE_VAR_LASTNAME);
			SessionDialogVariable prep = SvrControllerSessions.getInstance().getSessionDialogVariableByCode(s,ZIDSDataGenerator.NL_HANDSHAKE_VAR_PREPOSITION);
			if (
				(firstName!=null && firstName.getValue()!=null && firstName.getValue().length()>0) &&
				(lastName!=null && lastName.getValue()!=null && lastName.getValue().length()>0)
				) {
				String fName = m.getStringValueFromPatternValue(firstName.getValue().toString());
				String lName = m.getStringValueFromPatternValue(lastName.getValue().toString());
				
				fName = CtrHandshake.upperCaseFirst(fName);
				lName = CtrHandshake.upperCaseFirst(lName);
				
				String pre = "";
				if (prep!=null && prep.getValue()!=null && prep.getValue().length()>0) {
					pre = m.getPrepositionStringValueFromPatternValue(prep.getValue().toString());
				}

				String fullName = fName + " " + pre + " "  + lName;
				if (pre.length()==0) {
					fullName = fName + " " + lName;
				}
				if (SvrControllerSelf.getInstance().getSelf().getName().toLowerCase().equals(fullName.toLowerCase())) {
					SvrControllerSessions.getInstance().addOrUpdateSessionVariable(s,ZIDSDataGenerator.VARIABLE_INTERACTING_HUMAN_FULLNAME,new StringBuilder(fullName));
					response.append("Het lijkt erop dat deze interactie erg verwarrend gaat worden.");
					responseContext.append(ZIDSDataGenerator.NL_HANDSHAKE_CONTEXT);
				} else {
					PtnHuman pattern = (PtnHuman) m.getPatternByClassName(PtnHuman.class.getName());
					if (pattern.stringMatchesPattern(fullName.toLowerCase())) {
						SvrControllerSessions.getInstance().addOrUpdateSessionDialogVariable(s,ZIDSDataGenerator.NL_HANDSHAKE_VAR_HUMAN,new StringBuilder(pattern.getValueForString(fullName)));
						validateSessionDialogVariables(s,m);
						response = getResponse();
						responseContext = getResponseContext();
					} else {
						boolean confirmed = false;
						SessionDialogVariable confirmation = SvrControllerSessions.getInstance().getSessionDialogVariableByCode(s,ZIDSDataGenerator.NL_HANDSHAKE_VAR_CONFIRMATION);
						if (confirmation!=null) {
							confirmed = m.getConfirmationValueFromPatternValue(confirmation.getValue().toString());
						}
						if (confirmation==null) {
							SvrControllerSessions.getInstance().addOrUpdateSessionVariable(s,ZIDSDataGenerator.VARIABLE_INTERACTING_HUMAN_FULLNAME,new StringBuilder(fullName));
							response.append("Aangenaam, " + fullName + ". Zal ik u onthouden voor toekomstige interacties?");
							responseContext.append(ZIDSDataGenerator.NL_HANDSHAKE_VAR_CONFIRMATION_CONTEXT);
						} else if (!confirmed) {
							SvrControllerSessions.getInstance().removeSessionDialogVariablesFromSession(s);
							response.append("Geen probleem. Wat kan ik voor u doen?");
							responseContext.append(ZIDSDataGenerator.NL_SELF_INTRODUCTION_CONTEXT);
						} else {
							Human h = new Human();
							h.setName(fullName);
							addHumanRequest = new ReqAdd(ZIDSModel.HUMAN_CLASS_FULL_NAME);
							addHumanRequest.getObjects().add(new ReqDataObject(h.toDataObject()));
							addHumanRequest.addSubscriber(this);
							setDone(false);
							DbRequestQueue.getInstance().addRequest(addHumanRequest,this);
							waitTillDone();
							pattern.initializePatternStrings(m);
							if (addHumanRequest.getImpactedIds().size()>0) {
								String value = pattern.getValuePrefix() + ":" + addHumanRequest.getImpactedIds().get(0);
								SvrControllerSessions.getInstance().addOrUpdateSessionVariable(s,ZIDSDataGenerator.VARIABLE_INTERACTING_HUMAN,new StringBuilder(value));
								SvrControllerSessions.getInstance().addOrUpdateSessionVariable(s,ZIDSDataGenerator.VARIABLE_INTERACTING_HUMAN_FULLNAME,new StringBuilder(fullName));
								SvrControllerSessions.getInstance().removeSessionDialogVariablesFromSession(s);
								response.append("Geregeld. Wat kan ik voor u doen?");
								responseContext.append(ZIDSDataGenerator.NL_HANDSHAKE_VAR_CONFIRMATION_CONTEXT);
							} else {
								response.append("It seems we have already met, can you repeat your full name?");
								responseContext.append(ZIDSDataGenerator.NL_HANDSHAKE_VAR_HUMAN_CONTEXT);
							}
						}
					}
				}
			}
		}
		
		setResponse(response);
		setResponseContext(responseContext);
	}

	@Override
	public void handleEvent(EvtEvent e) {
		if (e.getValue()!=null && addHumanRequest!=null && e.getValue()==addHumanRequest) {
			setDone(true);
		}
	}
}
