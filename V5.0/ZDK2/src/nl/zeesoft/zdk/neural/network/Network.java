package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.function.Executor;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.function.FunctionList;
import nl.zeesoft.zdk.function.FunctionListList;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.model.CellStats;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;

public class Network {
	public static final int								ALL_LAYERS			= -1;
	public static final String							ALL_PROCESSORS		= "*";
	
	protected Executor									executor			= new Executor();
	
	protected List<String>								inputNames			= null;
	protected NetworkProcessors							processors			= new NetworkProcessors(executor);
	
	protected NetworkIO									previousIO			= null;
	
	public void setNumberOfWorkers(int num) {
		executor.setNumberOfWorkers(num);
	}
	
	public boolean initialize(NetworkConfig config) {
		return initialize(config, 10000);
	}
	
	public boolean initialize(NetworkConfig config, int timeoutMs) {
		inputNames = new ArrayList<String>(config.inputNames);
		return processors.initialize(this, config.processorConfigs, timeoutMs);
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
			FunctionListList fll = processors.getResetFunctionForProcessors(layer, name);
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
	
	public void processIO(NetworkIO io) {
		if (isInitialized(io) && isValidIO(io)) {
			int timeoutMs = io.getTimeoutMs();
			FunctionListList fll = getProcessFunctionForNetworkIO(io);
			if (executor.execute(this, fll, timeoutMs) == null) {
				io.addError("Processing network IO timed out after " + timeoutMs + " ms");
			}
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

	protected FunctionListList getProcessFunctionForNetworkIO(NetworkIO io) {
		return getNewLayerProcessorFunctionListList(getProcessorFunctionsForNetworkIO(io));
	}

	protected FunctionListList getNewLayerProcessorFunctionListList(SortedMap<String,Function> processorFunctions) {
		FunctionListList r = new FunctionListList();
		for (Entry<Integer,List<NetworkProcessor>> entry: processors.getLayerProcessors().entrySet()) {
			FunctionList list = new FunctionList();
			for (NetworkProcessor np: entry.getValue()) {
				list.addFunction(processorFunctions.get(np.name));
			}
			r.addFunctionList(list);
		}
		return r;
	}

	protected SortedMap<String,Function> getProcessorFunctionsForNetworkIO(NetworkIO io) {
		SortedMap<String,Function> r = new TreeMap<String,Function>();
		for (NetworkProcessor np: processors.getProcessors()) {
			Function function = new Function() {
				@Override
				protected Object exec() {
					return processNetworkIOForProcessor((NetworkIO) param1, (NetworkProcessor) param2);
				}
			};
			function.param1 = io;
			function.param2 = np;
			r.put(np.name, function);
		}
		return r;
	}
	
	protected boolean processNetworkIOForProcessor(NetworkIO io, NetworkProcessor toProcessor) {
		Object inputValue = null;
		Sdr[] inputs = new Sdr[toProcessor.inputLinks.size()];
		boolean complete = processors.addInputsForProcessor(inputs, io, previousIO, toProcessor);
		for (LinkConfig link: toProcessor.inputLinks) {
			if (inputNames.contains(link.fromName)) {
				Object value = io.getInput(link.fromName);
				if (value instanceof Sdr) {
					inputs[link.toInput] = (Sdr)value;
				} else {
					inputValue = io.inputs.get(link.fromName);
				}
			}
		}
		ProcessorIO pio = new ProcessorIO();
		if (complete) {
			for (int i = 0; i < inputs.length; i++) {
				pio.inputs.add(inputs[i]);
			}
		}
		pio.inputValue = inputValue;
		toProcessor.processor.processIO(pio);
		io.addProcessorIO(toProcessor.name,pio);
		return pio.error.length() == 0;
	}
}
