package nl.zeesoft.zjmo.orchestra;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.members.WorkClient;

public class ConductorConnector extends Locker {
	private boolean							closing		= false;
	private WorkerUnion						union		= null;
	private	ConductorConnectorWorker		worker		= null;
	private List<ConductorConnectorClient>	clients		= new ArrayList<ConductorConnectorClient>();
	
	public ConductorConnector(Messenger msgr, WorkerUnion uni) {
		super(msgr);
		this.union = uni;
	}
	
	public void initialize(List<OrchestraMember> conductors,boolean control) {
		lockMe(this);
		clients.clear();
		for (OrchestraMember conductor: conductors) {
			ConductorConnectorClient client = new ConductorConnectorClient(getMessenger());
			client.setConductor(conductor);
			if (control) {
				client.setClient(conductor.getNewControlClient(getMessenger(), union));
			} else {
				client.setClient(conductor.getNewWorkClient(getMessenger(), union));
			}
			clients.add(client);
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
		List<ConductorConnectorClient> cls = new ArrayList<ConductorConnectorClient>(clients);
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
	
	public WorkClient getWorkClient() {
		WorkClient r = null;
		MemberClient c = getClient();
		if (c!=null && c instanceof WorkClient) {
			r = (WorkClient) c;
		}
		return r;
	}
	
	public MemberClient getClient() {
		MemberClient r = null;
		lockMe(this);
		for (ConductorConnectorClient cl: clients) {
			if (cl.isOpen()) {
				r = cl.getClient();
				break;
			}
		}
		unlockMe(this);
		return r;
	}

	public OrchestraMember getConductorForClient(MemberClient client) {
		OrchestraMember r = null;
		lockMe(this);
		for (ConductorConnectorClient cl: clients) {
			if (cl.getClient()==client) {
				r = cl.getConductor();
			}
		}
		unlockMe(this);
		return r;
	}
	
	public void connect() {
		lockMe(this);
		List<ConductorConnectorClient> cls = new ArrayList<ConductorConnectorClient>(clients);
		unlockMe(this);
		openClients(cls);
	}
	
	protected void onOpenClient(ConductorConnectorClient client) {
		// Override to implement
	}
	
	protected void openClients(List<ConductorConnectorClient> clients) {
		for (ConductorConnectorClient client: clients) {
			if (!client.checkOpen() && !isClosing()) {
				client.open();
				if (client.isOpen()) {
					onOpenClient(client);
				}
			}
		}
	}

	protected void closeClients(List<ConductorConnectorClient> clients) {
		for (ConductorConnectorClient client: clients) {
			if (client.isOpen()) {
				client.close();
			}
		}
	}
}
