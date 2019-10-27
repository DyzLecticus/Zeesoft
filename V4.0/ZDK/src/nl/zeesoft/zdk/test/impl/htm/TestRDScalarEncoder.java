package nl.zeesoft.zdk.test.impl.htm;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.enc.RDScalarEncoder;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestRDScalarEncoder extends TestObject {
	public TestRDScalarEncoder(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestRDScalarEncoder(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use an *RDScalarEncoder* to convert a range of scalar values into sparse distributed representations.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the encoder");
		System.out.println("RDScalarEncoder enc = new RDScalarEncoder(50,4);");
		System.out.println("// Obtain the SDR for a certain value");
		System.out.println("SDR sdr = enc.getSDRForValue(0);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestRDScalarEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(RDScalarEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(SDR.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows a random distributed scalar encoder and the SDRs it generated for several values.");
	}
	
	@Override
	protected void test(String[] args) {
		RDScalarEncoder enc = null;
		SDR sdr = null;

		enc = new RDScalarEncoder(50,4);
		System.out.println(enc.getDescription());
		assertEqual(enc.getCapacity().toString(),"230300","Encoder capacity does not match expectation");
		
		sdr = enc.getSDRForValue(0);
		assertEqual(sdr.onBits(),4,"SDR(1) number of on bits does not match expectation");
		assertEqual(enc.getBuckets(),1,"Number encoder of buckets (1) does not match expectation");
		System.out.println("SDR for value 0:  " + sdr.toBitString());
		
		sdr = enc.getSDRForValue(1);
		assertEqual(sdr.onBits(),4,"SDR(2) number of on bits does not match expectation");
		assertEqual(enc.getBuckets(),2,"Number encoder of buckets (2) does not match expectation");
		System.out.println("SDR for value 1:  " + sdr.toBitString());
		
		sdr = enc.getSDRForValue(24);
		assertEqual(sdr.onBits(),4,"SDR(3) number of on bits does not match expectation");
		assertEqual(enc.getBuckets(),25,"Number encoder of buckets (3) does not match expectation");
		System.out.println("SDR for value 24: " + sdr.toBitString());
		
		sdr = enc.getSDRForValue(25);
		assertEqual(sdr.onBits(),4,"SDR(4) number of on bits does not match expectation");
		assertEqual(enc.getBuckets(),26,"Number encoder of buckets (4) does not match expectation");
		System.out.println("SDR for value 25: " + sdr.toBitString());

		sdr = enc.getSDRForValue(75);
		assertEqual(sdr.onBits(),4,"SDR(5) number of on bits does not match expectation");
		assertEqual(enc.getBuckets(),76,"Number encoder of buckets (5) does not match expectation");
		System.out.println("SDR for value 75: " + sdr.toBitString());
		
		sdr = enc.getSDRForValue(76);
		assertEqual(sdr.onBits(),4,"SDR(6) number of on bits does not match expectation");
		assertEqual(enc.getBuckets(),77,"Number encoder of buckets (6) does not match expectation");
		System.out.println("SDR for value 76: " + sdr.toBitString());

		sdr = enc.getSDRForValue(-1);
		assertEqual(sdr.onBits(),4,"SDR(7) number of on bits does not match expectation");
		assertEqual(enc.getBuckets(),78,"Number encoder of buckets (7) does not match expectation");
		System.out.println("SDR for value -1: " + sdr.toBitString());
		
		ZStringBuilder str = enc.toStringBuilder();
		RDScalarEncoder encCopy = new RDScalarEncoder(50,4);
		encCopy.fromStringBuilder(str);
		ZStringBuilder strCopy = encCopy.toStringBuilder();
		if (!assertEqual(strCopy.equals(str),true,"Encoder StringBuilder does not match expectation")) {
			System.err.println(str);
			System.err.println(strCopy);
		}
	}
}
