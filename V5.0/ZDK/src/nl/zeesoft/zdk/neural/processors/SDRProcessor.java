package nl.zeesoft.zdk.neural.processors;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.StrAble;
import nl.zeesoft.zdk.grid.Grid;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerList;
import nl.zeesoft.zdk.thread.RunCode;

public class SDRProcessor implements StrAble {
	protected final static String	OBJECT_SEPARATOR	= "@OBJECT@\n";
	
	protected int					processed			= 0;
	protected List<SDR>				inputs				= new ArrayList<SDR>();
	protected List<SDR>				outputs				= new ArrayList<SDR>();
	
	public void configure(SDRProcessorConfig config) {
		// Override to implement
	}
	
	public Str getDescription() {
		return new Str(this.getClass().getSimpleName());
	}
	
	public final void initialize() {
		Str msg = new Str("Initializing ");
		msg.sb().append(getDescription());
		msg.sb().append(" ...");
		Logger.dbg(this, msg);
		initialize(null);
		msg = new Str("Initialized");
		Logger.dbg(this, msg);
	}

	public void initialize(CodeRunnerList runnerList) {
		// Override to implement
	}
	
	public final void setInput(List<SDR> sdrs) {
		SDR[] in = new SDR[sdrs.size()];
		int i = 0;
		for (SDR sdr: sdrs) {
			in[i] = sdr;
			i++;
		}
		setInput(in);
	}
	
	public void setInput(SDR... sdrs) {
		inputs.clear();
		for (SDR sdr: sdrs) {
			inputs.add(sdr);
		}
		outputs.clear();
	}

	public final void buildProcessorChain(CodeRunnerChain runnerChain, boolean learn) {
		buildProcessorChain(runnerChain, learn, Grid.THREADS);
	}

	public void buildProcessorChain(CodeRunnerChain runnerChain, boolean learn, int threads) {
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
		return new ArrayList<SDR>(outputs);
	}
	
	public SDR getOutput() {
		return getOutput(0);
	}
	
	public SDR getOutput(int index) {
		SDR r = null;
		if (index < outputs.size()) {
			r = outputs.get(index);
		}
		return r;
	}

	public int getProcessed() {
		return processed;
	}

	public void setProcessed(int processed) {
		this.processed = processed;
	}

	@Override
	public Str toStr() {
		// Override to implement
		return new Str();
	}

	@Override
	public void fromStr(Str str) {
		// Override to implement
	}
}
