package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.processors.Merger;
import nl.zeesoft.zdk.neural.processors.MergerConfig;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.Waiter;

public class TestMerger extends TestObject {
	public TestMerger(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestMerger(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *Merger* to merge, concatenate, subsample and/or distort SDRs.");
		System.out.println("A *MergerConfig* can be used to configure the *Merger* before initialization.");
		TestSpatialPooler.printSDRProcessorInfo();
		System.out.println();
		System.out.println(MergerConfig.DOCUMENTATION);
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the merger");
		System.out.println("Merger mr = new Merger();");
		System.out.println("// Initialize the merger");
		System.out.println("mr.initialize();");
		System.out.println("// Create and build the processor chain");
		System.out.println("CodeRunnerChain processorChain = new CodeRunnerChain();");
		System.out.println("mr.buildProcessorChain(processorChain, true);");
		System.out.println("// Set the input");
		System.out.println("mr.setInput(new SDR(), new SDR());");
		System.out.println("// Run the processor chain");
		System.out.println("if (Waiter.startAndWaitFor(processorChain, 1000)) {");
		System.out.println("    // Get the output");
		System.out.println("    SDR output = mr.getOutput();");
		System.out.println("}");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestMerger.class));
		System.out.println(" * " + getTester().getLinkForClass(Merger.class));
		System.out.println(" * " + getTester().getLinkForClass(MergerConfig.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows a *Merger* and an example of a merged and distorted output SDR.  ");
	}

	@Override
	protected void test(String[] args) {
		Logger.setLoggerDebug(true);
		
		// Test merge
		Merger mr = new Merger();
		MergerConfig config = new MergerConfig();
		config.sizeX = 8;
		config.sizeY = 8;
		config.maxOnBits = 10;
		config.distortion = 0.2F;
		mr.configure(config);
		mr.initialize();
		
		CodeRunnerChain processorChain = new CodeRunnerChain();
		mr.buildProcessorChain(processorChain, true);
		
		SDR sdr1 = new SDR(8,8);
		SDR sdr2 = new SDR(8,8);
		for (int i = 0; i < 6; i++) {
			sdr1.setBit(i, true);
			sdr2.setBit(i + 20, true);
		}
		SDR sdr3 = new SDR(sdr1);
		sdr3.or(sdr2);
		
		mr.setInput(sdr1, sdr2);
		Waiter.startAndWaitFor(processorChain, 1000);
		SDR output = mr.getOutput();

		System.out.println();
		System.out.println("Merged and distorted;");
		System.out.println(output.toVisualStr());
		assertEqual(output.onBits(),10,"Number of on bits does not match expectation");
		assertEqual(output.getOverlap(sdr3)<10,true,"Overlap does not match expectation");

		// Test concatenate
		Logger.setLoggerDebug(false);
		
		Merger mr2 = new Merger();
		MergerConfig config2 = new MergerConfig();
		config2.sizeX = 8;
		config2.sizeY = 8;
		config2.concatenate = true;
		mr2.configure(config2);
		mr2.initialize();
		
		CodeRunnerChain processorChain2 = new CodeRunnerChain();
		mr2.buildProcessorChain(processorChain2, true);
		
		sdr1 = new SDR(8,4);
		sdr2 = new SDR(8,4);
		for (int i = 0; i < 6; i++) {
			sdr1.setBit(i, true);
			sdr2.setBit(i, true);
		}
		sdr3 = new SDR(8,8);
		sdr3.concat(sdr1, 0);
		sdr3.concat(sdr2, sdr1.length());
		
		mr2.setInput(sdr1, sdr2);
		Waiter.startAndWaitFor(processorChain2, 1000);
		output = mr2.getOutput();

		assertEqual(output.onBits(),12,"Number of on bits does not match expectation");
		assertEqual(output.getOverlap(sdr3),12,"Overlap does not match expectation");
	}
}
