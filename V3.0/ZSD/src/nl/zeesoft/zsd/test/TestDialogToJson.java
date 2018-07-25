package nl.zeesoft.zsd.test;

import java.util.Date;

import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.DialogSet;
import nl.zeesoft.zsd.util.DialogToJson;

public class TestDialogToJson extends TestEntityToJson {
	private static final int	SEQUENCE_ELEMENTS	= 1817;
	
	public TestDialogToJson(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDialogToJson(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *DialogToJson* instance to convert a list of dialogs into a JSON file.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create dialog set");
		System.out.println("DialogSet ds = new DialogSet();");
		System.out.println("// Initialize the dialog set");
		System.out.println("ds.initialize();");
		System.out.println("// Create the DialogToJson instance");
		System.out.println("DialogToJson convertor = new DialogToJson();");
		System.out.println("// Convert the dialogs to JSON");
		System.out.println("JsFile json = convertor.getJsonForDialogs(ds.getDialogs());");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockEntityValueTranslator.class.getName());
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
		EntityValueTranslator t = (EntityValueTranslator) getTester().getMockedObject(MockEntityValueTranslator.class.getName());
		DialogSet ds = new DialogSet();
		ds.initialize(t);
		DialogToJson convertor = new DialogToJson();
		Date started = new Date();
		JsFile json = convertor.getJsonForDialogs(ds.getDialogs());
		assertEqual(json.rootElement.children.size(),1,"The number of children does not match expectation");
		if (json.rootElement.children.size()>0) {
			System.out.println("Converting " + json.rootElement.children.get(0).children.size() + " dialog examples took: " + ((new Date()).getTime() - started.getTime()) + " ms");
			assertEqual(json.rootElement.children.get(0).children.size(),SEQUENCE_ELEMENTS,"The number of sequence elements does not match expectation");
			showJsonSample(json,10);
		}
	}
}
