package nl.zeesoft.zdk.test.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.KeyValueSDR;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.ScalarEncoder;
import nl.zeesoft.zdk.neural.processors.Classifier;
import nl.zeesoft.zdk.neural.processors.ClassifierConfig;
import nl.zeesoft.zdk.neural.processors.ProcessorFactory;
import nl.zeesoft.zdk.neural.processors.ProcessorIO;
import nl.zeesoft.zdk.neural.processors.ProcessorManager;
import nl.zeesoft.zdk.neural.processors.SpatialPooler;
import nl.zeesoft.zdk.neural.processors.SpatialPoolerConfig;
import nl.zeesoft.zdk.neural.processors.TemporalMemory;
import nl.zeesoft.zdk.neural.processors.TemporalMemoryConfig;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;

public class TestProcessorFactory extends TestObject {
	public TestProcessorFactory(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestProcessorFactory(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		/* TODO: Describe
		System.out.println("This test shows how to use a *TemporalMemory* to learn SDR sequences.");
		System.out.println("A *TemporalMemoryConfig* can be used to configure the *TemporalMemory* before initialization.");
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
		System.out.println("sp.setInput(new SDR());");
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
		System.out.println(" * " + getTester().getLinkForClass(TestProcessorFactory.class));
		System.out.println(" * " + getTester().getLinkForClass(TemporalMemory.class));
		System.out.println(" * " + getTester().getLinkForClass(TemporalMemoryConfig.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows an example temporal memory learning a sequence of SDRs.  ");
		*/
	}

	@Override
	protected void test(String[] args) {
		Logger.setLoggerDebug(true);
		
		ProcessorManager sp = ProcessorFactory.getNewProcessorManager("SP", new SpatialPoolerConfig(), true);
		ProcessorManager tm = ProcessorFactory.getNewProcessorManager("TM", new TemporalMemoryConfig());
		ProcessorManager cl = ProcessorFactory.getNewProcessorManager("CL", new ClassifierConfig());
		
		List<SDR> inputList = getInputSDRList(1000);
		List<SDR> outputList = new ArrayList<SDR>();
		
		int i = 0;
		for (SDR input: inputList) {
			ProcessorIO io1 = new ProcessorIO();
			io1.inputs.add(input);
			sp.processIO(io1);
			SDR output = io1.outputs.get(SpatialPooler.ACTIVE_COLUMNS_OUTPUT);
			
			ProcessorIO io2 = new ProcessorIO();
			io2.inputs.add(io1.outputs.get(SpatialPooler.ACTIVE_COLUMNS_OUTPUT));
			tm.processIO(io2);
			output = io2.outputs.get(TemporalMemory.ACTIVE_CELLS_OUTPUT);
			
			if (i > 950) {
				KeyValueSDR kvSdr = new KeyValueSDR(io2.outputs.get(TemporalMemory.ACTIVE_CELLS_OUTPUT));
				kvSdr.put(Classifier.DEFAULT_VALUE_KEY, i % 4);
				ProcessorIO io3 = new ProcessorIO();
				io3.inputs.add(kvSdr);
				cl.processIO(io3);
				output = io3.outputs.get(Classifier.CLASSIFICATION_OUTPUT);
			}
			
			outputList.add(output);
			if (i % 100 == 0) {
				System.out.println(output);
			}
			
			i++;
		}
		
		System.out.println(outputList.get(outputList.size() - 1));
	}
	
	private List<SDR> getInputSDRList(int num) {
		ScalarEncoder encoder = new ScalarEncoder();
		
		Str err = encoder.testMinimalOverlap();
		assertEqual(err, new Str(), "Error message does not match expectation");
		
		List<SDR> r = new ArrayList<SDR>();
		for (int i = 0; i < num; i++) {
			r.add(encoder.getEncodedValue(i % 4));
		}
		return r;
	}
}
