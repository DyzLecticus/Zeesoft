package nl.zeesoft.zjmo.orchestra.client;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
import nl.zeesoft.zjmo.orchestra.members.WorkClient;
import nl.zeesoft.zjmo.orchestra.protocol.WorkRequest;

public class OrchestraConnector extends ActiveClients {
	public OrchestraConnector(Messenger msgr, WorkerUnion uni, boolean control) {
		super(msgr, uni, control);
	}

	public void initialize(Orchestra orch,String exceptMemberId) {
		List<OrchestraMember> members = new ArrayList<OrchestraMember>();
		if (exceptMemberId!=null && exceptMemberId.length()>0) {
			for (OrchestraMember member: getOrchestraMembers(orch)) {
				if (!member.getId().equals(exceptMemberId)) {
					members.add(member);
				}
			}
		} else {
			members = getOrchestraMembers(orch);
		}
		super.initialize(members);
	}

	public WorkRequest sendWorkRequest(WorkRequest wr) {
		WorkRequest response = null;
		List<ActiveClient> acs = getOpenClients(Orchestra.CONDUCTOR);
		for (ActiveClient client: acs) {
			WorkClient wc = client.getWorkClient();
			if (wc!=null) {
				ZStringBuilder resp = wc.sendWorkRequest(wr);
				response = new WorkRequest();
				response.fromStringBuilder(resp);
				if (response.getError().length()==0) {
					break;
				}
			}
		}
		return response;
	}
	
	protected List<OrchestraMember> getOrchestraMembers(Orchestra orch) {
		return orch.getMembers();
	}
}
