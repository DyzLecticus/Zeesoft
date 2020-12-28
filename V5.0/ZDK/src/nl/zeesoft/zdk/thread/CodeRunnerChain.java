package nl.zeesoft.zdk.thread;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;

public class CodeRunnerChain implements Waitable {
	protected Lock						lock					= new Lock();
	
	protected boolean					logExceptions			= true;
	
	protected List<ProgressListener>	progressListeners		= new ArrayList<ProgressListener>();
	
	protected List<CodeRunnerList>		runnerLists				= new ArrayList<CodeRunnerList>();
	protected int						activeRunnerListIndex	= -1;
	protected int						doneCodes				= 0;
	
	public void addProgressListener(ProgressListener listener) {
		lock.lock(this);
		if (!progressListeners.contains(listener)) {
			progressListeners.add(listener);
		}
		lock.unlock(this);
	}
	
	public void add(RunCode code) {
		if (code!=null) {
			List<RunCode> codes = new ArrayList<RunCode>();
			codes.add(code);
			addAll(codes);
		}
	}

	public void addAll(List<RunCode> codes) {
		if (codes.size()>0) {
			lock.lock(this);
			if (!isBusyNoLock()) {
				CodeRunnerList runnerList = getNewCodeRunnerList(codes);
				runnerList.addAll(codes);
				runnerLists.add(runnerList);
			}
			lock.unlock(this);
		}
	}
	
	public void add(CodeRunnerList runnerList) {
		if (runnerList!=null) {
			addAll(runnerList.getCodes());
		}
	}
	
	public void add(CodeRunnerChain chain) {
		if (chain!=null) {
			List<CodeRunnerList> lists = chain.getRunnerLists();
			for (CodeRunnerList runnerList: lists) {
				add(runnerList);
			}
		}
	}
	
	public void setPriority(int priority) {
		if (priority>=Thread.MIN_PRIORITY && priority<=Thread.MAX_PRIORITY) {
			lock.lock(this);
			for (CodeRunnerList runnerList: runnerLists) {
				runnerList.setPriority(priority);
			}
			lock.unlock(this);
		}
	}
	
	public void setLogExceptions(boolean logExceptions) {
		lock.lock(this);
		this.logExceptions = logExceptions;
		lock.unlock(this);
	}

	public List<RunCode> getCodes() {
		lock.lock(this);
		List<RunCode> r = getCodesNoLock();
		lock.unlock(this);
		return r;
	}

	public int getDoneCodes() {
		lock.lock(this);
		int r = doneCodes;
		lock.unlock(this);
		return r;
	}
	
	protected List<CodeRunnerList> getRunnerLists() {
		lock.lock(this);
		List<CodeRunnerList> r = new ArrayList<CodeRunnerList>(runnerLists);
		lock.unlock(this);
		return r;
	}
	
	public void start() {
		lock.lock(this);
		if (!isBusyNoLock()) {
			int todoCodes = getCodesNoLock().size();
			for (ProgressListener listener: progressListeners) {
				listener.initialized(todoCodes);
			}
			doneCodes = 0;
			if (runnerLists.size()>0) {
				activeRunnerListIndex = 0;
				runnerLists.get(activeRunnerListIndex).start();
			}
		}
		lock.unlock(this);
	}
	
	public int size() {
		lock.lock(this);
		int r = runnerLists.size();
		lock.unlock(this);
		return r;
	}

	@Override
	public boolean isBusy() {
		lock.lock(this);
		boolean r = isBusyNoLock();
		lock.unlock(this);
		return r;
	}
	
	public void runSequential() {
		Exception exception = null;
		lock.lock(this);
		List<RunCode> codes = getCodesNoLock();
		for (ProgressListener listener: progressListeners) {
			listener.initialized(codes.size());
		}
		for (RunCode code: codes) {
			code.tryRunCatch();
			exception = code.getException();
			if (exception!=null) {
				break;
			}
			for (ProgressListener listener: progressListeners) {
				listener.progressed(1);
			}
		}
		lock.unlock(this);
		if (exception!=null) {
			caughtException(exception);
		}
		doneCallback();
	}

	protected CodeRunnerList getNewCodeRunnerList(List<RunCode> code) {
		CodeRunnerList r = new CodeRunnerList() {
			@Override
			protected void doneCallback() {
				runnerListDoneCallback(this);
			}
			@Override
			protected void codeDoneCallback(RunCode code) {
				codeIsDone(code);
			}
			
		};
		return r;
	}
	
	protected final void runnerListDoneCallback(CodeRunnerList runnerList) {
		boolean done = false;
		Exception exception = runnerList.getException();
		lock.lock(this);
		activeRunnerListIndex++;
		CodeRunnerList next = null;
		if (activeRunnerListIndex<runnerLists.size()) {
			next = runnerLists.get(activeRunnerListIndex);
		}
		if (exception==null && next!=null) {
			next.start();
		} else {
			done = true;
			activeRunnerListIndex = -1;
		}
		lock.unlock(this);
		if (exception!=null) {
			caughtException(exception);
		}
		if (done) {
			doneCallback();
		}
	}

	protected void codeIsDone(RunCode code) {
		lock.lock(this);
		doneCodes++;
		for (ProgressListener listener: progressListeners) {
			listener.progressed(1);
		}
		lock.unlock(this);
	}
	
	protected void caughtException(Exception exception) {
		lock.lock(this);
		boolean log = logExceptions;
		lock.unlock(this);
		if (log) {
			Logger.err(this, new Str(this.getClass().getName() + " caught exception"), exception);
		}
	}
	
	protected void doneCallback() {
		// Override to implement
	}
	
	protected boolean isBusyNoLock() {
		return activeRunnerListIndex >= 0;
	}
	
	protected List<RunCode> getCodesNoLock() {
		List<RunCode> r = new ArrayList<RunCode>();
		for (CodeRunnerList runnerList: runnerLists) {
			for (RunCode code: runnerList.getCodes()) {
				r.add(code);
			}
		}
		return r;
	}
}
