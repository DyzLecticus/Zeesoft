package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.database.DatabaseCollection;
import nl.zeesoft.zdk.database.DatabaseConfiguration;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.ProgressBar;
import nl.zeesoft.zdk.thread.Waiter;

public class DCTest extends DatabaseCollection {
	public DCTest(DatabaseConfiguration config) {
		super(config);
	}

	public CodeRunnerChain saveIndex(boolean force, int minDiffMs, int timeoutMs) {
		CodeRunnerChain r = getSaveIndexChain(force, minDiffMs);
		if (r!=null) {
			ProgressBar progressBar = new ProgressBar("Saving index", 10);
			r.addProgressListener(progressBar);
			Waiter.startAndWaitTillDone(r,timeoutMs);
		}
		return r;
	}

	public CodeRunnerChain loadIndex(int timeoutMs) {
		CodeRunnerChain r = super.getLoadIndexChain();
		if (r!=null) {
			ProgressBar progressBar = new ProgressBar("Loading index", 10);
			r.addProgressListener(progressBar);
			Waiter.startAndWaitTillDone(r,timeoutMs);
		}
		return r;
	}

	public CodeRunnerChain saveAllBlocks(boolean force, int minDiffMs, int timeoutMs) {
		CodeRunnerChain r = getSaveAllBlocksChain(force, minDiffMs);
		Waiter.startAndWaitTillDone(r,timeoutMs);
		return r;
	}

	public CodeRunnerChain loadAllBlocks(int timeoutMs) {
		CodeRunnerChain r = getLoadAllBlocksChain();
		Waiter.startAndWaitTillDone(r,timeoutMs);
		return r;
	}
}
