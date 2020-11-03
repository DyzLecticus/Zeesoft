package nl.zeesoft.zdk.test.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.BasicScalarEncoder;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.processors.TemporalMemory;
import nl.zeesoft.zdk.neural.processors.TemporalMemoryConfig;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.Waiter;

public class TestTemporalMemory extends TestObject {
	public TestTemporalMemory(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestTemporalMemory(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *TemporalMemory* to learn SDR sequences.");
		System.out.println("A *TemporalMemoryConfig* can be used to configure the *TemporalMemory* before initialization.");
		System.out.println(TemporalMemoryConfig.DOCUMENTATION);
		TestSpatialPooler.printSDRProcessorInfo();
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the temporal memory");
		System.out.println("TemporalMemory tm = new TemporalMemory();");
		System.out.println("// Initialize the temporal memory");
		System.out.println("tm.initialize();");
		System.out.println("// Initialize the temporal memory connections (optional)");
		System.out.println("tm.resetConnections();");
		System.out.println("// Create and build the processor chain");
		System.out.println("CodeRunnerChain processorChain = new CodeRunnerChain();");
		System.out.println("tm.buildProcessorChain(processorChain, true);");
		System.out.println("// Set the input (dimensions should match configured X/Y dimensions)");
		System.out.println("tm.setInput(new SDR());");
		System.out.println("// Run the processor chain");
		System.out.println("if (Waiter.startAndWaitFor(processorChain, 1000)) {");
		System.out.println("    // Get the output");
		System.out.println("    SDR output = tm.getOutput();");
		System.out.println("}");
		System.out.println("// Get a Str containing the data");
		System.out.println("Str data = tm.toStr();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestTemporalMemory.class));
		System.out.println(" * " + getTester().getLinkForClass(TemporalMemory.class));
		System.out.println(" * " + getTester().getLinkForClass(TemporalMemoryConfig.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows an example temporal memory learning a sequence of SDRs.  ");
	}

	@Override
	protected void test(String[] args) {
		Logger.setLoggerDebug(true);
		
		TemporalMemory tm = new TemporalMemory();
		tm.initialize();
		tm.resetConnections();

		CodeRunnerChain processorChain = new CodeRunnerChain();
		tm.buildProcessorChain(processorChain, true);

		List<SDR> inputList = getInputSDRList(500);
		List<SDR> outputList = new ArrayList<SDR>();
		List<SDR> burstingList = new ArrayList<SDR>();

		int i = 0;
		for (SDR input: inputList) {
			tm.setInput(input);
			Waiter.startAndWaitFor(processorChain, 10000);
			SDR output = tm.getOutput();
			output.sort();
			outputList.add(output);
			SDR bursting = tm.getOutput(TemporalMemory.BURSTING_COLUMNS_OUTPUT);
			burstingList.add(bursting);
			
			i++;
			if (i < 50 || i % 50 == 0) {
				Str msg = new Str();
				msg.sb().append(tm.getProcessed());
				msg.sb().append(" > bursting: "); 
				msg.sb().append(tm.getOutput(TemporalMemory.BURSTING_COLUMNS_OUTPUT).onBits()); 
				msg.sb().append(", active: "); 
				msg.sb().append(tm.getOutput(TemporalMemory.ACTIVE_CELLS_OUTPUT).onBits()); 
				msg.sb().append(", winners: "); 
				msg.sb().append(tm.getOutput(TemporalMemory.WINNER_CELLS_OUTPUT).onBits()); 
				msg.sb().append(", predictive: ");
				msg.sb().append(tm.getOutput(TemporalMemory.PREDICTIVE_CELLS_OUTPUT).onBits()); 
				Logger.dbg(this, msg);
			}
		}
		
		TemporalMemory tm2 = new TemporalMemory();
		tm2.initialize(null);
		tm2.fromStr(tm.toStr());
		CodeRunnerChain processorChain2 = new CodeRunnerChain();
		tm2.buildProcessorChain(processorChain2, true);
		tm2.setInput(inputList.get(0));
		Waiter.startAndWaitFor(processorChain2, 1000);
		tm2.setInput(inputList.get(1));
		Waiter.startAndWaitFor(processorChain2, 1000);
		SDR burstSDR = tm2.getOutput(TemporalMemory.BURSTING_COLUMNS_OUTPUT);
		assertEqual(burstSDR.onBits(),0,"Number of burst SDR on bits does not match expectation");
	}
	
	private List<SDR> getInputSDRList(int num) {
		BasicScalarEncoder encoder = new BasicScalarEncoder();
		encoder.setEncodeDimensions(48, 48);
		encoder.setOnBits(46);
		encoder.setMaxValue(50);
		
		Str err = encoder.testNoOverlap();
		assertEqual(err, new Str(), "Error message does not match expectation");
		
		List<SDR> r = new ArrayList<SDR>();
		for (int i = 0; i < num; i++) {
			r.add(encoder.getEncodedValue(i % 4));
		}
		return r;
	}
}
