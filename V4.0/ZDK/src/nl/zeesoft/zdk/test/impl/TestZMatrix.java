package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.ZMatrix;
import nl.zeesoft.zdk.ZStringBuilder;
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
		System.out.println(" * " + getTester().getLinkForClass(TestZMatrix.class));
		System.out.println(" * " + getTester().getLinkForClass(ZStringEncoder.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the generated key, the input text, the encoded text, and the decoded text.");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		ZMatrix m1 = new ZMatrix(2,3);

		// TODO: Add some more assertions
		
		m1.add(10);
		System.out.println(m1.getTable());
		assertEqual(m1.data[0][0],10F,"Matrix value does match expectation");
		
		m1.multiply(3);
		System.out.println();
		System.out.println(m1.getTable());
		assertEqual(m1.data[0][0],30F,"Matrix value does match expectation");

		ZMatrix m2 = new ZMatrix(2,3);
		m2.randomize();
		System.out.println();
		System.out.println(m2.getTable());
		
		m2.multiply(m1);
		System.out.println();
		System.out.println(m2.getTable());
		
		m1.data[0][0] = 1;
		m1.data[0][1] = 1;
		m1.data[0][2] = 1;
		m1.data[1][0] = 2;
		m1.data[1][1] = 2;
		m1.data[1][2] = 2;
		System.out.println();
		System.out.println(m1.getTable());
		
		m2 = new ZMatrix(3,2);
		m2.data[0][0] = 3;
		m2.data[0][1] = 3;
		m2.data[1][0] = 4;
		m2.data[1][1] = 4;
		m2.data[2][0] = 5;
		m2.data[2][1] = 5;
		System.out.println();
		System.out.println(m2.getTable());
		
		ZMatrix m3 = ZMatrix.multiply(m1,m2);
		System.out.println();
		System.out.println(m3.getTable());

		ZMatrix m4 = new ZMatrix(2,3);
		m4.randomize();
		System.out.println();
		System.out.println(m4.getTable());
		
		ZMatrix m5 = ZMatrix.transpose(m4);
		System.out.println();
		System.out.println(m5.getTable());
		
		ZStringBuilder str = m5.toStringBuilder();
		ZMatrix m6 = ZMatrix.fromStringBuilder(str);
		System.out.println();
		System.out.println(m6.getTable());
	}
}
