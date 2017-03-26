package nl.zeesoft.zjmo.orchestra.members;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.ProtocolObject;

public class ConductorMemberDrainOfflineWorker extends Worker {
	private ConductorMemberController	controller	= null;
	private String						memberId	= "";

	public ConductorMemberDrainOfflineWorker(Messenger msgr, WorkerUnion union,ConductorMemberController controller,String memberId) {
		super(msgr,union);
		setSleep(1000);
		this.controller = controller;
		this.memberId = memberId;
	}
	
	@Override
	public void whileWorking() {
		controller.closeUnusedClients(memberId,0);
		controller.getState(memberId,false);
		if (controller.getMemberWorkLoad(memberId)==0) {
			ZStringBuilder response = getTakeOfflineResponse(controller,memberId);
			if (response==null || ProtocolObject.isResponseJson(response)) {
				controller.getState(memberId,false);
			}
			stop();
		}
	}
	
	public ZStringBuilder getTakeOfflineResponse(ConductorMemberController controller, String memberId) {
		return controller.takeOffline(memberId);
	}
}
