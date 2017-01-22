package nl.zeesoft.zodb.database;

import java.net.Socket;
import java.util.Date;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Session;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.model.impl.DbSession;
import nl.zeesoft.zodb.protocol.PtcObject;
import nl.zeesoft.zodb.protocol.PtcServer;

public final class DbServerSession extends Session {
	private PtcServer			protocol	= null;
	private DbSession			session		= null;
	private boolean				opened		= false;
	
	public DbServerSession(Socket sock, DbSession sess, PtcServer prot) {
		super(sock, sess.getId().getValue());
		protocol = prot;
		session = sess;
	}
	
	@Override
	protected boolean open() {
		opened = super.open();
		if (opened) {
			session.getStarted().setValue(new Date());
    		QryUpdate q = new QryUpdate(session);
			QryTransaction t = new QryTransaction(DbConfig.getInstance().getModel().getAdminUser(this));
			t.addQuery(q);
			DbIndex.getInstance().executeTransaction(t, this);

			StringBuffer output = new StringBuffer();
			output.append(PtcObject.START_SESSION);
			output.append(Generic.SEP_STR);
			output.append(getSessionId());
			output.append(Generic.SEP_STR);
			output.append(DbConfig.getInstance().getModel().getCrc());
            writeOutput(output,0);
		}
		return opened;
    }
    
	@Override
	protected void close() {
		writeOutput(new StringBuffer(PtcObject.STOP_SESSION),getSessionId());
		super.close();
		if ((getSocket()==null) && (session.getEnded().getValue()==null)) {
    		session.getName().setValue(DbSession.CLOSED);
    		session.getEnded().setValue(new Date());
    		QryUpdate q = new QryUpdate(session);
    		QryTransaction t = new QryTransaction(DbConfig.getInstance().getModel().getAdminUser(this));
    		t.addQuery(q);
    		DbIndex.getInstance().executeTransaction(t, this);
		}
    }

	@Override
    protected void readAndProcessInput() {
		StringBuffer input = readInput(getSessionId());
    	if (
    		(getSocket()!=null) && (getSocket().isConnected()) && 
    		(input!=null) && (input.length()>0)
    		) {
    		StringBuffer output = protocol.processInputAndReturnOutput(input);
			writeOutput(output,getSessionId());
    	} else if ((getSocket()!=null) && (!getSocket().isConnected())) {
    		DbSessionServer.getInstance().stopSession(session.getId().getValue());
    	}
    }

	/**
	 * @return the opened
	 */
	public boolean isOpened() {
		return opened;
	}

	/**
	 * @return the session
	 */
	public DbSession getSession() {
		return session;
	}
}
