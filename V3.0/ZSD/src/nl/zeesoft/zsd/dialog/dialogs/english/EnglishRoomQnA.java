package nl.zeesoft.zsd.dialog.dialogs.english;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.RoomQnA;

public class EnglishRoomQnA extends RoomQnA {
	private static final String		ANSWER_COST		= "There are no costs attached to booking a room.";
	
	private List<String>			rooms			= new ArrayList<String>();
	private List<String>			books			= new ArrayList<String>();
	
	public EnglishRoomQnA() {
		setLanguage(BaseConfiguration.LANG_ENG);

		books.add("book");
		books.add("reserve");
		
		rooms.add("room");
		rooms.add("space");
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		for (String book: books) {
			for (String room: rooms) {
				addRoomBookingExample("What does it cost to {book} a {room}?",book,room,ANSWER_COST);
				if (book.equals("book")) {
					book = "booking";
				} else if (book.equals("reserve")) {
					book = "reserving";
				}
				addRoomBookingExample("Are there any costs attached to {book} van een {room}?",book,room,ANSWER_COST);
			}
		}
	}
	private void addRoomBookingExample(String question,String book,String room,String answer) {
		ZStringBuilder phrase = new ZStringBuilder(question);
		phrase.replace("{book}",book);
		phrase.replace("{room}",room);
		addExample(phrase.toString(),answer);
	}
}
