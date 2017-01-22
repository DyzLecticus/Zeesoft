package nl.zeesoft.zodb.database;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.model.impl.DbSession;
import nl.zeesoft.zodb.model.impl.DbWhiteListItem;
import nl.zeesoft.zodb.protocol.PtcServer;

public abstract class DbServerObject {
	private ServerSocket 			serverSocket 	= null;
	private	int						port			= 0;
	private List<DbWhiteListItem>	whiteList		= new ArrayList<DbWhiteListItem>();
	private List<DbWhiteListItem>	newWhiteList	= new ArrayList<DbWhiteListItem>();
	
	private boolean					timedOut		= false;

	protected int getPort() {
		return DbConfig.getInstance().getPort();
	}
	
	protected boolean open() {
		whiteList = new ArrayList<DbWhiteListItem>(DbWhiteList.getInstance().getWhiteList());
		for (DbWhiteListItem item: new ArrayList<DbWhiteListItem>(whiteList)) {
			if (!whiteListItemValidForServer(item)) {
				whiteList.remove(item);
			}
		}
		boolean opened = false;
		try {
			serverSocket = new ServerSocket(getPort());
			serverSocket.setSoTimeout(1000);
			opened = true;
		} catch (IOException e) {
			Messenger.getInstance().error(this,"Unable to listen on port: " + port + ", error: " + e);
		}
		return opened;
	}
	
	protected void close() {
		try {
			serverSocket.close();
		} catch(IOException e) {
			Messenger.getInstance().error(this,"Unable to stop listening on port: " + port + ", error: " + e);
		}
	}

	protected void acceptAndProcessSessions() {
		Socket s = acceptSession();
		if (s!=null) {
			String ipAndPort = s.getRemoteSocketAddress().toString();
			boolean ok = (getWhiteList().size() == 0);
			for (DbWhiteListItem item: whiteList) {
				if (ipAndPort.startsWith(item.getStartsWith().getValue())) {
					ok = true;
					break;
				}
			}
			if (ok) {
				DbSession session = new DbSession();
				session.getName().setValue(DbSession.OPEN);
				session.getIpAndPort().setValue(ipAndPort);
	
				QryAdd q = new QryAdd(session);
				QryTransaction t = new QryTransaction(DbConfig.getInstance().getModel().getAdminUser(this));
				t.addQuery(q);
				DbIndex.getInstance().executeTransaction(t, this);
				
				getNewServerSessionWorker(s,session,new PtcServer(session));
			} else {
				refuseSession();
				Messenger.getInstance().debug(this,"Refused session based on white list from: " + ipAndPort);
			}
		}
	}
		
	protected DbServerSessionWorker getNewServerSessionWorker(Socket s,DbSession session,PtcServer protocol) {
		DbServerSession serverSession = new DbServerSession(s,session,protocol);
		DbServerSessionWorker sessionWorker = new DbServerSessionWorker(serverSession);
		Messenger.getInstance().debug(this, "Start server session: " + session.getId());
		sessionWorker.start();
		if (serverSession.isOpened()) {
			while (!sessionWorker.isWorking()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return sessionWorker;
	}
			
	private Socket acceptSession() {
		timedOut = false;
		Socket r = null;
		try {
			r = serverSocket.accept();
		} catch (SocketTimeoutException se) {
			timedOut = true;
		} catch (IOException e) {
			Messenger.getInstance().error(this,"Accept failed on port: " + port + ", error: " + e);
		}
		return r;
	}
	
	protected void refuseSession() {
		Socket s = acceptSession();
		DbServerSessionClose close = new DbServerSessionClose(s);
		if (close.open()) {
			close.close();
		}
	}
	
	/**
	 * @return the whiteList
	 */
	private List<DbWhiteListItem> getWhiteList() {
		if (newWhiteList!=null) {
			whiteList = newWhiteList;
			newWhiteList = null;
		}
		return whiteList;
	}

	/**
	 * @param newWhiteList the newWhiteList to set
	 */
	protected void setNewWhiteList(List<DbWhiteListItem> newWhiteList) {
		this.newWhiteList = newWhiteList;
	}
	
	/**
	 * Override to implement
	 * 
	 * @param sessionId The ID of the session that is to be stopped
	 * @return a boolean indicating the session has stopped
	 */
	protected abstract boolean stopSession(long sessionId);
	
	protected boolean whiteListItemValidForServer(DbWhiteListItem item) {
		return true;
	}

	/**
	 * @return the timedOut
	 */
	public boolean isTimedOut() {
		return timedOut;
	}
}
