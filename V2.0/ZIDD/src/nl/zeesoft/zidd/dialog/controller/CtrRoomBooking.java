package nl.zeesoft.zidd.dialog.controller;

import java.util.Calendar;
import java.util.Date;

import nl.zeesoft.zidd.database.model.ZIDDDataGenerator;
import nl.zeesoft.zids.database.model.Session;
import nl.zeesoft.zids.database.model.SessionDialogVariable;
import nl.zeesoft.zids.dialog.controller.CtrObject;
import nl.zeesoft.zids.dialog.pattern.PtnManager;
import nl.zeesoft.zids.server.SvrControllerSessions;
import nl.zeesoft.zodb.event.EvtEvent;

public class CtrRoomBooking extends CtrObject {
	
	public void validateSessionDialogVariables(Session s, PtnManager m) {
		StringBuilder response = new StringBuilder();
		if (allVariablesFilled(s)) {
			SessionDialogVariable confirmation = SvrControllerSessions.getInstance().getSessionDialogVariableByCode(s,ZIDDDataGenerator.EN_BOOKING_CONFIRMATION);
			boolean confirmed = m.getConfirmationValueFromPatternValue(confirmation.getValue().toString());
			if (confirmed) {
				SessionDialogVariable date = SvrControllerSessions.getInstance().getSessionDialogVariableByCode(s,ZIDDDataGenerator.EN_BOOKING_DATE);
				SessionDialogVariable time = SvrControllerSessions.getInstance().getSessionDialogVariableByCode(s,ZIDDDataGenerator.EN_BOOKING_TIME);
				SessionDialogVariable duration = SvrControllerSessions.getInstance().getSessionDialogVariableByCode(s,ZIDDDataGenerator.EN_BOOKING_DURATION);
				SessionDialogVariable number = SvrControllerSessions.getInstance().getSessionDialogVariableByCode(s,ZIDDDataGenerator.EN_BOOKING_NUMBER);
				
				Date dat = m.getDateValueFromPatternValue(date.getValue().toString());
				long tim = m.getTimeValueFromPatternValue(time.getValue().toString());
				long dur = m.getDurationValueFromPatternValue(duration.getValue().toString());
				int num = m.getNumberValueFromPatternValue(number.getValue().toString());
				
				String bookedRoom = bookRoom(dat,tim,dur,num);
				if (bookedRoom.length()>0) {
					response.append("I have booked room " + bookedRoom + " for you.");
				} else {
					response.append("There is no room available for the specified date, time, duration and number of people.");
					SvrControllerSessions.getInstance().removeSessionDialogVariablesFromSession(s);
				}
			} else {
				SvrControllerSessions.getInstance().removeSessionDialogVariablesFromSession(s);
			}
		}
		setResponse(response);
	}
	
	protected String bookRoom(Date date, long time, long duration, int people) {
		String bookedRoom = "";
		Date dt = new Date();
		dt.setTime(date.getTime() + time);
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if (hour>=14) {
			bookedRoom = "HASHTAG";
		} else if (hour>=12) {
			bookedRoom = "BLOG";
		}
		return bookedRoom;
	}

	@Override
	public void handleEvent(EvtEvent e) {
		// TODO: Create empty in super
	}
}
