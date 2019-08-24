package nl.zeesoft.zenn.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zenn.environment.EnvironmentConfig;
import nl.zeesoft.zenn.environment.EnvironmentState;

public class TestEnvironment extends TestObject {
	public TestEnvironment(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestEnvironment(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		/* TODO: describe
		System.out.println("This test shows how to create an *AnimalNN* and an *AnimalTestCycleSet* to test its functioning.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the animal neural network");
		System.out.println("AnimalNN nn = new AnimalNN();");
		System.out.println("// Initialize the animal neural network");
		System.out.println("ZStringBuilder err = nn.initialize();");
		System.out.println("// Create the animal test cycle set");
		System.out.println("AnimalTestCycleSet tcs = AnimalTestCycleSet();");
		System.out.println("// Initialize the animal test cycle set");
		System.out.println("tcs.initialize(nn,true);");
		System.out.println("// Run the animal test cycle set");
		System.out.println("nn.runTestCycleSet(tcs);");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockAnimalNN.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestEvolver.class));
		System.out.println(" * " + getTester().getLinkForClass(MockAnimalNN.class));
		System.out.println(" * " + getTester().getLinkForClass(AnimalNN.class));
		System.out.println(" * " + getTester().getLinkForClass(AnimalTestCycleSet.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * The time it took to generate a trainable animal neural network  ");
		System.out.println(" * The neural network state after running the final test cycle  ");
		System.out.println(" * The test cycle set results  ");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		EnvironmentConfig env = new EnvironmentConfig();
		JsFile json = env.toJson();
		ZStringBuilder oriStr = json.toStringBuilderReadFormat();
		System.out.println(oriStr);
		
		testJsAble(env,new EnvironmentConfig(),"The environment configuration JSON does not match expectation");
		
		EnvironmentState envS = new EnvironmentState();
		envS.initialize(env);
		json = envS.toJson();
		oriStr = json.toStringBuilderReadFormat();
		System.out.println(oriStr);
		
		testJsAble(envS,new EnvironmentState(),"The environment state JSON does not match expectation");
	}
}
