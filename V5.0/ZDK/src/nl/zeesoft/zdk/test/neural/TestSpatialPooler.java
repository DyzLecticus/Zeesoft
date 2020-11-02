package nl.zeesoft.zdk.test.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.BasicScalarEncoder;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.model.CellGrid;
import nl.zeesoft.zdk.neural.processors.SpatialPooler;
import nl.zeesoft.zdk.neural.processors.SpatialPoolerConfig;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.Waiter;

public class TestSpatialPooler extends TestObject {
	public TestSpatialPooler(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestSpatialPooler(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *SpatialPooler* to create consistent sparse representations of less sparse and/or smaller SDRs.");
		System.out.println("A *SpatialPoolerConfig* can be used to configure the *SpatialPooler* before initialization.");
		printSDRProcessorInfo();
		System.out.println();
		System.out.println(SpatialPoolerConfig.DOCUMENTATION);
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the spatial pooler");
		System.out.println("SpatialPooler sp = new SpatialPooler();");
		System.out.println("// Initialize the spatial pooler");
		System.out.println("sp.initialize();");
		System.out.println("// Initialize the spatial pooler connections");
		System.out.println("sp.resetConnections();");
		System.out.println("// Create and build the processor chain");
		System.out.println("CodeRunnerChain processorChain = new CodeRunnerChain();");
		System.out.println("sp.buildProcessorChain(processorChain, true);");
		System.out.println("// Set the input (dimensions should match configured input dimensions)");
		System.out.println("sp.setInput(new SDR());");
		System.out.println("// Run the processor chain");
		System.out.println("if (Waiter.startAndWaitFor(processorChain, 1000)) {");
		System.out.println("    // Get the output");
		System.out.println("    SDR output = sp.getOutput();");
		System.out.println("}");
		System.out.println("// Get a Str containing the data");
		System.out.println("Str data = sp.toStr();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestSpatialPooler.class));
		System.out.println(" * " + getTester().getLinkForClass(SpatialPooler.class));
		System.out.println(" * " + getTester().getLinkForClass(SpatialPoolerConfig.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows an example spatial pooler and the input/output for similar inputs among a certain input variation.  ");
		System.out.println("It also shows the average overlap for similar inputs and the average overall overlap.  ");
	}

	@Override
	protected void test(String[] args) {
		Logger.setLoggerDebug(true);
		
		SpatialPooler sp = new SpatialPooler();
		sp.initialize();
		sp.resetConnections();

		System.out.println();
		sp = new SpatialPooler();
		Logger.dbg(this, new Str("Initializing spatial pooler (asynchronous) ..."));
		CodeRunnerChain initializeChain = new CodeRunnerChain();
		sp.buildInitializeChain(initializeChain, true);
		Waiter.startAndWaitFor(initializeChain, 500);
		Logger.dbg(this, new Str("Initialized spatial pooler (asynchronous)"));
		
		CodeRunnerChain processorChain = new CodeRunnerChain();
		sp.buildProcessorChain(processorChain, true);
		
		List<SDR> inputList = getInputSDRList(100);
		List<SDR> outputList = new ArrayList<SDR>();
		
		System.out.println();
		int num = 0;
		for (SDR input: inputList) {
			sp.setInput(input);
			Waiter.startAndWaitFor(processorChain, 1000);
			SDR output = sp.getOutput();
			output.sort();
			outputList.add(output);
			
			if (num % 20 == 0) {
				System.out.println("Input SDR: " + input.toStr());
				System.out.println("Output SDR: " + output.toStr());
			}
			num++;
		}
		
		int s = outputList.size() - 1;
		SDR lastOutput = outputList.get(s);
		int overlap = 0;
		num = 0;
		for (int i = (s - 20); i >= 0; i = i - 20) {
			SDR compare = outputList.get(i);
			overlap = overlap + lastOutput.getOverlap(compare);
			num++;
		}
		float averageSimilar = overlap / num;
		
		overlap = 0;
		for (SDR output: outputList) {
			for (SDR compare: outputList) {
				if (output!=compare) {
					overlap = overlap + output.getOverlap(compare);
					num++;
				}
			}
		}
		float averageOverall = overlap / num;
		
		System.out.println();
		System.out.println("Average overlap for similar inputs: " + averageSimilar + ", overall: " + averageOverall);
		assertEqual(averageSimilar>=44F,true,"Average overlap for similar inputs is below expectation");
		assertEqual(averageOverall<=3F,true,"Average overlap for overall inputs is above expectation");
		
		CellGrid cellGrid = sp.toCellGrid(null);
		SpatialPooler sp2 = new SpatialPooler();
		sp2.initialize(null);
		sp2.fromCellGrid(cellGrid, null);
		CellGrid cellGrid2 = sp2.toCellGrid(null);
		assertEqual(cellGrid2.toStr(),cellGrid.toStr(),"Cell grid Str does not match expectation");
		
		CodeRunnerChain processorChain2 = new CodeRunnerChain();
		sp2.buildProcessorChain(processorChain2, true);
		sp2.fromStr(sp.toStr());
		sp2.setInput(inputList.get(inputList.size()-1));
		Waiter.startAndWaitFor(processorChain2, 1000);
		SDR output = sp2.getOutput();
		overlap = output.getOverlap(lastOutput);
		assertEqual(overlap>=45,true,"Overlap does not match expectation");
	}
	
	private List<SDR> getInputSDRList(int num) {
		BasicScalarEncoder encoder = new BasicScalarEncoder();
		encoder.setMaxValue(20);
		encoder.setPeriodic(true);
		
		Str err = encoder.testMinimalOverlap();
		assertEqual(err, new Str(), "Error message does not match expectation");
		
		List<SDR> r = new ArrayList<SDR>();
		for (int i = 0; i < num; i++) {
			r.add(encoder.getEncodedValue(i));
		}
		return r;
	}
	
	protected static void printSDRProcessorInfo() {
		System.out.println();
		System.out.println("All *SDRProcessor* instances implement the same pattern/life cycle to ensure the processing is done using multiple threads.");
		System.out.println("Please note that bare *SDRProcessor* instances are not thread safe beyond the specified life cycle.");
	}
}
