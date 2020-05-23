package nl.zeesoft.zdk.thread;

import java.util.ArrayList;
import java.util.List;

public class CodeRunnerList {
	private Lock				lock			= new Lock();
	
	private List<CodeRunner>	runners			= new ArrayList<CodeRunner>();
	private boolean				running			= false;
	
	public void setListener(LockListener listener) {
		lock.setListener(this, listener);
	}
	
	public void addCode(RunCode code) {
		CodeRunner runner = getNewCodeRunner(code);
		runner.setListener(lock.getListener(this));
		lock.lock(this);
		if (!running) {
			runners.add(runner);
		}
		lock.unlock(this);
	}
	
	public void setSleepMs(int sleepMs) {
		lock.lock(this);
		for (CodeRunner runner: runners) {
			runner.setSleepMs(sleepMs);
		}
		lock.unlock(this);
	}
	
	public void setSleepNs(int sleepNs) {
		lock.lock(this);
		for (CodeRunner runner: runners) {
			runner.setSleepNs(sleepNs);
		}
		lock.unlock(this);
	}
	
	public boolean isRunning() {
		lock.lock(this);
		boolean r = running;
		lock.unlock(this);
		return r;
	}
	
	public void start() {
		boolean started = false;
		lock.lock(this);
		if (!running) {
			for (CodeRunner runner: runners) {
				runner.start();
			}
			running = true;
			started = true;
		}
		lock.unlock(this);
		if (started) {
			started();
		}
	}

	public void stop() {
		stop(true, 10);
	}

	public void stop(boolean wait, int sleepMs) {
		lock.lock(this);
		if (running) {
			for (CodeRunner runner: runners) {
				runner.stop();
			}
		}
		lock.unlock(this);
		if (wait && sleepMs>0) {
			while(isRunning()) {
				try {
					Thread.sleep(sleepMs);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public List<RunCode> getCodes() {
		List<RunCode> r = new ArrayList<RunCode>();
		lock.lock(this);
		for (CodeRunner runner: runners) {
			r.add(runner.getCode());
		}
		lock.unlock(this);
		return r;
	}

	public void clearCodes() {
		lock.lock(this);
		if (!running) {
			runners.clear();
		}
		lock.unlock(this);
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
	protected void caughtException(Exception ex) {
		stop(false, 0);
	}
	
	protected CodeRunner getNewCodeRunner(RunCode code) {
		final CodeRunnerList owner = this;
		CodeRunner r = new CodeRunner(code) {
			@Override
			protected void stopped() {
				owner.stoppedRunning();
			}
			@Override
			protected void caughtException(Exception exception) {
				owner.caughtException(exception);
			}
		};
		return r;
	}
	
	protected void stoppedRunning() {
		lock.lock(this);
		boolean stopped = true;
		for (CodeRunner runner: runners) {
			if (runner.isRunning()) {
				stopped = false;
				break;
			}
		}
		if (stopped) {
			running = false;
		}
		lock.unlock(this);
		if (stopped) {
			stopped();
		}
	}
}
