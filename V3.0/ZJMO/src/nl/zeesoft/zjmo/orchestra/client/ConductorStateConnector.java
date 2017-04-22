package nl.zeesoft.zjmo.orchestra.client;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControlConductor;

/**
 * Used by players to connect to conductors. 
 */
public class ConductorStateConnector extends ConductorConnector {
	private String	memberId		= "";
	
	public ConductorStateConnector(Messenger msgr, WorkerUnion uni, boolean control,String memberId) {
		super(msgr, uni, control);
		this.memberId = memberId;
	}

	@Override
	protected ActiveClient getNewActiveClient(WorkerUnion union, OrchestraMember member, boolean control) {
		ActiveClient client = new ActiveClient(getMessenger(),union,member,control) {
			@Override
			protected void connected() {
				getClient().sendCommand(ProtocolControlConductor.CONNECT_MEMBER,"id",memberId);
			}
		};
		return client;
	}
}
