package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.htm.pool.Memory;
import nl.zeesoft.zdk.htm.pool.MemoryConfig;
import nl.zeesoft.zdk.htm.pool.MemoryProcessor;
import nl.zeesoft.zdk.htm.pool.Pooler;
import nl.zeesoft.zdk.htm.pool.PoolerConfig;
import nl.zeesoft.zdk.htm.pool.PoolerProcessor;
import nl.zeesoft.zdk.htm.pool.PoolerProcessorListener;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRSet;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestMemory extends TestObject implements PoolerProcessorListener {
	private int	counter	= 0;
	
	public TestMemory(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestMemory(new Tester())).test(args);
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
		SDRSet inputSDRSet = (SDRSet) getTester().getMockedObject(MockRegularSDRSet.class.getName());
		assertEqual(inputSDRSet.size(),17521,"Input SDR set size does not match expectation");
		
		PoolerConfig poolerConfig = new PoolerConfig(inputSDRSet.width(),1024,21);
		Pooler pooler = new Pooler(poolerConfig);
		pooler.randomizeConnections();
		
		MemoryConfig memoryConfig = new MemoryConfig(poolerConfig);
		Memory memory = new Memory(memoryConfig);
		
		long start = System.currentTimeMillis();
		memory.initialize(pooler);
		System.out.println("Initializing memory took: " + (System.currentTimeMillis() - start) + " ms");
				
		start = System.currentTimeMillis();
		memory.randomizeConnections();
		System.out.println("Randomizing connections took: " + (System.currentTimeMillis() - start) + " ms");
		
		System.out.println();
		System.out.println(memory.getDescription());
		
		MemoryProcessor processor = new MemoryProcessor(pooler,memory);
		processor.getListeners().add(this);

		//int num = 100;
		int num = inputSDRSet.size();
		processor.setIntputSDRSet(inputSDRSet);
		
		System.out.println();
		long started = System.currentTimeMillis();
		System.out.println("Processing input SDR set ...");
		processor.process(num);
		System.out.println("Processing input SDR set took: " + (System.currentTimeMillis() - started) + " ms");
		
		SDRSet burstSDRSet = processor.getBurstSDRSet();
		assertEqual(burstSDRSet.size(),num,"Burst SDR set size does not match expectation");
	}
	
	@Override
	public void processedSDR(PoolerProcessor processor, SDR inputSDR, SDR outputSDR) {
		if (counter % (24 * 7) == 0) {
			//System.out.println(counter + " <= " + inputSDR.toStringBuilder() + " => " + outputSDR.toStringBuilder());
		}
		counter++;
	}
}
