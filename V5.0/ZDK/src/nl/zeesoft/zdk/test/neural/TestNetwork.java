package nl.zeesoft.zdk.test.neural;

import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.KeyValueSDR;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkConfig;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.neural.processors.Classification;
import nl.zeesoft.zdk.neural.processors.ClassifierConfig;
import nl.zeesoft.zdk.neural.processors.Processor;
import nl.zeesoft.zdk.neural.processors.ProcessorIO;
import nl.zeesoft.zdk.neural.processors.SpatialPoolerConfig;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;

public class TestNetwork extends TestObject {
	public TestNetwork(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestNetwork(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *Network* to link *Processor* instances together. ");
		System.out.println("A *NetworkConfig* instance can be used to configure the *Network* before initialization. ");
		System.out.println("A *NetworkIO* instance can be used to feed input to the network and gather all the outputs of all processors in the network. ");
		System.out.println("Network links that link from processors in higher layers are interpreted as feedback links. ");
		System.out.println("Feedback links will receive the specified processor output from the previously processed *NetworkIO*. ");
		System.out.println("Non-SDR network input values will be wrapped in a *KeyValueSDR* before being passed to a processor. ");
		System.out.println("Processor thread usage numbers can be specified for each individual processor via the configuration. ");
		System.out.println("Processor learning and certain processor specific properties can be changed after initialization. ");
		System.out.println("This includes the option to switch between parallel (default) and sequential processing for the entire network and/or individual processors. ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the configuration");
		System.out.println("NetworkConfig config = new NetworkConfig();");
		System.out.println("config.directory = \"directoryName\";");
		System.out.println("// Define a network input");
		System.out.println("config.inputNames.add(\"input1\");");
		System.out.println("// Add some processors");
		System.out.println("config.addScalarEncoder(\"EN\");");
		System.out.println("config.addSpatialPooler(\"SP\");");
		System.out.println("config.addTemporalMemory(\"TM\");");
		System.out.println("config.addClassifier(\"CL\");");
		System.out.println("// Link the network inputs and processors");
		System.out.println("config.addLink(\"input1\", 0, \"EN\", 0);");
		System.out.println("config.addLink(\"EN\", 0, \"SP\", 0);");
		System.out.println("config.addLink(\"SP\", 0, \"TM\", 0);");
		System.out.println("config.addLink(\"TM\", 0, \"CL\", 0);");
		System.out.println("config.addLink(\"input1\", 0, \"CL\", 1);");
		System.out.println("// Create the network");
		System.out.println("Network network = new Network();");
		System.out.println("network.configureAndInitialize(config,true);");
		System.out.println("// Turn on step 1 accuracy logging in the classifier");
		System.out.println("network.setProcessorProperty(\"CL\", \"logPredictionAccuracy\", true);");
		System.out.println("// Use the network");
		System.out.println("NetworkIO io = new NetworkIO();");
		System.out.println("io.setValue(\"input1\", 1);");
		System.out.println("network.processIO(io);");
		System.out.println("// Get the average classifier accuracy");
		System.out.println("float accuracy = getAverageClassifierAccuracy(false);");
		System.out.println("// Turn of learning for all processors");
		System.out.println("network.setProcessorLearn(\"*\", false);");
		System.out.println("// Save the network data to the configured directory");
		System.out.println("network.save();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestNetwork.class));
		System.out.println(" * " + getTester().getLinkForClass(Network.class));
		System.out.println(" * " + getTester().getLinkForClass(Processor.class));
		System.out.println(" * " + getTester().getLinkForClass(NetworkConfig.class));
		System.out.println(" * " + getTester().getLinkForClass(NetworkIO.class));
		System.out.println(" * " + getTester().getLinkForClass(KeyValueSDR.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * The network configuration description  ");
		System.out.println(" * Some network debug logging  ");
		System.out.println(" * An example output  ");
	}

	@Override
	protected void test(String[] args) {
		Logger.setLoggerDebug(true);
		
		NetworkConfig config = new NetworkConfig();
		config.directory = "dist";
		config.addInput(KeyValueSDR.DEFAULT_VALUE_KEY);
		
		config.addScalarEncoder("EN");
		SpatialPoolerConfig poolerConfig = config.addSpatialPooler("SP");
		poolerConfig.boostFactorPeriod = 1;
		config.addTemporalMemory("TM");
		ClassifierConfig classifierConfig = config.addClassifier("CL");
		classifierConfig.logPredictionAccuracy = true;
		
		config.addLink(KeyValueSDR.DEFAULT_VALUE_KEY, 0, "EN", 0);
		config.addLink("EN", 0, "SP", 0);
		config.addLink("SP", 0, "TM", 0);
		config.addLink("TM", 0, "CL", 0);
		config.addLink(KeyValueSDR.DEFAULT_VALUE_KEY, 0, "CL", 1);

		Str err = config.testConfiguration();
		assertEqual(err,new Str(),"Error does not match expectation");
		
		Str desc = config.getDescription();
		System.out.println(desc);
		
		System.out.println();
		Network network = new Network();
		network.configureAndInitialize(config, true);
		
		System.out.println();
		Logger.dbg(this, new Str("Processing 100 SDRs ..."));
		NetworkIO io = null;
		for (int i = 0; i < 100; i++) {
			io = new NetworkIO();
			io.setValue(KeyValueSDR.DEFAULT_VALUE_KEY, i%2);
			network.processIO(io);
			assertEqual(io.hasErrors(),false,"Network IO has unexpected errors");
			if (io.hasErrors()) {
				SortedMap<String,Float> accuracy = io.getClassifierAccuracies(false);
				if (accuracy.containsKey("CL")) {
					float accuracyTrend = accuracy.get("CL");
					System.out.println("Accuracy: " + accuracyTrend);
				}
				SortedMap<String,Float> accuracyTrends = io.getClassifierAccuracies(true);
				if (accuracyTrends.containsKey("CL")) {
					float accuracyTrend = accuracyTrends.get("CL");
					System.out.println("Accuracy trend: " + accuracyTrend);
				}
				break;
			}
		}
		Logger.dbg(this, new Str("Processed 100 SDRs"));
		
		System.out.println();
		
		config.toFile();
		assertEqual(config.fileExists(),true,"Configuration file was not written");
		
		NetworkConfig config2 = new NetworkConfig();
		config2.directory = "dist";
		config2.fromFile();
		Str desc2 = config2.getDescription();
		assertEqual(desc2,desc,"Network description does not match expectation");
		List<Str> actionLog = FileIO.getActionLog();
		assertEqual(actionLog.size(),2,"Number of actions does not match expectation(1)");
		
		network.setProcessorLearn("*", false);
		network.save();
		actionLog = FileIO.getActionLog();
		assertEqual(actionLog.size(),8,"Number of actions does not match expectation(2)");
		
		Network network2 = new Network();
		Logger.setLoggerDebug(false);
		network2.initializeAndLoad(config2);
		Logger.setLoggerDebug(true);
		actionLog = FileIO.getActionLog();
		assertEqual(actionLog.size(),14,"Number of actions does not match expectation(3)");
		NetworkIO nIO = new NetworkIO();
		for (String name: io.getValueNames()) {
			nIO.setValue(name, io.getValue(name));
		}
		network2.processIO(nIO);
		assertEqual(nIO.getClassifications().size(),1,"Number of classifications does not match expectation");
		for (Classification classification: nIO.getClassifications()) {
			assertEqual(classification.valueCounts.size()>0,true,"Number of predicted values does not match expectation");
		}

		Str ioStr = io.toStr();
		NetworkIO io2 = new NetworkIO();
		io2.fromStr(ioStr);
		Str ioStr2 = io2.toStr();
		assertEqual(ioStr2,ioStr,"Network IO Str does not match expectation");
		assertEqual(network.getLastIO().toStr(),ioStr,"Last network IO does not match expectation");
		
		System.out.println();
		String[] names = {"EN", "SP", "TM", "CL"};
		for (String name: names) {
			System.out.println("Processor: " + name);
			ProcessorIO pIO = io.getProcessorIO(name);
			for (SDR output: pIO.outputs) {
				System.out.println("-> " + output);
			}
		}
		assertEqual(io.isAccurate(0.95F), true, "Network did not achieve expected accuracy");
	}
}
