package nl.zeesoft.zjmo.orchestra.members;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class ConductorMemberDrainOnlineWorker extends ConductorMemberDrainOfflineWorker {
	public ConductorMemberDrainOnlineWorker(Messenger msgr, WorkerUnion union, ConductorMemberController controller,String memberId) {
		super(msgr, union, controller, memberId);
	}
	public ZStringBuilder getTakeOfflineResponse(ConductorMemberController controller, String memberId) {
		controller.takeOffline(memberId);
		return controller.bringOnline(memberId);
	}
}
