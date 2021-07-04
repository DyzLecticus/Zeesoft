package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.function.Executor;
import nl.zeesoft.zdk.function.ExecutorTask;
import nl.zeesoft.zdk.function.FunctionListList;
import nl.zeesoft.zdk.json.Finalizable;
import nl.zeesoft.zdk.json.JsonTransient;
import nl.zeesoft.zdk.neural.model.CellStats;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;

public class Network implements Finalizable {
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
	
	/**
	 * Specifies the number of worker threads this network should use.
	 * Remember to set the number of workers back to 0 (zero) when the network is no longer used.
	 * 
	 * @param num The number of worker threads
	 */
	public synchronized void setNumberOfWorkers(int num) {
		executor.setWorkers(num);
	}

	@Override
	public synchronized void finalizeObject() {
		ioProcessor = new NetworkIOProcessor(inputNames, processors);
	}
	
	/**
	 * Initializes the network.
	 * Optionally uses multiple worker threads (see setNumberOfWorkers).
	 * 
	 * @param config The network configuration
	 * @return True if the network initialized within 10 seconds
	 */
	public boolean initialize(NetworkConfig config) {
		return initialize(config, 10000);
	}
	
	/**
	 * Initializes the network.
	 * Optionally uses multiple worker threads (see setNumberOfWorkers).
	 * 
	 * @param config The network configuration
	 * @param timeoutMs The maximum number of milliseconds initialization can take
	 * @return True if the network initialized within the specified time out milliseconds
	 */
	public synchronized boolean initialize(NetworkConfig config, int timeoutMs) {
		inputNames = new ArrayList<String>(config.getInputNames());
		ioProcessor = new NetworkIOProcessor(inputNames, processors);
		return processors.initialize(this, config.getProcessorConfigs(), timeoutMs);
	}
	
	public synchronized boolean isInitialized() {
		return inputNames!=null;
	}
	
	/**
	 * Resets the network.
	 * Optionally uses multiple worker threads (see setNumberOfWorkers).
	 * 
	 * @return True if the network initialized within 10 seconds
	 */
	public boolean reset() {
		return reset(10000);
	}
	
	/**
	 * Resets the network.
	 * Optionally uses multiple worker threads (see setNumberOfWorkers).
	 * 
	 * @param timeoutMs The maximum number of milliseconds reset can take
	 * @return True if the network initialized within the specified time out milliseconds
	 */
	public boolean reset(int timeoutMs) {
		return reset(ALL_LAYERS, ALL_PROCESSORS, timeoutMs);
	}
	
	/**
	 * Resets the network.
	 * Optionally uses multiple worker threads (see setNumberOfWorkers).
	 * 
	 * @param layer Processor layer number filter; -1 = * (see ALL_LAYERS)
	 * @param name Processor name contains filter; '*' = * (see ALL_PROCESSORS) 
	 * @param timeoutMs The maximum number of milliseconds reset can take
	 * @return True if the network initialized within the specified time out milliseconds
	 */
	public synchronized boolean reset(int layer, String name, int timeoutMs) {
		boolean r = false;
		if (isInitialized()) {
			previousIO = null;
			FunctionListList fll = resetter.getResetFunctionForProcessors(layer, name);
			r = (executor.execute(this, fll, timeoutMs) != null);
		}
		return r;
	}
	
	/**
	 * Toggles learning for all learning network processors.
	 * 
	 * @param learn Indicates learning on/off
	 */
	public void setLearn(boolean learn) {
		setLearn(ALL_LAYERS, ALL_PROCESSORS, learn);
	}
	
	/**
	 * Toggles learning for the specified learning network processors.
	 * 
	 * @param layer Processor layer number filter; -1 = * (see ALL_LAYERS)
	 * @param name Processor name contains filter; '*' = * (see ALL_PROCESSORS) 
	 * @param learn Indicates learning on/off
	 */
	public synchronized void setLearn(int layer, String name, boolean learn) {
		processors.setLearn(layer, name, learn);
	}
	
	/**
	 * Specifies the number of workers individual executor processors should use.
	 */
	public void setNumberOfWorkersForProcessors(int workers) {
		setNumberOfWorkersForProcessor(ALL_LAYERS, ALL_PROCESSORS, workers);
	}
	
	/**
	 * Specifies the number of workers individual executor processors should use.
	 * 
	 * @param layer Processor layer number filter; -1 = * (see ALL_LAYERS)
	 * @param name Processor name contains filter; '*' = * (see ALL_PROCESSORS) 
	 */
	public synchronized void setNumberOfWorkersForProcessor(int layer, String name, int workers) {
		processors.setNumberOfWorkers(layer, name, workers);
	}
	
	/**
	 * Main network IO processing method.
	 * Optionally uses multiple worker threads (see setNumberOfWorkers).
	 * 
	 * @param io The network IO to process
	 */
	public synchronized void processIO(NetworkIO io) {
		if (isInitialized(io) && isValidIO(io)) {
			long start = System.nanoTime();
			FunctionListList fll = ioProcessor.getProcessFunctionForNetworkIO(io, previousIO);
			ExecutorTask task = executor.execute(this, fll, io.getTimeoutMs());
			if (task==null) {
				io.addError("Processing network IO timed out after " + io.getTimeoutMs() + " ms");
			}
			io.setStats(start, task);
			previousIO = io;
		}
	}
	
	public synchronized NetworkIO getPreviousIO() {
		return previousIO;
	}
	
	public CellStats getCellStats() {
		return getCellStats(ALL_LAYERS, ALL_PROCESSORS);
	}
	
	public synchronized CellStats getCellStats(int layer, String name) {
		return processors.getCellStats(this, layer, name);
	}
	
	public synchronized List<String> getInputNames() {
		return new ArrayList<String>(inputNames);
	}
	
	public synchronized List<String> getProcessorNames() {
		return processors.getProcessorNames();
	}
	
	public synchronized int getNumberOfLayers() {
		return processors.getNumberOfLayers();
	}
	
	public synchronized int getNumberOfProcessors() {
		return processors.getNumberOfProcessors();
	}
	
	public synchronized int getWidth() {
		return processors.getWidth();
	}
	
	public List<NetworkProcessor> getProcessors(int layer) {
		return getProcessors(layer, ALL_PROCESSORS);
	}
	
	public List<NetworkProcessor> getProcessors(String name) {
		return getProcessors(ALL_LAYERS, name);
	}
	
	public synchronized List<NetworkProcessor> getProcessors(int layer, String name) {
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
