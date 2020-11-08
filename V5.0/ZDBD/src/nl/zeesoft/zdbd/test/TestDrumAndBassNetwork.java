package nl.zeesoft.zdbd.test;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zdbd.neural.NetworkConfigFactory;
import nl.zeesoft.zdbd.pattern.PatternFactory;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.KeyValueSDR;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkConfig;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.neural.processors.Classifier;
import nl.zeesoft.zdk.neural.processors.ProcessorIO;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;

public class TestDrumAndBassNetwork extends TestObject {
	public TestDrumAndBassNetwork(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDrumAndBassNetwork(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		/* TODO: Describe
		System.out.println("This test shows how a *Str* instance can be used to split a comma separated string into a list of *Str* instances. ");
		System.out.println("The *Str* class is designed to add features of the Java String to a Java StringBuilder. ");
		System.out.println("It also contains methods for file writing and reading. ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the Str");
		System.out.println("Str str = new Str(\"qwer,asdf,zxcv\");");
		System.out.println("// Split the Str");
		System.out.println("List<Str> strs = str.split(\",\");");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockStr.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestController.class));
		System.out.println(" * " + getTester().getLinkForClass(Str.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the string input and lists the *Str* objects.  ");
		*/
	}

	@Override
	protected void test(String[] args) {
		NetworkConfig config = NetworkConfigFactory.getNetworkConfig();
		System.out.println(config.getDescription());
		
		assertEqual(config.testConfiguration(),new Str(),"Network configuration error does not match expectation");
		
		System.out.println();
		Network network = new Network();
		network.configure(config);
		System.out.println("Initializing network ...");
		network.initialize(true);
		System.out.println("Initialized network");
		
		network.setProcessorLearn("*", false);
		network.setLayerLearn(NetworkConfigFactory.POOLER_LAYER, true);
		
		System.out.println();
		List<NetworkIO> results = new ArrayList<NetworkIO>();
		List<NetworkIO> ioList = PatternFactory.getFourOnFloorDrumAndBassIO();
		int cycles = 64;
		for (int i = 0; i < cycles; i++) {
			System.out.println("Training cycle " + (i + 1) + "/" + cycles + " ...");
			if (i == 8) {
				network.setLayerLearn(NetworkConfigFactory.MEMORY_LAYER, true);
			}
			if (i == 16) {
				network.setLayerLearn(NetworkConfigFactory.CLASSIFIER_LAYER, true);
			}
			for (NetworkIO io: ioList) {
				NetworkIO result = new NetworkIO(io);
				network.processIO(result);
				results.add(result);
				if (result.hasErrors()) {
					System.err.println(result.getErrors().get(0));
					break;
				}
			}
			
			NetworkIO lastIO = results.get(results.size() - 1);
			SortedMap<String,Float> accuracies = lastIO.getClassifierAccuracies(false);
			if (accuracies.size()>0) {
				SortedMap<String,Float> accuracyTrends = lastIO.getClassifierAccuracies(true);
				
				System.out.println("Accuracies (average) / trends: " + accuracies.values() + " (" + lastIO.getAverageClassifierAccuracy(false) + ") / " + accuracyTrends.values());
			}
			if (lastIO.isAccurate(0.99F)) {
				break;
			}
		}
		
		NetworkIO lastIO = results.get(results.size() - 1);
		SortedMap<String,Float> accuracies = lastIO.getClassifierAccuracies(false);
		System.out.println("Accuracy keys: " + accuracies.keySet());
		
		System.out.println();
		System.out.println("Basebeat predictions;");
		ProcessorIO classifierIO = lastIO.getProcessorIO("BasebeatClassifier");
		KeyValueSDR keyValueSDR = (KeyValueSDR) classifierIO.outputs.get(Classifier.CLASSIFICATION_OUTPUT);
		System.out.println(keyValueSDR);
		//Classification classification = (Classification) keyValueSDR.get(Classifier.CLASSIFICATION_VALUE_KEY + ":1");
		//System.out.println(classification.toStr());
	}
}
