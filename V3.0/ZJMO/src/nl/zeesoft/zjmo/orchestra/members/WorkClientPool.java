package nl.zeesoft.zjmo.orchestra.members;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;

public class WorkClientPool extends Locker {
	private Orchestra			orchestra		= null;
	private List<WorkClients>	workClientsList	= new ArrayList<WorkClients>();

	protected WorkClientPool(Messenger msgr,WorkerUnion union,Orchestra orchestra) {
		super(msgr);
		this.orchestra = orchestra;
		for (OrchestraMember member: orchestra.getMembers()) {
			workClientsList.add(new WorkClients(msgr,union,member));
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

	protected void closeUnusedClients(long unusedMs) {
		lockMe(this);
		for (OrchestraMember member: orchestra.getMembers()) {
			closeUnusedClientsNoLock(member.getId(),unusedMs);
		}
		unlockMe(this);
	}
	
	protected void closeUnusedClients(String memberId,long unusedMs) {
		lockMe(this);
		closeUnusedClientsNoLock(memberId,unusedMs);
		unlockMe(this);
	}

	protected void closeUnusedClientsNoLock(String memberId,long unusedMs) {
		for (WorkClients workCl: workClientsList) {
			if (workCl.getMember().getId().equals(memberId)) {
				workCl.closeUnusedClients(unusedMs);
				break;
			}
		}
	}
	
	protected void closeAllClients() {
		lockMe(this);
		for (WorkClients workCl: workClientsList) {
			workCl.closeAllClients();
		}
		unlockMe(this);
	}
}
