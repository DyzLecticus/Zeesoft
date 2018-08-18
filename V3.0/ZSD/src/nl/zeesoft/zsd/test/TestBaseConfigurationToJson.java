package nl.zeesoft.zsd.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.BaseConfiguration;

public class TestBaseConfigurationToJson extends TestEntityToJson {
	public TestBaseConfigurationToJson(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestBaseConfigurationToJson(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to convert a *BaseConfiguration* instance to JSON.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the configuration");
		System.out.println("BaseConfiguration bc = new BaseConfiguration();");
		System.out.println("// Convert the configuration to JSON");
		System.out.println("JsFile json = bc.toJson();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestBaseConfigurationToJson.class));
		System.out.println(" * " + getTester().getLinkForClass(BaseConfiguration.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the converted JSON.  ");
	}
	
	@Override
	protected void test(String[] args) {
		BaseConfiguration bc = new BaseConfiguration();
		bc.setName("Test");
		bc.setEmail("test@email.com");
		bc.setSmiley(":]");
		bc.setFrowny(":[");
		bc.setDebug(true);
		bc.setDataDir("testData");
		bc.setBaseDir("testBase/");
		bc.setOverrideDir("testOverride/");
		bc.setExtendDir("testExtend/");
		bc.setMaxMsInterpretPerSymbol(111);
		bc.setMaxMsInterpretPerSequence(2222);
		bc.setMaxMsDialogPerSequence(1111);
		bc.setSelfTestBaseLineFileName("testFileName.json");
		bc.getParameters().put("test1","test1Val");
		bc.getParameters().put("test2","test2Val");
		JsFile json = bc.toJson();
		System.out.println(json.toStringBuilderReadFormat());
		assertEqual(json.rootElement.children.size(),18,"The number of children does not match expectation");
		
		BaseConfiguration bcTest = new BaseConfiguration();
		bcTest.fromJson(json);
		JsFile jsonTest = bcTest.toJson();
		ZStringBuilder str = json.toStringBuilderReadFormat();
		ZStringBuilder strTest = jsonTest.toStringBuilderReadFormat();
		assertEqual(json.toStringBuilder(),jsonTest.toStringBuilder(),"The converted JSON does not match the original");
		if (!str.equals(strTest)) {
			System.out.println();
			System.out.println(strTest);
		}
	}
}
