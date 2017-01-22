package nl.zeesoft.zidd.database.model;

import nl.zeesoft.zacs.database.model.Symbol;
import nl.zeesoft.zidd.dialog.controller.CtrKamerBoeking;
import nl.zeesoft.zidd.dialog.controller.CtrRoomBooking;
import nl.zeesoft.zids.database.model.Dialog;
import nl.zeesoft.zids.database.model.DialogVariable;
import nl.zeesoft.zids.database.model.ZIDSDataGenerator;
import nl.zeesoft.zids.dialog.pattern.PtnObject;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;

public class ZIDDDataGenerator extends ZIDSDataGenerator {
	/*
	public final static String		EN_INTRODUCTION						= "Introduction";
	public final static String		EN_INTRO_CONTEXT					= "introduction";
	public final static String		NL_INTRODUCTION						= "Introductie";
	public final static String		NL_INTRO_CONTEXT					= "introductie";
	 */
	
	public final static String		EN_BOOKING							= "Room booking";
	public final static String		EN_BOOKING_CONTEXT					= "roomBooking";
	public final static String		EN_BOOKING_DATE						= "{BOOKING_DATE}";
	public final static String		EN_BOOKING_DATE_CONTEXT				= "bookingDate";
	public final static String		EN_BOOKING_TIME						= "{BOOKING_TIME}";
	public final static String		EN_BOOKING_TIME_CONTEXT				= "bookingTime";
	public final static String		EN_BOOKING_DURATION					= "{BOOKING_DURATION}";
	public final static String		EN_BOOKING_DURATION_CONTEXT			= "bookingDuration";
	public final static String		EN_BOOKING_NUMBER					= "{BOOKING_NUMBER}";
	public final static String		EN_BOOKING_NUMBER_CONTEXT			= "bookingNumber";
	public final static String		EN_BOOKING_CONFIRMATION				= "{BOOKING_CONFIRMATION}";
	public final static String		EN_BOOKING_CONFIRMATION_CONTEXT		= "bookingConfirmation";

	public final static String		NL_BOOKING							= "Kamer boeking";
	public final static String		NL_BOOKING_CONTEXT					= "kamerBoeking";
	public final static String		NL_BOOKING_DATE						= "{BOEKING_DATUM}";
	public final static String		NL_BOOKING_DATE_CONTEXT				= "boekingDatum";
	public final static String		NL_BOOKING_TIME						= "{BOEKING_TIJD}";
	public final static String		NL_BOOKING_TIME_CONTEXT				= "boekingTijd";
	public final static String		NL_BOOKING_DURATION					= "{BOEKING_DUUR}";
	public final static String		NL_BOOKING_DURATION_CONTEXT			= "boekingDuur";
	public final static String		NL_BOOKING_NUMBER					= "{BOEKING_AANTAL}";
	public final static String		NL_BOOKING_NUMBER_CONTEXT			= "boekingAantal";
	public final static String		NL_BOOKING_CONFIRMATION				= "{BOEKING_BEVESTIGING}";
	public final static String		NL_BOOKING_CONFIRMATION_CONTEXT		= "bookingBevestiging";
	
	/*
	public final static String		EN_OUT_INTRODUCTION1				= "Hello, how can I be of service to you?";
	public final static String		EN_OUT_INTRODUCTION2				= "Hello, what can I do for you?";
	public final static String		EN_OUT_INTRODUCTION3				= "Hello, how may I help you?";
	public final static String		EN_OUT_INTRO_OPTIONS				= "I can book a room for you.";
	public final static String		EN_OUT_INTRO_WELCOME				= "You are welcome.";
	public final static String		EN_OUT_INTRO_RETURN					= "What can I do to help you?";
	public final static String		EN_OUT_INTRO_WELCOME_RETURN			= "You are welcome, what else can I do to help you?";

	public final static String		NL_OUT_INTRODUCTION1				= "Hallo, hoe kan ik u van dienst zijn?";
	public final static String		NL_OUT_INTRODUCTION2				= "Hallo, wat kan ik voor u betekenen?";
	public final static String		NL_OUT_INTRODUCTION3				= "Hallo, hoe kan ik u helpen?";
	public final static String		NL_OUT_INTRO_OPTIONS				= "Ik kan een kamer voor u boeken.";
	public final static String		NL_OUT_INTRO_WELCOME				= "Graag gedaan.";
	public final static String		NL_OUT_INTRO_RETURN					= "Wat kan ik doen om u te helpen?";
	public final static String		NL_OUT_INTRO_WELCOME_RETURN			= "Graag gedaan, wat kan ik nog meer doen om u te helpen?";
	 */
	
