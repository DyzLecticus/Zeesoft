package nl.zeesoft.zdk;

public class Lock {
	private Object	owner	= null;
	private Boolean locked	= false;
	
	public Lock(Object owner) {
		this.owner = owner;
	}
	
	public synchronized void lock() {
		int attempts = 0;
		while (isLocked()) {
			try {
				wait();
			} catch (InterruptedException ex) { 
				Logger.error(owner, "Lock wait was interrupted", ex);
			}
			attempts++;
			if (attempts%getErrorAttempts()==0) {
				String error = "Warning: lock attempts: " + attempts;
				Logger.error(owner, error, new Exception("Lock failed"));
			}
		}
		locked = true;
	}

	public synchronized void unlock() {
		locked = false;
		notifyAll();
	}
	
	public synchronized boolean isLocked() {
		return locked;
	}
	
	protected int getErrorAttempts() {
		return 1000;
	}
}	
