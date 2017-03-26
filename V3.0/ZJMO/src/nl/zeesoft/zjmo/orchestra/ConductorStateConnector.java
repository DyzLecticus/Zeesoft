package nl.zeesoft.zjmo.orchestra;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControlConductor;

public class ConductorStateConnector extends ConductorConnector {
	private String memberId = "";
	
	public ConductorStateConnector(Messenger msgr, WorkerUnion uni,String memberId) {
		super(msgr,uni);
		this.memberId = memberId;
	}
	
	@Override
	public void initialize(List<OrchestraMember> conductors,boolean control) {
		List<OrchestraMember> cons = new ArrayList<OrchestraMember>();
		for (OrchestraMember conductor: conductors) {
			if (!conductor.getId().equals(memberId)) {
				cons.add(conductor);
			}
		}
		super.initialize(cons,control);
	}
	
	protected void onOpenClient(MemberClient client) {
		client.sendCommand(ProtocolControlConductor.CONNECT_MEMBER,"id",memberId);
	}
}
