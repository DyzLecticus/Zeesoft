package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.function.FunctionList;
import nl.zeesoft.zdk.function.FunctionListList;
import nl.zeesoft.zdk.neural.processor.Processor;

public class Network {
	protected List<String>							inputNames			= null;
	protected SortedMap<String,Processor>			processors			= new TreeMap<String,Processor>();
	protected SortedMap<String,List<LinkConfig>>	processorLinks		= new TreeMap<String,List<LinkConfig>>();
	protected SortedMap<Integer,List<Processor>>	layerProcessors		= new TreeMap<Integer,List<Processor>>();
	
	protected NetworkIO								previousIO			= null;
	
	public void initialize(NetworkConfig config) {
		inputNames = new ArrayList<String>(config.inputNames);
		for (ProcessorConfig pc: config.processorConfigs) {
			Processor processor = pc.getNewInstance();
			processors.put(pc.name, processor);
			
			List<LinkConfig> links = new ArrayList<LinkConfig>();
			for (LinkConfig link: pc.inputLinks) {
				links.add(link.copy());
			}
			processorLinks.put(pc.name, links);
			
			List<Processor> lps = layerProcessors.get(pc.layer);
			if (lps==null) {
				lps = new ArrayList<Processor>();
				layerProcessors.put(pc.layer, lps);
			}
			lps.add(processor);
		}
	}
	
	public boolean isInitialized() {
		return inputNames!=null;
	}
	
	public void processIO(NetworkIO io) {
		if (isInitialized(io) && isValidIO(io)) {
			FunctionListList fll = new FunctionListList();
			for (Entry<Integer,List<Processor>> entry: layerProcessors.entrySet()) {
				FunctionList list = new FunctionList();
				fll.functionLists.add(list);
				for (Processor processor: entry.getValue()) {
					Function function = new Function() {
						@Override
						protected Object exec() {
							Processor processor = (Processor) param1;
							
							return true;
						}
					};
					function.param1 = processor;
					list.functions.add(function);
				}
			}
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
	
	public List<Processor> getProcessorsForLayer(int layer) {
		List<Processor> r = layerProcessors.get(layer);
		if (r==null) {
			r = new ArrayList<Processor>();
		} else {
			r = new ArrayList<Processor>(r);
		}
		return r;
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
}
