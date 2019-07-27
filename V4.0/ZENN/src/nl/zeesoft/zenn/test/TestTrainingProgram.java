package nl.zeesoft.zenn.test;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zenn.animal.AnimalNN;
import nl.zeesoft.zenn.animal.AnimalTestCycleSet;
import nl.zeesoft.zenn.network.TrainingProgram;

public class TestTrainingProgram extends TestObject {
	public TestTrainingProgram(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestTrainingProgram(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *TrainingProgram* to train an *AnimalNN*.");
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
		System.out.println("// Create the training program");
		System.out.println("TrainingProgram tp = new TrainingProgram(nn,tcs);");
		System.out.println("// Run the training program");
		System.out.println("AnimalNN trainedNN = (AnimalNN) tp.runProgram();");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockAnimalNN.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestTrainingProgram.class));
		System.out.println(" * " + getTester().getLinkForClass(MockAnimalNN.class));
		System.out.println(" * " + getTester().getLinkForClass(AnimalNN.class));
		System.out.println(" * " + getTester().getLinkForClass(AnimalTestCycleSet.class));
		System.out.println(" * " + getTester().getLinkForClass(TrainingProgram.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * The time it took to train the animal neural network including the amount of training cycles and learned tests  ");
		System.out.println(" * The test cycle set results for the trained neural network  ");
	}
	
	@Override
	protected void test(String[] args) {
		AnimalNN nn = (AnimalNN) getTester().getMockedObject(MockAnimalNN.class.getName());
		if (nn!=null) {
			AnimalTestCycleSet tcs = new AnimalTestCycleSet();
			tcs.initialize(nn,true);
			TrainingProgram tp = new TrainingProgram(nn,tcs);
			long started = System.currentTimeMillis();
			System.out.println("Training animal neural network ...");
			AnimalNN trainedNN = (AnimalNN) tp.runProgram();
			long ms = (System.currentTimeMillis() - started);
			int learnedTests = tp.getFinalResults().successes - tp.getInitialResults().successes;
			System.out.println("Training animal neural network took " + ms + " ms, cycles: " + tp.getTrainedCycles() + ", learned tests: " + learnedTests);
			System.out.println();
			tcs = (AnimalTestCycleSet) tcs.copy();
			trainedNN.runTestCycleSet(tcs);
			System.out.println(tcs.summary);
		}
	}
}
