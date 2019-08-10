package nl.zeesoft.zenn.test;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.genetic.EvolverUnit;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zenn.animal.AnimalEvolver;

public class TestAnimalEvolver extends TestObject {
	public TestAnimalEvolver(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestAnimalEvolver(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		/* TODO: describe
		System.out.println("This test shows how to create an *AnimalNN* and an *AnimalTestCycleSet* to test its functioning.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the animal neural network");
		System.out.println("AnimalNN nn = new AnimalNN();");
		System.out.println("// Initialize the animal neural network");
		System.out.println("ZStringBuilder err = nn.initialize();");
		System.out.println("// Create the animal test cycle set");
		System.out.println("AnimalTestCycleSet tcs = AnimalTestCycleSet();");
		System.out.println("// Initialize the animal test cycle set");
		System.out.println("tcs.initialize(nn,true);");
		System.out.println("// Run the animal test cycle set");
		System.out.println("nn.runTestCycleSet(tcs);");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockAnimalNN.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestEvolver.class));
		System.out.println(" * " + getTester().getLinkForClass(MockAnimalNN.class));
		System.out.println(" * " + getTester().getLinkForClass(AnimalNN.class));
		System.out.println(" * " + getTester().getLinkForClass(AnimalTestCycleSet.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * The time it took to generate a trainable animal neural network  ");
		System.out.println(" * The neural network state after running the final test cycle  ");
		System.out.println(" * The test cycle set results  ");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		ZDKFactory factory = new ZDKFactory();
		Messenger messenger = factory.getMessenger();
		messenger.setPrintDebugMessages(true);
		WorkerUnion union = factory.getWorkerUnion(messenger);
		
		AnimalEvolver evolver = new AnimalEvolver(messenger,union,true);
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
		assertNotNull(bestSoFar,"Failed to generate and successfully train an animal neural network within 30 seconds");
	}
}
