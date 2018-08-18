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
		// TODO: Describe.
		/*
		System.out.println("This test shows how to use a *DialogSetToJson* instance to convert a list of dialogs into a JSON file.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the dialog set");
		System.out.println("DialogSet ds = new DialogSet();");
		System.out.println("// Initialize the dialog set");
		System.out.println("ds.initialize();");
		System.out.println("// Create the DialogToJson instance");
		System.out.println("DialogSetToJson convertor = new DialogSetToJson();");
		System.out.println("// Convert the english dialogs to JSON");
		System.out.println("JsFile json = convertor.getJsonForDialogs(ds,\"Optional language code\"));");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockEntityValueTranslator.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestDialogSetToJson.class));
		System.out.println(" * " + getTester().getLinkForClass(DialogSetToJson.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows a sample of the converted JSON.  ");
		*/
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
