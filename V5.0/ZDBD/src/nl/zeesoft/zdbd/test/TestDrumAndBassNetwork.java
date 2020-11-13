package nl.zeesoft.zdbd.test;

import java.util.List;

import nl.zeesoft.zdbd.neural.NetworkConfigFactory;
import nl.zeesoft.zdbd.neural.NetworkTrainer;
import nl.zeesoft.zdbd.pattern.PatternFactory;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.KeyValueSDR;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkConfig;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.neural.processors.Classification;
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
		Logger.setLoggerDebug(true);
		
		NetworkConfig config = NetworkConfigFactory.getNetworkConfig();
		System.out.println(config.getDescription());
		
		assertEqual(config.testConfiguration(),new Str(),"Network configuration error does not match expectation");
		
		System.out.println();
		Network network = new Network();
		network.configure(config);
		System.out.println("Initializing network ...");
		network.initialize(true);
		System.out.println("Initialized network");
		
		System.out.println();
		System.out.println("Training network ...");
		PatternSequence sequence = PatternFactory.getFourOnFloorDrumAndBassPatternSequence();
		NetworkTrainer trainer = new NetworkTrainer();
		List<NetworkIO> results = trainer.trainNetwork(network, sequence);
		System.out.println("Trained network");
		
		NetworkIO lastIO = results.get(results.size() - 1);
		System.out.println();
		System.out.println("Basebeat predictions;");
		ProcessorIO classifierIO = lastIO.getProcessorIO("BasebeatClassifier");
		KeyValueSDR keyValueSDR = (KeyValueSDR) classifierIO.outputs.get(Classifier.CLASSIFICATION_OUTPUT);
		Classification classification = (Classification) keyValueSDR.get(Classifier.CLASSIFICATION_VALUE_KEY + ":1");
		int prediction = (int) classification.getMostCountedValues().get(0);
		assertEqual(prediction, 2, "Basebeat prediction does not match expectation");
	}
}
