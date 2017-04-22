package nl.zeesoft.zjmo.orchestra.protocol;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.client.ActiveClient;

/**
 * Handles control channel requests for the conductor control protocol.
 */
public class ControlChannelWorker extends Worker {
	private ZStringBuilder		json		= null;
	private	ActiveClient		client		= null;

	private boolean				done		= false;
	private	ZStringBuilder		response	= null;
	
	protected ControlChannelWorker(Messenger msgr, WorkerUnion union,ZStringBuilder json,ActiveClient client) {
		super(msgr,union);
		setSleep(1);
		this.json = json;
		this.client = client;
	}
	
	@Override
	public void start() {
		setDone(false);
		super.start();
	}

	@Override
	public void whileWorking() {
		response = client.getClient().writeOutputReadInput(json,client.getTimeout());
		stop();
		setDone(true);
	}

	public boolean isDone() {
		boolean r = false;
		lockMe(this);
		r = done;
		unlockMe(this);
		return r;
	}

	protected void setDone(boolean done) {
		lockMe(this);
		this.done = done;
		unlockMe(this);
	}

	public ActiveClient getClient() {
		return client;
	}

	public ZStringBuilder getResponse() {
		return response;
	}
}
