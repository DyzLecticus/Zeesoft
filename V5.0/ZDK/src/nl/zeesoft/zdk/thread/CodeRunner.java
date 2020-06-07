package nl.zeesoft.zdk.thread;

import nl.zeesoft.zdk.Logger;

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
	
	public void setLogger(Logger logger) {
		getLock().setLogger(this, logger);
		code.setLogger(logger);
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
		if (!isBusyNoLock() && runner == null) {
			runner = new Thread(this);
			runner.start();
			setBusyNoLock(true);
			CodeRunnerManager.startedRunner(this);
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
				
		if (code.getException()!=null) {
			caughtException(code.getException());
		}
		
		stopped();
		
		CodeRunnerManager.stoppedRunner(this);
		
		getLock().lock(this);
		setBusyNoLock(false);
		getLock().unlock(this);
		
		doneCallback();
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
