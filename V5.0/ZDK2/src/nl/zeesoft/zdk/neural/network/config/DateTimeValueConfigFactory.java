package nl.zeesoft.zdk.neural.network.config;

import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.encoder.datetime.DateTimeSdrEncoder;
import nl.zeesoft.zdk.neural.network.config.type.ClassifierConfig;
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
		int length = addEncoders(r);
		addMerger(r, length);
		addSpatialPooler(r, length);
		addTemporalMemory(r);
		addClassifier(r);
		return r;
	}

	protected static int addEncoders(NetworkConfig config) {
		return addDateTimeEncoder(config) + addValueEncoder(config);
	}

	protected static int addDateTimeEncoder(NetworkConfig config) {
		DateTimeEncoderConfig deConfig = config.addDateTimeEncoder("DateTimeEncoder");
		deConfig.encoder.setOnBitsPerEncoder(16);
		deConfig.encoder.setScale(120, 2);
		deConfig.encoder.setEncode(DateTimeSdrEncoder.DAY_OF_WEEK, DateTimeSdrEncoder.HOUR);
		config.addLink("DateTime", "DateTimeEncoder");
		return deConfig.encoder.getEncodeLength();
	}

	protected static int addValueEncoder(NetworkConfig config) {
		ScalarEncoderConfig seConfig = config.addScalarEncoder(0, "ValueEncoder");
		seConfig.encoder.onBits = 16;
		seConfig.encoder.encodeLength = 120;
		seConfig.encoder.maxValue = 100F;
		seConfig.encoder.resolution = 0.1F;
		config.addLink("Value", "ValueEncoder");
		return seConfig.encoder.encodeLength;
	}

	protected static void addMerger(NetworkConfig config, int length) {
		MergerConfig mrConfig = config.addMerger(1, "Merger");
		mrConfig.config.concatenate = true;
		mrConfig.config.size = new Size(length);
		config.addLink("DateTimeEncoder", DateTimeEncoder.ENCODED_SENSOR_OUTPUT, "Merger", 0);
		config.addLink("ValueEncoder", ScalarEncoder.ENCODED_SENSOR_OUTPUT, "Merger", 1);
	}

	protected static void addSpatialPooler(NetworkConfig config, int length) {
		SpatialPoolerConfig spc = config.addSpatialPooler(2, "SpatialPooler");
		spc.config.inputSize = new Size(length);
		spc.config.boostFactorPeriod = 100;
		config.addLink("Merger", "SpatialPooler");
	}

	protected static void addTemporalMemory(NetworkConfig config) {
		TemporalMemoryConfig tmc = config.addTemporalMemory(3, "TemporalMemory");
		tmc.config.size = new Size(45,45,32);
		tmc.config.segmentCreationSubsample = 1F;
		tmc.config.maxNewSynapseCount = 20;
		tmc.config.activationThreshold = 14;
		tmc.config.matchingThreshold = 11;
		tmc.config.maxSegmentsPerCell = 128;
		tmc.config.maxSynapsesPerSegment = 32;
		config.addLink("SpatialPooler", "TemporalMemory");
	}

	protected static void addClassifier(NetworkConfig config) {
		ClassifierConfig clc = config.addClassifier("Classifier");
		clc.config.size = new Size(45,45,32);
		clc.config.maxOnBits = 40 * 32;
		clc.config.initialCount = 1;
		clc.config.alpha = 0.005F;
		clc.config.avgPredictionTop = 5;
		clc.config.avgPredictionStdDevFactor = 2F;
		config.addLink("TemporalMemory", "Classifier");
		config.addLink("Value", 0, "Classifier", Classifier.ASSOCIATE_VALUE_INPUT);
	}
}
