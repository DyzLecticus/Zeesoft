package nl.zeesoft.zodb.database;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;

public final class DbControlServerWorker extends Worker {
	private static DbControlServerWorker 	serverControlWorker = null;
	private DbControlServerWorker() {
		setSleep(1);
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}
	public static DbControlServerWorker getInstance() {
		if (serverControlWorker==null) {
			serverControlWorker = new DbControlServerWorker();
		}
		return serverControlWorker;
	}
	@Override
	public void start() {
		Messenger.getInstance().debug(this, "Starting server control ...");
		if (DbControlServer.getInstance().open()) {
			super.start();
		}
		Messenger.getInstance().debug(this, "Started server control");
    }
	@Override
    public void stop() {
		Messenger.getInstance().debug(this, "Stopping server control ...");
		super.stop();
		while(isWorking()) {
			sleep(10);
		}
		DbControlServer.getInstance().close();
		Messenger.getInstance().debug(this, "Stopped server control");
    }
	@Override
	public void whileWorking() {
		DbControlServer.getInstance().acceptAndProcessSessions();
	}
}
