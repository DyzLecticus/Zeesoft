package nl.zeesoft.zdk.thread;

import nl.zeesoft.zdk.messenger.Messenger;

/**
 * Synchronization locker 
 */
public class Locker {
	private Messenger	msgr		= null;
	private Object		lockedBy	= null;
	
	public Locker(Messenger msgr) {
		this.msgr = msgr;
	}
	
	public static String getLockFailedMessage(int attempts, Object source) {
		return "Lock failed after " + attempts + " attempts. Source:" + source;
	}
	
	protected synchronized void lockMe(Object source) {
		while (isLocked()) {
			int attempt = 0;
			try {
				wait();
			} catch (InterruptedException e) { 
				// Ignore
			}
			attempt ++;
			if (attempt>=1000) {
				if (msgr!=null) {
					msgr.warn(this,getLockFailedMessage(attempt,source));
				}
				attempt = 0;
			}
		}
		lockedBy = source;
	}

	protected synchronized void unlockMe(Object source) {
		if (lockedBy==source) {
			lockedBy=null;
			notifyAll();
		}
	}

	protected synchronized boolean ifLockMe(Object source) {
		boolean locked = false;
		if (!isLocked()) {
			lockMe(source);
			locked = true;
		}
		return locked;
	}

	protected Messenger getMsgr() {
		return msgr;
	}

	private synchronized boolean isLocked() {
		return (lockedBy!=null);
	}
}	
