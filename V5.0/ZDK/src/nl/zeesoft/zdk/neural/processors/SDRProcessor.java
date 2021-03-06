package nl.zeesoft.zdk.neural.processors;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.StrAble;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerList;
import nl.zeesoft.zdk.thread.RunCode;

public abstract class SDRProcessor implements StrAble {
	protected final static String	OBJECT_SEPARATOR	= "@OBJECT@\n";
	
	protected boolean				learn				= true;
	protected int					processed			= 0;
	protected List<SDR>				inputs				= new ArrayList<SDR>();
	protected List<SDR>				outputs				= new ArrayList<SDR>();
	
	public void configure(SDRProcessorConfig config) {
		// Override to implement
	}
	
	public void setProperty(String property, Object value) {
		if (property.equals("learn") && value instanceof Boolean) {
			learn = (Boolean) value;
		} else if (property.equals("processed") && value instanceof Integer) {
			processed = (Integer) value;
		}
	}
	
	public Str getName() {
		return new Str(this.getClass().getSimpleName());
	}
	
	public final void initialize() {
		Str msg = new Str("Initializing ");
		msg.sb().append(getName());
		msg.sb().append(" ...");
		Logger.dbg(this, msg);
		initialize(null);
		msg = new Str("Initialized");
		Logger.dbg(this, msg);
	}

	public void initialize(CodeRunnerList runnerList) {
		// Override to implement
	}
	
	public final Str setInput(List<SDR> sdrs) {
		SDR[] in = new SDR[sdrs.size()];
		int i = 0;
		for (SDR sdr: sdrs) {
			in[i] = sdr;
			i++;
		}
		return setInput(in);
	}
	
	public Str setInput(SDR... sdrs) {
		Str err = new Str();
		inputs.clear();
		for (SDR sdr: sdrs) {
			inputs.add(sdr);
		}
		outputs.clear();
		return err;
	}

	public final void buildProcessorChain(CodeRunnerChain runnerChain) {
		buildProcessorChain(runnerChain, ProcessorFactory.THREADS);
	}

	public void buildProcessorChain(CodeRunnerChain runnerChain, int threads) {
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
