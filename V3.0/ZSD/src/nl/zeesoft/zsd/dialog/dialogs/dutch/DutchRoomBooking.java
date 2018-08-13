package nl.zeesoft.zsd.dialog.dialogs.dutch;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.RoomBooking;

public class DutchRoomBooking extends RoomBooking {
	private static final String		EXAMPLE_OUTPUT_1	= "Okee.";
	private static final String		EXAMPLE_OUTPUT_2	= "Okee.";
	
	private List<ZStringBuilder>	phrases				= new ArrayList<ZStringBuilder>();
	private List<String>			vars				= new ArrayList<String>();
	private List<String>			rooms				= new ArrayList<String>();
	private List<String>			books				= new ArrayList<String>();
	
	public DutchRoomBooking() {
		setLanguage(BaseConfiguration.LANG_NLD);
		setHandlerClassName(DutchRoomBookingHandler.class.getName());
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		books.add("boeken");
		books.add("reserveren");
		
		rooms.add("kamer");
		rooms.add("ruimte");

		vars.add("voor [" + BaseConfiguration.TYPE_NUMERIC + "] personen");
		vars.add("op [" + BaseConfiguration.TYPE_DATE + "]");
		vars.add("van [" + BaseConfiguration.TYPE_TIME + "]");
		vars.add("voor een duur van [" + BaseConfiguration.TYPE_DURATION + "]");
		vars.add("om [" + BaseConfiguration.TYPE_TIME + "]");

		phrases.add(new ZStringBuilder("Ik wil een {room} {book}."));
		phrases.add(new ZStringBuilder("Ik wil graag een {room} {book}."));
		phrases.add(new ZStringBuilder("Ik heb een {room} nodig."));
		phrases.add(new ZStringBuilder("Kan ik een {room} {book}?"));
		phrases.add(new ZStringBuilder("Mag ik een {room} {book}?"));
		
		addExample("Kamer boeken.",getOutput1());
		addExample("Kamer reserveren.",getOutput2());
		addExample("Ruimte boeken.",getOutput2());
		addExample("Ruimte reserveren.",getOutput1());
		
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
		addVariablePrompt(VARIABLE_BOOK_DATE,"Op welke datum?");
		addVariablePrompt(VARIABLE_BOOK_DATE,"Op welke datum heeft u een kamer nodig?");
		addVariablePrompt(VARIABLE_BOOK_DATE,"Op welke datum wilt u een kamer boeken?");

		addVariable(VARIABLE_BOOK_TIME,BaseConfiguration.TYPE_TIME,false);
		addVariablePrompt(VARIABLE_BOOK_TIME,"Vanaf welk tijdstip?");
		addVariablePrompt(VARIABLE_BOOK_TIME,"Vanaf welk tijdstip heeft u een kamer nodig?");
		addVariablePrompt(VARIABLE_BOOK_TIME,"Vanaf welk tijdstip wilt u een kamer boeken?");

		addVariable(VARIABLE_BOOK_DURATION,BaseConfiguration.TYPE_DURATION,false);
		addVariablePrompt(VARIABLE_BOOK_DURATION,"Hoe lang?");
		addVariablePrompt(VARIABLE_BOOK_DURATION,"Hoe lang heeft u een kamer nodig?");
		addVariablePrompt(VARIABLE_BOOK_DURATION,"Hoe lang wilt u een kamer gebruiken?");

		addVariable(VARIABLE_BOOK_PEOPLE,BaseConfiguration.TYPE_NUMERIC);
		addVariablePrompt(VARIABLE_BOOK_PEOPLE,"Voor hoeveel personen?");
		addVariablePrompt(VARIABLE_BOOK_PEOPLE,"Voor hoeveel personen heeft u een kamer nodig?");
		addVariablePrompt(VARIABLE_BOOK_PEOPLE,"Hoeveel personen moeten in de kamer passen?");

		addVariable(VARIABLE_BOOK_CONFIRMATION,BaseConfiguration.TYPE_CONFIRMATION);
		addVariablePrompt(VARIABLE_BOOK_CONFIRMATION,
			"Begrijp ik goed dat u een kamer wilt boeken op {" + VARIABLE_BOOK_DATE +
			"}, voor {" + VARIABLE_BOOK_PEOPLE +
			"} personen, vanaf {" + VARIABLE_BOOK_TIME +
			"}, voor {" + VARIABLE_BOOK_DURATION + "}?");
		addVariablePrompt(VARIABLE_BOOK_CONFIRMATION,
			"Begrijp ik goed dat u een kamer wilt boeken op {" + VARIABLE_BOOK_DATE +
			"}, voor {" + VARIABLE_BOOK_PEOPLE +
			"} personen, vanaf {" + VARIABLE_BOOK_TIME +
			"}, voor een duur van {" + VARIABLE_BOOK_DURATION + "}?");
		addVariablePrompt(VARIABLE_BOOK_CONFIRMATION,
			"U wilt een kamer op {" + VARIABLE_BOOK_DATE +
			"}, voor {" + VARIABLE_BOOK_PEOPLE +
			"} personen, vanaf {" + VARIABLE_BOOK_TIME +
			"}, voor een duur van {" + VARIABLE_BOOK_DURATION + "}. Klopt dat?");
		
		addNextDialogVariable();
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"Kan ik nog iets anders voor u doen?");
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"Kan ik u nog ergens anders mee helpen?");
	}
	
	protected String getOutput1() {
		return EXAMPLE_OUTPUT_1;
	}
	
	protected String getOutput2() {
		return EXAMPLE_OUTPUT_2;
	}
}
