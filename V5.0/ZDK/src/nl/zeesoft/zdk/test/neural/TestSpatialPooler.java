package nl.zeesoft.zdk.test.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.grid.SDR;
import nl.zeesoft.zdk.neural.ScalarEncoder;
import nl.zeesoft.zdk.neural.SpatialPooler;
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
		SpatialPooler sp = new SpatialPooler();
		sp.logger = new Logger(true);
		sp.initialize();
		sp.randomizeConnections();

		sp = new SpatialPooler();
		sp.logger = new Logger(true);
		sp.logger.debug(this, new Str("Initializing spatial pooler (asynchronous) ..."));
		CodeRunnerChain initializeChain = new CodeRunnerChain();
		sp.buildInitializeChain(initializeChain, true);
		Waiter.startAndWaitFor(initializeChain, 500);
		sp.logger.debug(this, new Str("Initialized spatial pooler (asynchronous)"));
		
		CodeRunnerChain processorChain = new CodeRunnerChain();
		sp.buildProcessorChain(processorChain, true);
		
		// TODO: Expand test and add assertions
		
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
	}
	
	private List<SDR> getInputSDRList(int num) {
		ScalarEncoder encoder = new ScalarEncoder();
		List<SDR> r = new ArrayList<SDR>();
		for (int i = 0; i < num; i++) {
			encoder.value = i;
			r.add(encoder.getEncodedValue());
		}
		return r;
	}
}
