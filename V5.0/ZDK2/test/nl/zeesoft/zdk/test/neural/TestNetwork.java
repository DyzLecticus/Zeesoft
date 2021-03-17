package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkConfig;
import nl.zeesoft.zdk.neural.network.NetworkIO;

public class TestNetwork {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		NetworkConfig config = TestNetworkConfig.getNewNetworkConfig();
		config.addInput("TestInput2");
		config.addMerger(4,"TestMerger2");
		
		Network network = new Network();
		assert !network.isInitialized();
		
		NetworkIO io = new NetworkIO();
		network.processIO(io);
		assert io.errors.size() == 1;
		assert io.errors.get(0).equals("Network is not initialized");
		assert network.getPreviousIO() == io;

		network.initialize(config);
		assert network.isInitialized();
		assert network.getInputNames().size() == 2;
		assert network.getProcessorNames().size() == 6;
		assert network.getProcessorsForLayer(0).size() == 1;
		assert network.getProcessorsForLayer(4).size() == 2;
		assert network.getProcessorsForLayer(100).size() == 0;
		
		io = new NetworkIO("TestInput",0);
		network.processIO(io);
		assert io.errors.size() == 1;
		assert io.errors.get(0).equals("Missing network input value for TestInput2");
		
		io = new NetworkIO("TestInput",0);
		io.inputs.put("TestInput2", null);
		network.processIO(io);
		assert io.errors.size() == 1;
		assert io.errors.get(0).equals("Missing network input value for TestInput2");
		
		io = new NetworkIO("TestInput",0);
		io.inputs.put("TestInput2", "Pizza");
		network.processIO(io);
		assert io.errors.size() == 0;
	}
}