	public final static String		EN_PROMPT_DATE1						= "On what date?";
	public final static String		EN_PROMPT_DATE2						= "On what date do you need the room?";
	public final static String		EN_PROMPT_DATE3						= "On what date do you want to book?";
	public final static String		EN_PROMPT_TIME1						= "From what time?";
	public final static String		EN_PROMPT_TIME2						= "From what time do you need the room?";
	public final static String		EN_PROMPT_TIME3						= "From what time do you want to book?";
	public final static String		EN_PROMPT_DURATION1					= "How long?";
	public final static String		EN_PROMPT_DURATION2					= "How long do you need the room?";
	public final static String		EN_PROMPT_DURATION3					= "How long do you want to use the room?";
	public final static String		EN_PROMPT_NUMBER1					= "How many people?";
	public final static String		EN_PROMPT_NUMBER2					= "How many people will be using the room?";
	public final static String		EN_PROMPT_NUMBER3					= "How many people must the room accomodate?";
	public final static String		EN_PROMPT_CONFIRMATION1				= "Do I understand correctly that you want a room on " + EN_BOOKING_DATE + ", for " + EN_BOOKING_NUMBER + " people, from " + EN_BOOKING_TIME + ", for " + EN_BOOKING_DURATION + "?";
	public final static String		EN_PROMPT_CONFIRMATION2				= "Do I understand correctly that you want a room on " + EN_BOOKING_DATE + ", for " + EN_BOOKING_NUMBER + " people, from " + EN_BOOKING_TIME + ", for a duration of " + EN_BOOKING_DURATION + "?";
	public final static String		EN_PROMPT_CONFIRMATION3				= "You want to book a room on " + EN_BOOKING_DATE + ", for " + EN_BOOKING_NUMBER + " people, from " + EN_BOOKING_TIME + ", for a duration of " + EN_BOOKING_DURATION + "?";

	public final static String		NL_PROMPT_DATE1						= "Op welke datum?";
	public final static String		NL_PROMPT_DATE2						= "Op welke datum heeft u de kamer nodig?";
	public final static String		NL_PROMPT_DATE3						= "Op welke datum wilt u boeken?";
	public final static String		NL_PROMPT_TIME1						= "Vanaf hoelaat?";
	public final static String		NL_PROMPT_TIME2						= "Vanaf hoelaat heeft u de kamer nodig?";
	public final static String		NL_PROMPT_TIME3						= "Vanaf hoelaat wilt u boeken?";
	public final static String		NL_PROMPT_DURATION1					= "Hoelang?";
	public final static String		NL_PROMPT_DURATION2					= "Hoelang heeft u de kamer nodig?";
	public final static String		NL_PROMPT_DURATION3					= "Hoelang wilt u de kamer gebruiken?";
	public final static String		NL_PROMPT_NUMBER1					= "Voor hoeveel personen?";
	public final static String		NL_PROMPT_NUMBER2					= "Hoeveel personen gaan de kamer gebruiken?";
	public final static String		NL_PROMPT_NUMBER3					= "Hoeveel personen moeten in de kamer kunnen?";
	public final static String		NL_PROMPT_CONFIRMATION1				= "Begrijp ik goed dat u een kamer wilt op " + NL_BOOKING_DATE + ", voor " + NL_BOOKING_NUMBER + " personen, vanaf " + NL_BOOKING_TIME + ", voor " + NL_BOOKING_DURATION + "?";
	public final static String		NL_PROMPT_CONFIRMATION2				= "Begrijp ik goed dat u een kamer wilt op " + NL_BOOKING_DATE + ", voor " + NL_BOOKING_NUMBER + " personen, vanaf " + NL_BOOKING_TIME + ", voor een duur van " + NL_BOOKING_DURATION + "?";
	public final static String		NL_PROMPT_CONFIRMATION3				= "U wilt een kamer boeken op " + NL_BOOKING_DATE + ", voor " + NL_BOOKING_NUMBER + " personen, om " + NL_BOOKING_TIME + ", voor een duur van " + NL_BOOKING_DURATION + "?";
	
