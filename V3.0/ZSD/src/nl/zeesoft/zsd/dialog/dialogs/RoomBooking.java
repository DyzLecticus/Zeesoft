package nl.zeesoft.zsd.dialog.dialogs;

public abstract class RoomBooking extends Generic {
	public static final String	CONTEXT_ROOM_BOOKING		= "Booking";

	public static final String	VARIABLE_BOOK_DATE			= "bookDate";
	public static final String	VARIABLE_BOOK_TIME			= "bookTime";
	public static final String	VARIABLE_BOOK_DURATION		= "bookDuration";
	public static final String	VARIABLE_BOOK_PEOPLE		= "bookPeople";
	public static final String	VARIABLE_BOOK_CONFIRMATION	= "bookConfirmation";

	public RoomBooking() {
		setContext(CONTEXT_ROOM_BOOKING);
		setHandlerClassName(RoomBookingHandler.class.getName());
	}
}
