package nl.zeesoft.zdk.test.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.ScalarEncoder;
import nl.zeesoft.zdk.neural.processors.TemporalMemory;
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
		Logger.setLoggerDebug(true);
		
		TemporalMemory tm = new TemporalMemory();
		tm.initialize();
		tm.resetConnections();

		CodeRunnerChain processorChain = new CodeRunnerChain();
		tm.buildProcessorChain(processorChain, true);
		tm.addDebugToProcesssorChain(processorChain);

		List<SDR> inputList = getInputSDRList(500);
		List<SDR> outputList = new ArrayList<SDR>();
		List<SDR> burstingList = new ArrayList<SDR>();

		for (SDR input: inputList) {
			tm.setInput(input);
			Waiter.startAndWaitFor(processorChain, 10000);
			SDR output = tm.getOutput();
			outputList.add(output);
			SDR bursting = tm.getOutput(TemporalMemory.BURSTING_COLUMNS_OUTPUT);
			burstingList.add(bursting);
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
		ScalarEncoder encoder = new ScalarEncoder();
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
