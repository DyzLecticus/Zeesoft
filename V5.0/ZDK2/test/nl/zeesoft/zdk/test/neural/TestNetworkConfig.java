package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.neural.network.ClassifierConfig;
import nl.zeesoft.zdk.neural.network.MergerConfig;
import nl.zeesoft.zdk.neural.network.NetworkConfig;
import nl.zeesoft.zdk.neural.network.ProcessorConfig;
import nl.zeesoft.zdk.neural.network.ScalarEncoderConfig;
import nl.zeesoft.zdk.neural.network.SpatialPoolerConfig;
import nl.zeesoft.zdk.neural.network.TemporalMemoryConfig;

public class TestNetworkConfig {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		NetworkConfig config = getNewNetworkConfig();
		assert config.getNumberOfInputs() == 1;
		assert config.getNumberOfProcessors() == 5;
		
		ProcessorConfig pc = config.getProcessorConfig("TestSpatialPooler");
		pc.inputLinks.get(0).toInput = 2;
		StringBuilder err = config.test();
		assert err.toString().equals("TestSpatialPooler: link can not connect to input index 2");
		pc.inputLinks.get(0).toInput = 0;
		
		config.addLink("TestSpatialPooler", 1, "TestMerger", 1);
		err = config.test();
		assert err.toString().equals("TestMerger: link from TestSpatialPooler can not connect to output index 1");
		pc = config.getProcessorConfig("TestMerger");
		pc.inputLinks.remove(1);

		pc = config.getProcessorConfig("TestSpatialPooler");
		pc.inputLinks.clear();
		config.addLink("TestTemporalMemory", 1, "TestSpatialPooler", 0);
		err = config.test();
		assert err.toString().equals("TestSpatialPooler: link from TestTemporalMemory/1 output volume is greater than input volume  (2304 > 256)");
	}
	
	public static NetworkConfig getNewNetworkConfig() {
		Logger.setLoggerDebug(true);
		
		NetworkConfig config = new NetworkConfig();
		config.addInput("TestInput");
		assert config.getNumberOfInputs() == 1;
		
		ScalarEncoderConfig sec = config.addScalarEncoder("TestEncoder");
		assert sec != null;
		assert sec.layer == 0;
		assert config.getNumberOfProcessors() == 1;
		config.addLink("TestInput", "TestEncoder");
		assert sec.inputLinks.size() == 1;
		
		config.addInput("TestEncoder");
		assert config.getNumberOfInputs() == 1;
		config.addScalarEncoder("TestInput");
		assert config.getNumberOfProcessors() == 1;
		
		SpatialPoolerConfig spc = config.addSpatialPooler("TestSpatialPooler");
		assert spc != null;
		assert spc.layer == 1;
		assert config.getNumberOfProcessors() == 2;
		config.addLink("TestEncoder", "TestSpatialPooler");
		assert spc.inputLinks.size() == 1;
		
		TemporalMemoryConfig tmc = config.addTemporalMemory("TestTemporalMemory");
		assert tmc != null;
		assert tmc.layer == 2;
		assert config.getNumberOfProcessors() == 3;
		config.addLink("TestSpatialPooler", "TestTemporalMemory");
		assert tmc.inputLinks.size() == 1;
		
		ClassifierConfig clc = config.addClassifier("TestClassifier");
		assert clc != null;
		assert clc.layer == 3;
		assert config.getNumberOfProcessors() == 4;
		config.addLink("TestTemporalMemory", "TestClassifier");
		assert clc.inputLinks.size() == 1;
		
		MergerConfig mrc = config.addMerger("TestMerger");
		assert mrc != null;
		assert mrc.layer == 4;
		assert config.getNumberOfProcessors() == 5;
		config.addLink("TestClassifier", "TestMerger");
		assert mrc.inputLinks.size() == 1;
		
		config.addInput("");
		assert config.getNumberOfInputs() == 1;
		
		config.addInput("TestInput");
		assert config.getNumberOfInputs() == 1;
		
		config.addClassifier("");
		assert config.getNumberOfProcessors() == 5;
		
		config.addClassifier("TestClassifier");
		assert config.getNumberOfProcessors() == 5;
		
		config.addLink("","TestMerger");
		assert mrc.inputLinks.size() == 1;
		
		config.addLink("Pizza","TestMerger");
		assert mrc.inputLinks.size() == 1;
		
		config.addLink("TestClassifier","Pizza");
		assert mrc.inputLinks.size() == 1;
		
		config.addLink("TestClassifier", "TestMerger");
		assert mrc.inputLinks.size() == 1;
		
		config.addLink("TestClassifier", 0, "TestMerger", 1);
		assert mrc.inputLinks.size() == 2;
		mrc.inputLinks.remove(1);
		
		StringBuilder err = config.test();
		assert err.length() == 0;
		
		return config;
	}
}
