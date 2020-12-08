package nl.zeesoft.zdk.thread;

public abstract class RunnerObject implements Waitable {
	protected Lock	lock	= new Lock();
	protected Busy	busy	= new Busy(this);
	
	@Override
	public boolean isBusy() {
		return busy.isBusy();
	}
	
	public abstract void start();
	
	public abstract void stop();
	
	public void stopWait() {
		stopWait(100);
	}
	
	public void stopWait(int waitMs) {
		stop();
		Waiter.waitFor(this,waitMs);
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

	/**
	 * Called when the runner caught an exception.
	 * 
	 * @param exception The exception that was caught
	 */
	protected void caughtException(Exception exception) {
		// Override to implement
	}

	/**
	 * Called when the runner is done.
	 */
	protected void doneCallback() {
		// Override to implement
	}
}
