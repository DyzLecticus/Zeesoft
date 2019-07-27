package nl.zeesoft.zenn.test;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zenn.animal.AnimalNN;
import nl.zeesoft.zenn.animal.AnimalTestCycleSet;

public class TestAnimalNN extends TestObject {
	public TestAnimalNN(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestAnimalNN(new Tester())).test(args);
	}

	@Override
	protected void describe() {
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
		System.out.println(" * " + getTester().getLinkForClass(TestAnimalNN.class));
		System.out.println(" * " + getTester().getLinkForClass(MockAnimalNN.class));
		System.out.println(" * " + getTester().getLinkForClass(AnimalNN.class));
		System.out.println(" * " + getTester().getLinkForClass(AnimalTestCycleSet.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * The time it took to generate a trainable animal neural network  ");
		System.out.println(" * The neural network state after running the final test cycle  ");
		System.out.println(" * The test cycle set results  ");
	}
	
	@Override
	protected void test(String[] args) {
		AnimalNN nn = (AnimalNN) getTester().getMockedObject(MockAnimalNN.class.getName());
		if (nn!=null) {
			AnimalTestCycleSet tcs = new AnimalTestCycleSet();
			tcs.initialize(nn,true);
			nn.runTestCycleSet(tcs);
			System.out.println();
			System.out.println(nn.getSummary());
			System.out.println();
			System.out.println(tcs.summary);
		}
	}
}
