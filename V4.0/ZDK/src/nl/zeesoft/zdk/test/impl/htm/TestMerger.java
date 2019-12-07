package nl.zeesoft.zdk.test.impl.htm;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.proc.Merger;
import nl.zeesoft.zdk.htm.proc.MergerConfig;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestMerger extends TestObject {
	public TestMerger(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestMerger(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *Merger* to merge multiple SDRs into a single SDR.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the merger");
		System.out.println("Merger merger = new Merger(new MergerConfig());");
		System.out.println("// Create an input SDR");
		System.out.println("SDR sdr = new SDR(100);");
		System.out.println("sdr.setBit(0,true);");
		System.out.println("// Create a context SDR list");
		System.out.println("SDR sdr2 = new SDR(100);");
		System.out.println("sdr2.setBit(0,true);");
		System.out.println("List<SDR> context = new ArrayList<SDR>();");
		System.out.println("context.add(sdr2);");
		System.out.println("// Obtain the output SDRs for the input SDR and its context SDRs");
		System.out.println("Lis<SDR> outputSDRs = merger.getSDRsForInput(sdr,context,false);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestMerger.class));
		System.out.println(" * " + getTester().getLinkForClass(Merger.class));
		System.out.println(" * " + getTester().getLinkForClass(SDR.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows a description of the merger and the result of several SDR merges.  ");
	}
	
	@Override
	protected void test(String[] args) {
		Merger merger = new Merger(new MergerConfig(4));
		
		testJsAble(new MergerConfig(4),new MergerConfig(),"Merger configuration JSON does not match expectation");
		
		System.out.println(merger.getDescription());
		System.out.println();
		
		DateTimeSDR input = new DateTimeSDR(50);
		input.setBit(0,true);
		input.setBit(24,true);
		input.keyValues.put("VALUE1",1F);
		
		DateTimeSDR context1 = new DateTimeSDR(50);
		context1.setBit(0,true);
		context1.setBit(24,true);
		context1.keyValues.put("VALUE2",2F);
		
		List<SDR> context = new ArrayList<SDR>();
		context.add(context1);
		
		List<SDR> outputSDRs = merger.getSDRsForInput(input,context,false);
		DateTimeSDR merged = (DateTimeSDR) outputSDRs.get(0);
		
		System.out.println("Input SDR: " + input.toStringBuilder());
		System.out.println("Context SDR: " + context1.toStringBuilder());
		System.out.println("Merged SDR: " + merged.toStringBuilder());
		
		testAssertions(merged);
		
		context.add(input);
		outputSDRs = merger.getSDRsForInput(null,context,false);
		merged = (DateTimeSDR) outputSDRs.get(0);
		
		System.out.println();
		System.out.println("Context SDR 1: " + context1.toStringBuilder());
		System.out.println("Context SDR 2: " + input.toStringBuilder());
		System.out.println("Merged SDR: " + merged.toStringBuilder());
		testAssertions(merged);
	}
	
	private void testAssertions(DateTimeSDR merged) {
		if (assertEqual(merged.length(),100,"SDR length does not match expectation")) {
			assertEqual(merged.getBit(0),true,"Bit 0 does not match expectation");
			assertEqual(merged.getBit(24),true,"Bit 24 does not match expectation");
			assertEqual(merged.getBit(50),true,"Bit 50 does not match expectation");
			assertEqual(merged.getBit(74),true,"Bit 74 does not match expectation");
		}
		assertEqual(merged.keyValues.size(),2,"Number of key values does not match expectation");
	}
}
