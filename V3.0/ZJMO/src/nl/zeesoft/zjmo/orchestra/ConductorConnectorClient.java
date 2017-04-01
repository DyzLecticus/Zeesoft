package nl.zeesoft.zjmo.orchestra;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

public class ConductorConnectorClient extends Locker {
	private OrchestraMember		conductor	= null;
	private MemberClient		client		= null;
	private boolean				open		= false;
	
	public ConductorConnectorClient(Messenger msgr) {
		super(msgr);
	}
	
	public OrchestraMember getConductor() {
		return conductor;
	}
	
	public void setConductor(OrchestraMember conductor) {
		this.conductor = conductor;
	}
	
	public MemberClient getClient() {
		return client;
	}
	
	public void setClient(MemberClient client) {
		this.client = client;
	}

	public boolean checkOpen() {
		boolean r = client.isOpen();
		lockMe(this);
		open = r;
		unlockMe(this);
		return r;
	}
	
	public boolean isOpen() {
		boolean r = false;
		lockMe(this);
		r = open;
		unlockMe(this);
		return r;
	}
	
	public void open() {
		client.open();
		lockMe(this);
		open = client.isOpen();
		unlockMe(this);
	}
	
	public void close() {
		client.sendCloseSessionCommand();
		client.close();
		lockMe(this);
		open = client.isOpen();
		unlockMe(this);
	}
}
