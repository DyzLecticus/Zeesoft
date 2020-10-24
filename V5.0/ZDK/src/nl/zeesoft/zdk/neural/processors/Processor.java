package nl.zeesoft.zdk.neural.processors;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.StrAble;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.Waiter;

public class Processor implements StrAble {
	private Lock			lock						= new Lock();
	private String			name						= "";
	private SDRProcessor	processor					= null;
	
	private	CodeRunnerChain	processingAndLearningChain	= new CodeRunnerChain();
	private	CodeRunnerChain	processingOnlyChain			= new CodeRunnerChain();
	
	public Processor(String name, SDRProcessor processor) {
		this.name = name;
		this.processor = processor;
		processor.buildProcessorChain(processingAndLearningChain, true);
		processor.buildProcessorChain(processingOnlyChain, false);
	}
	
	public void processIO(ProcessorIO io) {
		lock.lock(this);
		processor.setInput(io.inputs);
		boolean timedOut = false;
		if (io.learn) {
			timedOut = !Waiter.startAndWaitFor(processingAndLearningChain, io.timeoutMs);
		} else {
			timedOut = !Waiter.startAndWaitFor(processingOnlyChain, io.timeoutMs);
		}
		if (timedOut) {
			io.error.sb().append("Processing " + name + " input timed out");
		}
		io.outputs = processor.getOutputs();
		lock.unlock(this);
	}
	
	public void save(String path) {
		Str str = toStr();
		str.toFile(path);
	}
	
	public void load(String path) {
		Str str = new Str();
		Str err = str.fromFile(path);
		if (err.length()==0) {
			fromStr(str);
		}
	}

	@Override
	public Str toStr() {
		lock.lock(this);
		Str r = processor.toStr();
		lock.unlock(this);
		return r;
	}

	@Override
	public void fromStr(Str str) {
		lock.lock(this);
		processor.fromStr(str);
		lock.unlock(this);
	}
}
