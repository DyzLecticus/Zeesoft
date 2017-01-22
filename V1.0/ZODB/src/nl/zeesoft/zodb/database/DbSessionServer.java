package nl.zeesoft.zodb.database;

import java.net.Socket;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.model.impl.DbSession;
import nl.zeesoft.zodb.protocol.PtcServer;

public final class DbSessionServer extends DbServerObject {
	private static DbSessionServer					server			= null;
	
	private SortedMap<Long,DbServerSessionWorker>	sessionWorkers	= new TreeMap<Long,DbServerSessionWorker>();

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException(); 
	}

	public static DbSessionServer getInstance() {
		if (server==null) {
			server = new DbSessionServer();
		}
		return server;
	}

	@Override
	protected boolean open() {
		boolean opened = super.open();
		return opened;
	}
	
	@Override
	protected void close() {
		stopSessions();
		super.close();
	}

	@Override
	protected void acceptAndProcessSessions() {
		if (sessionWorkers.size()<DbConfig.getInstance().getMaxSessions()) {
			super.acceptAndProcessSessions();
		} else {
			refuseSession();
			if (!isTimedOut()) {
				Messenger.getInstance().warn(this,"Refused session due to session limit: " + DbConfig.getInstance().getMaxSessions());
			}
		}
	}
	
	@Override
	protected DbServerSessionWorker getNewServerSessionWorker(Socket s,DbSession session,PtcServer protocol) {
		DbServerSessionWorker worker = super.getNewServerSessionWorker(s, session, protocol);
		if (worker.isWorking()) {
			sessionWorkers.put(session.getId().getValue(),worker);
		} else {
			Messenger.getInstance().error(this,"Server session worker not working");			
		}
		return worker;
	}
	
	@Override
	public boolean stopSession(long sessionId) {
		boolean stopped = false;
		DbServerSessionWorker s = sessionWorkers.remove(sessionId);
		if (s!=null) {
			s.stop();
			stopped = true;
		}
		return stopped;
	}

	protected void stopSessions() {
		SortedMap<Long,DbServerSessionWorker> sessWorkers = new TreeMap<Long,DbServerSessionWorker>(sessionWorkers);
		if (sessWorkers.size()>0) {
			Messenger.getInstance().debug(this,"Stopping sessions ...");			
			for (Entry<Long,DbServerSessionWorker> e: sessWorkers.entrySet()) {
				Messenger.getInstance().debug(this,"Stopping session: " + e.getKey());			
				stopSession((Long) e.getKey());
			}
		}
	}
}
