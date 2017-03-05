package nl.zeesoft.zjmo.orchestra.members;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class ConductorMemberDrainOfflineWorker extends Worker {
	private ConductorMemberController	controller	= null;
	private String						memberId	= "";

	public ConductorMemberDrainOfflineWorker(ConductorMemberController controller,String memberId) {
		super(null,null);
		setSleep(1000);
		this.controller = controller;
		this.memberId = memberId;
	}

	public ConductorMemberDrainOfflineWorker(Messenger msgr, WorkerUnion union,ConductorMemberController controller,String memberId) {
		super(msgr,union);
		setSleep(1000);
		this.controller = controller;
		this.memberId = memberId;
	}
	
	@Override
	public void whileWorking() {
		controller.getState(memberId);
		if (controller.getMemberWorkLoad(memberId)==0) {
			ZStringBuilder response = controller.takeOffline(memberId);
			if (response.toString().equals("{\"response\":\"Executed command\"}")) {
				controller.getState(memberId);
			}
			stop();
		}
	}
}
