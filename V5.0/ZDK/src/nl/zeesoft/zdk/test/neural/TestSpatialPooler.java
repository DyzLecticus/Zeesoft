package nl.zeesoft.zdk.test.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.ScalarEncoder;
import nl.zeesoft.zdk.neural.model.CellGrid;
import nl.zeesoft.zdk.neural.processors.SpatialPooler;
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
		// TODO: Describe
		//System.out.println("This test is not included in the ZDK test set");
	}

	@Override
	protected void test(String[] args) {
		Logger.setLoggerDebug(true);
		
		SpatialPooler sp = new SpatialPooler();
		sp.initialize();
		sp.resetConnections();

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
		
		int num = 0;
		for (SDR input: inputList) {
			sp.setInput(input);
			Waiter.startAndWaitFor(processorChain, 1000);
			SDR output = sp.getOutput();
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
		ScalarEncoder encoder = new ScalarEncoder();
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
}
