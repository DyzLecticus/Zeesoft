package nl.zeesoft.zodb;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventPublishWorker;

/**
 * Debug, warning and error logging.
 * 
 * Publishes all messages.
 */
public final class Messenger extends EvtEventPublishWorker  {
	private static Messenger 			messenger		= null;
	
	public	static final String			MSG_ERROR		= "ERR";
	public	static final String			MSG_WARNING		= "WRN";
	public	static final String			MSG_DEBUG		= "DBG";

	private boolean						started			= false;
	private boolean						stopped			= false;
	
	private boolean						warning			= false;
	private boolean						error			= false;
	
	private Messenger() {
		setSleep(100);
		setPublishOnStop(true);
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
		addMessage(MSG_ERROR,source,message);
	}
	
	public void warn(Object source, String message) {
		addMessage(MSG_WARNING,source,message);
	}
	
	public void debug(Object source, String message) {
		if (
			DbConfig.getInstance().getDebugClassPrefix().length()==0 || 
			source.getClass().getName().startsWith(DbConfig.getInstance().getDebugClassPrefix())
			) {
			addMessage(MSG_DEBUG,source,message);
		}
	}
	
	public static String getMessageEventString(EvtEvent e) {
		return Generic.getDateTimeString(e.getDatetime()) + " " + e.getType() + " " + e.getSource().getClass().getName() + ": " + e.getValue();
	}
	
	public boolean isWarning() {
		boolean r = false;
		lockMe(this);
		r = warning;
		unlockMe(this);
		return r;
	}
	
	public boolean isError() {
		boolean r = false;
		lockMe(this);
		r = error;
		unlockMe(this);
		return r;
	}

	@Override
	public void start() {
		super.start();
		setStarted(true);
	}

	@Override
	public void stop() {
		boolean wasWorking = isWorking();
		super.stop();
		if (wasWorking) {
			setStopped(true);
		}
	}

	@Override
	protected void publishEvent(EvtEvent e) {
		if (!isStartedAndStopped()) {
			super.publishEvent(e);
		} else {
			publishingEvent(e);
		}
	}

	@Override
	protected void publishingEvent(EvtEvent e) {
		if ( 
			(e.getType().equals(MSG_DEBUG) && DbConfig.getInstance().isDebug()) ||
			e.getType().equals(MSG_WARNING) ||
			e.getType().equals(MSG_ERROR)
			) {
			
			messageToSystemOut(e);
			if (e.getType().equals(MSG_WARNING)) {
			}
			if (e.getType().equals(MSG_ERROR)) {
				messageToSystemErr(e);				
			}
			
			if (e.getType().equals(MSG_WARNING) || e.getType().equals(MSG_ERROR)) {
				lockMe(this);
				if (e.getType().equals(MSG_WARNING)) {
					warning = true;
				} else if (e.getType().equals(MSG_ERROR)) {
					error = true;
				}
				unlockMe(this);
			}
		}
	}
	
	private void setStarted(boolean started) {
		lockMe(this);
		this.started = started;
		unlockMe(this);
	}

	private void setStopped(boolean stopped) {
		lockMe(this);
		this.stopped = stopped;
		unlockMe(this);
	}
	
	private boolean isStartedAndStopped() {
		boolean r;
		lockMe(this);
		r = started && stopped;
		unlockMe(this);
		return r;
	}
	
	private void addMessage(String type, Object source, String message) {
		publishEvent(new EvtEvent(type, source, message));
	}

	private void messageToSystemOut(EvtEvent e) {
		System.out.println(getMessageEventString(e));
	}
	
	private void messageToSystemErr(EvtEvent e) {
		System.err.println(getMessageEventString(e));
	}
}
