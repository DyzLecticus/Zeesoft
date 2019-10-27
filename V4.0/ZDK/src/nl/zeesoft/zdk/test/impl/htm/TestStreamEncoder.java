package nl.zeesoft.zdk.test.impl.htm;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.stream.StreamEncoder;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestStreamEncoder extends TestObject {
	public TestStreamEncoder(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestStreamEncoder(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create and scale a *StreamEncoder*.");
		System.out.println("A *StreamEncoder* can be used to customize value to SDR translation for stream input.");
		System.out.println("By default it merely translates values into scalar SDRs.");
		System.out.println("it can be customized to include periodic date and/or time representations into the encoded SDRs.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the encoder");
		System.out.println("StreamEncoder enc = new StreamEncoder();");
		System.out.println("// Customize the encoder scale");
		System.out.println("enc.setScale(2);");
		System.out.println("// Obtain the SDR for a certain value");
		System.out.println("SDR sdr = enc.getSDRForValue(dateTime,value);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestStreamEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(StreamEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(SDR.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * How scaling changes the output length and bits of the SDRs the encoder will generate.");
		System.out.println(" * The JSON structure of the encoder.");
	}
	
	@Override
	protected void test(String[] args) {
		StreamEncoder enc = new StreamEncoder();
		System.out.println(enc.getDescription());
		
		assertEqual(enc.length(),256,"Encoder size does not match expectation");
		assertEqual(enc.bits(),32,"Encoder bits does not match expectation");
		ZStringBuilder err = null;
		err = enc.testScalarOverlap(true);
		assertEqual(err,new ZStringBuilder(),"Encoder scalar overlap does not match expectation");
		
		enc.setScale(2);
		System.out.println();
		System.out.println(enc.getDescription());
		assertEqual(enc.length(),512,"Encoder size does not match expectation");
		assertEqual(enc.bits(),64,"Encoder bits does not match expectation");
		err = enc.testScalarOverlap(true);
		assertEqual(err,new ZStringBuilder(),"Encoder scalar overlap does not match expectation");
		
		enc.setScale(4);
		System.out.println();
		System.out.println(enc.getDescription());
		assertEqual(enc.length(),1024,"Encoder size does not match expectation");
		assertEqual(enc.bits(),128,"Encoder bits does not match expectation");
		err = enc.testScalarOverlap(true);
		assertEqual(err,new ZStringBuilder(),"Encoder scalar overlap does not match expectation");
		
		System.out.println();
		System.out.println("Stream encoder JSON;");
		StreamEncoder encNew = new StreamEncoder();
		if (testJsAble(enc,encNew,"Encoder JSON does not match expectation")) {
			System.out.println(enc.toJson().toStringBuilderReadFormat());
		}
	}
}
