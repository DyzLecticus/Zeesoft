package nl.zeesoft.zodb.database;

import java.net.Socket;

import nl.zeesoft.zodb.Session;
import nl.zeesoft.zodb.protocol.PtcObject;

/**
 * Used to refuse sessions without adding objects to the database session collection
 */
public final class DbServerSessionClose extends Session {
	public DbServerSessionClose(Socket sock) {
		super(sock, 0);
	}
    
	@Override
	protected boolean open() {
		return super.open();
	}
	    
	@Override
	protected void close() {
		writeOutput(new StringBuffer(PtcObject.STOP_SESSION),getSessionId());
		super.close();
    }
}
