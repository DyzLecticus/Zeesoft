package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.processors.Encoder;
import nl.zeesoft.zdk.neural.processors.EncoderConfig;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.Waiter;

public class TestEncoder extends TestObject {
	public TestEncoder(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestEncoder(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use an *Encoder* to generate scalar SDRs.");
		System.out.println("An *EncoderConfig* can be used to configure the *Encoder* before initialization.");
		TestSpatialPooler.printSDRProcessorInfo();
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the encoder");
		System.out.println("Encoder en = new Encoder();");
		System.out.println("// Initialize the encoder");
		System.out.println("en.initialize();");
		System.out.println("// Create and build the processor chain");
		System.out.println("CodeRunnerChain processorChain = new CodeRunnerChain();");
		System.out.println("en.buildProcessorChain(processorChain, true);");
		System.out.println("// Set the input");
		System.out.println("en.setValue(20);");
		System.out.println("// Run the processor chain");
		System.out.println("if (Waiter.startAndWaitFor(processorChain, 1000)) {");
		System.out.println("    // Get the output");
		System.out.println("    SDR output = en.getOutput();");
		System.out.println("}");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(Encoder.class));
		System.out.println(" * " + getTester().getLinkForClass(EncoderConfig.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows an *Encoder* and 2 example output SDRs.  ");
	}

	@Override
	protected void test(String[] args) {
		Logger.setLoggerDebug(true);
		
		Encoder en = new Encoder();
		en.initialize();
		
		CodeRunnerChain processorChain = new CodeRunnerChain();
		en.buildProcessorChain(processorChain, true);
		
		en.setValue(20);
		Waiter.startAndWaitFor(processorChain, 1000);
		SDR output1 = en.getOutput();
		System.out.println("Encoded SDR for value 20: " + output1);

		en.setValue(21);
		Waiter.startAndWaitFor(processorChain, 1000);
		SDR output2 = en.getOutput();
		System.out.println("Encoded SDR for value 21: " + output2);

		assertEqual(output1.onBits(),16,"Number of on bits does not match expectation");
		assertEqual(output2.getOverlap(output1),15,"Overlap does not match expectation");
	}
}
