package nl.zeesoft.zdk.neural.network;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.function.FunctionList;
import nl.zeesoft.zdk.function.FunctionListList;
import nl.zeesoft.zdk.neural.processor.LearningProcessor;

public class NetworkProcessorResetter extends AbstractNetworkProcessor {
	protected NetworkProcessors		processors	= null;
	
	protected NetworkProcessorResetter(NetworkProcessors processors) {
		this.processors = processors;
	}

	protected FunctionListList getResetFunctionForProcessors(int layer, String name) {
		return getNewProcessorFunctionListList(getProcessorResetFunctions(layer,name));
	}

	protected FunctionListList getNewProcessorFunctionListList(SortedMap<String,Function> processorFunctions) {
		FunctionListList r = new FunctionListList();
		FunctionList list = new FunctionList();
		for (NetworkProcessor np: processors.getProcessors()) {
			list.addFunction(processorFunctions.get(np.name));
		}
		r.addFunctionList(list);
		return r;
	}

	protected SortedMap<String,Function> getProcessorResetFunctions(int layer, String name) {
		SortedMap<String,Function> r = new TreeMap<String,Function>();
		List<NetworkProcessor> nps = processors.getProcessors(layer, name);
		for (NetworkProcessor np: nps) {
			Function function = new Function() {
				@Override
				protected Object exec() {
					NetworkProcessor np = (NetworkProcessor) param1;
					if (np.processor instanceof LearningProcessor) {
						((LearningProcessor) np.processor).reset();
					}
					return true;
				}
			};
			function.param1 = np;
			r.put(np.name, function);
		}
		return r;
	}
}
