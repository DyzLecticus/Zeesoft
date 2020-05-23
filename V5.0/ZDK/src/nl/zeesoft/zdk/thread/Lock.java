package nl.zeesoft.zdk.thread;

/**
 * Synchronization lock 
 */
public class Lock {
	private LockListener	listener	= new LockListener();
	
	private Object			lockedBy	= null;
	
	public void setListener(Object source, LockListener listener) {
		lock(source);
		this.listener = listener;
		unlock(source);
	}
	
	public LockListener getListener(Object source) {
		lock(source);
		LockListener r = listener;
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
				if (listener!=null) {
					listener.lockFailed(source, attempt);
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
	
	private synchronized boolean isLocked() {
		return (lockedBy!=null);
	}
}	
