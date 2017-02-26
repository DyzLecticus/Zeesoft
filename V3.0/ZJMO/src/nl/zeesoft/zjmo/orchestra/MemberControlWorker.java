package nl.zeesoft.zjmo.orchestra;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class MemberControlWorker extends Worker {
	private MemberObject		member		= null;

	protected MemberControlWorker(MemberObject member) {
		super(null,null);
		this.member = member;
	}

	protected MemberControlWorker(Messenger msgr, WorkerUnion union,MemberObject member) {
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
		member.acceptControl();
	}
}
