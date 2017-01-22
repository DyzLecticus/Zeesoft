package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.EncoderDecoder;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestEncoderDecoder extends TestObject {	
	public static void main(String[] args) {
		(new TestEncoderDecoder()).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use the *EncoderDecoder* class to generate a key and use that to encode and decode a text.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Generate a key");
		System.out.println("String key = EncoderDecoder.generateNewKey(1024);");
		System.out.println("// Use the key to encode a text");
		System.out.println("StringBuilder encodedText = EncoderDecoder.encodeKey(new StringBuilder(\"Example text to be encoded.\"),key,0);");
		System.out.println("// Use the key to decode an encoded text");
		System.out.println("StringBuilder decodedText = EncoderDecoder.decodeKey(encodedText,key,0);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("This encoding mechanism can be used to encode and decode passwords and other sensitive data.");
		System.out.println("The minimum key length is 64. Longer keys provide stronger encoding.");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + Tester.getInstance().getLinkForClass(TestEncoderDecoder.class));
		System.out.println(" * " + Tester.getInstance().getLinkForClass(EncoderDecoder.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the generated key, the input text, the encoded text, and the decoded text.");
	}
	
	@Override
	protected void test(String[] args) {
		String key = EncoderDecoder.generateNewKey(64);
		System.out.println("Key: " + key);
		StringBuilder text = new StringBuilder(getTestText());
		System.out.println("Input text: " + text);
		text = EncoderDecoder.encodeKey(text,key,0);
		System.out.println("Encoded text: " + text);
		text = EncoderDecoder.decodeKey(text,key,0);
		System.out.println("Decoded text: " + text);
		
		assertEqual(text.toString(),getTestText(),"The decoded text does not match the original");
	}
	
	public static final String getTestText() {
		return "Hello, my name is Dyz Lecticus. How are you feeling today?";
	}
}
