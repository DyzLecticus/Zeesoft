package nl.zeesoft.zdk.neural.network;

import java.util.List;

import nl.zeesoft.zdk.function.Executor;
import nl.zeesoft.zdk.function.ExecutorTask;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.function.FunctionList;
import nl.zeesoft.zdk.function.FunctionListList;
import nl.zeesoft.zdk.neural.network.config.ProcessorConfig;

public class NetworkProcessorInitializer extends AbstractNetworkProcessor {
	protected Executor				executor	= null;
	protected NetworkProcessors		processors	= null;

	NetworkProcessorInitializer(Executor executor, NetworkProcessors processors) {
		this.executor = executor;
		this.processors = processors;
	}
	
	protected boolean initialize(Object caller, List<ProcessorConfig> processorConfigs, int timeoutMs) {
		FunctionListList fll = getInitializeProcessorFunctionListList(processorConfigs);
		ExecutorTask task = executor.execute(caller, fll, timeoutMs);
		if (task!=null) {
			processors.processors.clear();
			processors.layerProcessors.clear();
			for (Object obj: task.getReturnValues()) {
				processors.addProcessor((NetworkProcessor) obj);
			}
		}
		return task!=null;
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
}
