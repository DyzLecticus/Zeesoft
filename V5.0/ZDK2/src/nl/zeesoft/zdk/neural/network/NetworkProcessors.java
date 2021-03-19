package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.function.FunctionList;
import nl.zeesoft.zdk.function.FunctionListList;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;

public class NetworkProcessors extends AbstractNetworkProcessor {
	protected SortedMap<String,NetworkProcessor>		processors			= new TreeMap<String,NetworkProcessor>();
	protected SortedMap<Integer,List<NetworkProcessor>>	layerProcessors		= new TreeMap<Integer,List<NetworkProcessor>>();
	
	protected void initialize(List<ProcessorConfig> processorConfigs) {
		for (ProcessorConfig pc: processorConfigs) {
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

	protected SortedMap<String,Function> getProcessorResetFunctions(int layer, String name) {
		SortedMap<String,Function> r = new TreeMap<String,Function>();
		List<NetworkProcessor> nps = getProcessors(layer, name);
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
