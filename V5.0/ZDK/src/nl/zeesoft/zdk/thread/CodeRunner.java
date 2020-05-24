package nl.zeesoft.zdk.thread;

public class CodeRunner extends RunnerObject implements Runnable {
	private RunCode		code		= null;
	private int			sleepMs		= 100;
	private int			sleepNs		= 0;

	private Thread		runner 		= null;
	
	public CodeRunner(RunCode code) {
		this.code = code;
	}
	
	public RunCode getCode() {
		return code;
	}
	
	public void setListener(LockListener listener) {
		getLock().setListener(this, listener);
	}

	public void setSleepMs(int sleepMs) {
		setSleep(sleepMs,true);
	}

	public void setSleepNs(int sleepNs) {
		setSleep(sleepNs,false);
	}
	
	@Override
	public void start() {
		getLock().lock(this);
		if (!isRunningNoLock() && runner == null) {
			runner = new Thread(this);
			runner.start();
			setRunningNoLock(true);
		}
		getLock().unlock(this);
	}

	@Override
	public void stop() {
		getLock().lock(this);
		if (runner != null) {
			runner = null;
		}
		getLock().unlock(this);
	}

	@Override
	public final void run() {
		started();
		
		whileRunning();
		
		getLock().lock(this);
		setRunningNoLock(false);
		getLock().unlock(this);
		
		stopped();
		
		if (code.getException()!=null) {
			caughtException(code.getException());
		}
	}

	protected void whileRunning() {
		getLock().lock(this);
		Thread rnnr = runner;
		int slpMs = sleepMs;
		int slpNs = sleepNs;
		getLock().unlock(this);
		
		while (rnnr!=null) {
			tryRunCatchSleep(slpMs,slpNs);
			
			getLock().lock(this);
			rnnr = runner;
			slpMs = sleepMs;
			slpNs = sleepNs;
			getLock().unlock(this);
		}
	}

	protected void tryRunCatchSleep(int slpMs, int slpNs) {
		boolean stop = code.tryRunCatch();
		if (code.getException()!=null || stop) {
			stop();
		} else {
			if (slpMs>0) {
				sleepMs(slpMs);
			} else {
				sleepNs(slpNs);
			}
		}
	}
	
	
	
	/**
	 * Called when an exception has been caught.
	 */
	protected void caughtException(Exception exception) {
		// Override to implement
	}
	
	private void setSleep(int sleep, boolean ms) {
		getLock().lock(this);
		if (ms) {
			this.sleepMs = sleep;
			this.sleepNs = 0;
		} else {
			this.sleepMs = 0;
			this.sleepNs = sleep;
		}
		getLock().unlock(this);
	}

	private final void sleepMs(long sleepMs) {
		try {
			Thread.sleep(sleepMs);
		} catch (InterruptedException e) {
			// Ignore
		}
	}
	
	private final void sleepNs(int sleepNs) {
		try {
			Thread.sleep(0,sleepNs);
		} catch (InterruptedException e) {
			// Ignore
		}
	}
}
