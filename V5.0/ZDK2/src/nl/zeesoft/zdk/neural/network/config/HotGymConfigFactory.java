package nl.zeesoft.zdk.neural.network.config;

import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.network.config.type.DateTimeEncoderConfig;
import nl.zeesoft.zdk.neural.network.config.type.MergerConfig;
import nl.zeesoft.zdk.neural.network.config.type.ScalarEncoderConfig;
import nl.zeesoft.zdk.neural.network.config.type.SpatialPoolerConfig;
import nl.zeesoft.zdk.neural.processor.de.DateTimeEncoder;
import nl.zeesoft.zdk.neural.processor.se.ScalarEncoder;

public class HotGymConfigFactory {
	public static NetworkConfig getHotGymNetworkConfig() {
		NetworkConfig r = new NetworkConfig();
		r.addInput("DateTime");
		r.addInput("Value");
		int length = addHotGymEncoders(r);
		addHotGymColumn(r, length);
		return r;
	}
	
	protected static int addHotGymEncoders(NetworkConfig config) {
		DateTimeEncoderConfig deConfig = config.addDateTimeEncoder("DateTimeEncoder");
		deConfig.encoder.includeDate = false;
		deConfig.encoder.includeMinute = false;
		deConfig.encoder.includeSecond = false;
		config.addLink("DateTime", "DateTimeEncoder");
		ScalarEncoderConfig seConfig = config.addScalarEncoder(0, "ValueEncoder");
		seConfig.encoder.onBits = 16;
		seConfig.encoder.encodeLength = 1016;
		seConfig.encoder.maxValue = 100F;
		seConfig.encoder.resolution = 0.1F;
		config.addLink("Value", "ValueEncoder");
		return deConfig.encoder.getEncodeLength() + seConfig.encoder.encodeLength;
	}
	
	protected static void addHotGymColumn(NetworkConfig config, int length) {
		MergerConfig mrConfig = config.addMerger(1, "Merger");
		mrConfig.config.concatenate = true;
		mrConfig.config.size = new Size(length);
		config.addLink("DateTimeEncoder", DateTimeEncoder.ENCODED_SENSOR_OUTPUT, "Merger", 0);
		config.addLink("ValueEncoder", ScalarEncoder.ENCODED_SENSOR_OUTPUT, "Merger", 1);
		
		SpatialPoolerConfig spc = config.addSpatialPooler(2, "SpatialPooler");
		spc.config.inputSize = new Size(length);
		config.addLink("Merger", "SpatialPooler");
		
		config.addTemporalMemory(3, "TemporalMemory");
		config.addLink("SpatialPooler", "TemporalMemory");
		
		config.addClassifier(4, "Classifier");
		config.addLink("TemporalMemory", "Classifier");
	}
}
