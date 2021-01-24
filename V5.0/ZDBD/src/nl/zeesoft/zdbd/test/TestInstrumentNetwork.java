package nl.zeesoft.zdbd.test;

import nl.zeesoft.zdbd.neural.NetworkConfigFactory;
import nl.zeesoft.zdbd.neural.NetworkTrainer;
import nl.zeesoft.zdbd.pattern.PatternFactory;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdk.FileIO;
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
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.Waiter;

public class TestInstrumentNetwork extends TestObject {
	public TestInstrumentNetwork(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestInstrumentNetwork(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how a *PatternSequence* and a *NetworkTrainer* can be used to train a *Network*. ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the network configuration");
		System.out.println("NetworkConfig config = NetworkConfigFactory.getNetworkConfig();");
		System.out.println("// Create and initialize the network");
		System.out.println("Network network = new Network();");
		System.out.println("network.initialize(config,true);");
		System.out.println("// Create the pattern sequence");
		System.out.println("PatternSequence sequence = PatternFactory.getFourOnFloorInstrumentPatternSequence();");
		System.out.println("// Create and configure the network trainer");
		System.out.println("NetworkTrainer trainer = new NetworkTrainer();");
		System.out.println("trainer.setSequence(sequence);");
		System.out.println("// Train the network");
		System.out.println("CodeRunnerChain chain = trainer.getTrainNetworkChain(network);");
		System.out.println("Waiter.startAndWaitFor(chain, 60000);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(PatternSequence.class));
		System.out.println(" * " + getTester().getLinkForClass(NetworkTrainer.class));
		System.out.println(" * " + getTester().getLinkForClass(Network.class));
		System.out.println(" * " + getTester().getLinkForClass(CodeRunnerChain.class));
		System.out.println(" * " + getTester().getLinkForClass(Waiter.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the network configuration and the network / network trainer debug log.  ");
	}

	@Override
	protected void test(String[] args) {
		Logger.setLoggerDebug(true);

		PatternSequence sequence = PatternFactory.getFourOnFloorInstrumentPatternSequence();
		NetworkTrainer trainer = new NetworkTrainer();
		trainer.setSequence(sequence);
		trainer.toFile("trainer.txt");
		
		Str str = new Str();
		str.fromFile("trainer.txt");
		
		trainer = new NetworkTrainer();
		trainer.fromFile("trainer.txt");
		PatternSequence loadedSequence = trainer.getSequence();
		
		if (!assertEqual(loadedSequence.equals(sequence),true,"Loaded sequence does not match original")) {
			return;
		}
		
		NetworkConfig config = NetworkConfigFactory.getNetworkConfig();
		System.out.println(config.getDescription());
		
		if (assertEqual(config.check(),new Str(),"Network configuration error does not match expectation")) {
			Network network = createAndTrainNetwork();
			
			NetworkIO lastIO = network.getLastIO();
			assertNotNull(lastIO,"Last network IO does not match expectation");
			if (lastIO!=null) {
				ProcessorIO classifierIO = lastIO.getProcessorIO("KickClassifier");
				KeyValueSDR keyValueSDR = (KeyValueSDR) classifierIO.outputs.get(Classifier.CLASSIFICATION_OUTPUT);
				Classification classification = (Classification) keyValueSDR.get(Classifier.CLASSIFICATION_VALUE_KEY + ":1");
				int prediction = (int) classification.getMostCountedValues().get(0);
				assertEqual(prediction, 2, "Kick prediction does not match expectation");
			}
		}
	}
	
	protected static Network createAndTrainNetwork() {
		PatternSequence sequence = PatternFactory.getFourOnFloorInstrumentPatternSequence();
		
		NetworkConfig config = NetworkConfigFactory.getNetworkConfig();
		config.directory = "dist";
		
		System.out.println();
		Network network = new Network();
		Str err = config.check();
		if (err.length()==0) {
			System.out.println("Initializing network ...");
			network.initialize(config,true);
			System.out.println("Initialized network");
			
			System.out.println();
			System.out.println("Training network ...");
			NetworkTrainer trainer = new NetworkTrainer();
			trainer.setSequence(sequence);
			//trainer.trainNetwork(network);
			CodeRunnerChain chain = trainer.getTrainNetworkChain(network);
			Waiter.startAndWaitFor(chain, 60000);
			System.out.println("Trained network");
			
			if (trainer.getLastIO()!=null) {
				FileIO.mockIO = false;
				if (FileIO.checkDirectory(config.directory).length()!=0) {
					FileIO.mkDirs(config.directory);
				}
				network.save();
				FileIO.mockIO = true;
				
			}
		}
		
		return network;
	}
}
