package nl.zeesoft.zids.database.model;

import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zacs.database.model.Assignment;
import nl.zeesoft.zacs.database.model.Control;
import nl.zeesoft.zacs.database.model.Symbol;
import nl.zeesoft.zacs.database.model.ZACSDataGenerator;
import nl.zeesoft.zids.dialog.controller.CtrHanddruk;
import nl.zeesoft.zids.dialog.controller.CtrHandshake;
import nl.zeesoft.zids.dialog.pattern.PtnObject;
import nl.zeesoft.zids.server.SvrControllerSelf;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.database.request.ReqObject;
import nl.zeesoft.zodb.event.EvtEvent;

public class ZIDSDataGenerator extends ZACSDataGenerator {
	private ReqAdd					addHumanRequest 						= new ReqAdd(ZIDSModel.HUMAN_CLASS_FULL_NAME);
	
	private ReqAdd					addVariableTypeRequest 					= new ReqAdd(ZIDSModel.VARIABLE_TYPE_CLASS_FULL_NAME);
	private ReqGet					getVariableTypeRequest 					= new ReqGet(ZIDSModel.VARIABLE_TYPE_CLASS_FULL_NAME);
	private ReqAdd					addDialogRequest 						= new ReqAdd(ZIDSModel.DIALOG_CLASS_FULL_NAME);
	private ReqGet					getDialogRequest 						= new ReqGet(ZIDSModel.DIALOG_CLASS_FULL_NAME);
	private ReqAdd					addDialogExampleRequest 				= new ReqAdd(ZIDSModel.DIALOG_EXAMPLE_CLASS_FULL_NAME);
	private ReqAdd					addDialogVariableRequest 				= new ReqAdd(ZIDSModel.DIALOG_VARIABLE_CLASS_FULL_NAME);
	private ReqGet					getDialogVariableRequest 				= new ReqGet(ZIDSModel.DIALOG_VARIABLE_CLASS_FULL_NAME);
	private ReqAdd					addDialogVariableExampleRequest 		= new ReqAdd(ZIDSModel.DIALOG_VARIABLE_EXAMPLE_CLASS_FULL_NAME);
	private ReqAdd					addAssignmentRequest 					= new ReqAdd(ZIDSModel.ASSIGNMENT_CLASS_FULL_NAME);

	private Self 					self									= null;
	private String					selfPattern								= null;
	
	public final static String		VARIABLE_INTERACTING_HUMAN				= "{INTERACTING_HUMAN}";
	public final static String		VARIABLE_INTERACTING_HUMAN_FULLNAME		= "{INTERACTING_HUMAN_FULLNAME}";
	
	public final static String		EN_SELF_INTRODUCTION					= "Self introduction";
	public final static String		EN_SELF_INTRODUCTION_CONTEXT			= "selfIntroduction";
	public final static String		EN_SELF_INTRODUCTION_INTRO1				= "Hello, how can I be of service to you?";
	public final static String		EN_SELF_INTRODUCTION_INTRO2				= "Hello, what can I do for you?";
	public final static String		EN_SELF_INTRODUCTION_INTRO3				= "Hello, how may I help you?";
	public final static String		EN_SELF_INTRODUCTION_RETURN1			= "You're welcome.";
	public final static String		EN_SELF_INTRODUCTION_RETURN2			= "No problem.";
	public final static String		EN_SELF_INTRODUCTION_RETURN3			= "You are very welcome.";
	
	public final static String		NL_SELF_INTRODUCTION					= "Zelf introductie";
	public final static String		NL_SELF_INTRODUCTION_CONTEXT			= "zelfIntroductie";
	public final static String		NL_SELF_INTRODUCTION_INTRO1				= "Hallo, wat kan ik voor u betekenen?";
	public final static String		NL_SELF_INTRODUCTION_INTRO2				= "Hallo, wat kan ik voor u doen?";
	public final static String		NL_SELF_INTRODUCTION_INTRO3				= "Hallo, kan ik u ergens mee helpen?";
	public final static String		NL_SELF_INTRODUCTION_RETURN1			= "Graag gedaan.";
	public final static String		NL_SELF_INTRODUCTION_RETURN2			= "Geen probleem.";
	public final static String		NL_SELF_INTRODUCTION_RETURN3			= "Alstublieft.";

	public final static String		EN_HANDSHAKE							= "Handshake";
	public final static String		EN_HANDSHAKE_CONTEXT					= "handshake";

