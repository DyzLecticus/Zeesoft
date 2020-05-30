package nl.zeesoft.zdk.thread;

public abstract class RunnerObject implements Waitable {
	private Lock	lock	= new Lock();
	private boolean	busy	= false;
	
	@Override
	public boolean isBusy() {
		lock.lock(this);
		boolean r = isBusyNoLock();
		lock.unlock(this);
		return r;
	}
	
	public abstract void start();
	
	public abstract void stop();
	
	public void stopWait() {
		stopWait(100);
	}
	
	public void stopWait(int waitMs) {
		stop();
		Waiter.wait(this,waitMs);
	}
	
	/**
	 * Called when the runner has started.
	 */
	protected void started() {
		// Override to implement
	}

	/**
	 * Called when the runner has stopped.
	 */
	protected void stopped() {
		// Override to implement
	}
	
	protected Lock getLock() {
		return lock;
	}
	
	protected boolean isBusyNoLock() {
		return busy;
	}
	
	protected void setBusyNoLock(boolean busy) {
		this.busy = busy;
	}
}
