package nl.zeesoft.zdk.thread;

public class CodeRunner implements Runnable {
	private Lock		lock		= new Lock();

	private RunCode		code		= null;
	private int			sleepMs		= 100;
	private int			sleepNs		= 0;

	private Thread		runner 		= null;
	private boolean		running		= false;
	
	public CodeRunner(RunCode code) {
		this.code = code;
	}
	
	public RunCode getCode() {
		return code;
	}
	
	public void setListener(LockListener listener) {
		lock.setListener(this, listener);
	}

	public void setSleepMs(int sleepMs) {
		setSleep(sleepMs,true);
	}

	public void setSleepNs(int sleepNs) {
		setSleep(sleepNs,false);
	}
	
	public boolean isRunning() {
		lock.lock(this);
		boolean r = running;
		lock.unlock(this);
		return r;
	}
	
	public void start() {
		lock.lock(this);
		if (!running && runner == null) {
			runner = new Thread(this);
			runner.start();
			running = true;
		}
		lock.unlock(this);
	}

	public void stop() {
		lock.lock(this);
		if (runner != null) {
			runner = null;
		}
		lock.unlock(this);
	}

	@Override
	public final void run() {
		started();
		
		lock.lock(this);
		Thread rnnr = runner;
		int slpMs = sleepMs;
		int slpNs = sleepNs;
		lock.unlock(this);
		
		while (rnnr!=null) {
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
			
			lock.lock(this);
			rnnr = runner;
			slpMs = sleepMs;
			slpNs = sleepNs;
			lock.unlock(this);
		}
		
		lock.lock(this);
		running = false;
		lock.unlock(this);
		
		stopped();
		
		if (code.getException()!=null) {
			caughtException(code.getException());
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
	
	/**
	 * Called when an exception has been caught.
	 */
	protected void caughtException(Exception exception) {
		// Override to implement
	}
	
	private void setSleep(int sleep, boolean ms) {
		lock.lock(this);
		if (ms) {
			this.sleepMs = sleep;
			this.sleepNs = 0;
		} else {
			this.sleepMs = 0;
			this.sleepNs = sleep;
		}
		lock.unlock(this);
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
