package nl.zeesoft.zodb.database;

import java.net.Socket;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.model.impl.DbSession;
import nl.zeesoft.zodb.model.impl.DbWhiteListItem;
import nl.zeesoft.zodb.protocol.PtcServer;
import nl.zeesoft.zodb.protocol.PtcServerControl;

public final class DbControlServer extends DbServerObject {
	private static DbControlServer			server			= null;
	private DbServerSessionWorker			sessionWorker	= null;
	private DbSession						session			= null;
		
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException(); 
	}

	public static DbControlServer getInstance() {
		if (server==null) {
			server = new DbControlServer();
		}
		return server;
	}
	
	@Override
	protected int getPort() {
		return DbConfig.getInstance().getPort() + 1;
	}
	
	@Override
	protected boolean open() {
		boolean opened = super.open();
		return opened;
	}
	
	@Override
	protected void close() {
		if (session!=null) {
			stopSession(session.getId().getValue());
		}
		super.close();
	}
	
	@Override
	protected void acceptAndProcessSessions() {
		if (sessionWorker==null) {
			super.acceptAndProcessSessions();
		} else {
			refuseSession();
			if (!isTimedOut()) {
				Messenger.getInstance().warn(this,"Refused control session because control server port is already in use");
			}
		}
	}

	@Override
	protected DbServerSessionWorker getNewServerSessionWorker(Socket s,DbSession session, PtcServer protocol) {
		protocol = new PtcServerControl(session);
		this.session = session;
		sessionWorker = super.getNewServerSessionWorker(s,session,protocol);
		if (!sessionWorker.isWorking()) {
			Messenger.getInstance().error(this,"Control server session worker not working");
			this.session = null;
			sessionWorker = null;
		}
		return sessionWorker;
	}

	@Override
	public boolean stopSession(long id) {
		boolean stopped = false;
		if ((sessionWorker!=null) && (session!=null) && (id==session.getId().getValue())) {
			DbServerSessionWorker sw = sessionWorker;
			sessionWorker = null;
			session = null;
			if (sw.isWorking()) {
				sw.stop();
			}
			stopped = true;
		}
		return stopped;
	}
	
	@Override
	protected boolean whiteListItemValidForServer(DbWhiteListItem item) {
		return item.getControl().getValue();
	}
}
