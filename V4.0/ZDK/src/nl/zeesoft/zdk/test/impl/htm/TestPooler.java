package nl.zeesoft.zdk.test.impl.htm;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.proc.Pooler;
import nl.zeesoft.zdk.htm.proc.PoolerConfig;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.htm.util.SDRMap;
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
		System.out.println("This test shows how to use a *Pooler* to convert encoder output SDRs into consistently sparse representations.");
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
		System.out.println("SDR sdr = pooler.getSDRForInput(new SDR(100),true);");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockRegularSDRMap.class.getName());
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
		SDRMap inputSDRMap = (SDRMap) getTester().getMockedObject(MockRegularSDRMap.class.getName());
		assertEqual(inputSDRMap.size(),15330,"Input SDR map size does not match expectation");
		
		PoolerConfig config = new PoolerConfig(inputSDRMap.length(),1024,21);
		
		long start = System.currentTimeMillis();
		Pooler pooler = new Pooler(config);
		pooler.logStats = true;
		System.out.println("Initializing pooler took: " + (System.currentTimeMillis() - start) + " ms");
		
		start = System.currentTimeMillis();
		pooler.randomizeConnections();
		System.out.println("Randomizing connections took: " + (System.currentTimeMillis() - start) + " ms");
		
		System.out.println();
		System.out.println(pooler.getDescription());
		ZStringBuilder strOri = pooler.toStringBuilder();
		Pooler poolerNew = new Pooler(config);
		poolerNew.fromStringBuilder(strOri);
		ZStringBuilder strNew = poolerNew.toStringBuilder();
		if (!assertEqual(strNew.length(),strOri.length(),"Pooler string builder does not match expectation")) {
			System.out.println(strOri.substring(0,500));
			System.err.println(strNew.substring(0,500));
		} else {
			strOri = pooler.getDescription();
			strNew = poolerNew.getDescription();
			if (!assertEqual(strNew.equals(strOri),true,"Pooler description does not match expectation")) {
				System.err.println(poolerNew.getDescription());
			}
		}

		System.out.println();
		float ratio1 = processInputSDRMap(config,pooler,inputSDRMap,false);
		assertEqual(ratio1 > 8F,true,"Unlearned ratio is lower than minimal expectation");
		
		pooler.statsLog.log.clear();
		
		System.out.println();
		float ratio2 = processInputSDRMap(config,pooler,inputSDRMap,true);
		assertEqual(ratio2 > 15F,true,"Learned ratio is lower than minimal expectation");
		
		System.out.println();
		System.out.println("Original ratio: " + ratio1 + ", learned ratio: " + ratio2);
		assertEqual(ratio2>ratio1,true,"Learned ratio does not match expectation");
		
		poolerNew.destroy();
		pooler.destroy();
	}
	
	private float processInputSDRMap(PoolerConfig poolerConfig,Pooler pooler,SDRMap inputSDRMap, boolean learn) {
		SDRMap outputSDRMap = poolerConfig.getNewSDRMap();
				
		int num = inputSDRMap.size();
		
		long started = System.currentTimeMillis();
		System.out.println("Processing input SDR map (learning: " + learn + ") ...");
		for (int i = 0; i < inputSDRMap.size(); i++) {
			outputSDRMap.add(pooler.getSDRForInput(inputSDRMap.getSDR(i), learn));
		}
		System.out.println("Processing input SDR map took: " + (System.currentTimeMillis() - started) + " ms");
		
		assertEqual(outputSDRMap.size(),num,"Output SDR map size does not match expectation");
		
		System.out.println();
		System.out.println("Performance statistics;");
		System.out.println(pooler.statsLog.getSummary());
		
		return analyzeOutputSDRMap(outputSDRMap);
	}
	
	private float analyzeOutputSDRMap(SDRMap outputSDRMap) {
		float r = 0;
		int weeks = 0;
		float avg = 0;
		float avgWeek = 0;
		for (int i = (12 * 70); i < outputSDRMap.size(); i++) {
			if (i % (12 * 7) == 0) {
				SDR baseSDR = outputSDRMap.getSDR(i);
				int div = 0;
				int divWeek = 0;
				int total = 0;
				int totalWeek = 0;
				
				int start = (i - (12 * 7 * 10));
				if (start<0) {
					start = 0;
				}
				
				for (int i2 = start; i2 < i; i2++) {
					SDR compSDR = outputSDRMap.getSDR(i2);
					if (i2 % (12 * 7) == 0) {
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
}
