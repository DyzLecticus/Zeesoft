package nl.zeesoft.zbe.test;

import nl.zeesoft.zbe.animal.AnimalBrain;
import nl.zeesoft.zbe.animal.AnimalTestCycleSet;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestAnimalBrain extends TestObject {
	public TestAnimalBrain(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestAnimalBrain(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create an *AnimalBrain* and an *AnimalTestCycleSet* to test its functioning.");
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
		System.out.println("// Run the animal test cycle set");
		System.out.println("brain.runTestCycleSet(tcs);");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockAnimalBrain.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestAnimalBrain.class));
		System.out.println(" * " + getTester().getLinkForClass(MockAnimalBrain.class));
		System.out.println(" * " + getTester().getLinkForClass(AnimalBrain.class));
		System.out.println(" * " + getTester().getLinkForClass(AnimalTestCycleSet.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * The time it took to generate a trainable animal brain  ");
		System.out.println(" * The neural network state after running the final test cycle  ");
		System.out.println(" * The test cycle set results  ");
	}
	
	@Override
	protected void test(String[] args) {
		AnimalBrain brain = (AnimalBrain) getTester().getMockedObject(MockAnimalBrain.class.getName());
		if (brain!=null) {
			AnimalTestCycleSet tcs = new AnimalTestCycleSet();
			tcs.initialize(brain,true);
			brain.runTestCycleSet(tcs);
			System.out.println();
			brain.toSystemOut();
			System.out.println();
			tcs.toSystemOut();
		}
	}
}
