package nl.zeesoft.zdk.neural.processors;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerList;
import nl.zeesoft.zdk.thread.RunCode;

public class SDRProcessor {
	protected int				processed					= 0;
	protected List<SDR>			inputs						= new ArrayList<SDR>();
	protected List<SDR>			outputs						= new ArrayList<SDR>();
	
	public void configure(SDRProcessorConfig config) {
		// Override to implement
	}
	
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
	
	public void addIncrementProcessedToProcessorChain(CodeRunnerChain runnerChain) {
		CodeRunnerList incrementProcessed = new CodeRunnerList(new RunCode() {
			@Override
			protected boolean run() {
				processed++;
				return true;
			}
		});
		runnerChain.add(incrementProcessed);
	}
	
	public List<SDR> getOutputs() {
		return outputs;
	}

	public int getProcessed() {
		return processed;
	}

	public void setProcessed(int processed) {
		this.processed = processed;
	}
}
