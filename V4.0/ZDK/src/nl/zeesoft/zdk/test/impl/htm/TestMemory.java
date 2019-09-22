package nl.zeesoft.zdk.test.impl.htm;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.proc.Memory;
import nl.zeesoft.zdk.htm.proc.MemoryConfig;
import nl.zeesoft.zdk.htm.proc.MemoryProcessor;
import nl.zeesoft.zdk.htm.proc.Pooler;
import nl.zeesoft.zdk.htm.proc.PoolerConfig;
import nl.zeesoft.zdk.htm.proc.ProcessorListener;
import nl.zeesoft.zdk.htm.proc.ProcessorObject;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRMap;
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
		getTester().describeMock(MockRegularSDRMap.class.getName());
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
		SDRMap inputSDRMap = (SDRMap) getTester().getMockedObject(MockRegularSDRMap.class.getName());
		assertEqual(inputSDRMap.size(),17521,"Input SDR map size does not match expectation");
		
		PoolerConfig poolerConfig = new PoolerConfig(inputSDRMap.length(),1024,21);
		Pooler pooler = new Pooler(poolerConfig);
		pooler.randomizeConnections();
		
		MemoryConfig memoryConfig = new MemoryConfig(poolerConfig);
		System.out.println(memoryConfig.getDescription());
		
		Memory memory = new Memory(memoryConfig);
		
		MemoryProcessor processor = new MemoryProcessor(pooler,memory);
		processor.getListeners().add(this);

		int num = 5000;
		processor.setIntputSDRMap(inputSDRMap);
		
		System.out.println();
		long started = System.currentTimeMillis();
		System.out.println("Processing input SDR map (5000/" + inputSDRMap.size() + ") ...");
		processor.process(0,num);
		System.out.println("Processing input SDR map took: " + (System.currentTimeMillis() - started) + " ms");
		
		SDRMap burstSDRMap = processor.getBurstSDRMap();
		assertEqual(burstSDRMap.size(),num,"Burst SDR map size does not match expectation");
		
		assertEqual(averageBurst,0,"Average burst does not match expectation");

		System.out.println();
		System.out.println(memory.getDescription());
		
		System.out.println();
		System.out.println("Performance statistics;");
		System.out.println(processor.getMemoryStats().getDescription());

		System.out.println();
		System.out.println(memory.getDescription());
		ZStringBuilder strOri = memory.toStringBuilder();
		Memory memoryNew = new Memory(memoryConfig);
		memoryNew.fromStringBuilder(strOri);
		ZStringBuilder strNew = memoryNew.toStringBuilder();
		if (!assertEqual(strNew.length(),strOri.length(),"Memory string builder does not match expectation")) {
			System.out.println(strOri.substring(0,500));
			System.err.println(strNew.substring(0,500));
		} else {
			strOri = memory.getDescription();
			strNew = memoryNew.getDescription();
			if (!assertEqual(strNew.equals(strOri),true,"Memory description does not match expectation")) {
				System.err.println(memoryNew.getDescription());
			}
		}
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
