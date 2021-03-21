package nl.zeesoft.zdk.test.neural;

import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkConfig;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.neural.processor.cl.Classification;
import nl.zeesoft.zdk.test.AllTests;

public class TestNetwork {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		NetworkConfig config = TestNetworkConfig.getNewNetworkConfig();
		
		config.addInput("TestInput2");
		config.addMerger(4,"TestMerger2");
		
		config.addLink("TestMerger2", 0, "TestMerger", 1);
		config.addLink("TestInput2", 0, "TestMerger2", 0);
		
		assert config.test().length() == 0;
		
		Network network = new Network();
		assert !network.isInitialized();
		assert !network.reset();
		
		NetworkIO io = new NetworkIO();
		network.processIO(io);
		assert io.getErrors().size() == 1;
		assert io.getErrors().get(0).equals("Network is not initialized");

		network.initialize(config);
		assert network.isInitialized();
		assert network.getNumberOfLayers() == 5;
		assert network.getNumberOfProcessors() == 6;
		assert network.getWidth() == 2;
		assert network.getInputNames().size() == 2;
		assert network.getProcessorNames().size() == 6;
		assert network.getProcessors(0).size() == 1;
		assert network.getProcessors(4).size() == 2;
		assert network.getProcessors(100).size() == 0;
		assert network.getProcessors("Pizza").size() == 0;
		assert network.getProcessors("TestEncoder").size() == 1;
		assert network.getProcessors(1,"TestEncoder").size() == 0;
		
		io = new NetworkIO("TestInput",0);
		network.processIO(io);
		assert io.getErrors().size() == 1;
		assert io.getErrors().get(0).equals("Missing network input value for TestInput2");
		
		io = new NetworkIO("TestInput",0);
		io.addInput("TestInput2", null);
		network.processIO(io);
		assert io.getErrors().size() == 1;
		assert io.getErrors().get(0).equals("Missing network input value for TestInput2");
		
		io = new NetworkIO("TestInput",0);
		io.addInput("TestInput2", "Pizza");
		network.processIO(io);
		List<String> errors = io.getErrors();
		assert errors.size() == 5;
		assert errors.get(0).equals("Classifier requires at least one input SDR");
		assert errors.get(1).equals("Merger requires at least one input SDR");
		assert errors.get(2).equals("Merger requires at least one input SDR");
		assert errors.get(3).equals("SpatialPooler connections are not initialized");
		assert errors.get(4).equals("TemporalMemory requires at least one input SDR");
		assert network.getPreviousIO() == io;
		
		io = new NetworkIO("TestInput",new Sdr(100));
		io.addInput("TestInput2", "Pizza");
		network.processIO(io);
		errors = io.getErrors();
		assert errors.size() == 6;
		assert errors.get(1).toString().equals("ScalarEncoder requires an input value");
		
		assert network.reset();
		
		io = new NetworkIO("TestInput",0);
		io.addInput("TestInput2", new Sdr(100));
		network.processIO(io);
		assert io.getErrors().size() == 0;
		assert io.getProcessorIO("TestClassifier").outputValue != null;
		assert io.getProcessorIO("TestClassifier").outputValue instanceof Classification;
		Classification cl = (Classification) io.getProcessorIO("TestClassifier").outputValue;
		assert cl.valueCounts.size() == 0;

		io = new NetworkIO("TestInput",1);
		io.addInput("TestInput2", new Sdr(100));
		network.processIO(io);
		assert io.getErrors().size() == 0;
		assert io.getProcessorIO("TestClassifier").outputValue != null;
		assert io.getProcessorIO("TestClassifier").outputValue instanceof Classification;
		cl = (Classification) io.getProcessorIO("TestClassifier").outputValue;
		assert cl.valueCounts.size() == 0;

		network.setNumberOfWorkers(2);
		io = new NetworkIO("TestInput",1);
		io.addInput("TestInput2", new Sdr(100));
		io.setTimeoutMs(0);
		network.processIO(io);
		assert io.getErrors().size() == 1;
		assert io.getErrors().get(0).equals("Processing network IO timed out after 0 ms");

		AllTests.sleep(50);
		assert !network.reset(0);

		AllTests.sleep(50);
		assert !network.initialize(config,0);
		
		network.setNumberOfWorkers(0);
	}
}