	@Override
	protected void addDialogsToAddDialogRequest(ReqAdd addRequest) {
		super.addDialogsToAddDialogRequest(addRequest);
		
		Dialog d = null;

		/*
		d = new Dialog();
		d.setName(EN_INTRODUCTION);
		d.setContextSymbol(EN_INTRO_CONTEXT);
		addRequest.getObjects().add(new ReqDataObject(d.toDataObject()));
		
		d = new Dialog();
		d.setName(NL_INTRODUCTION);
		d.setContextSymbol(NL_INTRO_CONTEXT);
		addRequest.getObjects().add(new ReqDataObject(d.toDataObject()));
		*/

		d = new Dialog();
		d.setName(EN_BOOKING);
		d.setContextSymbol(EN_BOOKING_CONTEXT);
		d.setControllerClassName(CtrRoomBooking.class.getName());
		addRequest.getObjects().add(new ReqDataObject(d.toDataObject()));
		
		d = new Dialog();
		d.setName(NL_BOOKING);
		d.setContextSymbol(NL_BOOKING_CONTEXT);
		d.setControllerClassName(CtrKamerBoeking.class.getName());
		addRequest.getObjects().add(new ReqDataObject(d.toDataObject()));
	}

	@Override
	protected void addedDialog(Dialog d,ReqAdd addExampleRequest,ReqAdd addVariableRequest) {
		super.addedDialog(d, addExampleRequest, addVariableRequest);
		/*
		if (d.getName().equals(EN_INTRODUCTION)) {
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
					addDialogExampleToRequestForDialogId(d.getId(),greeting + lineEnd,EN_OUT_INTRODUCTION1);
					addDialogExampleToRequestForDialogId(d.getId(),greeting + lineEnd,EN_OUT_INTRODUCTION2);
					addDialogExampleToRequestForDialogId(d.getId(),greeting + lineEnd,EN_OUT_INTRODUCTION3);
				}
				for (int i2 = 0; i2 < 3; i2++) {
					String thankYou = "Thank you";
					if (i2==1) {
						thankYou = "Thanks";
					} else if (i2==2) {
						thankYou = "Thank you very much";
					}
					addDialogExampleToRequestForDialogId(d.getId(),thankYou + lineEnd,EN_OUT_INTRO_WELCOME);
				}
			}
			addDialogExampleToRequestForDialogId(d.getId(),"What can you do?",EN_OUT_INTRO_OPTIONS);
			addDialogExampleToRequestForDialogId(d.getId(),"What do you do?",EN_OUT_INTRO_OPTIONS);
			addDialogExampleToRequestForDialogId(d.getId(),"What are my options?",EN_OUT_INTRO_OPTIONS);
			addDialogExampleToRequestForDialogId(d.getId(),"What choice do I have?",EN_OUT_INTRO_OPTIONS);
			addDialogExampleToRequestForDialogId(d.getId(),"What choices do I have?",EN_OUT_INTRO_OPTIONS);
			addDialogExampleToRequestForDialogId(d.getId(),"What services do you provide?",EN_OUT_INTRO_OPTIONS);

			addDialogExampleToRequestForDialogId(d.getId(),"No.",EN_OUT_INTRO_RETURN);
			addDialogExampleToRequestForDialogId(d.getId(),"No thanks.",EN_OUT_INTRO_RETURN);
			addDialogExampleToRequestForDialogId(d.getId(),"No, thank you.",EN_OUT_INTRO_WELCOME_RETURN);
			addDialogExampleToRequestForDialogId(d.getId(),"No, thanks.",EN_OUT_INTRO_WELCOME_RETURN);

			addDialogExampleToRequestForDialogId(d.getId(),"I don't want to book a room.",EN_OUT_INTRO_RETURN);
			addDialogExampleToRequestForDialogId(d.getId(),"I do not want to reserve a room.",EN_OUT_INTRO_RETURN);
			addDialogExampleToRequestForDialogId(d.getId(),"No I don't want to book a room.",EN_OUT_INTRO_RETURN);
			addDialogExampleToRequestForDialogId(d.getId(),"No I do not want to reserve a room.",EN_OUT_INTRO_RETURN);
			addDialogExampleToRequestForDialogId(d.getId(),"No, I don't want to book a room.",EN_OUT_INTRO_RETURN);
			addDialogExampleToRequestForDialogId(d.getId(),"No, I do not want to reserve a room.",EN_OUT_INTRO_RETURN);
			addDialogExampleToRequestForDialogId(d.getId(),"No, I don't want to book another room.",EN_OUT_INTRO_RETURN);
			addDialogExampleToRequestForDialogId(d.getId(),"No, I do not want to reserve another room.",EN_OUT_INTRO_RETURN);
		} else if (d.getName().equals(NL_INTRODUCTION)) {
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
					addDialogExampleToRequestForDialogId(d.getId(),greeting + lineEnd,NL_OUT_INTRODUCTION1);
					addDialogExampleToRequestForDialogId(d.getId(),greeting + lineEnd,NL_OUT_INTRODUCTION2);
					addDialogExampleToRequestForDialogId(d.getId(),greeting + lineEnd,NL_OUT_INTRODUCTION3);
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
					addDialogExampleToRequestForDialogId(d.getId(),thankYou + lineEnd,NL_OUT_INTRO_WELCOME);
				}
			}
			addDialogExampleToRequestForDialogId(d.getId(),"Wat kunt u zoal?",NL_OUT_INTRO_OPTIONS);
			addDialogExampleToRequestForDialogId(d.getId(),"Wat kunt u?",NL_OUT_INTRO_OPTIONS);
			addDialogExampleToRequestForDialogId(d.getId(),"Welke diensten kun je leveren?",NL_OUT_INTRO_OPTIONS);
			addDialogExampleToRequestForDialogId(d.getId(),"Wat kun je zoal?",NL_OUT_INTRO_OPTIONS);
			addDialogExampleToRequestForDialogId(d.getId(),"Wat kun je?",NL_OUT_INTRO_OPTIONS);
			addDialogExampleToRequestForDialogId(d.getId(),"Welke diensten kunt u leveren?",NL_OUT_INTRO_OPTIONS);
			
			addDialogExampleToRequestForDialogId(d.getId(),"Wat zijn mijn opties?",NL_OUT_INTRO_OPTIONS);
			addDialogExampleToRequestForDialogId(d.getId(),"Welke keuzes heb ik?",NL_OUT_INTRO_OPTIONS);

			addDialogExampleToRequestForDialogId(d.getId(),"Nee.",NL_OUT_INTRO_RETURN);
			addDialogExampleToRequestForDialogId(d.getId(),"Nee bedankt.",NL_OUT_INTRO_WELCOME_RETURN);
			addDialogExampleToRequestForDialogId(d.getId(),"Nee, dank u.",NL_OUT_INTRO_WELCOME_RETURN);
			addDialogExampleToRequestForDialogId(d.getId(),"Nee, dank je.",NL_OUT_INTRO_WELCOME_RETURN);

			addDialogExampleToRequestForDialogId(d.getId(),"Ik wil geen kamer boeken.",NL_OUT_INTRO_RETURN);
			addDialogExampleToRequestForDialogId(d.getId(),"Ik wil geen kamer reserveren.",NL_OUT_INTRO_RETURN);
			addDialogExampleToRequestForDialogId(d.getId(),"Nee ik wil geen kamer boeken.",NL_OUT_INTRO_RETURN);
			addDialogExampleToRequestForDialogId(d.getId(),"Nee ik wil geen kamer reserveren.",NL_OUT_INTRO_RETURN);
			addDialogExampleToRequestForDialogId(d.getId(),"Nee, Ik wil niet een kamer boeken.",NL_OUT_INTRO_RETURN);
			addDialogExampleToRequestForDialogId(d.getId(),"Nee, Ik wil niet een kamer reserveren.",NL_OUT_INTRO_RETURN);
			addDialogExampleToRequestForDialogId(d.getId(),"Nee, Ik wil niet nog een kamer boeken.",NL_OUT_INTRO_RETURN);
			addDialogExampleToRequestForDialogId(d.getId(),"Nee, Ik wil niet nog een kamer reserveren.",NL_OUT_INTRO_RETURN);
		} 
		 */
		if (d.getName().equals(EN_BOOKING)) {
			for (int i = 0; i < 2; i++) {
				String room = "room";
				if (i==1) {
					room = "space";
				}
				addDialogExampleToRequestForDialogId(d.getId(),"I would like to book a " + room + ".",EN_PROMPT_DATE1);
				addDialogExampleToRequestForDialogId(d.getId(),"I want to reserve a " + room + ".",EN_PROMPT_DATE2);
				addDialogExampleToRequestForDialogId(d.getId(),"Can I book a " + room + "?",EN_PROMPT_DATE3);
				addDialogExampleToRequestForDialogId(d.getId(),"May I reserve a " + room + "?",EN_PROMPT_DATE1);
				
				addDialogExampleToRequestForDialogId(d.getId(),"I would like to book a " + room + " on " + EN_BOOKING_DATE + ".",EN_PROMPT_TIME1);
				addDialogExampleToRequestForDialogId(d.getId(),"I want to reserve a " + room + " on " + EN_BOOKING_DATE + ".",EN_PROMPT_TIME2);
				addDialogExampleToRequestForDialogId(d.getId(),"Can I book a " + room + " on " + EN_BOOKING_DATE + "?",EN_PROMPT_TIME3);
				addDialogExampleToRequestForDialogId(d.getId(),"May I reserve a " + room + " on " + EN_BOOKING_DATE + "?",EN_PROMPT_TIME1);

				for (int i2 = 0; i2 < 2; i2++) {
					String people = "people";
					if (i2==1) {
						people = "person";
					}
					addDialogExampleToRequestForDialogId(d.getId(),"I would like to book a " + room + " for " + EN_BOOKING_NUMBER + " " + people + " on " + EN_BOOKING_DATE + ".",EN_PROMPT_TIME2);
					addDialogExampleToRequestForDialogId(d.getId(),"I want to reserve a " + room + " for " + EN_BOOKING_NUMBER + " " + people + " on " + EN_BOOKING_DATE + ".",EN_PROMPT_TIME3);
					addDialogExampleToRequestForDialogId(d.getId(),"Can I book a " + room + " for " + EN_BOOKING_NUMBER + " " + people + " on " + EN_BOOKING_DATE + "?",EN_PROMPT_TIME1);
					addDialogExampleToRequestForDialogId(d.getId(),"May I reserve a " + room + " for " + EN_BOOKING_NUMBER + " " + people + " on " + EN_BOOKING_DATE + "?",EN_PROMPT_TIME2);
					
					addDialogExampleToRequestForDialogId(d.getId(),"I would like to book a " + room + " on " + EN_BOOKING_DATE + " for " + EN_BOOKING_NUMBER + " " + people + ".",EN_PROMPT_TIME3);
					addDialogExampleToRequestForDialogId(d.getId(),"I want to reserve a " + room + " on " + EN_BOOKING_DATE + " for " + EN_BOOKING_NUMBER + " " + people + ".",EN_PROMPT_TIME1);
					addDialogExampleToRequestForDialogId(d.getId(),"Can I book a " + room + " on " + EN_BOOKING_DATE + " for " + EN_BOOKING_NUMBER + " " + people + "?",EN_PROMPT_TIME2);
					addDialogExampleToRequestForDialogId(d.getId(),"May I reserve a " + room + " on " + EN_BOOKING_DATE + " for " + EN_BOOKING_NUMBER + " " + people + "?",EN_PROMPT_TIME3);

					addDialogExampleToRequestForDialogId(d.getId(),"I would like to book a " + room + " for " + EN_BOOKING_NUMBER + " " + people + " on " + EN_BOOKING_DATE + " at " + EN_BOOKING_TIME + ".",EN_PROMPT_DURATION1);
					addDialogExampleToRequestForDialogId(d.getId(),"I want to reserve a " + room + " for " + EN_BOOKING_NUMBER + " " + people + " on " + EN_BOOKING_DATE + " from " + EN_BOOKING_TIME + ".",EN_PROMPT_DURATION2);
					addDialogExampleToRequestForDialogId(d.getId(),"Can I book a " + room + " for " + EN_BOOKING_NUMBER + " " + people + " on " + EN_BOOKING_DATE + " from " + EN_BOOKING_TIME + "?",EN_PROMPT_DURATION3);
					addDialogExampleToRequestForDialogId(d.getId(),"May I reserve a " + room + " for " + EN_BOOKING_NUMBER + " " + people + " on " + EN_BOOKING_DATE + " at " + EN_BOOKING_TIME + "?",EN_PROMPT_DURATION1);
					
					addDialogExampleToRequestForDialogId(d.getId(),"I would like to book a " + room + " on " + EN_BOOKING_DATE + " for " + EN_BOOKING_NUMBER + " " + people + " at " + EN_BOOKING_TIME + ".",EN_PROMPT_DURATION2);
					addDialogExampleToRequestForDialogId(d.getId(),"I want to reserve a " + room + " on " + EN_BOOKING_DATE + " for " + EN_BOOKING_NUMBER + " " + people + " from " + EN_BOOKING_TIME + ".",EN_PROMPT_DURATION3);
					addDialogExampleToRequestForDialogId(d.getId(),"Can I book a " + room + " on " + EN_BOOKING_DATE + " for " + EN_BOOKING_NUMBER + " " + people + " from " + EN_BOOKING_TIME + "?",EN_PROMPT_DURATION1);
					addDialogExampleToRequestForDialogId(d.getId(),"May I reserve a " + room + " on " + EN_BOOKING_DATE + " for " + EN_BOOKING_NUMBER + " " + people + " at " + EN_BOOKING_TIME + "?",EN_PROMPT_DURATION2);
				}
			}
			addDialogVariableToRequestForDialogId(d.getId(),EN_BOOKING_DATE,PtnObject.TYPE_DATE,EN_BOOKING_DATE_CONTEXT,EN_PROMPT_DATE1,EN_PROMPT_DATE2,EN_PROMPT_DATE3);
			addDialogVariableToRequestForDialogId(d.getId(),EN_BOOKING_TIME,PtnObject.TYPE_TIME,EN_BOOKING_TIME_CONTEXT,EN_PROMPT_TIME1,EN_PROMPT_TIME2,EN_PROMPT_TIME3);
			addDialogVariableToRequestForDialogId(d.getId(),EN_BOOKING_DURATION,PtnObject.TYPE_DURATION,EN_BOOKING_DURATION_CONTEXT,EN_PROMPT_DURATION1,EN_PROMPT_DURATION2,EN_PROMPT_DURATION3);
			addDialogVariableToRequestForDialogId(d.getId(),EN_BOOKING_NUMBER,PtnObject.TYPE_NUMBER,EN_BOOKING_NUMBER_CONTEXT,EN_PROMPT_NUMBER1,EN_PROMPT_NUMBER2,EN_PROMPT_NUMBER3);
			addDialogVariableToRequestForDialogId(d.getId(),EN_BOOKING_CONFIRMATION,PtnObject.TYPE_CONFIRMATION,EN_BOOKING_CONFIRMATION_CONTEXT,EN_PROMPT_CONFIRMATION1,EN_PROMPT_CONFIRMATION2,EN_PROMPT_CONFIRMATION3);
		} else if (d.getName().equals(NL_BOOKING)) {
			for (int i = 0; i < 2; i++) {
				String room = "kamer";
				if (i==1) {
					room = "ruimte";
				}
				addDialogExampleToRequestForDialogId(d.getId(),"Ik zou graag een " + room + " willen boeken.",NL_PROMPT_DATE1);
				addDialogExampleToRequestForDialogId(d.getId(),"Ik wil een " + room + " reserveren.",NL_PROMPT_DATE2);
				addDialogExampleToRequestForDialogId(d.getId(),"Kan ik een " + room + " boeken?",NL_PROMPT_DATE3);
				addDialogExampleToRequestForDialogId(d.getId(),"Mag ik een " + room + " reserveren?",NL_PROMPT_DATE1);

				addDialogExampleToRequestForDialogId(d.getId(),"Ik zou graag een " + room + " willen boeken op " + NL_BOOKING_DATE + ".",NL_PROMPT_TIME1);
				addDialogExampleToRequestForDialogId(d.getId(),"Ik wil een " + room + " reserveren op " + NL_BOOKING_DATE + ".",NL_PROMPT_TIME2);
				addDialogExampleToRequestForDialogId(d.getId(),"Kan ik een " + room + " boeken op " + NL_BOOKING_DATE + "?",NL_PROMPT_TIME3);
				addDialogExampleToRequestForDialogId(d.getId(),"Mag ik een " + room + " reserveren op " + NL_BOOKING_DATE + "?",NL_PROMPT_TIME1);
				
				for (int i2 = 0; i2 < 2; i2++) {
					String people = "personen";
					if (i2==1) {
						people = "persoon";
					}
					addDialogExampleToRequestForDialogId(d.getId(),"Ik zou graag een " + room + " willen boeken voor " + NL_BOOKING_NUMBER + " " + people + " op " + NL_BOOKING_DATE + ".",NL_PROMPT_TIME2);
					addDialogExampleToRequestForDialogId(d.getId(),"Ik wil een " + room + " reserveren voor " + NL_BOOKING_NUMBER + " " + people + " op " + NL_BOOKING_DATE + ".",NL_PROMPT_TIME3);
					addDialogExampleToRequestForDialogId(d.getId(),"Kan ik een " + room + " boeken voor " + NL_BOOKING_NUMBER + " " + people + " op " + NL_BOOKING_DATE + "?",NL_PROMPT_TIME1);
					addDialogExampleToRequestForDialogId(d.getId(),"Mag ik een " + room + " reserveren voor " + NL_BOOKING_NUMBER + " " + people + " op " + NL_BOOKING_DATE + "?",NL_PROMPT_TIME2);
	
					addDialogExampleToRequestForDialogId(d.getId(),"Ik zou graag een " + room + " willen boeken op " + NL_BOOKING_DATE + " voor " + NL_BOOKING_NUMBER + " " + people + ".",NL_PROMPT_TIME3);
					addDialogExampleToRequestForDialogId(d.getId(),"Ik wil een " + room + " reserveren op " + NL_BOOKING_DATE + " voor " + NL_BOOKING_NUMBER + " " + people + ".",NL_PROMPT_TIME1);
					addDialogExampleToRequestForDialogId(d.getId(),"Kan ik een " + room + " boeken op " + NL_BOOKING_DATE + " voor " + NL_BOOKING_NUMBER + " " + people + "?",NL_PROMPT_TIME2);
					addDialogExampleToRequestForDialogId(d.getId(),"Mag ik een " + room + " reserveren op " + NL_BOOKING_DATE + " voor " + NL_BOOKING_NUMBER + " " + people + "?",NL_PROMPT_TIME3);

					addDialogExampleToRequestForDialogId(d.getId(),"Ik zou graag een " + room + " willen boeken voor " + NL_BOOKING_NUMBER + " " + people + " op " + NL_BOOKING_DATE + " om " + NL_BOOKING_TIME + ".",NL_PROMPT_DURATION1);
					addDialogExampleToRequestForDialogId(d.getId(),"Ik wil een " + room + " reserveren voor " + NL_BOOKING_NUMBER + " " + people + " op " + NL_BOOKING_DATE + " vanaf " + NL_BOOKING_TIME + ".",NL_PROMPT_DURATION2);
					addDialogExampleToRequestForDialogId(d.getId(),"Kan ik een " + room + " boeken voor " + NL_BOOKING_NUMBER + " " + people + " op " + NL_BOOKING_DATE + " vanaf " + NL_BOOKING_TIME + "?",NL_PROMPT_DURATION3);
					addDialogExampleToRequestForDialogId(d.getId(),"Mag ik een " + room + " reserveren voor " + NL_BOOKING_NUMBER + " " + people + " op " + NL_BOOKING_DATE + " om " + NL_BOOKING_TIME + "?",NL_PROMPT_DURATION1);
	
					addDialogExampleToRequestForDialogId(d.getId(),"Ik zou graag een " + room + " willen boeken op " + NL_BOOKING_DATE + " voor " + NL_BOOKING_NUMBER + " " + people + " om " + NL_BOOKING_TIME + ".",NL_PROMPT_DURATION2);
					addDialogExampleToRequestForDialogId(d.getId(),"Ik wil een " + room + " reserveren op " + NL_BOOKING_DATE + " voor " + NL_BOOKING_NUMBER + " " + people + " vanaf " + NL_BOOKING_TIME + ".",NL_PROMPT_DURATION3);
					addDialogExampleToRequestForDialogId(d.getId(),"Kan ik een " + room + " boeken op " + NL_BOOKING_DATE + " voor " + NL_BOOKING_NUMBER + " " + people + " vanaf " + NL_BOOKING_TIME + "?",NL_PROMPT_DURATION1);
					addDialogExampleToRequestForDialogId(d.getId(),"Mag ik een " + room + " reserveren op " + NL_BOOKING_DATE + " voor " + NL_BOOKING_NUMBER + " " + people + " om " + NL_BOOKING_TIME + "?",NL_PROMPT_DURATION2);
				}
			}
			addDialogVariableToRequestForDialogId(d.getId(),NL_BOOKING_DATE,PtnObject.TYPE_DATE,NL_BOOKING_DATE_CONTEXT,NL_PROMPT_DATE1,NL_PROMPT_DATE2,NL_PROMPT_DATE3);
			addDialogVariableToRequestForDialogId(d.getId(),NL_BOOKING_TIME,PtnObject.TYPE_TIME,NL_BOOKING_TIME_CONTEXT,NL_PROMPT_TIME1,NL_PROMPT_TIME2,NL_PROMPT_TIME3);
			addDialogVariableToRequestForDialogId(d.getId(),NL_BOOKING_DURATION,PtnObject.TYPE_DURATION,NL_BOOKING_DURATION_CONTEXT,NL_PROMPT_DURATION1,NL_PROMPT_DURATION2,NL_PROMPT_DURATION3);
			addDialogVariableToRequestForDialogId(d.getId(),NL_BOOKING_NUMBER,PtnObject.TYPE_NUMBER,NL_BOOKING_NUMBER_CONTEXT,NL_PROMPT_NUMBER1,NL_PROMPT_NUMBER2,NL_PROMPT_NUMBER3);
			addDialogVariableToRequestForDialogId(d.getId(),NL_BOOKING_CONFIRMATION,PtnObject.TYPE_CONFIRMATION,NL_BOOKING_CONFIRMATION_CONTEXT,NL_PROMPT_CONFIRMATION1,NL_PROMPT_CONFIRMATION2,NL_PROMPT_CONFIRMATION3);
		}
	}

