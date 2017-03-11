package nl.zeesoft.zjmo.orchestra.members;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.MemberClient;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControlConductor;

public class PlayerClientWorker extends Worker {
	private MemberClient		client		= null;
	private String				memberId	= "";

	public PlayerClientWorker(Messenger msgr, WorkerUnion union,MemberClient client, String memberId) {
		super(msgr,union);
		setSleep(1000);
		this.client = client;
		this.memberId = memberId;
	}
	
	@Override
	public void whileWorking() {
		if (!client.isOpen()) {
			boolean open = client.open();
			if (open) {
				client.sendCommand(ProtocolControlConductor.UPDATE_MEMBER_STATE,"id",memberId);
			}
		}
	}
}
