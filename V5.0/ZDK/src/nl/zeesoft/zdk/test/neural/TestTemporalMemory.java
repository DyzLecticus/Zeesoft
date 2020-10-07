package nl.zeesoft.zdk.test.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.grid.SDR;
import nl.zeesoft.zdk.neural.ScalarEncoder;
import nl.zeesoft.zdk.neural.TemporalMemory;
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
		// TODO: Describe
		//System.out.println("This test is not included in the ZDK test set");
	}

	@Override
	protected void test(String[] args) {
		TemporalMemory tm = new TemporalMemory();
		tm.logger = new Logger(true);
		tm.initialize();
		tm.randomizeConnections();

		CodeRunnerChain processorChain = new CodeRunnerChain();
		tm.buildProcessorChain(processorChain, true);

		List<SDR> inputList = getInputSDRList(100);
		List<SDR> outputList = new ArrayList<SDR>();

		// TODO: Expand test and add assertions

		int num = 0;
		for (SDR input: inputList) {
			tm.setInput(input);
			Waiter.startAndWaitFor(processorChain, 10000);
			SDR output = tm.getOutput();
			outputList.add(output);
			
			if (num % 4 == 0) {
				//System.out.println("Input SDR: " + input.toStr());
				//System.out.println("Output SDR: " + output.toStr());
			}
			tm.debug();
			num++;
		}
	}
	
	private List<SDR> getInputSDRList(int num) {
		ScalarEncoder encoder = new ScalarEncoder();
		encoder.encodeLength = 2304;
		encoder.onBits = 46;
		encoder.buckets = 40;
		
		Str err = encoder.testOverlap(0,0);
		assertEqual(err, new Str(),"Error message does not match expectation");
		
		List<SDR> r = new ArrayList<SDR>();
		for (int i = 0; i < num; i++) {
			encoder.value = i % 4;
			r.add(encoder.getEncodedValue());
		}
		return r;
	}
}
