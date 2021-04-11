package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zdk.function.Executor;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.function.FunctionList;
import nl.zeesoft.zdk.function.FunctionListList;
import nl.zeesoft.zdk.neural.network.config.ProcessorConfig;

public class NetworkProcessorInitializer extends AbstractNetworkProcessor {
	protected Executor									executor			= null;
	protected SortedMap<String,NetworkProcessor>		processors			= null;
	protected SortedMap<Integer,List<NetworkProcessor>>	layerProcessors		= null;

	NetworkProcessorInitializer(
		Executor executor, 
		SortedMap<String,NetworkProcessor> processors,
		SortedMap<Integer,List<NetworkProcessor>> layerProcessors
		) {
		this.executor = executor;
		this.processors = processors;
		this.layerProcessors = layerProcessors;
	}
	
	protected boolean initialize(Object caller, List<ProcessorConfig> processorConfigs, int timeoutMs) {
		FunctionListList fll = getInitializeProcessorFunctionListList(processorConfigs);
		List<Object> nps = executor.execute(caller, fll, timeoutMs);
		if (nps!=null) {
			processors.clear();
			layerProcessors.clear();
			for (Object obj: nps) {
				addNetworkProcessor((NetworkProcessor) obj);
			}
		}
		return nps!=null;
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
	
	protected void addNetworkProcessor(NetworkProcessor np) {
		processors.put(np.name, np);
		List<NetworkProcessor> lps = layerProcessors.get(np.layer);
		if (lps==null) {
			lps = new ArrayList<NetworkProcessor>();
			layerProcessors.put(np.layer, lps);
		}
		lps.add(np);
	}
}
