package nl.zeesoft.zjmo.orchestra;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class ConductorConnector extends Locker {
	private boolean						closing		= false;
	private WorkerUnion					union		= null;
	private	ConductorConnectorWorker	worker		= null;
	private List<OrchestraMember>		conductors	= new ArrayList<OrchestraMember>();
	private List<MemberClient>			clients		= new ArrayList<MemberClient>();
	
	public ConductorConnector(Messenger msgr, WorkerUnion uni) {
		super(msgr);
		this.union = uni;
	}
	
	public void initialize(List<OrchestraMember> conductors,boolean control) {
		lockMe(this);
		this.conductors = conductors;
		clients.clear();
		for (OrchestraMember conductor: conductors) {
			if (control) {
				clients.add(conductor.getNewControlClient(getMessenger(), union));
			}
		}
		unlockMe(this);
	}
	
	public void open() {
		lockMe(this);
		if (clients.size()>0) {
			worker = new ConductorConnectorWorker(getMessenger(),union,this);
			worker.start();
		}
		closing = false;
		unlockMe(this);
	}
	
	public void close() {
		lockMe(this);
		closing = true;
		if (worker!=null) {
			worker.setStopping(true);
		}
		List<MemberClient> cls = new ArrayList<MemberClient>(clients);
		unlockMe(this);
		closeClients(cls);
		lockMe(this);
		if (worker!=null) {
			worker.stop();
			worker = null;
		}
		unlockMe(this);
	}

	public boolean isClosing() {
		boolean r = false;
		lockMe(this);
		r = closing;
		unlockMe(this);
		return r;
	}
	
	
	public MemberClient getClient() {
		MemberClient r = null;
		lockMe(this);
		for (MemberClient client: clients) {
			if (client.isOpen()) {
				r = client;
				break;
			}
		}
		unlockMe(this);
		return r;
	}

	public OrchestraMember getConductorForClient(MemberClient client) {
		OrchestraMember r = null;
		lockMe(this);
		if (client!=null) {
			r = conductors.get(clients.indexOf(client));
		}
		unlockMe(this);
		return r;
	}
	
	protected void connect() {
		lockMe(this);
		List<MemberClient> cls = new ArrayList<MemberClient>(clients);
		unlockMe(this);
		openClients(cls);
	}
	
	protected void onOpenClient(MemberClient client) {
		// Override to implement
	}
	
	protected void openClients(List<MemberClient> clients) {
		for (MemberClient client: clients) {
			if (!client.isOpen() && !isClosing()) {
				boolean open = client.open();
				if (open) {
					onOpenClient(client);
				}
			}
		}
	}

	protected void closeClients(List<MemberClient> clients) {
		for (MemberClient client: clients) {
			if (client.isOpen()) {
				client.sendCloseSessionCommand();
				client.close();
			}
		}
	}

}
