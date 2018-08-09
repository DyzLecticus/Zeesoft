package nl.zeesoft.zsd.dialog.dialogs.english;

import nl.zeesoft.zsd.dialog.dialogs.RoomBookingHandler;

public class EnglishRoomBookingHandler extends RoomBookingHandler {
	@Override
	protected String getSuccesResponse() {
 		return "Room 1 has been booked.";
	}

	@Override
	protected String getMinimumPeopleResponse() {
		return "The minimum number of people to book a room is two.";
	}
}
