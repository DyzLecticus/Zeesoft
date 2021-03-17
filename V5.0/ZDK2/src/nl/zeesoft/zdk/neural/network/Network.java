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
	
	public void processIO(NetworkIO io) {
		if (isInitialized(io) && isValidIO(io)) {
			FunctionListList fll = new FunctionListList();
			for (Entry<Integer,List<NetworkProcessor>> entry: layerProcessors.entrySet()) {
				FunctionList list = new FunctionList();
				fll.functionLists.add(list);
				for (NetworkProcessor np: entry.getValue()) {
					Function function = new Function() {
						@Override
						protected Object exec() {
							NetworkProcessor toProcessor = (NetworkProcessor) param1;
							
							boolean complete = true;
							Object inputValue = null;
							Sdr[] inputs = new Sdr[toProcessor.inputLinks.size()];
							for (LinkConfig link: toProcessor.inputLinks) {
								if (inputNames.contains(link.fromName)) {
									Object value = io.inputs.get(link.fromName);
									if (value instanceof Sdr) {
										inputs[0] = (Sdr)value;
									} else {
										inputValue = io.inputs.get(link.fromName);
									}
								} else {
									NetworkIO sourceIO = io;
									NetworkProcessor fromProcessor = processors.get(link.fromName);
									if (fromProcessor.layer>=toProcessor.layer) {
										sourceIO = previousIO;
									}
									if (sourceIO!=null) {
										ProcessorIO sourcePIO = sourceIO.getProcessorIO(link.fromName);
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
					};
					function.param1 = np;
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
	
	public List<NetworkProcessor> getProcessorsForLayer(int layer) {
		List<NetworkProcessor> r = layerProcessors.get(layer);
		if (r==null) {
			r = new ArrayList<NetworkProcessor>();
		} else {
			r = new ArrayList<NetworkProcessor>(r);
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
