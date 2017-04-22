package nl.zeesoft.zjmo.orchestra.client;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

/**
 * Used to continuously attempt to connect active clients.
 */
public class ActiveClientConnectWorker extends Worker {
	private ActiveClient	activeClient	= null;
	
	protected ActiveClientConnectWorker(Messenger msgr, WorkerUnion union, ActiveClient activeClient) {
		super(msgr, union);
		setSleep(1000);
		this.activeClient = activeClient;
	}
	
	@Override
	public void whileWorking() {
		activeClient.connect();
	}
}
