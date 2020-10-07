package nl.zeesoft.zdk.thread;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;

public class CodeRunnerList extends RunnerObject {
	private List<CodeRunner>	runners			= new ArrayList<CodeRunner>();
	private Exception			exception		= null;
	private List<CodeRunner>	activeRunners	= new ArrayList<CodeRunner>();
	
	public CodeRunnerList() {
		
	}
	
	public CodeRunnerList(RunCode code) {
		add(code);
	}
	
	public CodeRunner add(RunCode code) {
		CodeRunner runner = getNewCodeRunner(code);
		runner.setLogger(getLock().getLogger(this));
		getLock().lock(this);
		if (!isBusyNoLock()) {
			runners.add(runner);
		}
		getLock().unlock(this);
		return runner;
	}
	
	public void addAll(List<RunCode> codes) {
		for (RunCode code: codes) {
			add(code);
		}
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
			exception = null;
			for (CodeRunner runner: runners) {
				runner.start();
				activeRunners.add(runner);
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

	public int size() {
		getLock().lock(this);
		int r = runners.size();
		getLock().unlock(this);
		return r;
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
	
	public Exception getException() {
		getLock().lock(this);
		Exception r = exception;
		getLock().unlock(this);
		return r;
	}
	
	protected CodeRunner getNewCodeRunner(RunCode code) {
		CodeRunner r = new CodeRunner(code) {
			@Override
			protected void doneCallback() {
				runnerDoneCallback(this);
			}
			@Override
			protected void caughtException(Exception exception) {
				runnerCaughtException(this,exception);
			}
		};
		return r;
	}
	
	protected final void runnerDoneCallback(CodeRunner runner) {
		codeDoneCallback(runner.getCode());
		boolean stopped = false;
		Exception ex = null;
		getLock().lock(this);
		activeRunners.remove(runner);
		if (activeRunners.size()==0) {
			stopped = true;
			ex = exception;
		}
		if (stopped) {
			setBusyNoLock(false);
		}
		getLock().unlock(this);
		if (ex!=null) {
			caughtException(ex);
		}
		if (stopped) {
			stopped();
			doneCallback();
		}
	}
	
	protected final void runnerCaughtException(CodeRunner runner, Exception exception) {
		getLock().lock(this);
		if (this.exception==null) {
			this.exception = exception;
		}
		getLock().unlock(this);
		stop();
	}
		
	protected void codeDoneCallback(RunCode code) {
		// Override to implement
	}
}
