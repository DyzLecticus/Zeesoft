package nl.zeesoft.zjmo.orchestra;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

/**
 * Accepts work sessions on the MemberObject.
 */
public class MemberWorkWorker extends Worker {
	private MemberObject		member		= null;

	protected MemberWorkWorker(Messenger msgr, WorkerUnion union,MemberObject member) {
		super(msgr,union);
		setSleep(1);
		this.member = member;
	}

	@Override
	public void whileWorking() {
		member.acceptWork();
	}
}
