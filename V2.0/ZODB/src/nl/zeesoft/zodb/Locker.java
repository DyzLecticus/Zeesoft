package nl.zeesoft.zodb;

/**
 * Synchronization locker 
 */
public abstract class Locker {
	private Object lockedBy = null;
	
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
				Messenger.getInstance().warn(this,getLockFailedMessage(attempt,source));
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

	private synchronized boolean isLocked() {
		return (lockedBy!=null);
	}
}	
