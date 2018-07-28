package nl.zeesoft.zsd.dialog.dialogs.english;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.RoomBooking;

public class EnglishRoomBooking extends RoomBooking {
	private static final String		EXAMPLE_OUTPUT_1	= "Okay.";
	private static final String		EXAMPLE_OUTPUT_2	= "Okay.";
	
	private List<ZStringBuilder>	phrases				= new ArrayList<ZStringBuilder>();
	private List<String>			vars				= new ArrayList<String>();
	private List<String>			rooms				= new ArrayList<String>();
	private List<String>			books				= new ArrayList<String>();
	
	public EnglishRoomBooking() {
		setLanguage(BaseConfiguration.LANG_ENG);
		setHandlerClassName(EnglishRoomBookingHandler.class.getName());
		
		books.add("book");
		books.add("reserve");
		
		rooms.add("room");
		rooms.add("space");

		vars.add("for {" + VARIABLE_BOOK_PEOPLE + "} people");
		vars.add("on {" + VARIABLE_BOOK_DATE + "}");
		vars.add("from {" + VARIABLE_BOOK_TIME + "}");
		vars.add("for a duration of {" + VARIABLE_BOOK_DURATION + "}");
		vars.add("at {" + VARIABLE_BOOK_TIME + "}");

		phrases.add(new ZStringBuilder("I want to {book} a {room}."));
		phrases.add(new ZStringBuilder("I would like to {book} a {room}."));
		phrases.add(new ZStringBuilder("I need a {room}."));
		phrases.add(new ZStringBuilder("Can I {book} a {room}?"));
		phrases.add(new ZStringBuilder("May I {book} a {room}?"));
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		for (String book: books) {
			for (String room: rooms) {
				for (ZStringBuilder phrase: phrases) {
					ZStringBuilder input = new ZStringBuilder(phrase);
					input.replace("{book}",book);
					input.replace("{room}",room);
					addExample(input.toString(),"");
					for (String var: vars) {
						ZStringBuilder inputVar = new ZStringBuilder(input);
						inputVar.replace("."," " + var + ".");
						inputVar.replace("?"," " + var + "?");
						addExample(inputVar.toString(),getOutput1());
						int i = 0;
						for (String var2: vars) {
							if (!var2.equals(var)) {
								ZStringBuilder inputVar2 = new ZStringBuilder(inputVar);
								inputVar2.replace("."," " + var2 + ".");
								inputVar2.replace("?"," " + var2 + "?");
								addExample(inputVar2.toString(),getOutput2());
							}
							i++;
							if (i==4) {
								break;
							}
						}
					}
				}
			}
		}
	
		addVariable(VARIABLE_BOOK_DATE,BaseConfiguration.TYPE_DATE);
		addVariablePrompt(VARIABLE_BOOK_DATE,"On what date?");
		addVariablePrompt(VARIABLE_BOOK_DATE,"On what date do you need the room?");
		addVariablePrompt(VARIABLE_BOOK_DATE,"On what date do you want to book the room?");

		addVariable(VARIABLE_BOOK_TIME,BaseConfiguration.TYPE_TIME);
		addVariablePrompt(VARIABLE_BOOK_TIME,"From what time?");
		addVariablePrompt(VARIABLE_BOOK_TIME,"From what time do you need the room?");
		addVariablePrompt(VARIABLE_BOOK_TIME,"From what time do you want to book the room?");

		addVariable(VARIABLE_BOOK_DURATION,BaseConfiguration.TYPE_DURATION);
		addVariablePrompt(VARIABLE_BOOK_DURATION,"For how long?");
		addVariablePrompt(VARIABLE_BOOK_DURATION,"For how long do you need the room?");
		addVariablePrompt(VARIABLE_BOOK_DURATION,"For how long do you want to book the room?");

		addVariable(VARIABLE_BOOK_PEOPLE,BaseConfiguration.TYPE_NUMERIC);
		addVariablePrompt(VARIABLE_BOOK_PEOPLE,"How many people?");
		addVariablePrompt(VARIABLE_BOOK_PEOPLE,"How many people will be using the room?");
		addVariablePrompt(VARIABLE_BOOK_PEOPLE,"How many people must the room accomodate?");

		addVariable(VARIABLE_BOOK_CONFIRMATION,BaseConfiguration.TYPE_CONFIRMATION);
		addVariablePrompt(VARIABLE_BOOK_CONFIRMATION,
			"Do I understand correctly that you want a room on {" + VARIABLE_BOOK_DATE +
			"}, for {" + VARIABLE_BOOK_PEOPLE +
			"} people, from {" + VARIABLE_BOOK_TIME +
			"}, for {" + VARIABLE_BOOK_DURATION + "}?");
		addVariablePrompt(VARIABLE_BOOK_CONFIRMATION,
			"Do I understand correctly that you want a room on {" + VARIABLE_BOOK_DATE +
			"}, for {" + VARIABLE_BOOK_PEOPLE +
			"} people, from {" + VARIABLE_BOOK_TIME +
			"}, for a duration of {" + VARIABLE_BOOK_DURATION + "}?");
		addVariablePrompt(VARIABLE_BOOK_CONFIRMATION,
			"You want a room on {" + VARIABLE_BOOK_DATE +
			"}, for {" + VARIABLE_BOOK_PEOPLE +
			"} people, from {" + VARIABLE_BOOK_TIME +
			"}, for a duration of {" + VARIABLE_BOOK_DURATION + "}. Is that correct?");
		
		addVariable(VARIABLE_NEXT_DIALOG,BaseConfiguration.TYPE_ALPHABETIC);
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"Is there anything else I can do for you?");
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"Is there anything else I can help you with?");
	}
	
	protected String getOutput1() {
		return EXAMPLE_OUTPUT_1;
	}
	
	protected String getOutput2() {
		return EXAMPLE_OUTPUT_2;
	}
}