	public final static String		EN_HANDSHAKE_VAR_HUMAN					= "{HANDSHAKE_HUMAN}";
	public final static String		EN_HANDSHAKE_VAR_FIRSTNAME				= "{HANDSHAKE_FIRSTNAME}";
	public final static String		EN_HANDSHAKE_VAR_LASTNAME				= "{HANDSHAKE_LASTNAME}";
	public final static String		EN_HANDSHAKE_VAR_PREPOSITION			= "{HANDSHAKE_PREPOSITION}";
	public final static String		EN_HANDSHAKE_VAR_CONFIRMATION			= "{HANDSHAKE_CONFIRMATION}";

	public final static String		EN_HANDSHAKE_VAR_HUMAN_CONTEXT			= "handshakeHuman";
	public final static String		EN_HANDSHAKE_VAR_FIRSTNAME_CONTEXT		= "handshakeFirstName";
	public final static String		EN_HANDSHAKE_VAR_LASTNAME_CONTEXT		= "handshakeLastName";
	public final static String		EN_HANDSHAKE_VAR_PREPOSITION_CONTEXT	= "handshakePreposition";
	public final static String		EN_HANDSHAKE_VAR_CONFIRMATION_CONTEXT	= "handshakeConfirmation";

	public final static String		NL_HANDSHAKE							= "Handdruk";
	public final static String		NL_HANDSHAKE_CONTEXT					= "handdruk";

	public final static String		NL_HANDSHAKE_VAR_HUMAN					= "{HANDDRUK_MENS}";
	public final static String		NL_HANDSHAKE_VAR_FIRSTNAME				= "{HANDDRUK_VOORNAAM}";
	public final static String		NL_HANDSHAKE_VAR_LASTNAME				= "{HANDDRUK_ACHTERNAAM}";
	public final static String		NL_HANDSHAKE_VAR_PREPOSITION			= "{HANDDRUK_PREPOSITIE}";
	public final static String		NL_HANDSHAKE_VAR_CONFIRMATION			= "{HANDDRUK_BEVESTIGING}";

	public final static String		NL_HANDSHAKE_VAR_HUMAN_CONTEXT			= "handdrukMens";
	public final static String		NL_HANDSHAKE_VAR_FIRSTNAME_CONTEXT		= "handdrukVoornaam";
	public final static String		NL_HANDSHAKE_VAR_LASTNAME_CONTEXT		= "handdrukAchternaam";
	public final static String		NL_HANDSHAKE_VAR_PREPOSITION_CONTEXT	= "handdrukPrepositie";
	public final static String		NL_HANDSHAKE_VAR_CONFIRMATION_CONTEXT	= "handdrukBevestigin";
	
	private SortedMap<String,Long>	variableTypeNameIdMap					= new TreeMap<String,Long>();	

	protected void addHumansToAddHumanRequest(ReqAdd addRequest) {
		addHumanToAddHumanRequest(addRequest,"Andre van der Zee","");
		addHumanToAddHumanRequest(addRequest,"Noam Chomsky","");
		addHumanToAddHumanRequest(addRequest,"Douglas Hofstadter","");
		addHumanToAddHumanRequest(addRequest,"Daniel Dennet","");
		addHumanToAddHumanRequest(addRequest,"Robert Hecht-Nielsen","");
	}
	
	protected void addDialogsToAddDialogRequest(ReqAdd addRequest) {
		Dialog d = null;
		d = new Dialog();
		d.setName(EN_SELF_INTRODUCTION);
		d.setContextSymbol(EN_SELF_INTRODUCTION_CONTEXT);
		addRequest.getObjects().add(new ReqDataObject(d.toDataObject()));
		
		d = new Dialog();
		d.setName(NL_SELF_INTRODUCTION);
		d.setContextSymbol(NL_SELF_INTRODUCTION_CONTEXT);
		addRequest.getObjects().add(new ReqDataObject(d.toDataObject()));

		d = new Dialog();
		d.setName(EN_HANDSHAKE);
		d.setContextSymbol(EN_HANDSHAKE_CONTEXT);
		d.setControllerClassName(CtrHandshake.class.getName());
		addRequest.getObjects().add(new ReqDataObject(d.toDataObject()));

		d = new Dialog();
		d.setName(NL_HANDSHAKE);
		d.setContextSymbol(NL_HANDSHAKE_CONTEXT);
		d.setControllerClassName(CtrHanddruk.class.getName());
		addRequest.getObjects().add(new ReqDataObject(d.toDataObject()));
	}

