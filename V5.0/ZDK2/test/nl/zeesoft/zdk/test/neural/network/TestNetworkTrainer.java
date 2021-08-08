package nl.zeesoft.zdk.test.neural.network;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.neural.network.NetworkTrainer;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;
import nl.zeesoft.zdk.neural.network.config.NetworkConfigFactory;
import nl.zeesoft.zdk.neural.network.config.ProcessorConfig;
import nl.zeesoft.zdk.neural.network.config.type.SpatialPoolerConfig;

public class TestNetworkTrainer {
	public static int	CYCLES		= 50;
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		NetworkConfigFactory factory = new NetworkConfigFactory();
		factory.setMediumScale();
		
		NetworkConfig config = factory.getSimpleConfig("Input1", "Input2", "Input3", "Input4");
		for (ProcessorConfig pcfg: config.getProcessorConfigs()) {
			if (pcfg instanceof SpatialPoolerConfig) {
				((SpatialPoolerConfig) pcfg).config.potentialRadius = 2;
			}
		}
		
		Network network = new Network();
		Rand.reset(0);
		assert network.initialize(config);
		assert network.reset();
		NetworkTrainer trainer = new NetworkTrainer(network);
		trainer.trainingSet.addAll(getTrainingSet());
		trainer.maximumIO = 120;
		trainer.minimumAverageAccuracy = 0F;
		trainer.trainNetwork();
		assert trainer.processed == 120;
		assert trainer.analyzer.getAccuracy().getAverage().accuracy >= 0.99F;

		trainer = testConfig(config);
		assert trainer.processed == 318;
		assert trainer.analyzer.getAccuracy().getAverage().accuracy == 1.0F;

		factory.addTemporalMemoryMerger(config, "TemporalMemory");
		trainer = testConfig(config);
		assert trainer.processed == 244;
		assert trainer.analyzer.getAccuracy().getAverage().accuracy == 1.0F;
	}
	
	public static List<NetworkIO> getTrainingSet() {
		List<NetworkIO> r = new ArrayList<NetworkIO>();
		for (int i = 0; i < CYCLES; i++) {
			r.addAll(TestNetworkTrainer.getInputPattern());
		}
		return r;
	}
	
	public static List<NetworkIO> getInputPattern() {
		List<NetworkIO> r = new ArrayList<NetworkIO>();
		for (int i = 0; i < 12; i++) {
			NetworkIO io = new NetworkIO();
			io.addInput("Input1", i);
			io.addInput("Input2", i % 2);
			io.addInput("Input3", i % 3);
			io.addInput("Input4", i % 4);
			r.add(io);
		}
		return r;
	}
	
	public static NetworkTrainer testConfig(NetworkConfig config) {
		Network network = new Network();
		Rand.reset(0);
		assert network.initialize(config);
		assert network.reset();
		NetworkTrainer r = new NetworkTrainer(network);
		r.trainNetwork();
		assert r.processed == 0;
		r.trainingSet.addAll(getTrainingSet());
		r.enableTemporalMemory = 60;
		r.temporalMemoryLayers.add(2);
		r.enableClassifiers = 120;
		r.classifierLayers.add(3);
		r.minimumAverageAccuracy = 1.0F;
		r.trainNetwork();
		return r;
	}
}
