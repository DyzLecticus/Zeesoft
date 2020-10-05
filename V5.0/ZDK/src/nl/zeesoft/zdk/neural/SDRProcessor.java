package nl.zeesoft.zdk.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.grid.GridColumn;
import nl.zeesoft.zdk.grid.SDR;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerList;

public class SDRProcessor {
	public Logger				logger						= new Logger();
	
	protected SDR				input						= null;
	protected SDR				output						= null;
	protected List<GridColumn>	activeInputColumns			= new ArrayList<GridColumn>();
	
	public Str getDescription() {
		return new Str();
	}
	
	public void initialize() {
		Str msg = new Str("Initializing " + this.getClass().getSimpleName());
		msg.sb().append(getDescription());
		msg.sb().append(" ...");
		logger.debug(this, msg);
		initialize(null);
		logger.debug(this, new Str("Initialized " + this.getClass().getSimpleName()));
	}

	public void initialize(CodeRunnerList runnerList) {
		// Override to implement
	}
	
	public void randomizeConnections() {
		logger.debug(this, new Str("Randomizing " + this.getClass().getSimpleName() + " connections ..."));
		randomizeConnections(null);
		logger.debug(this, new Str("Randomized " + this.getClass().getSimpleName() + " connections"));
	}
	
	public void randomizeConnections(CodeRunnerList runnerList) {
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
			randomizeConnections(rand);
			if (rand.size()>0) {
				runnerChain.add(rand);
			}
		}
	}
	
	public void setInput(SDR sdr) {
		input.copyValuesFrom(sdr.getColumns());
		output.setValue(false);
		activeInputColumns = input.getActiveColumns();
	}
	
	public void buildProcessorChain(CodeRunnerChain runnerChain, boolean learn) {
		// Override to implement
	}
	
	public SDR getOutput() {
		return new SDR(output);
	}
}
