package nl.zeesoft.zodb;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventPublisher;

/**
 * Debug, warning and error logging.
 * 
 * Publishes all messages.
 */
public final class Messenger extends EvtEventPublisher  {
	private static Messenger 			messenger		= null;
	
	public	static final String			MSG_ERROR		= "ERR";
	public	static final String			MSG_WARNING		= "WRN";
	public	static final String			MSG_DEBUG		= "DBG";

	private Messenger() {
		// Singleton
	}

	public static Messenger getInstance() {
		if (messenger==null) {
			messenger = new Messenger();
		}
		return messenger;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}
	
	public void error(Object source, String message) {
		addMessage(Messenger.MSG_ERROR,source,message);
	}
	
	public void warn(Object source, String message) {
		addMessage(Messenger.MSG_WARNING,source,message);
	}
	
	public void debug(Object source, String message) {
		if (
			(DbConfig.getInstance().isDebug()) &&
			(
				(DbConfig.getInstance().getDebugClassPrefix().length()==0) || 
				(source.getClass().getName().startsWith(DbConfig.getInstance().getDebugClassPrefix()))
			)
			) {
			addMessage(Messenger.MSG_DEBUG,source,message);
		}
	}
	
	private void addMessage(String type, Object source, String message) {
		EvtEvent e = new EvtEvent(type, source, message);
		publishEvent(e);
		if ( 
			((DbConfig.getInstance().isDebug()) && (type.equals(Messenger.MSG_DEBUG))) ||
			(type.equals(Messenger.MSG_WARNING)) ||
			(type.equals(Messenger.MSG_ERROR))
			) {
			messageToSystemOut(e);
			if (type.equals(MSG_ERROR)) {
				messageToSystemErr(e);				
			}
		}
	}

	private void messageToSystemOut(EvtEvent e) {
		System.out.println(Generic.getDateTimeString(e.getDatetime()) + " " + e.getType() + " " + e.getSource().getClass().getName() + ": " + e.getValue());
	}
	
	private void messageToSystemErr(EvtEvent e) {
		System.err.println(Generic.getDateTimeString(e.getDatetime()) + " " + e.getType() + " " + e.getSource().getClass().getName() + ": " + e.getValue());
	}
}
