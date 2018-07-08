package nl.zeesoft.zsd.test;

import java.util.Date;

import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.dialog.DialogSet;
import nl.zeesoft.zsd.util.DialogToJson;

public class TestDialogToJson extends TestEntityToJson {
	public TestDialogToJson(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDialogToJson(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use an *DialogToJson* instance to convert a list of dialogs into a JSON file.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the list of dialogs");
		System.out.println("DialogSet ds = new DialogSet();");
		System.out.println("// Initialize the EntityValueTranslator");
		System.out.println("ds.initialize();");
		System.out.println("// Create the DialogToJson instance");
		System.out.println("DialogToJson convertor = new DialogToJson();");
		System.out.println("// Convert the dialogs to JSON");
		System.out.println("JsFile json = convertor.getJsonForDialogs(ds.getDialogs());");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestDialogToJson.class));
		System.out.println(" * " + getTester().getLinkForClass(DialogToJson.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows a sample of the converted JSON.  ");
	}
	
	@Override
	protected void test(String[] args) {
		DialogSet ds = new DialogSet();
		ds.initialize();
		DialogToJson convertor = new DialogToJson();
		Date started = new Date();
		JsFile json = convertor.getJsonForDialogs(ds.getDialogs());
		System.out.println("Converting " + json.rootElement.children.size() + " dialog examples took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		assertEqual(json.rootElement.children.size(),71,"The number of children does not match expectation");
		showJsonSample(json);
	}
}