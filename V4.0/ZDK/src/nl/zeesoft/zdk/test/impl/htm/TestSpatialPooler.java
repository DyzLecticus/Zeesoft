package nl.zeesoft.zdk.test.impl.htm;

import nl.zeesoft.zdk.htm.impl.SpatialPooler;
import nl.zeesoft.zdk.htm.impl.SpatialPoolerConfig;
import nl.zeesoft.zdk.htm.mdl.Model;
import nl.zeesoft.zdk.htm.mdl.ModelConfig;
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
		ModelConfig config = new ModelConfig(256,4,1024,21);
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
	}
}
