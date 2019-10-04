package nl.zeesoft.zdk.test.impl.htm;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.proc.Memory;
import nl.zeesoft.zdk.htm.proc.MemoryConfig;
import nl.zeesoft.zdk.htm.proc.Pooler;
import nl.zeesoft.zdk.htm.proc.PoolerConfig;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRMap;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestMemory extends TestObject {
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
		assertEqual(inputSDRMap.size(),15330,"Input SDR map size does not match expectation");
		
		PoolerConfig poolerConfig = new PoolerConfig(inputSDRMap.length(),1024,21);
		Pooler pooler = new Pooler(poolerConfig);
		pooler.logStats = true;
		pooler.randomizeConnections();
		
		MemoryConfig memoryConfig = new MemoryConfig(poolerConfig);
		System.out.println(memoryConfig.getDescription());
		
		Memory memory = new Memory(memoryConfig);
		memory.logStats = true;

		int num = 5000;
		
		SDRMap burstSDRMap = memoryConfig.getNewSDRMap();
		
		System.out.println();
		long started = System.currentTimeMillis();
		System.out.println("Processing input SDR map (" + num + "/" + inputSDRMap.size() + ") ...");
		for (int i = 0; i < num; i++) {
			SDR outputSDR = pooler.getSDRForInput(inputSDRMap.getSDR(i),true);
			List<SDR> context = new ArrayList<SDR>();
			context.add(outputSDR);
			List<SDR> outputSDRs = memory.getSDRsForInput(outputSDR, context, true);
			SDR burstSDR = outputSDRs.get(1);
			processedSDR(burstSDR);
			burstSDRMap.add(burstSDR);
		}
		System.out.println("Processing input SDR map took: " + (System.currentTimeMillis() - started) + " ms");
		
		assertEqual(burstSDRMap.size(),num,"Burst SDR map size does not match expectation");
		
		assertEqual(averageBurst,0,"Average burst does not match expectation");

		System.out.println();
		System.out.println(memory.getDescription());
		
		System.out.println();
		System.out.println("Performance statistics;");
		System.out.println(memory.statsLog.getSummary());

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
		
		pooler.destroy();
		memory.destroy();
		memoryNew.destroy();
	}

	private void processedSDR(SDR burstSDR) {
		counter++;
		bursts.add(burstSDR.onBits());
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
