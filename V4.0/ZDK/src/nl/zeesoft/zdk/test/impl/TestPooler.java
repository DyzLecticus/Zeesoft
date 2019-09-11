package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.htm.pool.Pooler;
import nl.zeesoft.zdk.htm.pool.PoolerConfig;
import nl.zeesoft.zdk.htm.pool.PoolerWorker;
import nl.zeesoft.zdk.htm.sdr.SDRSet;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestPooler extends TestObject {
	public TestPooler(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestPooler(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		/* TODO: Describe
		System.out.println("This test shows how to use a *DateTimeValueEncoder* to convert a range of dates/times and values into combined periodic sparse distributed representations.");
		System.out.println("The *DateTimeValueEncoder* is merely an example implementation of a *CombinedEncoder* used to test this library.");
		System.out.println("It uses random distributed scalar encoders to represent the values in order to show how these use state to maintain consistent representations.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the encoder");
		System.out.println("DateTimeValueEncoder enc = new DateTimeEncoder();");
		System.out.println("// Obtain the SDR for a certain value");
		System.out.println("SDR sdr = enc.getSDRForValue(System.currentTimeMillis(),2,6);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestPooler.class));
		System.out.println(" * " + getTester().getLinkForClass(DateTimeValueEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(DateTimeEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(CombinedEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(SDR.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * How the generated SDRs represent several date/time and value combinations.");
		System.out.println(" * The StringBuilder representation of the encoder state.");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		SDRSet inputSDRSet = (SDRSet) getTester().getMockedObject(MockSDRSet.class.getName());
		assertEqual(inputSDRSet.size(),17521,"Input SDR set size does not match expectation");
		
		PoolerConfig config = new PoolerConfig(inputSDRSet.width(),256,5);
		
		Pooler pooler = new Pooler(config);
		pooler.randomizeConnections();
		
		PoolerWorker worker = new PoolerWorker(pooler);
		worker.setIntputSDRSet(inputSDRSet);
		worker.work(true);
		
		SDRSet outputSDRSet = worker.getOutputSDRSet();
		assertEqual(inputSDRSet.size(),outputSDRSet.size(),"Output SDR set size does not match expectation");
	}
}
