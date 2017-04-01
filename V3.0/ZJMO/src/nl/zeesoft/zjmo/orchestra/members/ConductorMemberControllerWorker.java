package nl.zeesoft.zjmo.orchestra.members;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class ConductorMemberControllerWorker extends Worker {
	private ConductorMemberController	controller	= null;
	private boolean						first		= true;

	public ConductorMemberControllerWorker(Messenger msgr, WorkerUnion union,ConductorMemberController controller) {
		super(msgr,union);
		setSleep(10000);
		this.controller = controller;
	}
	
	@Override
	public void start() {
		first = true;
		super.start();
	}
	
	@Override
	public void whileWorking() {
		controller.getState(null,first);
	}
}
