package nl.zeesoft.zjmo.orchestra;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class MemberWorkWorker extends Worker {
	private MemberObject		member		= null;

	protected MemberWorkWorker(MemberObject member) {
		super(null,null);
		this.member = member;
	}

	protected MemberWorkWorker(Messenger msgr, WorkerUnion union,MemberObject member) {
		super(msgr,union);
		this.member = member;
	}

	@Override
	public void stop() {
		waitForStop(10,false);
		super.stop();
	}

	@Override
	public void whileWorking() {
		member.acceptWork();
	}
}
