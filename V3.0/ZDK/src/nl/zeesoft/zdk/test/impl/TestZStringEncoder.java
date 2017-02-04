package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.ZStringEncoder;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestZStringEncoder extends TestObject {	
	public TestZStringEncoder(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestZStringEncoder(new Tester())).test(args);
	}

	@Override
	protected void describe() {
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
		System.out.println(" * " + getTester().getLinkForClass(TestZStringEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(ZStringEncoder.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the generated key, the input text, the encoded text, and the decoded text.");
	}
	
	@Override
	protected void test(String[] args) {
		ZStringEncoder encoder = new ZStringEncoder(getTestText());
		String key = encoder.generateNewKey(64);
		System.out.println("Key: " + key);
		assertEqual(key.length(),64,"The generated key length does not match expectation");
		System.out.println("Input text: " + encoder);
		encoder.encodeKey(key,0);
		System.out.println("Encoded text: " + encoder);
		encoder.decodeKey(key,0);
		System.out.println("Decoded text: " + encoder);
		assertEqual(encoder.toString(),getTestText(),"The decoded text does not match the original");
	}
	
	public static final String getTestText() {
		return "Hello, my name is Dyz Lecticus. How are you feeling today?";
	}
}
