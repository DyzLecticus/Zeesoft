package nl.zeesoft.zdk.test.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Console;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.neural.network.NetworkIOAnalyzer;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;
import nl.zeesoft.zdk.neural.network.config.NetworkConfigFactory;
import nl.zeesoft.zdk.neural.processor.cl.Classification;

public class TestNetworkPerformance {
	public static int	TEST_CYCLES		= 50;
	
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
			Network network = new Network();
			Rand.reset(0);
			assert network.initialize(config);
			assert network.reset();
			singleThreadMs += testNetworkPerformance(network, analyzerSeq);
			
			network = new Network();
			Rand.reset(0);
			assert network.initialize(config);
			assert network.reset();
			network.setNumberOfWorkers(4);
			multiThreadMs += testNetworkPerformance(network, analyzerPar);
			network.setNumberOfWorkers(0);
		}

		float factor = (float)multiThreadMs / (float)singleThreadMs;
		
		Console.log("Single thread: " + singleThreadMs + " ms");
		Console.log("Average per network IO; ");
		Console.log(indent(analyzerSeq.getAverage().toString(),"- "));
		
		Console.log("");
		Console.log("Multi thread: " + multiThreadMs + " ms");
		Console.log("Average per network IO; ");
		Console.log(indent(analyzerPar.getAverage().toString(),"- "));
		
		Console.log("");
		Console.log("Factor: " + factor);
		
		assert factor < 0.75F;
	}
	
	public static List<NetworkIO> getTestSet(int cycles) {
		List<NetworkIO> r = new ArrayList<NetworkIO>();
		for (int i = 0; i < cycles; i++) {
			r.addAll(getInputPattern());
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

	public static long testNetworkPerformance(Network network, NetworkIOAnalyzer analyzer) {
		List<NetworkIO> testSet = getTestSet(TEST_CYCLES);
		assert testSet.size() == TEST_CYCLES * getInputPattern().size();
		
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
		
		NetworkIO predictionIO = testSet.get(testSet.size() - 2);
		assert predictionIO.getProcessorIO("Classifier1").outputValue != null;
		assert predictionIO.getProcessorIO("Classifier2").outputValue != null;
		assert predictionIO.getProcessorIO("Classifier3").outputValue != null;
		assert predictionIO.getProcessorIO("Classifier4").outputValue != null;
		Classification input1Prediction = (Classification) predictionIO.getProcessorIO("Classifier1").outputValue;
		Classification input2Prediction = (Classification) predictionIO.getProcessorIO("Classifier2").outputValue;
		Classification input3Prediction = (Classification) predictionIO.getProcessorIO("Classifier3").outputValue;
		Classification input4Prediction = (Classification) predictionIO.getProcessorIO("Classifier4").outputValue;

		assert input1Prediction.getMostCountedValues().size() == 1;
		assert input2Prediction.getMostCountedValues().size() == 1;
		assert input3Prediction.getMostCountedValues().size() == 1;
		assert input4Prediction.getMostCountedValues().size() == 1;
		
		/*
		Console.log(input1Prediction.getStandardDeviation());
		Console.log(input2Prediction.getStandardDeviation());
		Console.log(input3Prediction.getStandardDeviation());
		Console.log(input4Prediction.getStandardDeviation());
		*/
		
		NetworkIO lastIO = testSet.get(testSet.size() - 1);
		assert input1Prediction.getMostCountedValues().get(0) == lastIO.getInput("Input1");
		assert input2Prediction.getMostCountedValues().get(0) == lastIO.getInput("Input2");
		assert input3Prediction.getMostCountedValues().get(0) == lastIO.getInput("Input3");
		assert input4Prediction.getMostCountedValues().get(0) == lastIO.getInput("Input4");
		
		return totalMs;
	}
	
	private static String indent(String source,String indent) {
		return indent + source.replace("\n", "\n" + indent);
	}
}
