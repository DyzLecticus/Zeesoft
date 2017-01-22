package nl.zeesoft.zodb.database;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;

public final class DbSessionServerWorker extends Worker {
	private static DbSessionServerWorker	serverWorker 	= null;
	private DbSessionServerWorker() {
		setSleep(1);
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}
	public static DbSessionServerWorker getInstance() {
		if (serverWorker==null) {
			serverWorker = new DbSessionServerWorker();
		}
		return serverWorker;
	}
	@Override
	public void start() {
		Messenger.getInstance().debug(this, "Starting server ...");
		if (DbSessionServer.getInstance().open()) {
			super.start();
		}
		Messenger.getInstance().debug(this, "Started server");
    }
	@Override
    public void stop() {
		Messenger.getInstance().debug(this, "Stopping server ...");
		super.stop();
		while(isWorking()) {
			sleep(10);
		}
		DbSessionServer.getInstance().close();
		Messenger.getInstance().debug(this, "Stopped server");
    }
	@Override
	public void whileWorking() {
		DbSessionServer.getInstance().acceptAndProcessSessions();
	}
}
