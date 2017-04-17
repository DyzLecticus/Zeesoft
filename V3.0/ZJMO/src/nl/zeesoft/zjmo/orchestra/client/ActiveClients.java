package nl.zeesoft.zjmo.orchestra.client;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;

public class ActiveClients extends Locker {
	private	WorkerUnion					union			= null;
	private boolean						control			= true;
	private List<ActiveClient>			clients			= new ArrayList<ActiveClient>();
	private int							timeout			= 0;

	public ActiveClients(Messenger msgr, WorkerUnion uni,boolean control) {
		super(msgr);
		this.union = uni;
		this.control = control;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void addClient(ActiveClient client) {
		lockMe(this);
		if (timeout>0) {
			client.setTimeout(timeout);
		}
		clients.add(client);
		unlockMe(this);
	}
	
	public void initialize(List<OrchestraMember> members) {
		lockMe(this);
		clients.clear();
		unlockMe(this);
		for (OrchestraMember member: members) {
			ActiveClient client = getNewActiveClient(union,member,control);
			addClient(client);
		}
	}
	
	public void open() {
		lockMe(this);
		for (ActiveClient client: clients) {
			client.open();
		}
		unlockMe(this);
	}

	public void connect() {
		lockMe(this);
		for (ActiveClient client: clients) {
			client.connectClient();
		}
		unlockMe(this);
	}

	public void close() {
		lockMe(this);
		for (ActiveClient client: clients) {
			client.close();
		}
		unlockMe(this);
	}

	public ActiveClient getClient(String memberId) {
		ActiveClient r = null;
		lockMe(this);
		for (ActiveClient client: clients) {
			if (client.getMember().getId().equals(memberId)) {
				r = client;
				break;
			}
		}
		unlockMe(this);
		return r;
	}

	public List<ActiveClient> getOpenClients() {
		return getOpenClients(null);
	}
	
	public List<ActiveClient> getOpenClients(String positionName) {
		List<ActiveClient> r = new ArrayList<ActiveClient>();
		lockMe(this);
		for (ActiveClient client: clients) {
			if ((positionName==null || positionName.length()==0 || client.getMember().getPosition().getName().equals(positionName)) &&
				client.isOpen()
				) {
				r.add(client);
			}
		}
		unlockMe(this);
		return r;
	}
	
	protected ActiveClient getNewActiveClient(WorkerUnion union,OrchestraMember member,boolean control) {
		return new ActiveClient(getMessenger(),union,member,control);
	}
}
