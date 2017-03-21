package nl.zeesoft.zjmo.orchestra.members;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.ProtocolObject;

public class ConductorMemberDrainOnlineWorker extends ConductorMemberDrainOfflineWorker {
	public ConductorMemberDrainOnlineWorker(Messenger msgr, WorkerUnion union, ConductorMemberController controller,String memberId) {
		super(msgr, union, controller, memberId);
	}
	public ZStringBuilder getTakeOfflineResponse(ConductorMemberController controller, String memberId) {
		ZStringBuilder response = controller.drainOffline(memberId);
		if (response!=null && ProtocolObject.isResponseJson(response)) {
			response = controller.takeOffline(memberId);
		}
		if (response!=null && ProtocolObject.isResponseJson(response)) {
			response = controller.bringOnline(memberId);
		}
		return response;
	}
}
