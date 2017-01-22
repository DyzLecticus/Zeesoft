package nl.zeesoft.zodb.client;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;

public final class ClSessionWorker extends Worker {
	private ClSession 				session 			= null;
	private ClRequestQueueWorker 	requestQueueWorker 	= null; 
	
	public ClSessionWorker(ClSession s) {
		session = s;
		requestQueueWorker = new ClRequestQueueWorker(session.getRequestQueue());
		setSleep(1);
	}
	
	@Override
	public void start() {
		if (session.open()) {
			Messenger.getInstance().debug(this, "Starting client session: " + session.getSessionId());
			super.start();
			requestQueueWorker.start();
			Messenger.getInstance().debug(this, "Started client session: " + session.getSessionId());
		} else {
			if (session.getSessionId()==0) {
				Messenger.getInstance().error(this, "Unable to open client session");
			} else {
				Messenger.getInstance().error(this, "Unable to open client session: " + session.getSessionId());
			}
		}
    }

	@Override
    public void stop() {
		Messenger.getInstance().debug(this, "Stopping client session: " + session.getSessionId());
		super.stop();
		session.close();
		requestQueueWorker.stop();
		Messenger.getInstance().debug(this, "Stopped client session: " + session.getSessionId());
    }
	
	@Override
	public void whileWorking() {
		session.readAndProcessInput();
	}
}
