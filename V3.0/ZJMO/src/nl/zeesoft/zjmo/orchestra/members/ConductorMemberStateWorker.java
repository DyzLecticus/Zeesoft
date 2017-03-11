package nl.zeesoft.zjmo.orchestra.members;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.MemberClient;

public class ConductorMemberStateWorker extends Worker {
	private ConductorMemberController	controller	= null;
	private MemberClient				client		= null;
	private String						memberId	= "";

	public ConductorMemberStateWorker(Messenger msgr, WorkerUnion union,ConductorMemberController controller,MemberClient client, String memberId) {
		super(msgr,union);
		setSleep(0);
		this.controller = controller;
		this.client = client;
		this.memberId = memberId;
	}
	
	@Override
	public void stop() {
		super.stop();
		client.close();
	}
	
	@Override
	public void whileWorking() {
		ZStringBuilder input = client.readInput();
		//System.out.println("-------------->>> Input: " + input);
		if (input==null || input.length()==0) {
			controller.setStateUnknown(memberId,"Lost connection");
			client.close();
			stop();
		}
	}
}
