package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;

public class MockNetworkIO extends NetworkIO {
	public void addProcessorIO(String name, ProcessorIO pio) {
		processorIO.put(name, pio);
	}
	
	public void addError(String error) {
		errors.add(error);
	}
}
