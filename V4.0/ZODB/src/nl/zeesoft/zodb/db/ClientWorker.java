package nl.zeesoft.zodb.db;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class ClientWorker extends Worker {
	private Client	client	= null;

	public ClientWorker(Messenger msgr, WorkerUnion union, Client client) {
		super(msgr, union);
		this.client = client;
		setSleep(1000);
	}

	@Override
	public void whileWorking() {
		// TODO Auto-generated method stub
		if (client.handleRequest()) {
			client = null;
			stop();
		}
	}

}
