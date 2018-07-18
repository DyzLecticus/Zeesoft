package nl.zeesoft.zsd.dialog.dialogs.dutch;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.RoomQnA;

public class DutchRoomQnA extends RoomQnA {
	private static final String		ANSWER_COST		= "Er zijn geen kosten verbonden aan het boeken van een kamer.";
	
	private List<String>			rooms			= new ArrayList<String>();
	private List<String>			books			= new ArrayList<String>();
	
	public DutchRoomQnA() {
		setLanguage(BaseConfiguration.LANG_NLD);

		books.add("boeken");
		books.add("reserveren");
		
		rooms.add("kamer");
		rooms.add("ruimte");
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		for (String book: books) {
			for (String room: rooms) {
				addRoomBookingExample("Wat kost het {book} van een {room}?",book,room,ANSWER_COST);
				addRoomBookingExample("Zijn er kosten verbonden aan {book} van een {room}?",book,room,ANSWER_COST);
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
