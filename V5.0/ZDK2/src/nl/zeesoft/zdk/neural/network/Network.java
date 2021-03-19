package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.function.FunctionList;
import nl.zeesoft.zdk.function.FunctionListList;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;

public class Network {
	public static final int								ALL_LAYERS			= -1;
	public static final String							ALL_PROCESSORS		= "*";
	
	protected List<String>								inputNames			= null;
	protected SortedMap<String,NetworkProcessor>		processors			= new TreeMap<String,NetworkProcessor>();
	protected SortedMap<Integer,List<NetworkProcessor>>	layerProcessors		= new TreeMap<Integer,List<NetworkProcessor>>();
	
	protected NetworkIO									previousIO			= null;
	
	public void initialize(NetworkConfig config) {
		inputNames = new ArrayList<String>(config.inputNames);
		for (ProcessorConfig pc: config.processorConfigs) {
			NetworkProcessor np = pc.getNewNetworkProcessor();
			processors.put(pc.name, np);
			List<NetworkProcessor> lps = layerProcessors.get(pc.layer);
			if (lps==null) {
				lps = new ArrayList<NetworkProcessor>();
				layerProcessors.put(pc.layer, lps);
			}
			lps.add(np);
		}
	}
	
	public boolean isInitialized() {
		return inputNames!=null;
	}
	
	public int getNumberOfLayers() {
		return layerProcessors.size();
	}
	
	public int getNumberOfProcessors() {
		return processors.size();
	}
	
	public int getWidth() {
		int r = 0;
		for (List<NetworkProcessor> lps: layerProcessors.values()) {
			if (lps.size()>r) {
				r = lps.size();
			}
		}
		return r;
	}
	
	public void reset() {
		previousIO = null;
		reset(ALL_LAYERS, ALL_PROCESSORS);
	}
	
	public void reset(int layer, String name) {
		if (isInitialized()) {
			FunctionListList fll = getResetFunctionForProcessors(layer, name);
			fll.execute(this);
		}
	}
	
	public void processIO(NetworkIO io) {
		if (isInitialized(io) && isValidIO(io)) {
			FunctionListList fll = getProcessFunctionForNetworkIO(io);
			fll.execute(this);
		}
		previousIO = io;
	}
	
	public NetworkIO getPreviousIO() {
		return previousIO;
	}
	
	public List<String> getInputNames() {
		return new ArrayList<String>(inputNames);
	}
	
	public List<String> getProcessorNames() {
		return new ArrayList<String>(processors.keySet());
	}
	
	public List<NetworkProcessor> getProcessors(int layer) {
		return this.getNetworkProcessors(layer, ALL_PROCESSORS);
	}
	
	public List<NetworkProcessor> getProcessors(String name) {
		return this.getNetworkProcessors(ALL_LAYERS, name);
	}
	
	public List<NetworkProcessor> getProcessors(int layer, String name) {
		return this.getNetworkProcessors(layer, name);
	}
	
	protected boolean isInitialized(NetworkIO io) {
		if (!isInitialized()) {
			io.errors.add(this.getClass().getSimpleName() + " is not initialized");
		}
		return io.errors.size() == 0;
	}
	
	protected boolean isValidIO(NetworkIO io) {
		for (String name: inputNames) {
			if (!io.inputs.containsKey(name) || io.inputs.get(name)==null) {
				io.errors.add("Missing network input value for " + name);
			}
		}
		return io.errors.size() == 0;
	}

	protected FunctionListList getResetFunctionForProcessors(int layer, String name) {
		return getNewProcessorFunctionListList(getProcessorResetFunctions(layer,name));
	}

	protected FunctionListList getProcessFunctionForNetworkIO(NetworkIO io) {
		return getNewLayerProcessorFunctionListList(getProcessorFunctionsForNetworkIO(io));
	}

	protected FunctionListList getNewProcessorFunctionListList(SortedMap<String,Function> processorFunctions) {
		FunctionListList r = new FunctionListList();
		FunctionList list = new FunctionList();
		for (NetworkProcessor np: processors.values()) {
			list.addFunction(processorFunctions.get(np.name));
		}
		r.addFunctionList(list);
		return r;
	}

	protected FunctionListList getNewLayerProcessorFunctionListList(SortedMap<String,Function> processorFunctions) {
		FunctionListList r = new FunctionListList();
		for (Entry<Integer,List<NetworkProcessor>> entry: layerProcessors.entrySet()) {
			FunctionList list = new FunctionList();
			for (NetworkProcessor np: entry.getValue()) {
				list.addFunction(processorFunctions.get(np.name));
			}
			r.addFunctionList(list);
		}
		return r;
	}

	protected SortedMap<String,Function> getProcessorResetFunctions(int layer, String name) {
		SortedMap<String,Function> r = new TreeMap<String,Function>();
		List<NetworkProcessor> nps = getNetworkProcessors(layer, name);
		for (NetworkProcessor np: nps) {
			Function function = new Function() {
				@Override
				protected Object exec() {
					np.processor.reset();
					return true;
				}
			};
			function.param2 = np;
			r.put(np.name, function);
		}
		return r;
	}

	protected SortedMap<String,Function> getProcessorFunctionsForNetworkIO(NetworkIO io) {
		SortedMap<String,Function> r = new TreeMap<String,Function>();
		for (NetworkProcessor np: processors.values()) {
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
		boolean complete = true;
		Object inputValue = null;
		Sdr[] inputs = new Sdr[toProcessor.inputLinks.size()];
		for (LinkConfig link: toProcessor.inputLinks) {
			if (inputNames.contains(link.fromName)) {
				Object value = io.inputs.get(link.fromName);
				if (value instanceof Sdr) {
					inputs[link.toInput] = (Sdr)value;
				} else {
					inputValue = io.inputs.get(link.fromName);
				}
			} else {
				NetworkIO sourceIO = io;
				ProcessorIO sourcePIO = null;
				NetworkProcessor fromProcessor = processors.get(link.fromName);
				if (fromProcessor.layer>=toProcessor.layer) {
					sourceIO = previousIO;
				}
				if (sourceIO!=null) {
					sourcePIO = sourceIO.getProcessorIO(link.fromName);
				}
				if (sourcePIO!=null) {
					if (link.fromOutput < sourcePIO.outputs.size()) {
						Sdr sdr = sourcePIO.outputs.get(link.fromOutput);
						inputs[link.toInput] = sdr;
					} else {
						complete = false;
					}
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
	
	protected List<NetworkProcessor> getNetworkProcessors(int layer, String name) {
		List<NetworkProcessor> r = new ArrayList<NetworkProcessor>();
		for (NetworkProcessor np: processors.values()) {
			if ((layer<=ALL_LAYERS || np.layer == layer) &&
				(name.equals(ALL_PROCESSORS) 
						|| np.name.equals(name))
				) {
				r.add(np);
			}
		}
		return r;
	}
}
