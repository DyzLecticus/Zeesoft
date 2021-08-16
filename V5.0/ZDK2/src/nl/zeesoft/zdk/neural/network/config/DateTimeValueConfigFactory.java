package nl.zeesoft.zdk.neural.network.config;

import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.network.config.type.DateTimeEncoderConfig;
import nl.zeesoft.zdk.neural.network.config.type.MergerConfig;
import nl.zeesoft.zdk.neural.network.config.type.ScalarEncoderConfig;
import nl.zeesoft.zdk.neural.network.config.type.SpatialPoolerConfig;
import nl.zeesoft.zdk.neural.network.config.type.TemporalMemoryConfig;
import nl.zeesoft.zdk.neural.processor.cl.Classifier;
import nl.zeesoft.zdk.neural.processor.de.DateTimeEncoder;
import nl.zeesoft.zdk.neural.processor.se.ScalarEncoder;

public class DateTimeValueConfigFactory {
	public static NetworkConfig getNewDateTimeValueConfig() {
		NetworkConfig r = new NetworkConfig();
		r.addInput("DateTime");
		r.addInput("Value");
		int length = addHotGymEncoders(r);
		addHotGymMerger(r, length);
		addHotGymSpatialPooler(r, length);
		addHotGymTemporalMemory(r);
		addHotGymClassifier(r);
		return r;
	}

	protected static int addHotGymEncoders(NetworkConfig config) {
		return addHotGymDateTimeEncoder(config) + addHotGymValueEncoder(config);
	}

	protected static int addHotGymDateTimeEncoder(NetworkConfig config) {
		DateTimeEncoderConfig deConfig = config.addDateTimeEncoder("DateTimeEncoder");
		deConfig.encoder.setOnBitsPerEncoder(6);
		deConfig.encoder.includeDate = false;
		deConfig.encoder.includeMinute = false;
		deConfig.encoder.includeSecond = false;
		config.addLink("DateTime", "DateTimeEncoder");
		return deConfig.encoder.getEncodeLength();
	}

	protected static int addHotGymValueEncoder(NetworkConfig config) {
		ScalarEncoderConfig seConfig = config.addScalarEncoder(0, "ValueEncoder");
		seConfig.encoder.onBits = 18;
		seConfig.encoder.encodeLength = 1017;
		seConfig.encoder.maxValue = 100F;
		seConfig.encoder.resolution = 0.1F;
		config.addLink("Value", "ValueEncoder");
		return seConfig.encoder.encodeLength;
	}

	protected static void addHotGymMerger(NetworkConfig config, int length) {
		MergerConfig mrConfig = config.addMerger(1, "Merger");
		mrConfig.config.concatenate = true;
		mrConfig.config.size = new Size(length);
		config.addLink("DateTimeEncoder", DateTimeEncoder.ENCODED_SENSOR_OUTPUT, "Merger", 0);
		config.addLink("ValueEncoder", ScalarEncoder.ENCODED_SENSOR_OUTPUT, "Merger", 1);
	}

	protected static void addHotGymSpatialPooler(NetworkConfig config, int length) {
		SpatialPoolerConfig spc = config.addSpatialPooler(2, "SpatialPooler");
		spc.config.inputSize = new Size(length);
		spc.config.boostFactorPeriod = 1000;
		config.addLink("Merger", "SpatialPooler");
	}

	protected static void addHotGymTemporalMemory(NetworkConfig config) {
		TemporalMemoryConfig tmc = config.addTemporalMemory(3, "TemporalMemory");
		tmc.config.segmentCreationSubsample = 0.9F;
		tmc.config.maxNewSynapseCount = 20;
		tmc.config.activationThreshold = 14;
		tmc.config.matchingThreshold = 11;
		tmc.config.maxSegmentsPerCell = 128;
		tmc.config.maxSynapsesPerSegment = 32;
		config.addLink("SpatialPooler", "TemporalMemory");
	}

	protected static void addHotGymClassifier(NetworkConfig config) {
		config.addLink("TemporalMemory", "Classifier");
		config.addLink("Value", 0, "Classifier", Classifier.ASSOCIATE_VALUE_INPUT);
	}
}
