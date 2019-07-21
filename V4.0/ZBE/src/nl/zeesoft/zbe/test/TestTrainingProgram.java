package nl.zeesoft.zbe.test;

import nl.zeesoft.zbe.animal.AnimalBrain;
import nl.zeesoft.zbe.animal.AnimalTestCycleSet;
import nl.zeesoft.zbe.brain.TrainingProgram;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestTrainingProgram extends TestObject {
	public TestTrainingProgram(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestTrainingProgram(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *TrainingProgram* to train an *AnimalBrain*.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the animal brain");
		System.out.println("AnimalBrain brain = new AnimalBrain();");
		System.out.println("// Initialize the animal brain");
		System.out.println("ZStringBuilder err = brain.initialize();");
		System.out.println("// Create the animal test cycle set");
		System.out.println("AnimalTestCycleSet tcs = AnimalTestCycleSet();");
		System.out.println("// Initialize the animal test cycle set");
		System.out.println("tcs.initialize(brain,true);");
		System.out.println("// Create the training program");
		System.out.println("TrainingProgram tp = new TrainingProgram(brain,tcs);");
		System.out.println("// Run the training program");
		System.out.println("AnimalBrain trainedBrain = (AnimalBrain) tp.runProgram();");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockAnimalBrain.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestTrainingProgram.class));
		System.out.println(" * " + getTester().getLinkForClass(MockAnimalBrain.class));
		System.out.println(" * " + getTester().getLinkForClass(AnimalBrain.class));
		System.out.println(" * " + getTester().getLinkForClass(AnimalTestCycleSet.class));
		System.out.println(" * " + getTester().getLinkForClass(TrainingProgram.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * The time it took to train the animal brain including the amount of training cycles and learned tests  ");
		System.out.println(" * The test cycle set results for the trained brain  ");
	}
	
	@Override
	protected void test(String[] args) {
		AnimalBrain brain = (AnimalBrain) getTester().getMockedObject(MockAnimalBrain.class.getName());
		if (brain!=null) {
			AnimalTestCycleSet tcs = new AnimalTestCycleSet();
			tcs.initialize(brain,true);
			TrainingProgram tp = new TrainingProgram(brain,tcs);
			long started = System.currentTimeMillis();
			System.out.println("Training animal brain ...");
			AnimalBrain trainedBrain = (AnimalBrain) tp.runProgram(0);
			long ms = (System.currentTimeMillis() - started);
			int learnedTests = tp.getFinalResults().successes - tp.getInitialResults().successes;
			System.out.println("Training animal brain took " + ms + " ms, cycles: " + tp.getTrainedCycles() + ", learned tests: " + learnedTests);
			System.out.println();
			tcs = (AnimalTestCycleSet) tcs.copy();
			trainedBrain.runTestCycleSet(tcs);
			tcs.toSystemOut();
		}
	}
}
