package nl.zeesoft.zjmo.orchestra.protocol;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.members.WorkClient;

/**
 * Accepts work sessions on the MemberObject.
 */
public class PublishRequestWorker extends Worker {
	private	WorkRequest			request		= null;
	private	WorkClient			client		= null;

	private boolean				done		= false;
	private	ZStringBuilder		response	= null;
	
	protected PublishRequestWorker(Messenger msgr, WorkerUnion union,WorkRequest request,WorkClient client) {
		super(msgr,union);
		setSleep(1);
		this.request = request;
		this.client = client;
	}
	
	@Override
	public void start() {
		setDone(false);
		super.start();
	}

	@Override
	public void whileWorking() {
		response = client.sendWorkRequestRequest(request);
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

	public WorkRequest getRequest() {
		return request;
	}

	public WorkClient getClient() {
		return client;
	}

	public ZStringBuilder getResponse() {
		return response;
	}
}
