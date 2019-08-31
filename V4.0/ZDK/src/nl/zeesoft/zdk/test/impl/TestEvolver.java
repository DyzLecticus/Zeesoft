package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.genetic.Evolver;
import nl.zeesoft.zdk.genetic.EvolverUnit;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.neural.TestSet;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class TestEvolver extends TestObject {	
	public TestEvolver(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestEvolver(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		/* TODO: Describe
		System.out.println("This test shows how to use the *ZStringEncoder* to generate a key and then use that to encode and decode a text.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the ZStringEncoder");
		System.out.println("ZStringEncoder encoder = new ZStringEncoder(\"Example text to be encoded.\");");
		System.out.println("// Generate a key");
		System.out.println("String key = encoder.generateNewKey(1024);");
		System.out.println("// Use the key to encode the text");
		System.out.println("encoder.encodeKey(key,0);");
		System.out.println("// Use the key to decode the encoded text");
		System.out.println("encoder.decodeKey(key,0);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("This encoding mechanism can be used to encode and decode passwords and other sensitive data.");
		System.out.println("The minimum key length is 64. Longer keys provide stronger encoding.");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestNeuralNet.class));
		System.out.println(" * " + getTester().getLinkForClass(ZStringEncoder.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the generated key, the input text, the encoded text, and the decoded text.");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		ZDKFactory factory = new ZDKFactory();
		Messenger messenger = factory.getMessenger();
		messenger.setPrintDebugMessages(true);
		WorkerUnion union = factory.getWorkerUnion(messenger);
		
		TestSet tSet = TestNeuralNet.getXORTestSet(false);
		
		Evolver evolver = new Evolver(messenger,union,1,2,0,tSet,10);
		evolver.setTrainEpochBatches(1000);
		evolver.setDebug(true);
		
		messenger.start();

		evolver.start();
		sleep(30000);
		evolver.stop();
		evolver.whileStopping();
		
		messenger.stop();
		messenger.handleMessages();
		union.stopWorkers();
		
		EvolverUnit bestSoFar = evolver.getBestSoFar();
		assertNotNull(bestSoFar,"Failed to evolve a neural net within 30 seconds");
		
		Evolver evolverCopy = new Evolver(messenger,union,1,2,0,tSet,10);
		evolverCopy.setDebug(true);

		testJsAble(evolver,evolverCopy,"Evolver JSON does not match expectation");
	}
}
