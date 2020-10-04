package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.grid.SDR;
import nl.zeesoft.zdk.neural.SpatialPooler;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerList;
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
		
		CodeRunnerChain chain = new CodeRunnerChain();
		CodeRunnerList init = new CodeRunnerList();
		CodeRunnerList rand = new CodeRunnerList();
		sp = new SpatialPooler();
		sp.logger = new Logger(true);
		sp.logger.debug(this, new Str("Async start"));
		sp.initialize(init);
		sp.randomizeConnections(rand);
		chain.add(init);
		chain.add(rand);
		Waiter.startAndWaitFor(chain, 500);
		sp.logger.debug(this, new Str("Async done"));
		
		CodeRunnerChain processorChain = sp.getProcessorChain(true);
		
		// TODO: Expand test and add assertions
		SDR input = new SDR();
		input.initialize(256, 1);
		for (int i = 0; i < 16; i++) {
			input.setValue(i,0,true);
		}
		System.out.println("Input SDR: " + input.toStr());

		sp.setInput(input);
		Waiter.startAndWaitFor(processorChain, 500);
		SDR output = sp.getOutput();
		System.out.println("Output SDR: " + output.toStr());
	}
}
