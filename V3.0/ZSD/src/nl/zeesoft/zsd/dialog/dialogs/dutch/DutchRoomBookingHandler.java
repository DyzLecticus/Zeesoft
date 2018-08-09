package nl.zeesoft.zsd.dialog.dialogs.dutch;

import nl.zeesoft.zsd.dialog.dialogs.RoomBookingHandler;

public class DutchRoomBookingHandler extends RoomBookingHandler {
	@Override
	protected String getSuccesResponse() {
 		return "Kamer 1 is geboekt.";
	}

	@Override
	protected String getMinimumPeopleResponse() {
		return "Het minimaal aantal personen voor het boeken van een kamer is twee.";
	}
}
