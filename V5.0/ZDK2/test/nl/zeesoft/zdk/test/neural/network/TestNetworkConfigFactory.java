package nl.zeesoft.zdk.test.neural.network;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.encoder.ScalarSdrEncoderTester;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;
import nl.zeesoft.zdk.neural.network.config.NetworkConfigFactory;
import nl.zeesoft.zdk.neural.processor.InputOutputConfig;

public class TestNetworkConfigFactory {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		NetworkConfigFactory factory = new NetworkConfigFactory();
		NetworkConfig config = factory.getSimpleConfig("Input1");
		
		assert config.test().length() == 0;
		assert config.getInputNames().size() == 1;
		assert config.getInputNames().contains("Input1");
		assert config.getProcessorConfigs().size() == 4;
		assert config.getProcessorConfig("ScalarEncoder1") != null;
		assert config.getProcessorConfig("SpatialPooler1") != null;
		assert config.getProcessorConfig("TemporalMemory1") != null;
		assert config.getProcessorConfig("Classifier1") != null;
		
		factory.addInputColumn(config, 2, "Input2");
		assert config.test().length() == 0;
		assert config.getInputNames().size() == 2;
		assert config.getInputNames().contains("Input2");
		assert config.getProcessorConfigs().size() == 8;
		assert config.getProcessorConfig("ScalarEncoder2") != null;
		assert config.getProcessorConfig("SpatialPooler2") != null;
		assert config.getProcessorConfig("TemporalMemory2") != null;
		assert config.getProcessorConfig("Classifier2") != null;

		ScalarSdrEncoderTester tester = new ScalarSdrEncoderTester();
		
		factory.setMediumScale();
		assert tester.testMinimalOverlap(factory.encoder).length()==0;
		testSimpleConfig(factory, 144, 4608, new Size(24,24,8));

		factory.setSmallScale();
		assert tester.testMinimalOverlap(factory.encoder).length()==0;
		testSimpleConfig(factory, 100, 1024, new Size(16,16,4));
	}
	
	public static void testSimpleConfig(NetworkConfigFactory factory, int maxSpVolume, int maxTmVolume, Size size) {
		NetworkConfig config = factory.getSimpleConfig("Input1", "Input2");
		assert config.test().length() == 0;
		assert config.getInputNames().size() == 2;
		assert config.getProcessorConfigs().size() == 8;
		
		InputOutputConfig ioCfg = config.getProcessorConfig("SpatialPooler1").getInputOutputConfig();
		assert ioCfg.inputs.get(0).maxVolume == maxSpVolume;
		assert ioCfg.outputs.get(0).size.equals(new Size(size.x,size.y));
		
		ioCfg = config.getProcessorConfig("TemporalMemory1").getInputOutputConfig();
		assert ioCfg.inputs.get(1).maxVolume == maxTmVolume;
		assert ioCfg.outputs.get(0).size.equals(new Size(size.x,size.y,size.z));
		
		assert config.getProcessorConfig("TemporalMemory1").inputLinks.size() == 1;
		factory.addTemporalMemoryMerger(config,"o");
		assert config.getProcessorConfig("Merger").inputLinks.size() == 2;
		assert config.getProcessorConfig("TemporalMemory1").inputLinks.size() == 2;
		
		config = factory.getApicalFeedbackConfig("Input1", "Input2");
		assert config.test().length() == 0;
		assert config.getInputNames().size() == 2;
		assert config.getProcessorConfigs().size() == 9;
	}
}
