package nl.zeesoft.zdk.thread;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;

/**
 * Synchronization lock 
 */
public class Lock {
	private Object			lockedBy	= null;
	
	public synchronized void lock(Object source) {
		while (isLocked()) {
			int attempt = 0;
			try {
				wait();
			} catch (InterruptedException e) { 
				// Ignore
			}
			attempt ++;
			if (attempt>=1000) {
				Str error = new Str();
				error.sb().append("Lock failed after ");
				error.sb().append(attempt);
				error.sb().append(" attempts.");
				Logger.err(source, error, new Exception("Lock failed"));
				attempt = 0;
			}
		}
		lockedBy = source;
	}

	public synchronized void unlock(Object source) {
		if (lockedBy==source) {
			lockedBy=null;
			notifyAll();
		}
	}

	public synchronized boolean ifLock(Object source) {
		boolean locked = false;
		if (!isLocked()) {
			lock(source);
			locked = true;
		}
		return locked;
	}
	
	public synchronized boolean isLocked() {
		return (lockedBy!=null);
	}
}	
