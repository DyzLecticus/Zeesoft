package nl.zeesoft.zdk.test.impl;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.pool.Memory;
import nl.zeesoft.zdk.htm.pool.MemoryConfig;
import nl.zeesoft.zdk.htm.pool.MemoryProcessor;
import nl.zeesoft.zdk.htm.pool.Pooler;
import nl.zeesoft.zdk.htm.pool.PoolerConfig;
import nl.zeesoft.zdk.htm.pool.ProcessorListener;
import nl.zeesoft.zdk.htm.pool.ProcessorObject;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRSet;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestMemory extends TestObject implements ProcessorListener {
	private List<Integer>	bursts			= new ArrayList<Integer>();
	private int				averageBurst	= 0;
	private int				counter			= 0;
	
	public TestMemory(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestMemory(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *Memory* instance to learn temporal sequences of SDRs.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the configuration");
		System.out.println("MemoryConfig config = new MemoryConfig(1024);");
		System.out.println("// Create the memory");
		System.out.println("Memory memory = new Memory(config);");
		System.out.println("// Obtain the output SDR for a certain input SDR");
		System.out.println("SDR sdr = memory.getSDRForInput(new SDR(),true);");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockRegularSDRSet.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestMemory.class));
		System.out.println(" * " + getTester().getLinkForClass(SDR.class));
		System.out.println(" * " + getTester().getLinkForClass(MemoryConfig.class));
		System.out.println(" * " + getTester().getLinkForClass(Memory.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * How memory column bursting is reduced after leaning several sequences  ");
		System.out.println(" * Information about the memory after passing the SDR test set through it  ");
	}
	
	@Override
	protected void test(String[] args) {
		SDRSet inputSDRSet = (SDRSet) getTester().getMockedObject(MockRegularSDRSet.class.getName());
		assertEqual(inputSDRSet.size(),17521,"Input SDR set size does not match expectation");
		
		PoolerConfig poolerConfig = new PoolerConfig(inputSDRSet.width(),1024,21);
		Pooler pooler = new Pooler(poolerConfig);
		pooler.randomizeConnections();
		
		MemoryConfig memoryConfig = new MemoryConfig(poolerConfig);
		System.out.println(memoryConfig.getDescription());
		
		Memory memory = new Memory(memoryConfig);
		
		MemoryProcessor processor = new MemoryProcessor(pooler,memory);
		processor.getListeners().add(this);

		int num = 5000;
		processor.setIntputSDRSet(inputSDRSet);
		
		System.out.println();
		long started = System.currentTimeMillis();
		System.out.println("Processing input SDR set (5000/" + inputSDRSet.size() + ") ...");
		processor.process(0,num);
		System.out.println("Processing input SDR set took: " + (System.currentTimeMillis() - started) + " ms");
		
		SDRSet burstSDRSet = processor.getBurstSDRSet();
		assertEqual(burstSDRSet.size(),num,"Burst SDR set size does not match expectation");
		
		assertEqual(averageBurst,0,"Average burst does not match expectation");

		System.out.println();
		System.out.println(memory.getDescription());
		
		System.out.println();
		System.out.println("Performance statistics;");
		System.out.println(processor.getMemoryStats().getDescription());
	}

	@Override
	public void processedSDR(ProcessorObject processor, SDR inputSDR, List<SDR> outputSDRs) {
		counter++;
		bursts.add(outputSDRs.get(1).onBits());
		int max = 0;
		while(bursts.size()>100) {
			bursts.remove(0);
		}
		averageBurst = 0;
		for (Integer b: bursts) {
			if (b > max) {
				max = b;
			}
			averageBurst += b;
		}
		if (averageBurst>0) {
			averageBurst = averageBurst / bursts.size();
		}
		if (counter % (500) == 0) {
			System.out.println("Processed SDRs: " + counter + ", bursting average: " + averageBurst + " (max: " + max + ")");
		}
	}
}
