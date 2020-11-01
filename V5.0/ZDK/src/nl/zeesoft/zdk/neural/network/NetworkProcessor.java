package nl.zeesoft.zdk.neural.network;

import nl.zeesoft.zdk.neural.processors.Processor;
import nl.zeesoft.zdk.neural.processors.SDRProcessor;
import nl.zeesoft.zdk.thread.CodeRunnerList;

public class NetworkProcessor extends Processor {
	public NetworkProcessor(String name, SDRProcessor processor, int threads) {
		super(name, processor, threads);
	}
	
	protected void resetConnections(CodeRunnerList runnerList) {
		super.resetConnections(runnerList);
	}
	
	protected void resetState(CodeRunnerList runnerList) {
		super.resetState(runnerList);
	}
}
