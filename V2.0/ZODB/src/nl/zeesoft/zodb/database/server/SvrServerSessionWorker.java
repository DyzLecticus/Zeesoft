package nl.zeesoft.zodb.database.server;

import nl.zeesoft.zodb.Worker;

public class SvrServerSessionWorker extends Worker {
	private SvrServer 			server					= null;

	protected SvrServerSessionWorker(SvrServer server) {
		setSleep(500);
		this.server = server;
	}
	
	@Override
	public void whileWorking() {
		server.checkSessionTimeOut(this);
	}
}
