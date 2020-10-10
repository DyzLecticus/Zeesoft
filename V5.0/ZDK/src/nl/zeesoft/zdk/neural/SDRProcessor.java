package nl.zeesoft.zdk.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.grid.SDR;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerList;

public class SDRProcessor {
	protected List<SDR>			inputs						= new ArrayList<SDR>();
	protected List<SDR>			outputs						= new ArrayList<SDR>();
	
	public Str getDescription() {
		return new Str();
	}
	
	public void initialize() {
		Str msg = new Str("Initializing " + this.getClass().getSimpleName());
		msg.sb().append(getDescription());
		msg.sb().append(" ...");
		Logger.dbg(this, msg);
		initialize(null);
		Logger.dbg(this, new Str("Initialized " + this.getClass().getSimpleName()));
	}

	public void initialize(CodeRunnerList runnerList) {
		// Override to implement
	}
	
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
	
	public void setInput(SDR... sdrs) {
		inputs.clear();
		for (SDR sdr: sdrs) {
			inputs.add(sdr);
		}
		outputs.clear();
	}
	
	public void buildProcessorChain(CodeRunnerChain runnerChain, boolean learn) {
		// Override to implement
	}
	
	public List<SDR> getOutputs() {
		return outputs;
	}
}
