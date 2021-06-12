package nl.zeesoft.zdk.neural.network;

import nl.zeesoft.zdk.function.ExecutorTask;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.function.FunctionList;
import nl.zeesoft.zdk.function.FunctionListList;
import nl.zeesoft.zdk.json.JElem;
import nl.zeesoft.zdk.json.Json;
import nl.zeesoft.zdk.json.JsonConstructor;

public class NetworkJsonConstructor {
	private static String	PROCESSORS	= "networkProcessors";
	
	public Json fromNetwork(Object caller, Network network, int timeoutMs) {
		Json r = toJson(network);
		FunctionListList fll = getToJsonFunctionListList(network);
		ExecutorTask task = network.executor.execute(caller, fll, timeoutMs);
		processTaskResults(task, r.root.get(PROCESSORS));
		return r;
	}
	
	protected Json toJson(Network network) {
		Json r = JsonConstructor.fromObjectUseConvertors(network);
		JElem elem = new JElem();
		elem.key = PROCESSORS;
		elem.isArray = true;
		r.root.children.add(elem);
		return r;
	}
	
	protected FunctionListList getToJsonFunctionListList(Network network) {
		FunctionListList r = new FunctionListList();
		FunctionList fl = new FunctionList();
		for (NetworkProcessor processor: network.getProcessors(Network.ALL_LAYERS, Network.ALL_PROCESSORS)) {
			fl.addFunction(getToJsonFunctionForProcessor(processor));
		}
		r.addFunctionList(fl);
		return r;
	}
	
	protected Function getToJsonFunctionForProcessor(NetworkProcessor processor) {
		return new Function() {
			@Override
			protected Object exec() {
				return JsonConstructor.fromObjectUseConvertors(processor);
			}
		};
	}
	
	protected void processTaskResults(ExecutorTask task, JElem processors) {
		for (Object js: task.getReturnValues()) {
			processors.children.add(((Json)js).root);
		}
	}
}