	protected void addedDialog(Dialog d,ReqAdd addExampleRequest,ReqAdd addVariableRequest) {
		if (d.getName().equals(EN_SELF_INTRODUCTION)) {
			for (int i = 0; i < Symbol.LINE_END_SYMBOLS.length; i++) {
				String lineEnd = Symbol.LINE_END_SYMBOLS[i];
				for (int i2 = 0; i2 < 5; i2++) {
					String greeting = "Hello";
					if (i2==1) {
						greeting = "Hi";
					} else if (i2==2) {
						greeting = "Good morning";
					} else if (i2==3) {
						greeting = "Good afternoon";
					} else if (i2==4) {
						greeting = "Good evening";
					}
					addDialogExampleToRequestForDialogId(d.getId(),greeting + lineEnd,EN_SELF_INTRODUCTION_INTRO1);
					addDialogExampleToRequestForDialogId(d.getId(),greeting + lineEnd,EN_SELF_INTRODUCTION_INTRO2);
					addDialogExampleToRequestForDialogId(d.getId(),greeting + lineEnd,EN_SELF_INTRODUCTION_INTRO3);
				}
				for (int i2 = 0; i2 < 3; i2++) {
					String thankYou = "Thank you";
					if (i2==1) {
						thankYou = "Thanks";
					} else if (i2==2) {
						thankYou = "Thank you very much";
					}
					addDialogExampleToRequestForDialogId(d.getId(),thankYou + lineEnd,EN_SELF_INTRODUCTION_RETURN1);
					addDialogExampleToRequestForDialogId(d.getId(),thankYou + lineEnd,EN_SELF_INTRODUCTION_RETURN2);
					addDialogExampleToRequestForDialogId(d.getId(),thankYou + lineEnd,EN_SELF_INTRODUCTION_RETURN3);
				}				
			}
		} else if (d.getName().equals(NL_SELF_INTRODUCTION)) {
			for (int i = 0; i < Symbol.LINE_END_SYMBOLS.length; i++) {
				String lineEnd = Symbol.LINE_END_SYMBOLS[i];
				for (int i2 = 0; i2 < 5; i2++) {
					String greeting = "Hallo";
					if (i2==1) {
						greeting = "Hoi";
					} else if (i2==2) {
						greeting = "Goedemorgen";
					} else if (i2==3) {
						greeting = "Goedemiddag";
					} else if (i2==4) {
						greeting = "Goedenavond";
					}
					addDialogExampleToRequestForDialogId(d.getId(),greeting + lineEnd,NL_SELF_INTRODUCTION_INTRO1);
					addDialogExampleToRequestForDialogId(d.getId(),greeting + lineEnd,NL_SELF_INTRODUCTION_INTRO2);
					addDialogExampleToRequestForDialogId(d.getId(),greeting + lineEnd,NL_SELF_INTRODUCTION_INTRO3);
				}
				for (int i2 = 0; i2 < 6; i2++) {
					String thankYou = "Dank je";
					if (i2==1) {
						thankYou = "Dankjewel";
					} else if (i2==2) {
						thankYou = "Dank je wel";
					} else if (i2==3) {
						thankYou = "Dank u";
					} else if (i2==3) {
						thankYou = "Dank u wel";
					} else if (i2==4) {
						thankYou = "Dankuwel";
					} else if (i2==5) {
						thankYou = "Bedankt";
					}
					addDialogExampleToRequestForDialogId(d.getId(),thankYou + lineEnd,NL_SELF_INTRODUCTION_RETURN1);
					addDialogExampleToRequestForDialogId(d.getId(),thankYou + lineEnd,NL_SELF_INTRODUCTION_RETURN2);
					addDialogExampleToRequestForDialogId(d.getId(),thankYou + lineEnd,NL_SELF_INTRODUCTION_RETURN3);
				}
			}
		} else if (d.getName().equals(EN_HANDSHAKE)) {
			for (int i = 0; i < Symbol.LINE_END_SYMBOLS.length; i++) {
				String lineEnd = Symbol.LINE_END_SYMBOLS[i];
				for (int i2 = 0; i2 < 4; i2++) {
					String name = EN_HANDSHAKE_VAR_HUMAN;
					String out = "Nice to interact with you again, " + EN_HANDSHAKE_VAR_HUMAN + ".";
					if (i2==1) {
						name = EN_HANDSHAKE_VAR_FIRSTNAME;
						out = "Nice to interact with you again, " + EN_HANDSHAKE_VAR_FIRSTNAME + ".";
					} else if (i2==2) {
						name = EN_HANDSHAKE_VAR_FIRSTNAME + " " + EN_HANDSHAKE_VAR_LASTNAME;
						out = "Nice to meet you.";
					} else if (i2==3) {
						name = EN_HANDSHAKE_VAR_FIRSTNAME + " " + EN_HANDSHAKE_VAR_PREPOSITION + " " + EN_HANDSHAKE_VAR_LASTNAME;
						out = "Nice to meet you.";
					}
					if (!lineEnd.equals("?")) {
						addDialogExampleToRequestForDialogId(d.getId(),"It's " + name + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"It is " + name + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"My name is " + name + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"I am " + name + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"I'm " + name + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Greetings my name is " + name + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Greetings, my name is " + name + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Greetings I am " + name + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Greetings, I'm " + name + lineEnd,out);
					} else {
						addDialogExampleToRequestForDialogId(d.getId(),"My name is " + name + ", what's yours" + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"My name is " + name + ", what is yours" + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"I am " + name + ", who are you" + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"I'm " + name + ", who are you"+ lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"My name is " + name + lineEnd + " what's yours" + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"My name is " + name + lineEnd + " what is yours" + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"I am " + name + lineEnd + " who are you" + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"I'm " + name + lineEnd + " who are you"+ lineEnd,out);
					}
				}
			}
			
			addDialogVariableToRequestForDialogId(d.getId(),EN_HANDSHAKE_VAR_FIRSTNAME,PtnObject.TYPE_ALPHABETIC,EN_HANDSHAKE_VAR_FIRSTNAME_CONTEXT,
				"My name is " + selfPattern + ", what is yours?",
				"I am " + selfPattern + ", what is yours?",
				"I am " + selfPattern + ", who are you?"
				);
			addDialogVariableToRequestForDialogId(d.getId(),EN_HANDSHAKE_VAR_LASTNAME,PtnObject.TYPE_ALPHABETIC,EN_HANDSHAKE_VAR_LASTNAME_CONTEXT,
				"What is your lastname?","What's your lastname?","Your lastname?"
				);
			addDialogVariableToRequestForDialogId(d.getId(),EN_HANDSHAKE_VAR_CONFIRMATION,PtnObject.TYPE_CONFIRMATION,EN_HANDSHAKE_VAR_CONFIRMATION_CONTEXT,
				"Would you like me to rememeber you for future interactions?",
				"Do you want me to remember you for future interactions?",
				"Shall I rememeber you for future interactions?"
				);
			addDialogVariableToRequestForDialogId(d.getId(),EN_HANDSHAKE_VAR_PREPOSITION,PtnObject.TYPE_PREPOSITION,EN_HANDSHAKE_VAR_PREPOSITION_CONTEXT,
				"What is your prepostion?","What's your prepostion?","Your lastname preposition?"
				);
			addDialogVariableToRequestForDialogId(d.getId(),EN_HANDSHAKE_VAR_HUMAN,PtnObject.TYPE_OBJECT,EN_HANDSHAKE_VAR_HUMAN_CONTEXT,
				"My name is " + selfPattern + ", what is yours?",
				"I am " + selfPattern + ", what is yours?",
				"I am " + selfPattern + ", who are you?"
				);
		} else if (d.getName().equals(NL_HANDSHAKE)) {
			for (int i = 0; i < Symbol.LINE_END_SYMBOLS.length; i++) {
				String lineEnd = Symbol.LINE_END_SYMBOLS[i];
				for (int i2 = 0; i2 < 4; i2++) {
					String name = NL_HANDSHAKE_VAR_HUMAN;
					String out = "Leuk om weer met u een interactie aan te gaan, " + NL_HANDSHAKE_VAR_HUMAN + ".";
					if (i2==1) {
						name = NL_HANDSHAKE_VAR_FIRSTNAME;
						out = "Leuk om weer met u een interactie aan te gaan, " + NL_HANDSHAKE_VAR_FIRSTNAME + ".";
					} else if (i2==2) {
						name = NL_HANDSHAKE_VAR_FIRSTNAME + " " + NL_HANDSHAKE_VAR_LASTNAME;
						out = "Aangenaam.";
					} else if (i2==3) {
						name = NL_HANDSHAKE_VAR_FIRSTNAME + " " + NL_HANDSHAKE_VAR_PREPOSITION + " " + NL_HANDSHAKE_VAR_LASTNAME;
						out = "Aangenaam.";
					}
					if (!lineEnd.equals("?")) {
						addDialogExampleToRequestForDialogId(d.getId(),"Mijn naam is " + name + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Ik ben " + name + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Ik heet " + name + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Gegroet, mijn naam is " + name + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Gegroet, ik ben " + name + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Gegroet, ik heet " + name + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Gegroet mijn naam is " + name + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Gegroet ik ben " + name + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Gegroet ik heet " + name + lineEnd,out);
					} else {
						addDialogExampleToRequestForDialogId(d.getId(),name + ", wat is de uwe" + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),name + ", wat is die van u" + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),name + ", wat is uw naam" + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),name + ", wat is de jouwe" + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),name + ", wat is die van jouw" + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),name + ", wat is jouw naam" + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Ik ben " + name + ", wie bent u" + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Ik ben " + name + ", wie ben jij" + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Ik heet " + name + ", hoe heet u" + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Ik heet " + name + ", hoe heet jij" + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Mijn naam is " + name + lineEnd + " wat is de uwe" + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Mijn naam is " + name + lineEnd + " wat is die van u" + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Mijn naam is " + name + lineEnd + " wat is uw naam" + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Mijn naam is " + name + lineEnd + " wat is de jouwe" + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Mijn naam is " + name + lineEnd + " wat is die van jouw" + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Mijn naam is " + name + lineEnd + " wat is jouw naam" + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Ik ben " + name + lineEnd + " wie bent u" + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Ik ben " + name + lineEnd + " wie ben je" + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Ik heet " + name + lineEnd + " hoe heet u" + lineEnd,out);
						addDialogExampleToRequestForDialogId(d.getId(),"Ik heet " + name + lineEnd + " hoe heet je" + lineEnd,out);
					}
				}
			}
			
			addDialogVariableToRequestForDialogId(d.getId(),NL_HANDSHAKE_VAR_FIRSTNAME,PtnObject.TYPE_ALPHABETIC,NL_HANDSHAKE_VAR_FIRSTNAME_CONTEXT,
				"Mijn naam is " + selfPattern + ", wat is die van u?",
				"Ik heet " + selfPattern + ", hoe heet u?",
				"I ben " + selfPattern + ", wie bent u?"
				);
			addDialogVariableToRequestForDialogId(d.getId(),NL_HANDSHAKE_VAR_LASTNAME,PtnObject.TYPE_ALPHABETIC,NL_HANDSHAKE_VAR_LASTNAME_CONTEXT,
				"Wat is uw achternaam?","Wat is uw volledige achternaam?","Uw achternaam?"
				);
			addDialogVariableToRequestForDialogId(d.getId(),NL_HANDSHAKE_VAR_CONFIRMATION,PtnObject.TYPE_CONFIRMATION,NL_HANDSHAKE_VAR_CONFIRMATION_CONTEXT,
				"Wilt u dat ik u onthoud voor toekomstige interacties?",
				"Moet ik u onthouden voor toekomstige interacties?",
				"zal ik u onthouden voor toekomstige interacties?"
				);
			addDialogVariableToRequestForDialogId(d.getId(),NL_HANDSHAKE_VAR_PREPOSITION,PtnObject.TYPE_PREPOSITION,NL_HANDSHAKE_VAR_PREPOSITION_CONTEXT,
				"What is uw prepositie?","Wat is uw achternaam prepositie?","Uw achternaam prepositie?"
				);
			addDialogVariableToRequestForDialogId(d.getId(),NL_HANDSHAKE_VAR_HUMAN,PtnObject.TYPE_OBJECT,NL_HANDSHAKE_VAR_HUMAN_CONTEXT,
				"Mijn naam is " + selfPattern + ", wat is die van u?",
				"Ik heet " + selfPattern + ", hoe heet u?",
				"I ben " + selfPattern + ", wie bent u?"
				);
		}
	}
	
	protected void addedDialogVariable(DialogVariable dv,ReqAdd addVariableExampleRequest) {
		for (int i = 0; i < Symbol.LINE_END_SYMBOLS.length; i++) {
			String lineEnd = Symbol.LINE_END_SYMBOLS[i];
			if (!lineEnd.equals("?")) {
				addDialogVariableExampleToRequestForDialogVariable(dv,dv.getCode() + lineEnd);
				// ENGLISH
				if (dv.getCode().equals(EN_HANDSHAKE_VAR_HUMAN) || dv.getCode().equals(EN_HANDSHAKE_VAR_FIRSTNAME)) {
					addDialogVariableExampleToRequestForDialogVariable(dv,"My name is " + EN_HANDSHAKE_VAR_HUMAN + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"I am " + EN_HANDSHAKE_VAR_HUMAN + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"It's " + EN_HANDSHAKE_VAR_HUMAN + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"It is " + EN_HANDSHAKE_VAR_HUMAN + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,EN_HANDSHAKE_VAR_FIRSTNAME + " " + EN_HANDSHAKE_VAR_LASTNAME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,EN_HANDSHAKE_VAR_FIRSTNAME + " " + EN_HANDSHAKE_VAR_PREPOSITION + " " + EN_HANDSHAKE_VAR_LASTNAME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"My name is " + EN_HANDSHAKE_VAR_FIRSTNAME + " " + EN_HANDSHAKE_VAR_LASTNAME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"My name is " + EN_HANDSHAKE_VAR_FIRSTNAME + " " + EN_HANDSHAKE_VAR_PREPOSITION + " " + EN_HANDSHAKE_VAR_LASTNAME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"I am " + EN_HANDSHAKE_VAR_FIRSTNAME + " " + EN_HANDSHAKE_VAR_LASTNAME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"I am " + EN_HANDSHAKE_VAR_FIRSTNAME + " " + EN_HANDSHAKE_VAR_PREPOSITION + " " + EN_HANDSHAKE_VAR_LASTNAME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"It's " + EN_HANDSHAKE_VAR_FIRSTNAME + " " + EN_HANDSHAKE_VAR_LASTNAME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"It's " + EN_HANDSHAKE_VAR_FIRSTNAME + " " + EN_HANDSHAKE_VAR_PREPOSITION + " " + EN_HANDSHAKE_VAR_LASTNAME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"It is " + EN_HANDSHAKE_VAR_FIRSTNAME + " " + EN_HANDSHAKE_VAR_LASTNAME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"It is " + EN_HANDSHAKE_VAR_FIRSTNAME + " " + EN_HANDSHAKE_VAR_PREPOSITION + " " + EN_HANDSHAKE_VAR_LASTNAME + lineEnd);
				} else if (dv.getCode().equals(EN_HANDSHAKE_VAR_LASTNAME)) {
					addDialogVariableExampleToRequestForDialogVariable(dv,EN_HANDSHAKE_VAR_PREPOSITION + " " + EN_HANDSHAKE_VAR_LASTNAME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,EN_HANDSHAKE_VAR_LASTNAME + ", " + EN_HANDSHAKE_VAR_PREPOSITION + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"My lastname is " + EN_HANDSHAKE_VAR_LASTNAME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"My lastname is " + EN_HANDSHAKE_VAR_PREPOSITION + " " + EN_HANDSHAKE_VAR_LASTNAME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"My lastname is " + EN_HANDSHAKE_VAR_LASTNAME + ", " + EN_HANDSHAKE_VAR_PREPOSITION + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"It's " + EN_HANDSHAKE_VAR_LASTNAME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"It's " + EN_HANDSHAKE_VAR_PREPOSITION + " " + EN_HANDSHAKE_VAR_LASTNAME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"It's " + EN_HANDSHAKE_VAR_LASTNAME + ", " + EN_HANDSHAKE_VAR_PREPOSITION + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"It is " + EN_HANDSHAKE_VAR_LASTNAME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"It is " + EN_HANDSHAKE_VAR_PREPOSITION + " " + EN_HANDSHAKE_VAR_LASTNAME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"It is " + EN_HANDSHAKE_VAR_LASTNAME + ", " + EN_HANDSHAKE_VAR_PREPOSITION + lineEnd);
				} else if (dv.getCode().equals(EN_HANDSHAKE_VAR_PREPOSITION)) {
					addDialogVariableExampleToRequestForDialogVariable(dv,"My preposition is " + EN_HANDSHAKE_VAR_PREPOSITION + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"It's " + EN_HANDSHAKE_VAR_PREPOSITION + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"It is " + EN_HANDSHAKE_VAR_LASTNAME + lineEnd);
				// DUTCH
				} else if (dv.getCode().equals(NL_HANDSHAKE_VAR_HUMAN) || dv.getCode().equals(NL_HANDSHAKE_VAR_FIRSTNAME)) {
					addDialogVariableExampleToRequestForDialogVariable(dv,"Mijn naam is " + NL_HANDSHAKE_VAR_HUMAN + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"Ik ben " + NL_HANDSHAKE_VAR_HUMAN + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,NL_HANDSHAKE_VAR_FIRSTNAME + " " + NL_HANDSHAKE_VAR_LASTNAME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,NL_HANDSHAKE_VAR_FIRSTNAME + " " + NL_HANDSHAKE_VAR_PREPOSITION + " " + NL_HANDSHAKE_VAR_LASTNAME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"Mijn naam is " + NL_HANDSHAKE_VAR_FIRSTNAME + " " + NL_HANDSHAKE_VAR_LASTNAME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"Mijn naam is " + NL_HANDSHAKE_VAR_FIRSTNAME + " " + NL_HANDSHAKE_VAR_PREPOSITION + " " + NL_HANDSHAKE_VAR_LASTNAME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"Ik ben " + NL_HANDSHAKE_VAR_FIRSTNAME + " " + NL_HANDSHAKE_VAR_LASTNAME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"Ik ben " + NL_HANDSHAKE_VAR_FIRSTNAME + " " + NL_HANDSHAKE_VAR_PREPOSITION + " " + NL_HANDSHAKE_VAR_LASTNAME + lineEnd);
				} else if (dv.getCode().equals(NL_HANDSHAKE_VAR_LASTNAME)) {
					addDialogVariableExampleToRequestForDialogVariable(dv,NL_HANDSHAKE_VAR_PREPOSITION + " " + NL_HANDSHAKE_VAR_LASTNAME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,NL_HANDSHAKE_VAR_LASTNAME + ", " + NL_HANDSHAKE_VAR_PREPOSITION + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"Mijn achternaam is " + NL_HANDSHAKE_VAR_LASTNAME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"Mijn achternaam is " + NL_HANDSHAKE_VAR_PREPOSITION + " " + NL_HANDSHAKE_VAR_LASTNAME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"Mijn achternaam is " + NL_HANDSHAKE_VAR_LASTNAME + ", " + NL_HANDSHAKE_VAR_PREPOSITION + lineEnd);
				} else if (dv.getCode().equals(NL_HANDSHAKE_VAR_PREPOSITION)) {
					addDialogVariableExampleToRequestForDialogVariable(dv,"Mijn prepositie is " + NL_HANDSHAKE_VAR_PREPOSITION + lineEnd);
				}
			}
		}
	}
	
	@Override
	protected boolean installExamplesAndAssignments() {
		return false;
	}

	@Override
	protected boolean installCrawlers() {
		return false;
	}

	@Override
	protected Control createControlObject() {
		Control control = super.createControlObject();
		control.setAutoReactivate(false);
		control.setCountSymbolMaximum(2000);
		control.setSkipCountMaxStructSyms(false);
		control.setLevelFireBase(500);
		control.setDoAssignments(false);
		control.setAssignmentPauzeMSecs(0);
		control.setAssignmentReloadSecs(0);
		control.setStateHistoryMaximum(60);
		return control;
	}

	@Override
	protected void generateInitialData() {
		super.generateInitialData();
		
		Messenger.getInstance().debug(this,"Generating initial ZIDS data ...");

		// Add self
		self = SvrControllerSelf.getInstance().getSelf();
		selfPattern = PtnObject.TYPE_OBJECT + "_" + ZIDSModel.SELF_CLASS_FULL_NAME + ":" + self.getId();

		// Add humans
		setDone(false);
		addHumanRequest.addSubscriber(this);
		addHumansToAddHumanRequest(addHumanRequest);
		DbRequestQueue.getInstance().addRequest(addHumanRequest,this);
		waitTillDone("Generating initial ZIDS data was interrupted");

		// Add assignment
		setDone(false);
		Assignment as = new Assignment();
		as.setName(ZIDSModel.SESSION_HANDLING_ASSIGNMENT_NAME);
		as.setLogExtended(true);
		as.setContextDynamic(true);
		as.setCorrectInput(true);
		addAssignmentRequest.getObjects().add(new ReqDataObject(as.toDataObject()));
		addAssignmentRequest.addSubscriber(this);
		DbRequestQueue.getInstance().addRequest(addAssignmentRequest,this);
		waitTillDone("Generating initial ZIDS data was interrupted");

		// Add dialogs
		setDone(false);
		addVariableTypeRequest.addSubscriber(this);
		for (int i = 0; i<PtnObject.TYPES.length; i++) {
			VariableType vt = new VariableType();
			vt.setName(PtnObject.TYPES[i]);
			addVariableTypeRequest.getObjects().add(new ReqDataObject(vt.toDataObject()));
		}
		DbRequestQueue.getInstance().addRequest(addVariableTypeRequest,this);
		waitTillDone("Generating initial ZIDS data was interrupted");
		
		Messenger.getInstance().debug(this,"Generated initial ZIDS data");
	}

	@Override
	protected void handleRequestEvent(ReqObject request, EvtEvent evt) {
		if (request.hasError()) {
			Messenger.getInstance().error(this,"Error executing request: " + request.getErrors().get(0).getMessage() + ", class: " + request.getClassName());
			setDone(true);
		} else if (request==addHumanRequest) {
			setDone(true);
		} else if (request==addVariableTypeRequest) {
			getVariableTypeRequest.addSubscriber(this);
			getVariableTypeRequest.getProperties().add(ReqGet.ALL_PROPERTIES);
			DbRequestQueue.getInstance().addRequest(getVariableTypeRequest,this);
		} else if (request==getVariableTypeRequest) {
			for (ReqDataObject object: getVariableTypeRequest.getObjects()) {
				VariableType vt = new VariableType();
				vt.fromDataObject(object.getDataObject());
				variableTypeNameIdMap.put(vt.getName(),vt.getId());
			}
			addDialogsToAddDialogRequest(addDialogRequest);
			addDialogRequest.addSubscriber(this);
			DbRequestQueue.getInstance().addRequest(addDialogRequest,this);
		} else if (request==addDialogRequest) {
			getDialogRequest.addSubscriber(this);
			getDialogRequest.getProperties().add(ReqGet.ALL_PROPERTIES);
			DbRequestQueue.getInstance().addRequest(getDialogRequest,this);
		} else if (request==getDialogRequest) {
			for (ReqDataObject object: getDialogRequest.getObjects()) {
				Dialog d = new Dialog();
				d.fromDataObject(object.getDataObject());
				addedDialog(d,addDialogExampleRequest,addDialogVariableRequest);
			}
			addDialogExampleRequest.addSubscriber(this);
			DbRequestQueue.getInstance().addRequest(addDialogExampleRequest,this);
		} else if (request==addDialogExampleRequest) {
			addDialogExampleRequest.getObjects().clear();
			addDialogVariableRequest.addSubscriber(this);
			DbRequestQueue.getInstance().addRequest(addDialogVariableRequest,this);
		} else if (request==addDialogVariableRequest) {
			addDialogVariableRequest.getObjects().clear();
			getDialogVariableRequest.addSubscriber(this);
			getDialogVariableRequest.getProperties().add(ReqGet.ALL_PROPERTIES);
			DbRequestQueue.getInstance().addRequest(getDialogVariableRequest,this);
		} else if (request==getDialogVariableRequest) {
			for (ReqDataObject object: getDialogVariableRequest.getObjects()) {
				DialogVariable dv = new DialogVariable();
				dv.fromDataObject(object.getDataObject());
				addedDialogVariable(dv,addDialogVariableExampleRequest);
			}
			addDialogVariableExampleRequest.addSubscriber(this);
			DbRequestQueue.getInstance().addRequest(addDialogVariableExampleRequest,this);
		} else if (request==addDialogVariableExampleRequest) {
			addDialogVariableExampleRequest.getObjects().clear();
			setDone(true);
		} else if (request==addAssignmentRequest) {
			setDone(true);
		}
	}
	
	protected void addDialogExampleToRequestForDialogId(long dialogId, String input, String output) {
		DialogExample de = new DialogExample();
		de.setDialogId(dialogId);
		de.setInput(new StringBuilder(input));
		de.setOutput(new StringBuilder(output));
		addDialogExampleRequest.getObjects().add(new ReqDataObject(de.toDataObject()));
	}

	protected void addDialogVariableToRequestForDialogId(long dialogId, String code,String type,String contextSymbol,String prompt1,String prompt2,String prompt3) {
		DialogVariable dv = new DialogVariable();
		dv.setDialogId(dialogId);
		dv.setContextSymbol(contextSymbol);
		dv.setPrompt1(new StringBuilder(prompt1));
		dv.setPrompt2(new StringBuilder(prompt2));
		dv.setPrompt3(new StringBuilder(prompt3));
		dv.setCode(code);
		dv.setTypeId(variableTypeNameIdMap.get(type));
		addDialogVariableRequest.getObjects().add(new ReqDataObject(dv.toDataObject()));
	}
	
	protected void addDialogVariableExampleToRequestForDialogVariable(DialogVariable variable, String output) {
		addDialogVariableExampleToRequestForDialogVariableId(variable.getId(),variable.getPrompt1().toString(),output);
		if (variable.getPrompt2().length()>0) {
			addDialogVariableExampleToRequestForDialogVariableId(variable.getId(),variable.getPrompt2().toString(),output);
		}
		if (variable.getPrompt3().length()>0) {
			addDialogVariableExampleToRequestForDialogVariableId(variable.getId(),variable.getPrompt3().toString(),output);
		}
	}
	
	private void addDialogVariableExampleToRequestForDialogVariableId(long variableId, String input, String output) {
		DialogVariableExample dve = new DialogVariableExample();
		dve.setVariableId(variableId);
		dve.setInput(new StringBuilder(input));
		dve.setOutput(new StringBuilder(output));
		addDialogVariableExampleRequest.getObjects().add(new ReqDataObject(dve.toDataObject()));
	}

	protected final void addHumanToAddHumanRequest(ReqAdd addRequest,String name,String emailAddress) {
		Human human = new Human();
		human.setName(name);
		human.setEmailAddress(emailAddress);
		addRequest.getObjects().add(new ReqDataObject(human.toDataObject()));
	}

}
