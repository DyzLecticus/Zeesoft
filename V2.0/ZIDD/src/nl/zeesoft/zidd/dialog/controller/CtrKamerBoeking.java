package nl.zeesoft.zidd.dialog.controller;

import java.util.Date;

import nl.zeesoft.zidd.database.model.ZIDDDataGenerator;
import nl.zeesoft.zids.database.model.Session;
import nl.zeesoft.zids.database.model.SessionDialogVariable;
import nl.zeesoft.zids.dialog.pattern.PtnManager;
import nl.zeesoft.zids.server.SvrControllerSessions;

public class CtrKamerBoeking extends CtrRoomBooking {
	
	public void validateSessionDialogVariables(Session s, PtnManager m) {
		StringBuilder response = new StringBuilder();
		if (allVariablesFilled(s)) {
			SessionDialogVariable confirmation = SvrControllerSessions.getInstance().getSessionDialogVariableByCode(s,ZIDDDataGenerator.NL_BOOKING_CONFIRMATION);
			boolean confirmed = m.getConfirmationValueFromPatternValue(confirmation.getValue().toString());
			if (confirmed) {
				SessionDialogVariable date = SvrControllerSessions.getInstance().getSessionDialogVariableByCode(s,ZIDDDataGenerator.NL_BOOKING_DATE);
				SessionDialogVariable time = SvrControllerSessions.getInstance().getSessionDialogVariableByCode(s,ZIDDDataGenerator.NL_BOOKING_TIME);
				SessionDialogVariable duration = SvrControllerSessions.getInstance().getSessionDialogVariableByCode(s,ZIDDDataGenerator.NL_BOOKING_DURATION);
				SessionDialogVariable number = SvrControllerSessions.getInstance().getSessionDialogVariableByCode(s,ZIDDDataGenerator.NL_BOOKING_NUMBER);
				
				Date dat = m.getDateValueFromPatternValue(date.getValue().toString());
				long tim = m.getTimeValueFromPatternValue(time.getValue().toString());
				long dur = m.getDurationValueFromPatternValue(duration.getValue().toString());
				int num = m.getNumberValueFromPatternValue(number.getValue().toString());
				
				String bookedRoom = bookRoom(dat,tim,dur,num);
				if (bookedRoom.length()>0) {
					response.append("Ik heb kamer " + bookedRoom + " voor u geboekt.");
				} else {
					response.append("Er is geen kamer beschikbaar voor de opgegeven datum, tijd, duur en aantal personen.");
					SvrControllerSessions.getInstance().removeSessionDialogVariablesFromSession(s);
				}
			} else {
				SvrControllerSessions.getInstance().removeSessionDialogVariablesFromSession(s);
			}
		}
		setResponse(response);
	}
}
