package nl.zeesoft.zdk.test.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.neural.network.NetworkTrainer;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;
import nl.zeesoft.zdk.neural.network.config.NetworkConfigFactory;

public class TestNetworkTrainer {
	public static int	CYCLES		= 50;
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		NetworkConfigFactory factory = new NetworkConfigFactory();
		factory.setMediumScale();
		
		NetworkConfig config = factory.getSimpleConfig("Input1", "Input2", "Input3", "Input4");
		
		Network network = new Network();
		Rand.reset(0);
		assert network.initialize(config);
		assert network.reset();
		NetworkTrainer trainer = new NetworkTrainer(network);
		trainer.trainNetwork();
		assert trainer.processed == 0;
		for (int c = 0; c < CYCLES; c++) {
			trainer.trainingSet.addAll(getInputPattern());
		}
		trainer.enableTemporalMemory = 60;
		trainer.temporalMemoryLayers.add(2);
		trainer.enableClassifiers = 120;
		trainer.classifierLayers.add(3);
		trainer.minimumAverageAccuracy = 1.0F;
		trainer.trainNetwork();
		assert trainer.processed == 241;
		assert trainer.analyzer.getAccuracy().getAverage() == 1.0F;

		network = new Network();
		Rand.reset(0);
		assert network.initialize(config);
		assert network.reset();
		trainer = new NetworkTrainer(network);
		for (int c = 0; c < CYCLES; c++) {
			trainer.trainingSet.addAll(getInputPattern());
		}
		trainer.maximumIO = 120;
		trainer.minimumAverageAccuracy = 0F;
		trainer.trainNetwork();
		assert trainer.processed == 120;
		assert trainer.analyzer.getAccuracy().getAverage() >= 0.99F;
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
}
