package nl.zeesoft.zdk.test.neural.network;

import java.util.List;
import java.util.TreeMap;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.model.CellStats;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.neural.network.NetworkIOAccuracy;
import nl.zeesoft.zdk.neural.network.NetworkIOAnalyzer;
import nl.zeesoft.zdk.neural.network.NetworkIOStats;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;
import nl.zeesoft.zdk.neural.processor.cl.Classification;
import nl.zeesoft.zdk.neural.processor.sp.SpatialPooler;
import nl.zeesoft.zdk.test.ZdkTests;

public class TestNetwork {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		Rand.reset(0);

		NetworkConfig config = TestNetworkConfig.getNewNetworkConfig();
		
		config.addInput("TestInput2");
		config.addMerger(4,"TestMerger2");
		
		config.addLink("TestMerger2", 0, "TestMerger", 1);
		config.addLink("TestInput2", 0, "TestMerger2", 0);
		
		assert config.test().length() == 0;
		
		Network network = new Network();
		assert !network.isInitialized();
		assert !network.reset();
		CellStats stats = network.getCellStats();
		assert stats.cells == 0;
		assert stats.proximalStats.segments == 0;
		assert stats.distalStats.segments == 0;
		assert stats.apicalStats.segments == 0;
		
		NetworkIO io = new NetworkIO();
		assert io.getInputNames().size() == 0;
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
		assert network.getProcessors("Merger").size() == 2;

		stats = network.getCellStats();
		assert stats.cells == 39168;
		assert stats.proximalStats.segments == 0;
		assert stats.distalStats.segments == 0;
		assert stats.apicalStats.segments == 0;
		
		SpatialPooler sp = (SpatialPooler) network.getProcessors("TestSpatialPooler").get(0).processor;
		assert sp.isLearn();
		network.setLearn(false);
		assert !sp.isLearn();
		network.setLearn(true);
		assert sp.isLearn();
		
		io = new NetworkIO("TestInput",0);
		assert io.getInputNames().size() == 1;
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
		assert errors.get(0).equals("Merger requires at least one input SDR");
		assert errors.get(1).equals("Classifier requires at least one input SDR");
		assert errors.get(2).equals("Merger requires at least one input SDR");
		assert errors.get(3).equals("TemporalMemory requires at least one input SDR");
		assert errors.get(4).equals("SpatialPooler connections are not initialized");
		assert network.getPreviousIO() == io;
		
		io = new NetworkIO("TestInput",new Sdr(100));
		io.addInput("TestInput2", "Pizza");
		network.processIO(io);
		errors = io.getErrors();
		assert errors.size() == 6;
		assert errors.get(3).toString().equals("ScalarEncoder requires an input value");
		
		assert network.reset();
		
		NetworkIOAnalyzer analyzer = new NetworkIOAnalyzer();
		assert analyzer.getNetworkIO().size() == 0;
		assert analyzer.getAverageStats().totalNs == 0;
		assert new NetworkIOAccuracy() != null;
		
		io = new NetworkIO("TestInput",0);
		io.addInput("TestInput2", new Sdr(100));
		network.processIO(io);
		assert io.getErrors().size() == 0;
		assert io.getProcessorIO("TestClassifier").outputValue != null;
		assert io.getProcessorIO("TestClassifier").outputValue instanceof Classification;
		Classification cl = (Classification) io.getProcessorIO("TestClassifier").outputValue;
		assert cl.valueCounts.size() == 0;

		NetworkIOStats ioStats = io.getStats();
		assert ioStats.totalNs > 0;
		assert ioStats.nsPerLayer.size() == network.getNumberOfLayers();
		long totalNs = ioStats.totalNs;
		analyzer.add(io);
		assert analyzer.getNetworkIO().size() == 1;

