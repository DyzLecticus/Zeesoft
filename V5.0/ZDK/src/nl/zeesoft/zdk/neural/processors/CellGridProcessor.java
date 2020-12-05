package nl.zeesoft.zdk.neural.processors;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.model.CellGrid;
import nl.zeesoft.zdk.neural.model.ModelStatistics;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerList;

public abstract class CellGridProcessor extends SDRProcessor {
	protected float				permanenceThreshold			= 0.1F;
	protected float				permanenceIncrement			= 0.05F;
	protected float				permanenceDecrement			= 0.008F;
	
	public final void resetConnections() {
		Str msg = new Str("Resetting ");
		msg.sb().append(getName());
		msg.sb().append(" connections ...");
		Logger.dbg(this, msg);
		resetConnections(null);
		Logger.dbg(this, new Str("Reset connections"));
	}
	
	public abstract void resetConnections(CodeRunnerList runnerList);

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
	
	public abstract CellGrid getCellGrid();
	
	public ModelStatistics getStatistics() {
		return getCellGrid().getStatistics(permanenceThreshold);
	}
}
