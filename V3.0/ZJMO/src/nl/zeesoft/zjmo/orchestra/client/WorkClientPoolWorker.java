package nl.zeesoft.zjmo.orchestra.client;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

/**
 * Closes work clients in the pool that have not been used for a certain amount of time.
 */
public class WorkClientPoolWorker extends Worker {
	private WorkClientPool		pool		= null;

	public WorkClientPoolWorker(Messenger msgr, WorkerUnion union,WorkClientPool pool,int sleep) {
		super(msgr,union);
		setSleep(sleep);
		this.pool = pool;
	}
	
	@Override
	public void whileWorking() {
		pool.closeUnusedClients(getSleep());
	}
}
