package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.htm.pool.Pooler;
import nl.zeesoft.zdk.htm.pool.PoolerConfig;
import nl.zeesoft.zdk.htm.pool.PoolerProcessor;
import nl.zeesoft.zdk.htm.pool.PoolerProcessorListener;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRSet;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestPooler extends TestObject implements PoolerProcessorListener {
	private int	counter	= 0;
	
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
		SDRSet inputSDRSet = (SDRSet) getTester().getMockedObject(MockRegularSDRSet.class.getName());
		assertEqual(inputSDRSet.size(),17521,"Input SDR set size does not match expectation");
		
		PoolerConfig config = new PoolerConfig(inputSDRSet.width(),1024,21);
		
		Pooler pooler = new Pooler(config);
		pooler.randomizeConnections();
		
		System.out.println(pooler.getDescription());
		
		PoolerProcessor processor = new PoolerProcessor(pooler);
		processor.getListeners().add(this);

		System.out.println();
		float ratio1 = processInputSDRSet(processor,inputSDRSet,false);
		
		System.out.println();
		float ratio2 = processInputSDRSet(processor,inputSDRSet,true);
		
		System.out.println();
		System.out.println("Original ratio: " + ratio1 + ", learned ratio: " + ratio2);
		assertEqual(ratio2>ratio1,true,"Learned ratio does not match expectation");
	}

	private float processInputSDRSet(PoolerProcessor processor,SDRSet inputSDRSet, boolean learn) {
		int num = inputSDRSet.size();
		processor.setIntputSDRSet(inputSDRSet);
		
		long started = System.currentTimeMillis();
		System.out.println("Processing input SDR set (learning: " + learn + ") ...");
		processor.process(learn);
		System.out.println("Processing input SDR set took: " + (System.currentTimeMillis() - started) + " ms");
		
		SDRSet outputSDRSet = processor.getOutputSDRSet();
		assertEqual(outputSDRSet.size(),num,"Output SDR set size does not match expectation");
		
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
			System.out.println("Combined average: " + avg + ", Combined weekly average: " + avgWeek);
			
			r = avgWeek / avg;
			assertEqual(r > 10F,true,"Combined weekly average does not match expectation");
		}
		return r;
	}

	@Override
	public void processedSDR(PoolerProcessor processor, SDR inputSDR, SDR outputSDR) {
		if (counter % (24 * 7) == 0) {
			//System.out.println(counter + " <= " + inputSDR.toStringBuilder() + " => " + outputSDR.toStringBuilder());
		}
		counter++;
	}
}
