package nl.zeesoft.zodb.database.model.helpers;

import java.util.Date;

import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventExceptionSubscriber;

public abstract class HlpControllerObject extends Locker implements EvtEventExceptionSubscriber {	
	private boolean		done 			= false;
	private int 		timeOutSeconds 	= 10;
	private long 		start 			= 0;

	@Override
	public void handleEventException(EvtEvent evt, Exception ex) {
		handleException(ex);
	}
	
	protected abstract void initialize();
	
	protected void setDone(boolean d) {
		lockMe(this);
		done = d;
		unlockMe(this);
	}

	protected boolean isDone() {
		boolean r = false;
		lockMe(this);
		r = done;
		unlockMe(this);
		return r;
	}
	
	protected void resetTimeOut() {
		lockMe(this);
		start = (new Date()).getTime();
		unlockMe(this);
	}
	
	protected void waitTillDone() {
		resetTimeOut();
		while (!isDone()) {
			try {
				whileWaiting();
				Thread.sleep(10);
				lockMe(this);
				long waitStart = start;
				unlockMe(this);
				if (((new Date()).getTime() - waitStart) > (timeOutSeconds * 1000)) {
					handleTimeOut();
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				handleException(e);
			}
		}
	}
	
	protected void whileWaiting() {
		
	}
	
	protected void handleException(Exception ex) {
		setDone(true);
	}

	protected void handleTimeOut() {
		Messenger.getInstance().error(this,"Timed out after " + timeOutSeconds + " seconds of inactivity.");
		setDone(true);
	}
	
	protected void setTimeOutSeconds(int timeOutSeconds) {
		this.timeOutSeconds = timeOutSeconds;
	}
}
