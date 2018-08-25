package nl.zeesoft.zodb.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.app.AppZODB;

public class TestConfig extends TestObject {
	public TestConfig(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestConfig(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		// TODO: Describe
		/*
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
		*/
	}
	
	@Override
	protected void test(String[] args) {
		Config config = new Config();
		config.getApplication(AppZODB.NAME).url = "http://test.domain";
		config.setSelfTest(false);
		config.setDataDir("dir/");
		config.initialize(true,"installDir/","http://url.domain",false);
		config.destroy();
		String fullDataDir = config.getFullDataDir();
		
		JsFile json = config.toJson();
		assertEqual(json.rootElement.children.size(),5,"Number of children does not match expectation");
		ZStringBuilder oriStr = json.toStringBuilderReadFormat();
		System.out.println(oriStr);
		
		config = new Config();
		config.initialize(false,"installDir/","http://different.domain",false);
		config.destroy();
		config.fromJson(json);
		
		json = config.toJson();
		ZStringBuilder newStr = json.toStringBuilderReadFormat();
		if (!oriStr.equals(newStr)) {
			assertEqual(false,true,"Configuration does not match original");
			System.out.println(newStr);
		}
		assertEqual(config.getFullDataDir(),fullDataDir,"Full data directory does not match expectation");
	}
}
