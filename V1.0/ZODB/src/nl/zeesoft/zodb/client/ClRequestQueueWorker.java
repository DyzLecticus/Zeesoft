package nl.zeesoft.zodb.client;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;

public final class ClRequestQueueWorker extends Worker {
	private static ClRequestQueue queue = null;
	
	public ClRequestQueueWorker(ClRequestQueue q) {
		queue = q;
		setSleep(1);
	}
	
	@Override
	public void start() {
		Messenger.getInstance().debug(this, "Starting request queue ...");
		super.start();
		Messenger.getInstance().debug(this, "Started request queue");
    }

	@Override
    public void stop() {
		Messenger.getInstance().debug(this, "Stopping request queue ...");
		super.stop();
		Messenger.getInstance().debug(this, "Stopped request queue");
    }
	
	@Override
	public void whileWorking() {
		queue.sendRequestAndProcessResponse(this);
	}
}
