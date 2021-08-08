package nl.zeesoft.zdk.test.neural.network;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.neural.network.config.LinkConfig;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;
import nl.zeesoft.zdk.neural.network.config.ProcessorConfig;
import nl.zeesoft.zdk.neural.network.config.type.ClassifierConfig;
import nl.zeesoft.zdk.neural.network.config.type.MergerConfig;
import nl.zeesoft.zdk.neural.network.config.type.ScalarEncoderConfig;
import nl.zeesoft.zdk.neural.network.config.type.SpatialPoolerConfig;
import nl.zeesoft.zdk.neural.network.config.type.TemporalMemoryConfig;

public class TestNetworkConfig {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		assert new ScalarEncoderConfig() != null;
		assert new SpatialPoolerConfig() != null;
		assert new TemporalMemoryConfig() != null;
		assert new ClassifierConfig() != null;
		assert new MergerConfig() != null;
		assert new LinkConfig() != null;
		
		NetworkConfig config = getNewNetworkConfig();
		assert config.getInputNames().size() == 1;
		assert config.getProcessorConfigs().size() == 5;
		
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
		LinkConfig save = pc.inputLinks.get(0);
		pc.inputLinks.clear();
		config.addLink("TestTemporalMemory", 1, "TestSpatialPooler", 0);
		err = config.test();
		assert err.toString().startsWith("TestSpatialPooler: link from TestTemporalMemory/1 output volume is greater than input volume");

		pc.inputLinks.clear();
		err = config.test();
		assert err.toString().equals("TestSpatialPooler: Processor has no input link(s)");
		pc.inputLinks.add(save);
		
		config.addInput("Pizza");
		err = config.test();
		assert err.toString().equals("Pizza: Input is not used"); // Some would call it a crime
	}
	
	public static NetworkConfig getNewNetworkConfig() {
		Logger.setLoggerDebug(true);
		
		NetworkConfig config = new NetworkConfig();
		
		StringBuilder err = config.test();
		assert err.toString().equals(
			"A network must have at least one input\n" + 
			"A network must have at least one processor"
		);
		
		config.addInput("TestInput");
		assert config.getInputNames().size() == 1;
		assert config.getInputNames().contains("TestInput");
		
		ScalarEncoderConfig sec = config.addScalarEncoder("TestEncoder");
		sec.encoder.maxValue = 20;
		assert sec != null;
		assert sec.layer == 0;
		assert config.getProcessorConfigs().size() == 1;
		config.addLink("TestInput", "TestEncoder");
		assert sec.inputLinks.size() == 1;
		
		config.addInput("TestEncoder");
		assert config.getInputNames().size() == 1;
		config.addScalarEncoder("TestInput");
		assert config.getProcessorConfigs().size() == 1;
		
		SpatialPoolerConfig spc = config.addSpatialPooler("TestSpatialPooler");
		assert spc != null;
		assert spc.layer == 1;
		assert config.getProcessorConfigs().size() == 2;
		config.addLink("TestEncoder", "TestSpatialPooler");
		assert spc.inputLinks.size() == 1;
		
		TemporalMemoryConfig tmc = config.addTemporalMemory("TestTemporalMemory");
		assert tmc != null;
		assert tmc.layer == 2;
		assert config.getProcessorConfigs().size() == 3;
		config.addLink("TestSpatialPooler", "TestTemporalMemory");
		assert tmc.inputLinks.size() == 1;
		
		ClassifierConfig clc = config.addClassifier("TestClassifier");
		assert clc != null;
		assert clc.layer == 3;
		assert config.getProcessorConfigs().size() == 4;
		config.addLink("TestTemporalMemory", "TestClassifier");
		config.addLink("TestInput", 0, "TestClassifier", 1);
		assert clc.inputLinks.size() == 2;
		
		MergerConfig mrc = config.addMerger("TestMerger");
		assert mrc != null;
		assert mrc.layer == 4;
		assert config.getProcessorConfigs().size() == 5;
		config.addLink("TestClassifier", "TestMerger");
		assert mrc.inputLinks.size() == 1;
		
		config.addInput("");
		assert config.getInputNames().size() == 1;
		
		config.addInput("TestInput");
		assert config.getInputNames().size() == 1;
		
		config.addClassifier("");
		assert config.getProcessorConfigs().size() == 5;
		
		config.addClassifier("TestClassifier");
		assert config.getProcessorConfigs().size() == 5;
		
		String error = config.addLink("","TestMerger");
		assert mrc.inputLinks.size() == 1;
		assert error.length() > 0;
		
		error = config.addLink("Pizza","TestMerger");
		assert mrc.inputLinks.size() == 1;
		assert error.length() > 0;
		
		error = config.addLink("TestClassifier","Pizza");
		assert error.length() > 0;
		
		config.addLink("TestClassifier", "TestMerger");
		assert mrc.inputLinks.size() == 1;
		
		config.addLink("TestClassifier", 0, "TestMerger", 1);
		assert mrc.inputLinks.size() == 2;
		mrc.inputLinks.remove(1);
		
		assert config.getProcessorConfigs("ory").size() == 1;
		assert config.getProcessorConfigs("er").size() == 4;
		
		err = config.test();
		assert err.length() == 0;
		
		return config;
	}
}
