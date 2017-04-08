package nl.zeesoft.zjmo.orchestra.client;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
import nl.zeesoft.zjmo.orchestra.members.WorkClient;
import nl.zeesoft.zjmo.orchestra.protocol.PublishRequest;
import nl.zeesoft.zjmo.orchestra.protocol.RequestObject;
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
		RequestObject r = sendRequest(wr);
		if (r!=null) {
			response = (WorkRequest) r;
		}
		return response;
	}

	public PublishRequest publishRequest(PublishRequest pr) {
		PublishRequest response = null;
		RequestObject r = sendRequest(pr);
		if (r!=null) {
			response = (PublishRequest) r;
		}
		return response;
	}

	public RequestObject sendRequest(RequestObject r) {
		RequestObject response = null;
		List<ActiveClient> acs = getOpenClients(Orchestra.CONDUCTOR);
		for (ActiveClient client: acs) {
			WorkClient wc = client.getWorkClient();
			if (wc!=null) {
				ZStringBuilder resp = wc.sendRequest(r);
				if (r instanceof WorkRequest) {
					response = new WorkRequest();
				} else if (r instanceof PublishRequest) {
					response = new PublishRequest();
				}
				response.fromStringBuilder(resp);
			}
		}
		return response;
	}
	
	protected List<OrchestraMember> getOrchestraMembers(Orchestra orch) {
		return orch.getMembers();
	}
}
