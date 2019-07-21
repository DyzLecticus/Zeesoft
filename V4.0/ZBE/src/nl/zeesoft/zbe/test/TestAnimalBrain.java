package nl.zeesoft.zbe.test;

import nl.zeesoft.zbe.animal.AnimalBrain;
import nl.zeesoft.zbe.animal.AnimalTestCycleSet;
import nl.zeesoft.zdk.ZStringBuilder;
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
		/* TODO: describe
		System.out.println("This test shows how to create, mutate and use a *GeneticCode*.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the genetic code");
		System.out.println("GeneticCode genCode = new GeneticCode(100);");
		System.out.println("// Mutate 5 genes");
		System.out.println("genCode.mutate(5);");
		System.out.println("// Get the number of properties");
		System.out.println("int size = genCode.size();");
		System.out.println("// Get a property value");
		System.out.println("float f = genCode.get(4);");
		System.out.println("// Get a scaled property value");
		System.out.println("int i = genCode.getValue(4,100);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestBrain.class));
		System.out.println(" * " + getTester().getLinkForClass(GeneticCode.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * A generated genetic code  ");
		System.out.println(" * The mutated genetic code and the resulting scaled property values  ");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		long started = System.currentTimeMillis();
		System.out.println("Generating trainable animal brain ...");
		AnimalBrain brain = AnimalBrain.getTrainableAnimalBrain(true,1,1000);
		if (brain==null) {
			brain = new AnimalBrain();
			ZStringBuilder err = brain.initialize();
			if (err.length()>0) {
				System.err.println(err);
			} else {
				System.err.println("Failed to generate trainable animal brain within one second");
			}
		} else {
			System.out.println("Generating trainable animal brain took " + (System.currentTimeMillis() - started) + " ms");
			System.out.println();
			brain.toSystemOut();
			AnimalTestCycleSet tcs = new AnimalTestCycleSet(brain,true);
			brain.runTestCycleSet(tcs);
			System.out.println();
			brain.toSystemOut();
			System.out.println();
			tcs.toSystemOut();
		}
	}
}
