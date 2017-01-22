package nl.zeesoft.zodb.database;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;

public final class DbRequestQueueWorker extends Worker {
	private static DbRequestQueueWorker	serverWorker 	= null;
	private DbRequestQueueWorker() {
		setSleep(1);
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}
	protected static DbRequestQueueWorker getInstance() {
		if (serverWorker==null) {
			serverWorker = new DbRequestQueueWorker();
		}
		return serverWorker;
	}
	@Override
	public void start() {
		Messenger.getInstance().debug(this, "Starting request queue worker ...");
		DbIndex.getInstance();
		super.start();
		Messenger.getInstance().debug(this, "Started request queue worker");
    }
	@Override
    public void stop() {
		Messenger.getInstance().debug(this, "Stopping request queue worker ...");
		super.stop();
		waitForStop(10,false);
		Messenger.getInstance().debug(this, "Stopped request queue worker");
    }
	@Override
	public void whileWorking() {
		DbIndex.getInstance().processNextRquestsFromQueue(this);
	}
}
