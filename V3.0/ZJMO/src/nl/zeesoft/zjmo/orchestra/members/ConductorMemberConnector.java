package nl.zeesoft.zjmo.orchestra.members;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
import nl.zeesoft.zjmo.orchestra.client.ActiveClient;
import nl.zeesoft.zjmo.orchestra.client.OrchestraConnector;

/**
 * Connector for the conductor member controller.
 * Sets the member state to unknown if the connection to one of the orchestra members is lost.
 */
public class ConductorMemberConnector extends OrchestraConnector {
	private ConductorMemberController controller = null;
	
	public ConductorMemberConnector(Messenger msgr, WorkerUnion uni, boolean control,ConductorMemberController controller) {
		super(msgr, uni, control);
		this.controller = controller;
	}
	
	@Override
	protected ActiveClient getNewActiveClient(WorkerUnion union, OrchestraMember member, boolean control) {
		ActiveClient client = new ActiveClient(getMessenger(),union,member,control) {
			@Override
			protected void disconnected() {
				controller.setStateUnknown(getMember().getId(),"Lost connection");
			}
		};
		return client;
	}
}
