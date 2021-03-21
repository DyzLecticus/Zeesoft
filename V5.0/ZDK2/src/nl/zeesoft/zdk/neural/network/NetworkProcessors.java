package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.function.Executor;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.function.FunctionList;
import nl.zeesoft.zdk.function.FunctionListList;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;

public class NetworkProcessors extends AbstractNetworkProcessor {
	protected Executor									executor			= null;
	
	protected SortedMap<String,NetworkProcessor>		processors			= new TreeMap<String,NetworkProcessor>();
	protected SortedMap<Integer,List<NetworkProcessor>>	layerProcessors		= new TreeMap<Integer,List<NetworkProcessor>>();
	
	public NetworkProcessors(Executor executor) {
		this.executor = executor;
	}
	
	protected boolean initialize(Object caller, List<ProcessorConfig> processorConfigs, int timeoutMs) {
		FunctionListList fll = getInitializeProcessorFunctionListList(processorConfigs);
		List<Object> nps = executor.execute(caller, fll, timeoutMs);
		if (nps!=null) {
			for (Object obj: nps) {
				NetworkProcessor np = (NetworkProcessor) obj;
				processors.put(np.name, np);
				List<NetworkProcessor> lps = layerProcessors.get(np.layer);
				if (lps==null) {
					lps = new ArrayList<NetworkProcessor>();
					layerProcessors.put(np.layer, lps);
				}
				lps.add(np);
			}
		}
		return nps!=null;
	}
	
	protected int getNumberOfLayers() {
		return layerProcessors.size();
	}
	
	protected int getNumberOfProcessors() {
		return processors.size();
	}
	
	protected int getWidth() {
		int r = 0;
		for (List<NetworkProcessor> lps: layerProcessors.values()) {
			if (lps.size()>r) {
				r = lps.size();
			}
		}
		return r;
	}
	
	protected List<String> getProcessorNames() {
		return new ArrayList<String>(processors.keySet());
	}
	
	protected List<NetworkProcessor> getProcessors() {
		return getProcessors(Network.ALL_LAYERS, Network.ALL_PROCESSORS);
	}
	
	protected List<NetworkProcessor> getProcessors(int layer, String name) {
		List<NetworkProcessor> r = new ArrayList<NetworkProcessor>();
		for (NetworkProcessor np: processors.values()) {
			if ((layer<=Network.ALL_LAYERS || np.layer == layer) &&
				(name.equals(Network.ALL_PROCESSORS) 
						|| np.name.equals(name))
				) {
				r.add(np);
			}
		}
		return r;
	}
	
	public SortedMap<Integer,List<NetworkProcessor>> getLayerProcessors() {
		return new TreeMap<Integer,List<NetworkProcessor>>(layerProcessors);
	}

	protected FunctionListList getInitializeProcessorFunctionListList(List<ProcessorConfig> processorConfigs) {
		FunctionListList r = new FunctionListList();
		FunctionList list = new FunctionList();
		for (ProcessorConfig pc: processorConfigs) {
			list.addFunction(getInitializeProcessorFunction(pc));
		}
		r.addFunctionList(list);
		return r;
	}

	protected FunctionListList getResetFunctionForProcessors(int layer, String name) {
		return getNewProcessorFunctionListList(getProcessorResetFunctions(layer,name));
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

	protected Function getInitializeProcessorFunction(ProcessorConfig pc) {
		Function r = new Function() {
			@Override
			protected Object exec() {
				return new NetworkProcessor((ProcessorConfig)param1);
			}
		};
		r.param1 = pc;
		return r;
	}

	protected SortedMap<String,Function> getProcessorResetFunctions(int layer, String name) {
		SortedMap<String,Function> r = new TreeMap<String,Function>();
		List<NetworkProcessor> nps = getProcessors(layer, name);
		for (NetworkProcessor np: nps) {
			Function function = new Function() {
				@Override
				protected Object exec() {
					((NetworkProcessor)param1).processor.reset();
					return true;
				}
			};
			function.param1 = np;
			r.put(np.name, function);
		}
		return r;
	}
	
	protected boolean addInputsForProcessor(Sdr[] inputs, NetworkIO io, NetworkIO previousIO, NetworkProcessor toProcessor) {
		boolean complete = true;
		for (LinkConfig link: toProcessor.inputLinks) {
			NetworkProcessor fromProcessor = processors.get(link.fromName);
			if (fromProcessor!=null) {
				NetworkIO sourceIO = io;
				ProcessorIO sourcePIO = null;
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
		return complete;
	}
}
