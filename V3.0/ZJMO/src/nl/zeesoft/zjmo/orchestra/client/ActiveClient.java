package nl.zeesoft.zjmo.orchestra.client;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.MemberClient;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
import nl.zeesoft.zjmo.orchestra.members.WorkClient;

public class ActiveClient extends Locker {
	private	WorkerUnion					union			= null;
	private OrchestraMember				member			= null;
	private boolean						control			= true;

	private boolean						open			= false;
	private boolean						closing			= false;

	private	MemberClient				client			= null;
	private MemberClient				stateClient		= null;
	
	private	ActiveClientConnectWorker	connectWorker	= null;
	private	ActiveClientStateWorker		stateWorker		= null;
	
	public ActiveClient(Messenger msgr, WorkerUnion uni,OrchestraMember member,boolean control) {
		super(msgr);
		this.union = uni;
		this.member = member;
		this.control = control;
		initializeClients();
	}
	
	public void open() {
		lockMe(this);
		open = false;
		closing = false;
		stateWorker = new ActiveClientStateWorker(getMessenger(),union,this,stateClient);
		connectWorker = new ActiveClientConnectWorker(getMessenger(),union,this);
		connectWorker.start();
		unlockMe(this);
	}
	
	public void close() {
		lockMe(this);
		open = false;
		closing = true;
		unlockMe(this);

		lockMe(this);
		if (connectWorker!=null && connectWorker.isWorking()) {
			connectWorker.stop();
			connectWorker = null;
		}
		if (stateWorker!=null && stateWorker.isWorking()) {
			stateWorker.stop();
			stateClient.sendCloseSessionCommand();
			stateWorker = null;
		}
		unlockMe(this);
		
		if (client.isOpen()) {
			client.sendCloseSessionCommand();
			client.close();
		}
	}

	public OrchestraMember getMember() {
		return member;
	}
	
	public WorkClient getWorkClient() {
		WorkClient r = null;
		if (client instanceof WorkClient) {
			r = (WorkClient) client;
		}
		return r;
	}

	public MemberClient getClient() {
		return client;
	}
	
	public boolean isOpen() {
		boolean r = false;
		lockMe(this);
		r = open;
		unlockMe(this);
		return r;
	}

	protected void connect() {
		boolean connect = false;
		lockMe(this);
		if (!closing && stateWorker!=null && !stateWorker.isWorking()) {
			stateWorker.start();
			connect = true;
		}
		unlockMe(this);
		if (connect) {
			client.open();
			if (client.isOpen()) {
				lockMe(this);
				open = true;
				unlockMe(this);
				connected();
			}
		}
	}

	protected void disconnect() {
		if (client.isOpen()) {
			client.sendCloseSessionCommand();
			client.close();
			lockMe(this);
			open = false;
			unlockMe(this);
		}
		disconnected();
	}

	protected void connected() {
		// Override to implement
	}

	protected void disconnected() {
		// Override to implement
	}
	
	private void initializeClients() {
		if (control) {
			client = member.getNewControlClient(getMessenger(),union);
			stateClient = member.getNewControlClient(getMessenger(),union);
		} else {
			client = member.getNewWorkClient(getMessenger(),union);
			stateClient = member.getNewWorkClient(getMessenger(),union);
		}
	}
}
