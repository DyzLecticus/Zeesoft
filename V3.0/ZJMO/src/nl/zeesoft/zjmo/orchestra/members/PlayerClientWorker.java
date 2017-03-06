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
			//System.out.println("Attempting to connect to conductor ...");
			boolean open = client.open();
			if (open) {
				//System.out.println("Connected to conductor");
				client.sendCommand(ProtocolControlConductor.UPDATE_MEMBER_STATE,"id",memberId);
			} else {
				//System.out.println("Failed to connect to conductor");
			}
		}
	}
}
