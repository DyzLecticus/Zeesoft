package nl.zeesoft.zsd.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.dialog.Dialog;
import nl.zeesoft.zsd.dialog.DialogSet;
import nl.zeesoft.zsd.dialog.DialogVariable;
import nl.zeesoft.zsd.dialog.dialogs.Generic;
import nl.zeesoft.zsd.dialog.dialogs.GenericHandshake;
import nl.zeesoft.zsd.util.DialogSetToJson;

public class TestDialogSetToJson extends TestEntityToJson {
	public TestDialogSetToJson(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDialogSetToJson(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *DialogSetToJson* instance to convert a list of dialogs into a JSON file.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the list of dialogs");
		System.out.println("DialogSet ds = new DialogSet();");
		System.out.println("// Initialize the EntityValueTranslator");
		System.out.println("ds.initialize();");
		System.out.println("// Create the DialogToJson instance");
		System.out.println("DialogSetToJson convertor = new DialogSetToJson();");
		System.out.println("// Convert the english dialogs to JSON");
		System.out.println("JsFile json = convertor.getJsonForDialogs(ds,\"Optional language code\"));");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestDialogSetToJson.class));
		System.out.println(" * " + getTester().getLinkForClass(DialogSetToJson.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the converted JSON.  ");
	}
	
	@Override
	protected void test(String[] args) {
		DialogSet ds = new DialogSet();
		ds.initialize();
		testDialogSetContent(ds,"before");
		DialogSetToJson convertor = new DialogSetToJson();
		Date started = new Date();
		JsFile json = convertor.toJson(ds,BaseConfiguration.LANG_ENG);
		assertEqual(json.rootElement.children.size(),1,"The number of children does not match expectation");
		if (json.rootElement.children.size()>0) {
			System.out.println("Converting " + json.rootElement.children.get(0).children.size() + " dialogs took: " + ((new Date()).getTime() - started.getTime()) + " ms");
			assertEqual(json.rootElement.children.get(0).children.size(),2,"The number of dialog elements does not match expectation");
			showJsonSample(json);
		}

		ds = new DialogSet();
		List<ZStringBuilder> data = new ArrayList<ZStringBuilder>();
		data.add(json.toStringBuilder());
		ds.initialize(data);
		testDialogSetContent(ds,"after");
	}
	
	protected void testDialogSetContent(DialogSet ds, String suffix) {
		assertEqual(ds.getDialogs().size(),4,"The number of dialogs does not match expectation" + suffix);
		Dialog d = ds.getDialog(BaseConfiguration.LANG_ENG,Generic.MASTER_CONTEXT_GENERIC,GenericHandshake.CONTEXT_GENERIC_HANDSHAKE);
		assertEqual(d!=null,true,"The expected dialog was not found" + suffix);
		if (d!=null) {
			assertEqual(d.getExamples().size(),11,"The number of dialog examples does not match expectation" + suffix);
			DialogVariable dv = d.getVariables().get("firstName");
			assertEqual(dv!=null,true,"The expected dialog variable was not found" + suffix);
			if (dv!=null) {
				assertEqual(dv.examples.size(),8,"The number of dialog variable examples does not match expectation" + suffix);
			}
		}
	}
}
