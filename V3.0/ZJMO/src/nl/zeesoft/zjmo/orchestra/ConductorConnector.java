package nl.zeesoft.zjmo.orchestra;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class ConductorConnector extends Locker {
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
		unlockMe(this);
	}

	public void close() {
		lockMe(this);
		if (worker!=null) {
			worker.setStopping(true);
		}
		closeClients();
		if (worker!=null) {
			worker.stop();
			worker = null;
		}
		unlockMe(this);
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
		openClients();
		unlockMe(this);
	}
	
	protected void onOpenClient(MemberClient client) {
		// Override to implement
	}
	
	protected void openClients() {
		for (MemberClient client: clients) {
			if (!client.isOpen()) {
				boolean open = client.open();
				if (open) {
					onOpenClient(client);
				}
			}
		}
	}

	protected void closeClients() {
		for (MemberClient client: clients) {
			client.sendCloseSessionCommand();
			client.close();
		}
	}
}
