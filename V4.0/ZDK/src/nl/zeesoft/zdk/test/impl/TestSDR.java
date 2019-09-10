package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestSDR extends TestObject {	
	public TestSDR(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestSDR(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create and compare sparse distributed representations using *SDR* instances.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the SDR");
		System.out.println("SDR sdrA = new SDR(100);");
		System.out.println("// Turn on the first and last bits");
		System.out.println("sdrA.setBit(0,true);");
		System.out.println("sdrA.setBit(99,true);");
		System.out.println("// Create another SDR");
		System.out.println("SDR sdrB = new SDR(100);");
		System.out.println("// Turn on the first and middle bits");
		System.out.println("sdrB.setBit(0,true);");
		System.out.println("sdrB.setBit(50,true);");
		System.out.println("// Check if the SDRs have one overlapping bit");
		System.out.println("System.out.println(sdrA.matches(sdrB,1));");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestSDR.class));
		System.out.println(" * " + getTester().getLinkForClass(SDR.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows two SDRs and if they match at least one bit.");
	}
	
	@Override
	protected void test(String[] args) {
		SDR sdrA = new SDR(100);
		sdrA.setBit(0,true);
		sdrA.setBit(99,true);
		System.out.println("SDR A: " + sdrA.toStringBuilder());
		
		SDR sdrB = new SDR(100);
		sdrB.setBit(0,true);
		sdrB.setBit(50,true);
		System.out.println("SDR B: " + sdrB.toStringBuilder());
		
		System.out.println("Match: " + sdrA.matches(sdrB,1));
		assertEqual(sdrA.matches(sdrB,1),true,"SDR A does not match SDR B");
		assertEqual(sdrA.matches(sdrB,2),false,"SDR A matches SDR B");
		assertEqual(sdrA.equals(sdrA.copy()),true,"SDR copy does not equal original");
		SDR sdrC = sdrB.copy();
		sdrC.fromStringBuilder(sdrA.toStringBuilder());
		assertEqual(sdrC.equals(sdrA),true,"SDR was not correctly parsed from string builder");
		sdrC = SDR.and(sdrA,sdrB);
		assertEqual(sdrC.onBits(),1,"SDR 'and' result does not match expectation");
		sdrC = SDR.or(sdrA,sdrB);
		assertEqual(sdrC.onBits(),3,"SDR 'or' result does not match expectation");
		sdrC = SDR.xor(sdrA,sdrB);
		assertEqual(sdrC.onBits(),2,"SDR 'xor' result does not match expectation");
		sdrC = SDR.not(sdrA);
		assertEqual(sdrC.onBits(),98,"SDR 'not' result does not match expectation");
		sdrC.subsample(10);
		assertEqual(sdrC.onBits(),10,"Subsampled SDR onbits does not match expectation");
		sdrC.randomize(20);
		assertEqual(sdrC.onBits(),20,"Randomized SDR onbits does not match expectation");
		sdrA = new SDR(10);
		sdrA.setBit(0,true);
		sdrA.setBit(9,true);
		sdrB = SDR.concat(sdrA,sdrA);
		assertEqual(sdrB.getBit(0),true,"Concatenated SDR bit(1) does not match expectation");
		assertEqual(sdrB.getBit(9),true,"Concatenated SDR bit(2) does not match expectation");
		assertEqual(sdrB.getBit(10),true,"Concatenated SDR bit(3) does not match expectation");
		assertEqual(sdrB.getBit(19),true,"Concatenated SDR bit(4) does not match expectation");
	}
}