	@Override
	protected void addedDialogVariable(DialogVariable dv,ReqAdd addVariableExampleRequest) {
		super.addedDialogVariable(dv, addVariableExampleRequest);
		for (int i = 0; i < Symbol.LINE_END_SYMBOLS.length; i++) {
			String lineEnd = Symbol.LINE_END_SYMBOLS[i];
			if (!lineEnd.equals("?")) {
				// ENGLISH
				if (dv.getCode().equals(EN_BOOKING_DATE)) {
					addDialogVariableExampleToRequestForDialogVariable(dv,"On " + EN_BOOKING_DATE + lineEnd);
				} else if (dv.getCode().equals(EN_BOOKING_TIME)) {
					addDialogVariableExampleToRequestForDialogVariable(dv,"At " + EN_BOOKING_TIME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"From " + EN_BOOKING_TIME + lineEnd);
				} else if (dv.getCode().equals(EN_BOOKING_DURATION)) {
					addDialogVariableExampleToRequestForDialogVariable(dv,"For " + EN_BOOKING_DURATION + lineEnd);
				} else if (dv.getCode().equals(EN_BOOKING_NUMBER)) {
					for (int i2 = 0; i2 < 2; i2++) {
						String people = "people";
						if (i2==1) {
							people = "person";
						}
						addDialogVariableExampleToRequestForDialogVariable(dv,EN_BOOKING_NUMBER + " " + people  + lineEnd);
						addDialogVariableExampleToRequestForDialogVariable(dv,"For " + EN_BOOKING_NUMBER + " " + people  + lineEnd);
					}
				// DUTCH
				} else if (dv.getCode().equals(NL_BOOKING_DATE)) {
					addDialogVariableExampleToRequestForDialogVariable(dv,"Op " + NL_BOOKING_DATE + lineEnd);
				} else if (dv.getCode().equals(NL_BOOKING_TIME)) {
					addDialogVariableExampleToRequestForDialogVariable(dv,"Om " + NL_BOOKING_TIME + lineEnd);
					addDialogVariableExampleToRequestForDialogVariable(dv,"From " + NL_BOOKING_TIME + lineEnd);
				} else if (dv.getCode().equals(NL_BOOKING_DURATION)) {
					addDialogVariableExampleToRequestForDialogVariable(dv,"Voor " + NL_BOOKING_DURATION + lineEnd);
				} else if (dv.getCode().equals(NL_BOOKING_NUMBER)) {
					for (int i2 = 0; i2 < 2; i2++) {
						String people = "personen";
						if (i2==1) {
							people = "persoon";
						}
						addDialogVariableExampleToRequestForDialogVariable(dv,NL_BOOKING_NUMBER + " " + people + lineEnd);
						addDialogVariableExampleToRequestForDialogVariable(dv,"Voor " + NL_BOOKING_NUMBER + " " + people + lineEnd);
					}
				// Confirmations
				//} else if (dv.getCode().equals(EN_BOOKING_CONFIRMATION)) {
				//} else if (dv.getCode().equals(NL_BOOKING_CONFIRMATION)) {
				}
			}
		}
	}
}
