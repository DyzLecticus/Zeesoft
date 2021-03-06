package nl.zeesoft.zdk.test.impl.htm2;

import nl.zeesoft.zdk.htm2.mdl.Model;
import nl.zeesoft.zdk.htm2.mdl.ModelConfig;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestModel extends TestObject {
	public TestModel(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestModel(new Tester())).test(args);
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
		ModelConfig config = null;
		Model model = null;
		
		config = new ModelConfig(100,4,256,5);
		System.out.println(config.getDescription());
		
		model = new Model(config);
		model.initialize();
		System.out.println("Model objects;");
		System.out.println(model.getDescription());
		assertEqual(model.size(),1537,"Model size does not match expectation");
		
		Model modelCopy = model.copy();
		if (!assertEqual(modelCopy.size(),model.size(),"Model copy size does not match expectation")) {
			System.err.println("Model copy objects;");
			System.err.println(modelCopy.getDescription());
		}
		
		System.out.println();
		config = new ModelConfig(1600,32,2304,48);
		System.out.println(config.getDescription());
		
		model = new Model(config);
		model.initialize();
		System.out.println("Model objects;");
		System.out.println(model.getDescription());
		assertEqual(model.size(),79120,"Model size does not match expectation");
	}
}
