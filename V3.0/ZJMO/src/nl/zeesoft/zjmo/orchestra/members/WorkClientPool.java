package nl.zeesoft.zjmo.orchestra.members;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;

public class WorkClientPool extends Locker {
	private List<WorkClients> workClientsList = new ArrayList<WorkClients>();

	protected WorkClientPool(Messenger msgr, Orchestra orch) {
		super(msgr);
		for (OrchestraMember member: orch.getMembers()) {
			workClientsList.add(new WorkClients(msgr,member));
		}
	}

	protected WorkClient getClient(Object source,String memberId) {
		WorkClient r = null;
		lockMe(source);
		for (WorkClients workCl: workClientsList) {
			if (workCl.getMember().getId().equals(memberId)) {
				r = workCl.getClient(source);
				if (r!=null) {
					break;
				} else {
					getMessenger().debug(this,"Failed to connect to: " + memberId);
				}
			}
		}
		unlockMe(source);
		return r;
	}

	protected void returnClient(WorkClient client) {
		lockMe(this);
		for (WorkClients workCl: workClientsList) {
			if (workCl.getMember().getId().equals(client.getMemberId())) {
				workCl.returnClient(client);
				break;
			}
		}
		unlockMe(this);
	}

	protected void closeUnusedClients(String memberId,long unusedMs) {
		lockMe(this);
		for (WorkClients workCl: workClientsList) {
			if (workCl.getMember().getId().equals(memberId)) {
				workCl.closeUnusedClients(unusedMs);
				break;
			}
		}
		unlockMe(this);
	}

	protected void closeAllClients() {
		lockMe(this);
		for (WorkClients workCl: workClientsList) {
			workCl.closeAllClients();
		}
		unlockMe(this);
	}
}
