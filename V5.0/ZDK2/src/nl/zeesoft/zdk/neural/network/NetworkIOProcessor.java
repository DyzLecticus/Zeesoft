package nl.zeesoft.zdk.neural.network;

import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.function.FunctionList;
import nl.zeesoft.zdk.function.FunctionListList;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.network.config.LinkConfig;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;

public class NetworkIOProcessor {
	protected List<String>		inputNames	= null;
	protected NetworkProcessors	processors	= null;
	
	protected NetworkIOProcessor(List<String> inputNames, NetworkProcessors processors) {
		this.inputNames = inputNames;
		this.processors = processors;
	}

	protected FunctionListList getProcessFunctionForNetworkIO(NetworkIO io, NetworkIO previousIO) {
		return getNewLayerProcessorFunctionListList(getProcessorFunctionsForNetworkIO(io, previousIO));
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

	protected SortedMap<String,Function> getProcessorFunctionsForNetworkIO(NetworkIO io, NetworkIO previousIO) {
		SortedMap<String,Function> r = new TreeMap<String,Function>();
		for (NetworkProcessor np: processors.getProcessors()) {
			Function function = new Function() {
				@Override
				protected Object exec() {
					return processNetworkIOForProcessor((NetworkIO) param1, (NetworkProcessor) param2, previousIO);
				}
			};
			function.param1 = io;
			function.param2 = np;
			r.put(np.name, function);
		}
		return r;
	}
	
	protected boolean processNetworkIOForProcessor(NetworkIO io, NetworkProcessor toProcessor, NetworkIO previousIO) {
		Sdr[] inputs = new Sdr[toProcessor.inputLinks.size()];
		boolean complete = addInputsForProcessor(inputs, io, previousIO, toProcessor);
		Object inputValue = getInputForProcessor(inputs, io, toProcessor);
		ProcessorIO pio = new ProcessorIO();
		if (complete) {
			addInputsToList(inputs, pio.inputs);
		}
		pio.inputValue = inputValue;
		toProcessor.processor.processIO(pio);
		io.addProcessorIO(toProcessor.name,pio);
		return pio.error.length() == 0;
	}
	
	protected boolean addInputsForProcessor(
		Sdr[] inputs, NetworkIO io, NetworkIO previousIO, NetworkProcessor toProcessor
		) {
		boolean complete = true;
		for (LinkConfig link: toProcessor.inputLinks) {
			NetworkProcessor fromProcessor = processors.getProcessor(link.fromName);
			if (fromProcessor!=null) {
				ProcessorIO sourcePIO = getSourceProcesorIO(
					io, previousIO, fromProcessor.layer, toProcessor.layer, link.fromName
				);
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

	protected ProcessorIO getSourceProcesorIO(
		NetworkIO io, NetworkIO previousIO, int fromLayer, int toLayer, String fromName
		) {
		ProcessorIO r = null;
		NetworkIO sourceIO = io;
		if (fromLayer>=toLayer) {
			sourceIO = previousIO;
		}
		if (sourceIO!=null) {
			r = sourceIO.getProcessorIO(fromName);
		}
		return r;
	}
	
	protected Object getInputForProcessor(Sdr[] inputs, NetworkIO io, NetworkProcessor toProcessor) {
		Object r = null;
		for (LinkConfig link: toProcessor.inputLinks) {
			if (inputNames.contains(link.fromName)) {
				Object value = io.getInput(link.fromName);
				if (value instanceof Sdr) {
					inputs[link.toInput] = (Sdr)value;
				} else {
					r = io.inputs.get(link.fromName);
				}
			}
		}
		return r;
	}
	
	protected void addInputsToList(Sdr[] inputs, List<Sdr> list) {
		for (int i = 0; i < inputs.length; i++) {
			if (inputs[i]!=null) {
				list.add(inputs[i]);
			}
		}
	}
}
