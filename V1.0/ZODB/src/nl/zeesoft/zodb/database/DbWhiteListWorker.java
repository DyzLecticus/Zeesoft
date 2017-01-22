package nl.zeesoft.zodb.database;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;

public final class DbWhiteListWorker extends Worker {
	private static DbWhiteListWorker indexWorker = null;
	private DbWhiteListWorker() {
		setSleep(300000);
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}
	public static DbWhiteListWorker getInstance() {
		if (indexWorker==null) {
			indexWorker = new DbWhiteListWorker();
			DbWhiteList.getInstance();
		}
		return indexWorker;
	}
	@Override
	public void start() {
		Messenger.getInstance().debug(this, "Starting whitelist worker ...");
		super.start();
		Messenger.getInstance().debug(this, "Started whitelist worker");
    }
	@Override
    public void stop() {
		Messenger.getInstance().debug(this, "Stopping whitelist worker ...");
		super.stop();
		Messenger.getInstance().debug(this, "Stopped whitelist worker");
    }
	@Override
	public void whileWorking() {
		DbWhiteList.getInstance().updateWhiteListAndKillSessions();
	}
}	
