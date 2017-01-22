package nl.zeesoft.zids.server;

import java.util.Date;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;

public class SvrSessionCleanUpWorker extends Worker {
	public SvrSessionCleanUpWorker() {
		setSleep(60000);
	}
	@Override
	public void whileWorking() {
		Date now = new Date();
		SvrControllerSessions.getInstance().closeExpiredSessions(now.getTime() - (60 * 60 * 1000));
		int removed = SvrControllerSessions.getInstance().removeClosedSessions();
		if (removed>0) {
			Messenger.getInstance().debug(this,"Removed closed sessions: " + removed);
		}
	}
}
