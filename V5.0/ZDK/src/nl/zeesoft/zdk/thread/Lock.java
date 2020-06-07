package nl.zeesoft.zdk.thread;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;

/**
 * Synchronization lock 
 */
public class Lock {
	private Logger			logger		= new Logger();
	
	private Object			lockedBy	= null;
	
	public void setLogger(Object source, Logger logger) {
		lock(source);
		this.logger = logger;
		unlock(source);
	}
	
	public Logger getLogger(Object source) {
		lock(source);
		Logger r = logger;
		unlock(source);
		return r;
	}
	
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
				if (logger!=null) {
					Str error = new Str();
					error.sb().append("Lock failed after ");
					error.sb().append(attempt);
					error.sb().append(" attempts.");
					logger.error(source, error);
				}
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
