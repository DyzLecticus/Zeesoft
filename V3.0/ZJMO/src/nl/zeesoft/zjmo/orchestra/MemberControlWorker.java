package nl.zeesoft.zjmo.orchestra;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

/**
 * Accepts control sessions on the MemberObject.
 */
public class MemberControlWorker extends Worker {
	private MemberObject		member		= null;

	protected MemberControlWorker(Messenger msgr, WorkerUnion union,MemberObject member) {
		super(msgr,union);
		setSleep(1);
		this.member = member;
	}
	
	@Override
	public void whileWorking() {
		member.acceptControl();
	}
}
