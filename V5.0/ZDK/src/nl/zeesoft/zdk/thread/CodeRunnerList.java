package nl.zeesoft.zdk.thread;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;

public class CodeRunnerList extends RunnerObject {
	private List<CodeRunner>	runners		= new ArrayList<CodeRunner>();
	
	public void addCode(RunCode code) {
		CodeRunner runner = getNewCodeRunner(code);
		runner.setLogger(getLock().getLogger(this));
		getLock().lock(this);
		if (!isBusyNoLock()) {
			runners.add(runner);
		}
		getLock().unlock(this);
	}
	
	public void setLogger(Logger logger) {
		getLock().setLogger(this, logger);
		getLock().lock(this);
		for (CodeRunner runner: runners) {
			runner.setLogger(logger);
		}
		getLock().unlock(this);
	}
	
	public void setSleepMs(int sleepMs) {
		getLock().lock(this);
		for (CodeRunner runner: runners) {
			runner.setSleepMs(sleepMs);
		}
		getLock().unlock(this);
	}
	
	public void setSleepNs(int sleepNs) {
		getLock().lock(this);
		for (CodeRunner runner: runners) {
			runner.setSleepNs(sleepNs);
		}
		getLock().unlock(this);
	}
	
	@Override
	public void start() {
		boolean started = false;
		getLock().lock(this);
		if (!isBusyNoLock()) {
			for (CodeRunner runner: runners) {
				runner.start();
			}
			setBusyNoLock(true);
			started = true;
		}
		getLock().unlock(this);
		if (started) {
			started();
		}
	}

	@Override
	public void stop() {
		getLock().lock(this);
		if (isBusyNoLock()) {
			for (CodeRunner runner: runners) {
				runner.stop();
			}
		}
		getLock().unlock(this);
	}

	public List<RunCode> getCodes() {
		List<RunCode> r = new ArrayList<RunCode>();
		getLock().lock(this);
		for (CodeRunner runner: runners) {
			r.add(runner.getCode());
		}
		getLock().unlock(this);
		return r;
	}

	public void clearCodes() {
		getLock().lock(this);
		if (!isBusyNoLock()) {
			runners.clear();
		}
		getLock().unlock(this);
	}
	
	/**
	 * Called when an exception has been caught.
	 */
	protected void caughtException(CodeRunner runner, Exception exception) {
		stop();
	}
	
	protected CodeRunner getNewCodeRunner(RunCode code) {
		final CodeRunnerList owner = this;
		CodeRunner r = new CodeRunner(code) {
			@Override
			protected void stopped() {
				owner.stoppedRunning(this);
			}
			@Override
			protected void caughtException(Exception exception) {
				owner.caughtException(this,exception);
			}
		};
		return r;
	}
	
	protected void stoppedRunning(CodeRunner runner) {
		getLock().lock(this);
		boolean stopped = true;
		for (CodeRunner rnnr: runners) {
			if (rnnr.isBusy()) {
				stopped = false;
				break;
			}
		}
		if (stopped) {
			setBusyNoLock(false);
		}
		getLock().unlock(this);
		if (stopped) {
			stopped();
		}
	}
}
