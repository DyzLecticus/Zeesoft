package nl.zeesoft.zdk.thread;

public class CodeRunner extends RunnerObject implements Runnable {
	private RunCode		code		= null;
	private int			sleepMs		= 100;
	private int			sleepNs		= 0;
	private int			priority	= Thread.NORM_PRIORITY;

	private Thread		runner 		= null;
	
	public CodeRunner(RunCode code) {
		this.code = code;
	}
	
	public RunCode getCode() {
		return code;
	}

	public void setSleepMs(int sleepMs) {
		setSleep(sleepMs,true);
	}

	public void setSleepNs(int sleepNs) {
		setSleep(sleepNs,false);
	}
	
	public void setPriority(int priority) {
		if (priority>=Thread.MIN_PRIORITY && priority<=Thread.MAX_PRIORITY) {
			lock.lock(this);
			this.priority = priority;
			lock.unlock(this);
		}
	}
	
	@Override
	public void start() {
		lock.lock(this);
		if (!busy.isBusy() && runner == null) {
			runner = new Thread(this);
			runner.setPriority(priority);
			runner.start();
			busy.setBusy(true);
			CodeRunnerManager.startedRunner(this);
		}
		lock.unlock(this);
	}

	@Override
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
		
		whileRunning();
				
		if (code.getException()!=null) {
			caughtException(code.getException());
		}
		
		stopped();
		
		CodeRunnerManager.stoppedRunner(this);
		
		busy.setBusy(false);
		
		doneCallback();
	}

	protected void whileRunning() {
		lock.lock(this);
		Thread rnnr = runner;
		int slpMs = sleepMs;
		int slpNs = sleepNs;
		lock.unlock(this);
		
		while (rnnr!=null) {
			tryRunCatchSleep(slpMs,slpNs);
			
			lock.lock(this);
			rnnr = runner;
			slpMs = sleepMs;
			slpNs = sleepNs;
			lock.unlock(this);
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
		int sleepMs = 0;
		if (sleepNs > 999999 ) {
			sleepMs = sleepNs / 1000000;
			sleepNs = sleepNs % 1000000;
		}
		try {
			Thread.sleep(sleepMs,sleepNs);
		} catch (InterruptedException e) {
			// Ignore
		}
	}
}
