package nl.zeesoft.zdk.test.impl.htm;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.stream.StreamEncoder;
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
		/* TODO: Describe
		System.out.println("This test shows how to use a *DateTimeValueEncoder* to convert a range of dates/times and values into combined periodic sparse distributed representations.");
		System.out.println("The *DateTimeValueEncoder* is merely an example implementation of a *CombinedEncoder* used to test this library.");
		System.out.println("It uses random distributed scalar encoders to represent the values in order to show how these use state to maintain consistent representations.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the encoder");
		System.out.println("DateTimeValueEncoder enc = new DateTimeEncoder();");
		System.out.println("// Obtain the SDR for a certain value");
		System.out.println("SDR sdr = enc.getSDRForValue(System.currentTimeMillis(),2,6);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestStreamEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(DateTimeValuesEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(DateTimeEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(CombinedEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(SDR.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * How the generated SDRs represent several date/time and value combinations.");
		System.out.println(" * The StringBuilder representation of the encoder state.");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		StreamEncoder enc = new StreamEncoder();
		
		assertEqual(enc.length(),256,"Encoder size does not match expectation");
		assertEqual(enc.bits(),32,"Encoder bits does not match expectation");
		ZStringBuilder err = null;
		err = enc.testScalarOverlap(true);
		System.out.println("Default encoding length: " + enc.length() + ", bits: " + enc.bits());
		assertEqual(err,new ZStringBuilder(),"Encoder scalar overlap does not match expectation");
		
		enc.setScale(2);
		System.out.println("Scaled (factor 2) encoding length: " + enc.length() + ", bits: " + enc.bits());
		assertEqual(enc.length(),512,"Encoder size does not match expectation");
		assertEqual(enc.bits(),64,"Encoder bits does not match expectation");
		err = enc.testScalarOverlap(true);
		assertEqual(err,new ZStringBuilder(),"Encoder scalar overlap does not match expectation");
		
		enc.setScale(4);
		System.out.println("Scaled (factor 4) encoding length: " + enc.length() + ", bits: " + enc.bits());
		assertEqual(enc.length(),1024,"Encoder size does not match expectation");
		assertEqual(enc.bits(),128,"Encoder bits does not match expectation");
		err = enc.testScalarOverlap(true);
		assertEqual(err,new ZStringBuilder(),"Encoder scalar overlap does not match expectation");
		
		StreamEncoder encNew = new StreamEncoder();
		testJsAble(enc,encNew,"Encoder JSON does not match expectation");
	}
}
