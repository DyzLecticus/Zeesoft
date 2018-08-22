package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.ZIntegerGenerator;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestZIntegerGenerator extends TestObject {	
	public TestZIntegerGenerator(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestZIntegerGenerator(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use the *ZIntegerGenerator* to generate non-negative random integers.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the ZIntegerGenerator");
		System.out.println("ZIntegerGenerator generator = new ZIntegerGenerator(0,9);");
		System.out.println("// Generate a random integer");
		System.out.println("int random = generator.getNewInteger();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestZIntegerGenerator.class));
		System.out.println(" * " + getTester().getLinkForClass(ZIntegerGenerator.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows 100 randomly generated integers within the range 0 - 9.");
	}
	
	@Override
	protected void test(String[] args) {
		ZIntegerGenerator generator = new ZIntegerGenerator(0,9);
		StringBuilder output = new StringBuilder();
		for (int i = 0; i<100; i++) {
			int random = generator.getNewInteger();
			if (i>0 && i%20==0) {
				output.append("\n");
			} else if (i>0) {
				output.append(" ");
			}
			output.append(random);
			assertEqual(true,(random>=0 && random<=9),"The generated integer value is not within the range 0 - 9");
		}
		System.out.println(output);
	}
}
