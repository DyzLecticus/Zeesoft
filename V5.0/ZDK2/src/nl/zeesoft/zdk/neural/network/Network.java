package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.function.Executor;
import nl.zeesoft.zdk.function.ExecutorTask;
import nl.zeesoft.zdk.function.FunctionListList;
import nl.zeesoft.zdk.json.JsonTransient;
import nl.zeesoft.zdk.neural.model.CellStats;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;

public class Network {
	public static final int				ALL_LAYERS			= -1;
	public static final String			ALL_PROCESSORS		= "*";
	
	protected Executor					executor			= new Executor();
	
	protected List<String>				inputNames			= null;
	@JsonTransient
	protected NetworkProcessors			processors			= new NetworkProcessors(executor);
	@JsonTransient
	protected NetworkProcessorResetter	resetter			= new NetworkProcessorResetter(processors);
	@JsonTransient
	protected NetworkIOProcessor		ioProcessor			= null;
	
	protected NetworkIO					previousIO			= null;
	
	public void setNumberOfWorkers(int num) {
		executor.setWorkers(num);
	}
	
	public boolean initialize(NetworkConfig config) {
		return initialize(config, 10000);
	}
	
	public boolean initialize(NetworkConfig config, int timeoutMs) {
		inputNames = new ArrayList<String>(config.getInputNames());
		ioProcessor = new NetworkIOProcessor(inputNames, processors);
		return processors.initialize(this, config.getProcessorConfigs(), timeoutMs);
	}
	
	public boolean isInitialized() {
		return inputNames!=null;
	}
	
	public boolean reset() {
		return reset(10000);
	}
	
	public boolean reset(int timeoutMs) {
		return reset(ALL_LAYERS, ALL_PROCESSORS, timeoutMs);
	}
	
	public boolean reset(int layer, String name, int timeoutMs) {
		boolean r = false;
		if (isInitialized()) {
			previousIO = null;
			FunctionListList fll = resetter.getResetFunctionForProcessors(layer, name);
			r = (executor.execute(this, fll, timeoutMs) != null);
		}
		return r;
	}
	
	public void setLearn(boolean learn) {
		setLearn(ALL_LAYERS, ALL_PROCESSORS, learn);
	}
	
	public void setLearn(int layer, String name, boolean learn) {
		processors.setLearn(layer, name, learn);
	}
	
	public void setNumberOfWorkersForProcessors(int workers) {
		setNumberOfWorkersForProcessor(ALL_LAYERS, ALL_PROCESSORS, workers);
	}
	
	public void setNumberOfWorkersForProcessor(int layer, String name, int workers) {
		processors.setNumberOfWorkers(layer, name, workers);
	}
	
	public void processIO(NetworkIO io) {
		if (isInitialized(io) && isValidIO(io)) {
			int timeoutMs = io.getTimeoutMs();
			long start = System.nanoTime();
			FunctionListList fll = ioProcessor.getProcessFunctionForNetworkIO(io, previousIO);
			ExecutorTask task = executor.execute(this, fll, timeoutMs);
			if (task==null) {
				io.addError("Processing network IO timed out after " + timeoutMs + " ms");
			}
			io.setStats(start, task);
			previousIO = io;
		}
	}
	
	public NetworkIO getPreviousIO() {
		return previousIO;
	}
	
	public CellStats getCellStats() {
		return getCellStats(ALL_LAYERS, ALL_PROCESSORS);
	}
	
	public CellStats getCellStats(int layer, String name) {
		return processors.getCellStats(this, layer, name);
	}
	
	public List<String> getInputNames() {
		return new ArrayList<String>(inputNames);
	}
	
	public List<String> getProcessorNames() {
		return processors.getProcessorNames();
	}
	
	public int getNumberOfLayers() {
		return processors.getNumberOfLayers();
	}
	
	public int getNumberOfProcessors() {
		return processors.getNumberOfProcessors();
	}
	
	public int getWidth() {
		return processors.getWidth();
	}
	
	public List<NetworkProcessor> getProcessors(int layer) {
		return getProcessors(layer, ALL_PROCESSORS);
	}
	
	public List<NetworkProcessor> getProcessors(String name) {
		return getProcessors(ALL_LAYERS, name);
	}
	
	public List<NetworkProcessor> getProcessors(int layer, String name) {
		return processors.getProcessors(layer, name);
	}
	
	protected boolean isInitialized(NetworkIO io) {
		if (!isInitialized()) {
			io.addError(this.getClass().getSimpleName() + " is not initialized");
		}
		return io.getErrors().size() == 0;
	}
	
	protected boolean isValidIO(NetworkIO io) {
		for (String name: inputNames) {
			if (io.getInput(name)==null) {
				io.addError("Missing network input value for " + name);
			}
		}
		return io.getErrors().size() == 0;
	}
}
