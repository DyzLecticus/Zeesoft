package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.enc.ScalarEncoder;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestScalarEncoder extends TestObject {	
	public TestScalarEncoder(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestScalarEncoder(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *ScalarEncoder* to convert a range of scalar values into sparse distributed representations.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the encoder");
		System.out.println("ScalarEncoder enc = new ScalarEncode(52,2,0,50);");
		System.out.println("// Obtain the SDR for a certain value");
		System.out.println("SDR sdr = enc.getSDRForValue(0);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestScalarEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(ScalarEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(SDR.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows two scalar encoders and the SDRs they generate for several values.");
	}
	
	@Override
	protected void test(String[] args) {
		ScalarEncoder enc = null;
		SDR sdr = null;

		enc = new ScalarEncoder(52,2,0,50);
		System.out.println("Encoder size: 52, bits: 2, min: 0, max: 50 ");
		sdr = enc.getSDRForValue(0);
		System.out.println("SDR for value 0:  " + sdr.toBitString());
		sdr = enc.getSDRForValue(1);
		System.out.println("SDR for value 1:  " + sdr.toBitString());
		sdr = enc.getSDRForValue(24);
		System.out.println("SDR for value 24: " + sdr.toBitString());
		sdr = enc.getSDRForValue(25);
		System.out.println("SDR for value 25: " + sdr.toBitString());
		sdr = enc.getSDRForValue(26);
		System.out.println("SDR for value 26: " + sdr.toBitString());
		sdr = enc.getSDRForValue(49);
		System.out.println("SDR for value 49: " + sdr.toBitString());
		sdr = enc.getSDRForValue(50);
		System.out.println("SDR for value 50: " + sdr.toBitString());
		sdr = enc.getSDRForValue(51);
		System.out.println("SDR for value 51: " + sdr.toBitString());
		
		System.out.println();

		enc = new ScalarEncoder(50,2,0,50);
		enc.setPeriodic(true);
		System.out.println("Periodic encoder size: 50, bits: 2, min: 0, max: 50 ");
		sdr = enc.getSDRForValue(0);
		System.out.println("SDR for value 0:  " + sdr.toBitString());
		sdr = enc.getSDRForValue(1);
		System.out.println("SDR for value 1:  " + sdr.toBitString());
		sdr = enc.getSDRForValue(24);
		System.out.println("SDR for value 24: " + sdr.toBitString());
		sdr = enc.getSDRForValue(25);
		System.out.println("SDR for value 25: " + sdr.toBitString());
		sdr = enc.getSDRForValue(26);
		System.out.println("SDR for value 26: " + sdr.toBitString());
		sdr = enc.getSDRForValue(49);
		System.out.println("SDR for value 49: " + sdr.toBitString());
		sdr = enc.getSDRForValue(50);
		System.out.println("SDR for value 50: " + sdr.toBitString());
		sdr = enc.getSDRForValue(51);
		System.out.println("SDR for value 51: " + sdr.toBitString());

		enc = new ScalarEncoder(80,4,0,800);
		sdr = enc.getSDRForValue(0);
		assertEqual(sdr.toStringBuilder(),new ZStringBuilder("80,0,1,2,3"),"SDR(1) does not match expectation");
		sdr = enc.getSDRForValue(400);
		assertEqual(sdr.toStringBuilder(),new ZStringBuilder("80,38,39,40,41"),"SDR(2) does not match expectation");
		sdr = enc.getSDRForValue(800);
		assertEqual(sdr.toStringBuilder(),new ZStringBuilder("80,76,77,78,79"),"SDR(3) does not match expectation");
		sdr = enc.getSDRForValue(-400);
		assertEqual(sdr.toStringBuilder(),new ZStringBuilder("80,0,1,2,3"),"SDR(4) does not match expectation");
		sdr = enc.getSDRForValue(1200);
		assertEqual(sdr.toStringBuilder(),new ZStringBuilder("80,76,77,78,79"),"SDR(5) does not match expectation");
		enc.setPeriodic(true);
		sdr = enc.getSDRForValue(-400);
		assertEqual(sdr.toStringBuilder(),new ZStringBuilder("80,40,41,42,43"),"SDR(6) does not match expectation");
		sdr = enc.getSDRForValue(1200);
		assertEqual(sdr.toStringBuilder(),new ZStringBuilder("80,40,41,42,43"),"SDR(7) does not match expectation");
		
		enc = new ScalarEncoder(100,4,-20,20);
		enc.setResolution(0.5F);
		enc.setPeriodic(true);
		sdr = enc.getSDRForValue(-20.5F);
		assertEqual(sdr.toStringBuilder(),new ZStringBuilder("100,99,0,1,2"),"SDR(8) does not match expectation");
		sdr = enc.getSDRForValue(-20);
		assertEqual(sdr.toStringBuilder(),new ZStringBuilder("100,0,1,2,3"),"SDR(9) does not match expectation");
		sdr = enc.getSDRForValue(-19.5F);
		assertEqual(sdr.toStringBuilder(),new ZStringBuilder("100,1,2,3,4"),"SDR(10) does not match expectation");
		sdr = enc.getSDRForValue(-19F);
		assertEqual(sdr.toStringBuilder(),new ZStringBuilder("100,2,3,4,5"),"SDR(11) does not match expectation");
		sdr = enc.getSDRForValue(-18.5F);
		assertEqual(sdr.toStringBuilder(),new ZStringBuilder("100,3,4,5,6"),"SDR(12) does not match expectation");
		sdr = enc.getSDRForValue(-18F);
		assertEqual(sdr.toStringBuilder(),new ZStringBuilder("100,5,6,7,8"),"SDR(12) does not match expectation");
		sdr = enc.getSDRForValue(0);
		assertEqual(sdr.toStringBuilder(),new ZStringBuilder("100,50,51,52,53"),"SDR(13) does not match expectation");
		sdr = enc.getSDRForValue(10);
		assertEqual(sdr.toStringBuilder(),new ZStringBuilder("100,75,76,77,78"),"SDR(14) does not match expectation");
		sdr = enc.getSDRForValue(19.0F);
		assertEqual(sdr.toStringBuilder(),new ZStringBuilder("100,97,98,99,0"),"SDR(15) does not match expectation");
		sdr = enc.getSDRForValue(19.5F);
		assertEqual(sdr.toStringBuilder(),new ZStringBuilder("100,98,99,0,1"),"SDR(16) does not match expectation");
		sdr = enc.getSDRForValue(20);
		assertEqual(sdr.toStringBuilder(),new ZStringBuilder("100,0,1,2,3"),"SDR(17) does not match expectation");
	}
}
