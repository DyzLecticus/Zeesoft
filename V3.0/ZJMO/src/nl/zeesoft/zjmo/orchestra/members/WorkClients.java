package nl.zeesoft.zjmo.orchestra.members;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;

public class WorkClients extends Locker {
	private OrchestraMember		member		= null;
	private List<WorkClient> 	clients 	= new ArrayList<WorkClient>();
	
	protected WorkClients(Messenger msgr,OrchestraMember member) {
		super(msgr);
		this.member = member;
	}
	
	protected OrchestraMember getMember() {
		return member;
	}
	
	protected WorkClient getClient(Object source) {
		WorkClient r = null;
		lockMe(source);
		List<WorkClient> testClients = new ArrayList<WorkClient>(clients);
		for (WorkClient client: testClients) {
			if (!client.isOpen()) {
				clients.remove(client);	
			} else if (client.getSetInUseBy(source)) {
				r = client;
				break;
			}
		}
		if (r==null) {
			WorkClient client = member.getNewWorkClient(getMessenger());
			if (client.open()) {
				client.getSetInUseBy(source);
				clients.add(client);
				r = client;
			} else {
				getMessenger().debug(this,"Failed to connect to: " + member.getId());				
			}
		}
		unlockMe(source);
		return r;
	}

	protected void returnClient(WorkClient client) {
		client.unSetInUseBy();
		if (!client.isOpen()) {
			clients.remove(client);
		}
	}
	
	protected void closeUnusedClients(long unusedMs) {
		lockMe(this);
		long cutOff = (new Date().getTime() - unusedMs);
		List<WorkClient> testClients = new ArrayList<WorkClient>(clients);
		for (WorkClient client: testClients) {
			if (!client.isInUse() && (cutOff > client.getLastUsed().getTime())) {
				client.sendCloseSessionCommand();
				client.close();
				clients.remove(client);
			}
		}
		unlockMe(this);
	}

	protected void closeAllClients() {
		lockMe(this);
		List<WorkClient> testClients = new ArrayList<WorkClient>(clients);
		for (WorkClient client: testClients) {
			client.sendCloseSessionCommand();
			client.close();
			clients.remove(client);
		}
		unlockMe(this);
	}
}
