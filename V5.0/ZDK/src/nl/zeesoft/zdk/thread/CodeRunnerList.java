package nl.zeesoft.zdk.thread;

import java.util.ArrayList;
import java.util.List;

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
		lock.lock(this);
		if (!busy.isBusy()) {
			runners.add(runner);
		}
		lock.unlock(this);
		return runner;
	}
	
	public void addAll(List<RunCode> codes) {
		for (RunCode code: codes) {
			add(code);
		}
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
	
	public void setPriority(int priority) {
		if (priority>=Thread.MIN_PRIORITY && priority<=Thread.MAX_PRIORITY) {
			lock.lock(this);
			for (CodeRunner runner: runners) {
				runner.setPriority(priority);
			}
			lock.unlock(this);
		}
	}
	
	@Override
	public void start() {
		boolean started = false;
		lock.lock(this);
		if (!busy.isBusy()) {
			exception = null;
			for (CodeRunner runner: runners) {
				runner.start();
				activeRunners.add(runner);
			}
			busy.setBusy(true);
			started = true;
		}
		lock.unlock(this);
		if (started) {
			started();
		}
	}

	@Override
	public void stop() {
		lock.lock(this);
		if (busy.isBusy()) {
			for (CodeRunner runner: runners) {
				runner.stop();
			}
		}
		lock.unlock(this);
	}

	public int size() {
		lock.lock(this);
		int r = runners.size();
		lock.unlock(this);
		return r;
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
		if (!busy.isBusy()) {
			runners.clear();
		}
		lock.unlock(this);
	}
	
	public Exception getException() {
		lock.lock(this);
		Exception r = exception;
		lock.unlock(this);
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
		lock.lock(this);
		activeRunners.remove(runner);
		if (activeRunners.size()==0) {
			stopped = true;
			ex = exception;
		}
		if (stopped) {
			busy.setBusy(false);
		}
		lock.unlock(this);
		if (ex!=null) {
			caughtException(ex);
		}
		if (stopped) {
			stopped();
			doneCallback();
		}
	}
	
	protected final void runnerCaughtException(CodeRunner runner, Exception exception) {
		lock.lock(this);
		if (this.exception==null) {
			this.exception = exception;
		}
		lock.unlock(this);
		stop();
	}
		
	protected void codeDoneCallback(RunCode code) {
		// Override to implement
	}
}
