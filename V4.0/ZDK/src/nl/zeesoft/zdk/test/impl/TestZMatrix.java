package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.ZMatrix;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.functions.StaticFunctions;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestZMatrix extends TestObject {	
	public TestZMatrix(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestZMatrix(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *ZMatrix* to do matrix calculations and transformations.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the matrix");
		System.out.println("ZMatrix m = new ZMatrix(2,3);");
		System.out.println("// Randomize the matrix");
		System.out.println("m.randomize();");
		System.out.println("// Print the matrix");
		System.out.println("System.out.println(m.getTable());");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestZMatrix.class));
		System.out.println(" * " + getTester().getLinkForClass(ZMatrix.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the results of several matrix calculations and transformations.  ");
	}
	
	@Override
	protected void test(String[] args) {
		ZMatrix m1 = new ZMatrix(2,3);
	
		m1.add(10);
		System.out.println("Initial;");
		System.out.println(m1.getTable());
		assertEqual(m1.data[0][0],10F,"Matrix value does match expectation");
		
		m1.multiply(3);
		System.out.println();
		System.out.println("Scalar multiplied by " + 3 + ";");
		System.out.println(m1.getTable());
		assertEqual(m1.data[0][0],30F,"Matrix value does match expectation");

		ZMatrix m2 = new ZMatrix(2,3);
		m2.randomize();
		System.out.println();
		System.out.println("Randomized;");
		System.out.println(m2.getTable());
		
		m2.multiply(m1);
		System.out.println();
		System.out.println("Randomized multiplied element wise;");
		System.out.println(m2.getTable());
		
		m1.data[0][0] = 1;
		m1.data[0][1] = 1;
		m1.data[0][2] = 1;
		m1.data[1][0] = 2;
		m1.data[1][1] = 2;
		m1.data[1][2] = 2;
		System.out.println();
		System.out.println("Matrix 1;");
		System.out.println(m1.getTable());
		
		m2 = new ZMatrix(3,2);
		m2.data[0][0] = 3;
		m2.data[0][1] = 3;
		m2.data[1][0] = 4;
		m2.data[1][1] = 4;
		m2.data[2][0] = 5;
		m2.data[2][1] = 5;
		System.out.println();
		System.out.println("Matrix 2;");
		System.out.println(m2.getTable());
		
		ZMatrix m3 = ZMatrix.multiply(m1,m2);
		System.out.println();
		System.out.println("Matrix multiplication of matrix 1 * matrix 2;");
		System.out.println(m3.getTable());

		ZMatrix m4 = new ZMatrix(2,3);
		m4.randomize();
		System.out.println();
		System.out.println("New randomized matrix;");
		System.out.println(m4.getTable());
		
		ZMatrix m5 = ZMatrix.transpose(m4);
		System.out.println();
		System.out.println("Randomized matrix transposed;");
		System.out.println(m5.getTable());
		assertEqual(m5.rows,3,"Number of rows does not match expectation");
		assertEqual(m5.cols,2,"Number of columns does not match expectation");
		
		ZStringBuilder str = m5.toStringBuilder();
		ZMatrix m6 = ZMatrix.fromStringBuilder(str);
		assertEqual(m6.toStringBuilder().equals(m5.toStringBuilder()),true,"Matrix string builder does not match expectation");
		if (!m6.toStringBuilder().equals(m5.toStringBuilder())) {
			System.err.println(m6.getTable());
		}
		
		int distance = StaticFunctions.getDistance(1,1,1,3,3,3);
		assertEqual(distance,3,"Distance (1) does not match expectation");
		distance = StaticFunctions.getDistance(0,0,0,4,3,0);
		assertEqual(distance,5,"Distance (2) does not match expectation");
		distance = StaticFunctions.getDistance(1,0,0,4,20,30);
		assertEqual(distance,36,"Distance (4) does not match expectation");
		distance = StaticFunctions.getDistance(4,20,30,1,0,0);
		assertEqual(distance,36,"Distance (3) does not match expectation");
	}
}
