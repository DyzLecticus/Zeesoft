package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.genetic.Evolver;
import nl.zeesoft.zdk.genetic.EvolverUnit;
import nl.zeesoft.zdk.genetic.GeneticNN;
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
		System.out.println("This test shows how to use an *Evolver* and a *TestSet* to generate, train and select the best *GeneticNN* for a certain task.  ");
		System.out.println("Evolvers use multi threading to use processing power effectively.  ");
		System.out.println("When specifying multiple evolvers, half of them are used to generate completely new neural nets.  ");
		System.out.println("The other half are used to generate mutations of the best-so-far generated neural net.  ");  
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the TestSet");
		System.out.println("TestSet tSet = new TestSet(inputs,outputs);");
		System.out.println("// (Add tests to the test ...)");
		System.out.println("// Create the Evolver");
		System.out.println("Evolver evolver = new Evolver(new Messenger(),new WorkerUnion(),maxHiddenLayers,maxHiddenNeurons,codePropertyStart,tSet,evolvers);");
		System.out.println("// Start the evolver");
		System.out.println("evolver.start();");
		System.out.println("// (Give it some time ...)");
		System.out.println("// Stop the evolver");
		System.out.println("evolver.stop();");
		System.out.println("// Get the best-so-far result");
		System.out.println("EvolverUnit unit = evolver.getBestSoFar();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestEvolver.class));
		System.out.println(" * " + getTester().getLinkForClass(Evolver.class));
		System.out.println(" * " + getTester().getLinkForClass(TestSet.class));
		System.out.println(" * " + getTester().getLinkForClass(GeneticNN.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the evolver debug output and the evolver object converted to JSON.  ");
	}
	
	@Override
	protected void test(String[] args) {
		ZDKFactory factory = new ZDKFactory();
		Messenger messenger = factory.getMessenger();
		messenger.setPrintDebugMessages(true);
		WorkerUnion union = factory.getWorkerUnion(messenger);
		
		TestSet tSet = TestNeuralNet.getXORTestSet(false);
		
		Evolver evolver = new Evolver(messenger,union,1,2,0,tSet,20);
		evolver.setTrainEpochBatches(1000);
		evolver.setSleepMsFoundBest(10);
		evolver.setDebug(true);
		
		messenger.start();

		evolver.start();
		for (int i = 0; i < 300; i++) {
			sleep(100);
			if (!evolver.isWorking()) {
				break;
			}
		}
		if (evolver.isWorking()) {
			evolver.stop();
			evolver.whileStopping();
		}
		
		messenger.stop();
		messenger.handleMessages();
		union.stopWorkers();
		
		EvolverUnit bestSoFar = evolver.getBestSoFar();
		assertNotNull(bestSoFar,"Failed to evolve a neural net within 30 seconds");
		
		Evolver evolverCopy = new Evolver(messenger,union,1,2,0,tSet,10);
		evolverCopy.setDebug(true);

		System.out.println();
		System.out.println("Evolver JSON;");
		if (testJsAble(evolver,evolverCopy,"Evolver JSON does not match expectation")) {
			System.out.println(evolver.toJson().toStringBuilderReadFormat());
		}
	}
}
