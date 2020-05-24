package nl.zeesoft.zdk.thread;

public abstract class RunnerObject {
	private Lock	lock	= new Lock();
	private boolean running = false;
	
	public boolean isRunning() {
		lock.lock(this);
		boolean r = isRunningNoLock();
		lock.unlock(this);
		return r;
	}
	
	public abstract void start();
	
	public abstract void stop();
	
	public void stopWait() {
		stop();
		wait(10,100);
	}

	public void wait(int sleepMs, int max) {
		int counter = 0;
		while(isRunning()) {
			try {
				Thread.sleep(sleepMs);
				counter++;
				if (counter>=max) {
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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
	
	protected boolean isRunningNoLock() {
		return running;
	}
	
	protected void setRunningNoLock(boolean running) {
		this.running = running;
	}
}
