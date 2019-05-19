package nl.zeesoft.zodb.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModZODB;

public class TestConfig extends TestObject {
	public TestConfig(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestConfig(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to convert a *Config* instance to and from JSON.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the test configuration");
		System.out.println("Config config = new Config();");
		System.out.println("// Convert the test configuration to JSON");
		System.out.println("JsFile json = config.toJson();");
		System.out.println("// Convert the test configuration from JSON");
		System.out.println("config.fromJson(json);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestConfig.class));
		System.out.println(" * " + getTester().getLinkForClass(Config.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the converted JSON.  ");
	}
	
	@Override
	protected void test(String[] args) {
		Config config = new Config();
		config.getModule(ModZODB.NAME).url = "http://test.domain";
		config.getModule(ModZODB.NAME).selfTest = true;
		config.setDebug(true);
		config.setDataDir("dir/");
		config.setZODBKey(new StringBuilder("23894756239847569823478569283465987234658976"));
		String fullDataDir = config.getFullDataDir();
		StringBuilder key = config.getZODBKey();
		
		JsFile json = config.toJson();
		assertEqual(json.rootElement.children.size(),4,"Number of children does not match expectation");
		ZStringBuilder oriStr = json.toStringBuilderReadFormat();
		System.out.println(oriStr);
		
		config = new Config();
		config.fromJson(json);
		
		json = config.toJson();
		ZStringBuilder newStr = json.toStringBuilderReadFormat();
		if (!oriStr.equals(newStr)) {
			assertEqual(false,true,"Configuration does not match original");
			System.out.println(newStr);
		}
		assertEqual(config.getFullDataDir(),fullDataDir,"Full data directory does not match expectation");
		assertEqual(config.getZODBKey(),key,"Key does not match expectation");
	}
}
