package nl.zeesoft.zodb.database.server;

import nl.zeesoft.zodb.Worker;

public class SvrServerAcceptWorker extends Worker {
	private SvrServer 			server					= null;

	protected SvrServerAcceptWorker(SvrServer server) {
		setSleep(0);
		this.server = server;
	}
	
	@Override
	public void whileWorking() {
		boolean isAccepting = server.acceptAndProcessSockets();
		if (!isAccepting) {
			setSleep(1000);
		} else {
			setSleep(0);
		}
	}
}
