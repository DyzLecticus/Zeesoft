package nl.zeesoft.zdk.test.impl;

import java.util.List;

import nl.zeesoft.zdk.htm.pool.Pooler;
import nl.zeesoft.zdk.htm.pool.PoolerConfig;
import nl.zeesoft.zdk.htm.pool.PoolerProcessor;
import nl.zeesoft.zdk.htm.pool.ProcessorListener;
import nl.zeesoft.zdk.htm.pool.ProcessorObject;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRSet;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestPooler extends TestObject implements ProcessorListener {
	private int	counter	= 0;
	
	public TestPooler(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestPooler(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *Pooler* to convert encoder out SDRs into consistently sparse representations.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the configuration");
		System.out.println("PoolerConfig config = new PoolerConfig(200,1024,21);");
		System.out.println("// Create the pooler");
		System.out.println("Pooler pooler = new Pooler(config);");
		System.out.println("// Randomize the connections");
		System.out.println("pooler.randomizeConnections();");
		System.out.println("// Obtain the output SDR for a certain input SDR");
		System.out.println("SDR sdr = pooler.getSDRForInput(new SDR(),true);");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockRegularSDRSet.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestPooler.class));
		System.out.println(" * " + getTester().getLinkForClass(SDR.class));
		System.out.println(" * " + getTester().getLinkForClass(PoolerConfig.class));
		System.out.println(" * " + getTester().getLinkForClass(Pooler.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows information about the pooler after passing the SDR test set through it.  ");
		System.out.println("It asserts that learning increases the difference in overlap between the regular weekly recurring values and all the other pooler output SDRs.  ");
	}
	
	@Override
	protected void test(String[] args) {
		SDRSet inputSDRSet = (SDRSet) getTester().getMockedObject(MockRegularSDRSet.class.getName());
		assertEqual(inputSDRSet.size(),17521,"Input SDR set size does not match expectation");
		
		PoolerConfig config = new PoolerConfig(inputSDRSet.width(),1024,21);
		
		long start = System.currentTimeMillis();
		Pooler pooler = new Pooler(config);
		System.out.println("Initializing pooler took: " + (System.currentTimeMillis() - start) + " ms");
		
		start = System.currentTimeMillis();
		pooler.randomizeConnections();
		System.out.println("Randomizing connections took: " + (System.currentTimeMillis() - start) + " ms");
		
		System.out.println();
		System.out.println(pooler.getDescription());
		
		PoolerProcessor processor = new PoolerProcessor(pooler);
		processor.getListeners().add(this);

		System.out.println();
		float ratio1 = processInputSDRSet(processor,inputSDRSet,false);
		assertEqual(ratio1 > 6F,true,"Unlearned ratio is lower than minimal expectation");
		
		processor.resetPoolerStats();
		
		System.out.println();
		float ratio2 = processInputSDRSet(processor,inputSDRSet,true);
		assertEqual(ratio1 > 8F,true,"Learned ratio is lower than minimal expectation");
		
		System.out.println();
		System.out.println("Original ratio: " + ratio1 + ", learned ratio: " + ratio2);
		assertEqual(ratio2>ratio1,true,"Learned ratio does not match expectation");
	}
	
	private float processInputSDRSet(PoolerProcessor processor,SDRSet inputSDRSet, boolean learn) {
		int num = inputSDRSet.size();
		processor.setIntputSDRSet(inputSDRSet);
		processor.setLearn(learn);
		
		long started = System.currentTimeMillis();
		System.out.println("Processing input SDR set (learning: " + learn + ") ...");
		processor.process();
		System.out.println("Processing input SDR set took: " + (System.currentTimeMillis() - started) + " ms");
		
		SDRSet outputSDRSet = processor.getOutputSDRSet();
		assertEqual(outputSDRSet.size(),num,"Output SDR set size does not match expectation");
		
		System.out.println();
		System.out.println("Performance statistics;");
		System.out.println(processor.getPoolerStats().getDescription());
		
		return analyzeOutputSDRSet(outputSDRSet);
	}
	
	private float analyzeOutputSDRSet(SDRSet outputSDRSet) {
		float r = 0;
		int weeks = 0;
		float avg = 0;
		float avgWeek = 0;
		for (int i = (24 * 70); i < outputSDRSet.size(); i++) {
			if (i % (24 * 7) == 0) {
				SDR baseSDR = outputSDRSet.get(i);
				int div = 0;
				int divWeek = 0;
				int total = 0;
				int totalWeek = 0;
				
				int start = (i - (24 * 7 * 10));
				if (start<0) {
					start = 0;
				}
				
				for (int i2 = start; i2 < i; i2++) {
					SDR compSDR = outputSDRSet.get(i2);
					if (i2 % (24 * 7) == 0) {
						divWeek++;
						totalWeek = totalWeek + baseSDR.getOverlapScore(compSDR);
					} else {
						div++;
						total = total + baseSDR.getOverlapScore(compSDR);
					}
				}
				if (div > 0) {
					float avgOverlap = (float) total / (float) div;
					float avgOverlapWeek = (float) totalWeek / (float) divWeek;
					//System.out.println("Average: " + avgOverlap + ", weekly average: " + avgOverlapWeek);
					
					weeks++;
					avg = avg + avgOverlap;
					avgWeek = avgWeek + avgOverlapWeek;
				}
			}
		}
		if (weeks>0) {
			avg = avg / (float) weeks;
			avgWeek = avgWeek / (float) weeks;
			System.out.println();
			System.out.println("Combined average: " + avg + ", Combined weekly average: " + avgWeek);
			
			r = avgWeek / avg;
		}
		return r;
	}

	@Override
	public void processedSDR(ProcessorObject processor, SDR inputSDR, List<SDR> outputSDRs) {
		counter++;
		if (counter % (24 * 7) == 0) {
			//System.out.println(counter + " <= " + inputSDR.toStringBuilder() + " => " + outputSDR.toStringBuilder());
		}
	}
}
