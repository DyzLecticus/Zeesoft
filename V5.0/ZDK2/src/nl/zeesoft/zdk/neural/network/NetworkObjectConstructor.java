package nl.zeesoft.zdk.neural.network;

import nl.zeesoft.zdk.function.ExecutorTask;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.function.FunctionList;
import nl.zeesoft.zdk.function.FunctionListList;
import nl.zeesoft.zdk.json.JElem;
import nl.zeesoft.zdk.json.Json;
import nl.zeesoft.zdk.json.ObjectConstructor;

public class NetworkObjectConstructor {
	public Network fromJson(Object caller, Json json, int workers, int timeoutMs) {
		Network r = (Network) ObjectConstructor.fromJson(json);
		r.setNumberOfWorkers(workers);
		FunctionListList fll = getToNetworkFunctionListList(json);
		ExecutorTask task = r.executor.execute(caller, fll, timeoutMs);
		processTaskResults(task, r);
		return r;
	}
		
	protected FunctionListList getToNetworkFunctionListList(Json json) {
		FunctionListList r = new FunctionListList();
		JElem processors = json.root.get(NetworkJsonConstructor.NETWORK_PROCESSORS);
		FunctionList fl = new FunctionList();
		for (JElem processor: processors.children) {
			fl.addFunction(getToNetworkFunctionForProcessor(processor));
		}
		r.addFunctionList(fl);
		return r;
	}
	
	protected Function getToNetworkFunctionForProcessor(JElem processor) {
		return new Function() {
			@Override
			protected Object exec() {
				Json json = new Json();
				json.root = processor;
				Object obj = ObjectConstructor.fromJson(json);
				return obj;
			}
		};
	}
	
	protected void processTaskResults(ExecutorTask task, Network network) {
		for (Object obj: task.getReturnValues()) {
			network.processors.addProcessor((NetworkProcessor) obj);
		}
	}
}
