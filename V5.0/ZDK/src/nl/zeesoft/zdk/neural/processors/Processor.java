package nl.zeesoft.zdk.neural.processors;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.StrAble;
import nl.zeesoft.zdk.neural.model.ModelStatistics;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerList;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.Waiter;

public class Processor implements StrAble {
	private Lock			lock						= new Lock();
	
	private String			name						= "";
	private SDRProcessor	processor					= null;
	private int				threads						= 0;
	private	CodeRunnerChain	processingChain				= new CodeRunnerChain();
	
	private boolean			sequential					= false;
	
	public Processor(String name, SDRProcessor processor, int threads) {
		this.name = name;
		this.processor = processor;
		this.threads = threads;
		processor.buildProcessorChain(processingChain, threads);
	}

	public String getName() {
		return name;
	}
	
	public void setProperty(String property, Object value) {
		lock.lock(this);
		if (property.equals("sequential") && value instanceof Boolean) {
			sequential = (Boolean) value;
		} else {
			processor.setProperty(property, value);
		}
		lock.unlock(this);
	}
	
	public void resetConnections() {
		resetConnections(null);
	}
	
	public ModelStatistics getStatistics() {
		ModelStatistics r = null;
		if (processor instanceof CellGridProcessor) {
			r = ((CellGridProcessor)processor).getStatistics();
			if (!(processor instanceof TemporalMemory)) {
				r.cells = 0;
			}
		}
		return r;
	}
	
	public void resetState() {
		resetState(null);
	}
	
	public void processIO(ProcessorIO io) {
		lock.lock(this);
		io.error = processor.setInput(io.inputs);
		if (io.error.length()==0) {
			if (sequential) {
				processingChain.runSequential();
			} else {
				if (!Waiter.startAndWaitFor(processingChain, io.timeoutMs)) {
					io.error.sb().append("Processing " + name + " input timed out");
				}
			}
		}
		io.outputs = processor.getOutputs();
		lock.unlock(this);
	}
	
	public int getProcessed() {
		lock.lock(this);
		int r = processor.getProcessed();
		lock.unlock(this);
		return r;
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
		processingChain = new CodeRunnerChain();
		processor.buildProcessorChain(processingChain, threads);
		lock.unlock(this);
	}
	
	protected void resetConnections(CodeRunnerList runnerList) {
		if (processor instanceof CellGridProcessor) {
			if (runnerList==null) {
				lock.lock(this);
			}
			((CellGridProcessor)processor).resetConnections(runnerList);
			if (runnerList==null) {
				lock.unlock(this);
			}
		}
	}
	
	protected void resetState(CodeRunnerList runnerList) {
		if (processor instanceof TemporalMemory) {
			if (runnerList==null) {
				lock.lock(this);
			}
			((TemporalMemory)processor).resetState(runnerList);
			if (runnerList==null) {
				lock.unlock(this);
			}
		}
	}
}