package nl.zeesoft.zodb.database.server;

import java.net.Socket;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventPublishWorker;

/**
 * Debug, warning and error logging.
 * 
 * Publishes all messages.
 */
public final class SvrSocketLogger extends EvtEventPublishWorker  {
	private static SvrSocketLogger 		logger				= null;
	
	public	static final String			OPENED_SOCKET		= "OPENED_SOCKET";
	public	static final String			CLOSED_SOCKET		= "CLOSED_SOCKET";
	public	static final String			OPEN_EXCEPTION		= "OPEN_EXCEPTION";
	public	static final String			CLOSE_EXCEPTION		= "CLOSE_EXCEPTION";
	public	static final String			READ_EXCEPTION		= "READ_EXCEPTION";
	public	static final String			WRITE_EXCEPTION		= "WRITE_EXCEPTION";

	private SvrSocketLogger() {
		setSleep(100);
		setPublishOnStop(true);
	}

	public static SvrSocketLogger getInstance() {
		if (logger==null) {
			logger = new SvrSocketLogger();
		}
		return logger;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}
	
	public void openedSocket(SvrServer server, Socket s) {
		publishEvent(new EvtEvent(OPENED_SOCKET,server,s));
	}

	public void closedSocket(SvrServer server, Socket s) {
		publishEvent(new EvtEvent(CLOSED_SOCKET,server,s));
	}

	public void openException(Socket s, Exception e) {
		publishEvent(new EvtEvent(OPEN_EXCEPTION,s,e));
	}

	public void closeException(Socket s, Exception e) {
		publishEvent(new EvtEvent(CLOSE_EXCEPTION,s,e));
	}

	public void readException(Socket s, Exception e) {
		publishEvent(new EvtEvent(READ_EXCEPTION,s,e));
	}

	public void writeException(Socket s, Exception e) {
		publishEvent(new EvtEvent(WRITE_EXCEPTION,s,e));
	}

	@Override
	protected void publishingEvent(EvtEvent e) {
		if (SvrConfig.getInstance().isDebugSockets()) {
			String message = e.getType() + ": ";
			if (e.getValue() instanceof Socket) {
				message += ((Socket) e.getValue()).getRemoteSocketAddress();
			} else if (e.getSource() instanceof Socket) {
				message += ((Socket) e.getValue()).getRemoteSocketAddress();
				message += " " + e.getValue();
			}
			Messenger.getInstance().debug(e.getSource(), message);
		}
	}
}