		io = new NetworkIO("TestInput",1);
		io.addInput("TestInput2", new Sdr(100));
		network.processIO(io);
		assert io.getErrors().size() == 0;
		assert io.getProcessorIO("TestClassifier").outputValue != null;
		assert io.getProcessorIO("TestClassifier").outputValue instanceof Classification;
		cl = (Classification) io.getProcessorIO("TestClassifier").outputValue;
		assert cl.valueCounts.size() == 1;
		
		ioStats = io.getStats();
		totalNs += ioStats.totalNs;
		analyzer.add(io);

		network.setNumberOfWorkers(2);
		network.setNumberOfWorkersForProcessors(2);
		
		io = new NetworkIO("TestInput",1);
		io.addInput("TestInput2", new Sdr(100));
		io.setTimeoutMs(0);
		network.processIO(io);
		assert io.getErrors().size() == 1;
		assert io.getErrors().get(0).equals("Processing network IO timed out after 0 ms");
		ZdkTests.sleep(10);

		stats = network.getCellStats();
		assert stats.cells == 39168;
		assert stats.proximalStats.segments == 2304;
		assert stats.proximalStats.synapses > 10000;
		assert stats.proximalStats.activeSynapses > 10000;
		assert stats.distalStats.segments > 25;
		assert stats.distalStats.synapses > 500;
		assert stats.distalStats.activeSynapses == 0;

		totalNs = totalNs / 2;
		analyzer.add(new NetworkIO());
		ioStats = analyzer.getAverageStats();
		assert ioStats.totalNs == totalNs;
		assert ioStats.nsPerLayer.size() == network.getNumberOfLayers();
		long layerNs = 0;
		for (Long ns: ioStats.nsPerLayer.values()) {
			layerNs += ns;
		}
		assert layerNs > (totalNs / 2);
		assert layerNs <= totalNs;
		
		ioStats = new NetworkIOStats();
		ioStats.totalNs = 1000000;
		assert ioStats.toString().equals("Total: 1.0 ms");
		ioStats.nsPerLayer = new TreeMap<Integer,Long>();
		ioStats.nsPerLayer.put(0, 1000000L);
		assert ioStats.toString().equals("Total: 1.0 ms\nLayer 1: 1.0 ms");
		
		float average = analyzer.getAccuracy().classifierAverages.get("TestClassifier");
		assert average == 0F;
		
		assert !network.reset(0);
		ZdkTests.sleep(100);
		assert !network.initialize(config,0);
		ZdkTests.sleep(100);

		network.setNumberOfWorkers(0);
		network.setNumberOfWorkersForProcessors(0);
				
		analyzer = new NetworkIOAnalyzer();
		analyzer.add(new NetworkIO());
		network = new Network();
		assert network.initialize(TestNetworkConfig.getNewNetworkConfig());
		assert network.reset();
		for (int i = 0; i < 16; i++) {
			io = new NetworkIO("TestInput",i % 2);
			network.processIO(io);
			if (i==5) {
				Classification cls = (Classification) io.getProcessorIO("TestClassifier").outputValue;
				cls.valueCounts.clear();
				cls.valueCounts.put(99, 1);
			}
			if (i==1) {
				io.getProcessorIO("TestClassifier").outputValue = null;
			}
			analyzer.add(io);
		}
		assert network.getPreviousIO().getProcessorIO("TestEncoder").inputs.size() == 0;
		NetworkIOAccuracy accuracy = analyzer.getAccuracy();
		assert accuracy != null;
		average = accuracy.classifierAverages.get("TestClassifier");
		assert accuracy.average > 0.5F;
		assert accuracy.average == average;
		assert accuracy.toString().length() >= 32;
		assert analyzer.getAccuracy(0).average == accuracy.average;
		
		assert analyzer.getNetworkIO().size() == 17;
		assert analyzer.getAverageStats(3).recorded == 17;
		assert analyzer.getAverageStats(3).totalNs > 0;
		assert analyzer.getAverageStats(-1).totalNs > 0;
		assert analyzer.getAverageStats(3).totalNs != analyzer.getAverageStats(-1).totalNs;
	}
}
