package nl.zeesoft.zdk.test.impl;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.pool.Memory;
import nl.zeesoft.zdk.htm.pool.MemoryConfig;
import nl.zeesoft.zdk.htm.pool.MemoryProcessor;
import nl.zeesoft.zdk.htm.pool.MemoryProcessorListener;
import nl.zeesoft.zdk.htm.pool.Pooler;
import nl.zeesoft.zdk.htm.pool.PoolerConfig;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRSet;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestMemory extends TestObject implements MemoryProcessorListener {
	private List<Integer>	bursts	= new ArrayList<Integer>();
	private int				counter	= 0;
	
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
		processor.getMemoryListeners().add(this);

		//for (int i = 0; i < 100; i++) {
			bursts.clear();
			counter = 0;
			//int num = 10000;
			int num = inputSDRSet.size();
			processor.setIntputSDRSet(inputSDRSet);
			
			System.out.println();
			long started = System.currentTimeMillis();
			System.out.println("Processing input SDR set ...");
			processor.process(num);
			System.out.println("Processing input SDR set took: " + (System.currentTimeMillis() - started) + " ms");
			
			SDRSet burstSDRSet = processor.getBurstSDRSet();
			assertEqual(burstSDRSet.size(),num,"Burst SDR set size does not match expectation");
			
			System.out.println();
			System.out.println(memory.getDescription());
		//}
	}
	
	@Override
	public void processedSDR(MemoryProcessor processor, SDR inputSDR, SDR outputSDR, SDR burstSDR) {
		counter++;
		bursts.add(burstSDR.onBits());
		int max = 0;
		while(bursts.size()>100) {
			bursts.remove(0);
		}
		int a = 0;
		for (Integer b: bursts) {
			if (b > max) {
				max = b;
			}
			a += b;
		}
		if (a>0) {
			a = a / bursts.size();
		}
		if (counter % (24 * 7) == 0) {
			System.out.println("--->>> Processed SDR " + counter + ", bursting average: " + a + " (max: " + max + ")");
		}
	}
}
