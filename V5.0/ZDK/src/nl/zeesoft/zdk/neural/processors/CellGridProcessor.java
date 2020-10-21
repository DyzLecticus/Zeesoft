package nl.zeesoft.zdk.neural.processors;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerList;

public class CellGridProcessor extends SDRProcessor {
	public void resetConnections() {
		Logger.dbg(this, new Str("Resetting " + this.getClass().getSimpleName() + " connections ..."));
		resetConnections(null);
		Logger.dbg(this, new Str("Reset " + this.getClass().getSimpleName() + " connections"));
	}
	
	public void resetConnections(CodeRunnerList runnerList) {
		// Override to implement
	}

	public void buildInitializeChain(CodeRunnerChain runnerChain, boolean randomizeConnections) {
		CodeRunnerList init = new CodeRunnerList();
		initialize(init);
		if (init.size()>0) {
			runnerChain.add(init);
		}
		if (randomizeConnections) {
			CodeRunnerList rand = new CodeRunnerList();
			resetConnections(rand);
			if (rand.size()>0) {
				runnerChain.add(rand);
			}
		}
	}
}
