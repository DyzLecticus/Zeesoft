package nl.zeesoft.zdk.test.impl.htm2;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.enc.DateTimeValueEncoder;
import nl.zeesoft.zdk.htm.util.DateTimeValue;
import nl.zeesoft.zdk.htm.util.DateTimeValueGenerator;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.htm2.impl.SpatialPooler;
import nl.zeesoft.zdk.htm2.impl.SpatialPoolerConfig;
import nl.zeesoft.zdk.htm2.impl.TemporalMemory;
import nl.zeesoft.zdk.htm2.impl.TemporalMemoryConfig;
import nl.zeesoft.zdk.htm2.mdl.Model;
import nl.zeesoft.zdk.htm2.mdl.ModelConfig;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestTemporalMemory extends TestObject {
	public TestTemporalMemory(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestTemporalMemory(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		/* TODO: Describe (HTM2)
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
		List<SDR> inputSDRs = new ArrayList<SDR>();
		DateTimeValueEncoder enc = new DateTimeValueEncoder();
		DateTimeValueGenerator generator = new DateTimeValueGenerator(3600,0,42,0.25F);
		int num = 24 * 7 * 365;
		for (int i = 0; i < num; i++) {
			DateTimeValue dtv = generator.getNextDateTimeValue();
			inputSDRs.add(enc.getSDRForValue(dtv.dateTime,dtv.value,dtv.label));
		}
		
		ModelConfig config = new ModelConfig(enc.length(),32,1024,21);
		System.out.println(config.getDescription());
		
		Model model = new Model(config);
		model.initialize();
		assertEqual(model.size(),6288,"Model size does not match expectation");
		
		SpatialPoolerConfig poolerConfig = new SpatialPoolerConfig();
		poolerConfig.setBoostStrength(0);
		SpatialPooler pooler = new SpatialPooler(model,poolerConfig);
		
		pooler.initializeProximalDendriteSynapses();
		
		System.out.println("Pooler objects;");
		System.out.println(pooler.getDescription());
		assertEqual(pooler.size(),106640,"Model size does not match expectation");
		
		TemporalMemoryConfig memoryConfig = new TemporalMemoryConfig();
		TemporalMemory memory = new TemporalMemory(model,memoryConfig);

		System.out.println("Memory objects;");
		System.out.println(memory.getDescription());
		assertEqual(memory.size(),5264,"Model size does not match expectation");
		
		List<Integer> bursts = new ArrayList<Integer>();
		
		int max = 5000;
		long started = System.currentTimeMillis();
		System.out.println();
		System.out.println("Processing " + max + " input SDRs ...");
		List<SDR> outputSDRs = new ArrayList<SDR>();
		int i = 0;
		for (SDR input: inputSDRs) {
			SDR memoryInputSDR = pooler.getOutputSDRForInputSDR(input,false);
			SDR outputSDR = memory.getOutputSDRForInputSDR(memoryInputSDR,true);
			outputSDRs.add(outputSDR);

			i++;
			
			bursts.add(outputSDR.onBits());
			if (bursts.size()>100) {
				bursts.remove(0);
			}
			if (i % 100 == 0) {
				int avg = 0;
				for (Integer burst: bursts) {
					avg += burst;
				}
				if (avg>0) {
					avg = avg / bursts.size();
					System.out.println("Processed SDRs: " + i + ", average burst: " + avg);
				}
			}
			
			if (i>=max) {
				break;
			}
		}
		System.out.println("Processing " + max + " input SDRs took: " + (System.currentTimeMillis() - started) + " ms");

		System.out.println();
		System.out.println("Memory objects after processing;");
		System.out.println(memory.getDescription());
	}
}
