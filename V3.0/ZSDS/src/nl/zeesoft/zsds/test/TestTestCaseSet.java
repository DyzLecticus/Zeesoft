package nl.zeesoft.zsds.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.dialog.DialogRequest;
import nl.zeesoft.zsd.dialog.DialogResponse;
import nl.zeesoft.zsds.util.TestCase;
import nl.zeesoft.zsds.util.TestCaseIO;
import nl.zeesoft.zsds.util.TestCaseSet;

public class TestTestCaseSet extends TestObject {
	public TestTestCaseSet(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestTestCaseSet(new Tester())).test(args);
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
		String name = "TC001";
		TestCaseSet tcs = new TestCaseSet();
		TestCase tc = new TestCase();
		tc.name = name;
		tcs.getTestCases().add(tc);
		TestCaseIO tcIO = new TestCaseIO();
		tcIO.request = new DialogRequest("What is your name?");
		tcIO.expectedResponse = new DialogResponse();
		tc.io.add(tcIO);
		tcIO = new TestCaseIO();
		tcIO.request = new DialogRequest("What is your goal?");
		tcIO.expectedResponse = new DialogResponse();
		tc.io.add(tcIO);

		JsFile json = tcs.toJson();
		ZStringBuilder oriJs = json.toStringBuilderReadFormat();
		
		assertEqual(json.rootElement.children.size(),1,"Number of children expectation");
		JsElem tcsElem = json.rootElement.children.get(0);
		assertEqual(tcsElem.children.size(),1,"Number of test cases does not match expectation");
		if (tcsElem.children.size()>0) {
			assertEqual(tcsElem.children.get(0).getChildByName("io").children.size(),2,"Number of test case input and output combinations does not match expectation");
		}
		
		tcs = new TestCaseSet();
		tcs.fromJson(json);
		json = tcs.toJson();
		ZStringBuilder newJs = json.toStringBuilderReadFormat();
		
		System.out.println(oriJs);
		if (!newJs.equals(oriJs)) {
			System.err.println();
			System.err.println(newJs);
			assertEqual(true,false,"Parsed test cases set does not match original");
		}
		TestCase testCase = tcs.getTestCase(name);
		assertEqual(testCase!=null,true,"The test case named '" + name + "' not found");
	}
}
