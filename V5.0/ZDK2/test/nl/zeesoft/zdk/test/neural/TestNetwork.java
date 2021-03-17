package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkConfig;

public class TestNetwork {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		NetworkConfig config = TestNetworkConfig.getNewNetworkConfig();
		config.addMerger(4,"TestMerger2");
		
		Network network = new Network();
		assert !network.isInitialized();
		network.initialize(config);
		assert network.isInitialized();
		assert network.getInputNames().size() == 1;
		assert network.getProcessorNames().size() == 6;
		assert network.getProcessorsForLayer(0).size() == 1;
		assert network.getProcessorsForLayer(4).size() == 2;
		assert network.getProcessorsForLayer(100).size() == 0;
	}
}
