package nl.zeesoft.zdk.test.impl.htm;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.impl.SpatialPooler;
import nl.zeesoft.zdk.htm.impl.SpatialPoolerConfig;
import nl.zeesoft.zdk.htm.mdl.Model;
import nl.zeesoft.zdk.htm.mdl.ModelConfig;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.stream.StreamEncoder;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestSpatialPooler extends TestObject {
	public TestSpatialPooler(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestSpatialPooler(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		/* TODO: Describe
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
		getTester().describeMock(MockRegularSDRMap.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestModel.class));
		System.out.println(" * " + getTester().getLinkForClass(SDR.class));
		System.out.println(" * " + getTester().getLinkForClass(PoolerConfig.class));
		System.out.println(" * " + getTester().getLinkForClass(Pooler.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows information about the pooler after passing the SDR test set through it.  ");
		System.out.println("It asserts that learning increases the difference in overlap between the regular weekly recurring values and all the other pooler output SDRs.  ");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		StreamEncoder enc = new StreamEncoder();
		List<SDR> inputSDRs = new ArrayList<SDR>();
		@SuppressWarnings("unchecked")
		List<MockDateTimeValue> mockVals = (List<MockDateTimeValue>) getTester().getMockedObject(MockRegularDateTimeValues.class.getName());
		for (MockDateTimeValue mockVal: mockVals) {
			inputSDRs.add(enc.getSDRForValue(mockVal.dateTime,mockVal.value2));
		}
		
		ModelConfig config = new ModelConfig(enc.length(),4,1024,21);
		System.out.println(config.getDescription());
		
		Model model = new Model(config);
		model.initialize();
		assertEqual(model.size(),6288,"Model size does not match expectation");
		
		SpatialPoolerConfig poolerConfig = new SpatialPoolerConfig();
		SpatialPooler pooler = new SpatialPooler(model,poolerConfig);
		
		pooler.initializeProximalDendriteSynapses();
		
		System.out.println("Pooler objects;");
		System.out.println(pooler.getDescription());
		assertEqual(pooler.size(),106640,"Model size does not match expectation");
		
		long started = System.currentTimeMillis();
		System.out.println();
		System.out.println("Processing " + inputSDRs.size() + " input SDRs ...");
		List<SDR> outputSDRs = new ArrayList<SDR>();
		for (SDR input: inputSDRs) {
			outputSDRs.add(pooler.getOutputSDRForInputSDR(input,true));
		}
		System.out.println("Processing " + inputSDRs.size() + " input SDRs took: " + (System.currentTimeMillis() - started) + " ms");
		
		float ratio = analyzeOutputSDRs(outputSDRs);
		assertEqual(ratio>10,true,"Ratio does not match expectation");
	}
	
	private float analyzeOutputSDRs(List<SDR> outputSDRs) {
		float r = 0;
		int weeks = 0;
		float avg = 0;
		float avgWeek = 0;
		for (int i = (24 * 70); i < outputSDRs.size(); i++) {
			if (i % (24 * 7) == 0) {
				SDR baseSDR = outputSDRs.get(i);
				int div = 0;
				int divWeek = 0;
				int total = 0;
				int totalWeek = 0;
				
				int start = (i - (24 * 7 * 10));
				if (start<0) {
					start = 0;
				}
				
				for (int i2 = start; i2 < i; i2++) {
					SDR compSDR = outputSDRs.get(i2);
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
}
