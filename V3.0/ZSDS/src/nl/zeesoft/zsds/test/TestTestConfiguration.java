package nl.zeesoft.zsds.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsds.tester.TestConfiguration;
import nl.zeesoft.zsds.tester.TestEnvironment;

public class TestTestConfiguration extends TestObject {
	public TestTestConfiguration(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestTestConfiguration(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to convert a *TestConfiguration* to and from JSON.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the test configuration");
		System.out.println("TestConfiguration tco = new TestConfiguration();");
		System.out.println("// Initialize the test configuration");
		System.out.println("tco.initialize();");
		System.out.println("// Convert the test configuration to JSON");
		System.out.println("JsFile json = tco.toJson();");
		System.out.println("// Convert the test configuration from JSON");
		System.out.println("tco.fromJson(json);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestTestConfiguration.class));
		System.out.println(" * " + getTester().getLinkForClass(TestConfiguration.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the converted JSON.  ");
	}
	
	@Override
	protected void test(String[] args) {
		TestConfiguration tco = new TestConfiguration();
		tco.setTestCaseDir("tests/");
		tco.setDefaultSleep(20);
		tco.setRetryIfBusy(false);
		TestEnvironment env = new TestEnvironment();
		env.name = "env";
		env.url = "http://env:9090";
		env.directory = "dir/";
		tco.getEnvironments().add(env);
		
		JsFile json = tco.toJson();
		ZStringBuilder oriJs = json.toStringBuilderReadFormat();
		
		assertEqual(json.rootElement.children.size(),5,"Number of children expectation");
		JsElem envsElem = json.rootElement.getChildByName("environments");
		assertEqual(envsElem.children.size(),1,"Number of environments does not match expectation");
		
		tco = new TestConfiguration();
		tco.fromJson(json);
		json = tco.toJson();
		ZStringBuilder newJs = json.toStringBuilderReadFormat();
		
		System.out.println(oriJs);
		if (!newJs.equals(oriJs)) {
			System.err.println();
			System.err.println(newJs);
			assertEqual(true,false,"Parsed test configuration does not match original");
		}
	}
}
