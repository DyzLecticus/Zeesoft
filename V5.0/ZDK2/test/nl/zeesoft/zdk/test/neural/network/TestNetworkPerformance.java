package nl.zeesoft.zdk.test.neural.network;

import java.util.List;

import nl.zeesoft.zdk.Console;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.neural.network.NetworkIOAccuracy;
import nl.zeesoft.zdk.neural.network.NetworkIOAnalyzer;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;
import nl.zeesoft.zdk.neural.network.config.NetworkConfigFactory;

public class TestNetworkPerformance {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		NetworkConfigFactory factory = new NetworkConfigFactory();
		factory.setMediumScale();
		
		NetworkConfig config = factory.getSimpleConfig("Input1", "Input2", "Input3", "Input4");
		
		long singleThreadMs = 0;
		long multiThreadMs = 0; 
		
		NetworkIOAnalyzer analyzerSeq = new NetworkIOAnalyzer();
		NetworkIOAnalyzer analyzerPar = new NetworkIOAnalyzer();
		
		for (int i = 0; i < 3; i++) {
			analyzerSeq.clear();
			Network network = new Network();
			Rand.reset(0);
			assert network.initialize(config);
			assert network.reset();
			singleThreadMs += testNetworkPerformance(network, analyzerSeq);
			
			analyzerPar.clear();
			network = new Network();
			Rand.reset(0);
			assert network.initialize(config);
			assert network.reset();
			network.setNumberOfWorkers(4);
			multiThreadMs += testNetworkPerformance(network, analyzerPar);
			network.setNumberOfWorkers(0);
		}

		float factor = (float)multiThreadMs / (float)singleThreadMs;
		Console.log("Factor: " + factor);
		assert factor < 0.75F;
		
		Console.log("");	
		Console.log("Single thread: " + singleThreadMs + " ms");
		Console.log("Average per network IO; ");
		Console.log(indent(analyzerSeq.getAverageStats().toString(),"- "));
		Console.log("Accuracy; ");
		Console.log(indent(analyzerSeq.getAccuracy().toString(),"- "));
		
		Console.log("");
		Console.log("Multi thread: " + multiThreadMs + " ms");
		Console.log("Average per network IO; ");
		Console.log(indent(analyzerPar.getAverageStats().toString(),"- "));
		Console.log("Accuracy; ");
		Console.log(indent(analyzerPar.getAccuracy().toString(),"- "));
	}

	public static long testNetworkPerformance(Network network, NetworkIOAnalyzer analyzer) {
		List<NetworkIO> testSet = TestNetworkTrainer.getTrainingSet();
		
		long started = System.currentTimeMillis();
		for (NetworkIO io: testSet) {
			network.processIO(io);
			List<String> errors = io.getErrors();
			if (errors.size()>0) {
				for (String error: errors) {
					Console.log(error);
				}
				assert false;
			}
			analyzer.add(io);
		}
		long totalMs = System.currentTimeMillis() - started;
		
		NetworkIOAccuracy accuracy = analyzer.getAccuracy();
		assert accuracy.average >= 0.975F;
		for (Float acc: accuracy.classifierAverages.values()) {
			assert acc >= 0.95F;
		}
		
		return totalMs;
	}
	
	private static String indent(String source,String indent) {
		return indent + source.replace("\n", "\n" + indent);
	}
}
