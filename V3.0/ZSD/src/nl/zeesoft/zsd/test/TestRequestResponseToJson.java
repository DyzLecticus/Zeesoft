package nl.zeesoft.zsd.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.dialog.DialogRequest;
import nl.zeesoft.zsd.dialog.DialogVariableValue;
import nl.zeesoft.zsd.dialog.dialogs.Generic;
import nl.zeesoft.zsd.dialog.dialogs.GenericHandshake;

public class TestRequestResponseToJson extends TestObject {
	public TestRequestResponseToJson(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestRequestResponseToJson(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to convert a *DialogRequest* and *DialogResponse* objects from and to JSON respectively.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the dialog request");
		System.out.println("DialogRequest dr = new DialogRequest();");
		System.out.println("// Create the JSON");
		System.out.println("JsFile json = new JsFile();");
		System.out.println("// Convert the dialog request from JSON");
		System.out.println("dr.fromJson(json);");
		// TODO: Dialog response
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestRequestResponseToJson.class));
		System.out.println(" * " + getTester().getLinkForClass(DialogRequest.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the converted JSON.  ");
	}
	
	@Override
	protected void test(String[] args) {
		DialogRequest dr = new DialogRequest();
		dr.setAllActions(true);
		dr.prompt.append("test prompt?");
		dr.input.append("test input.");
		dr.language = BaseConfiguration.LANG_ENG;
		dr.masterContext = Generic.MASTER_CONTEXT_GENERIC;
		dr.context = GenericHandshake.CONTEXT_GENERIC_HANDSHAKE;
		dr.classifyContextThreshold = 0.8;
		dr.translateEntityTypes.add(BaseConfiguration.TYPE_NAME);
		dr.matchThreshold = 0.8;
		dr.randomizeOutput = false;
		DialogVariableValue dvv = new DialogVariableValue();
		dvv.name = "testVariable";
		dvv.externalValue = "testExtVal";
		dvv.internalValue = "testIntVal";
		dr.dialogVariableValues.put(dvv.name,dvv);
		JsFile json = dr.toJson();
		ZStringBuilder txtOri = json.toStringBuilderReadFormat();
		System.out.println(txtOri);
		assertEqual(json.rootElement.children.size(),16,"The number of children does not match expectation");
		dr = new DialogRequest();
		dr.fromJson(json);
		json = dr.toJson();
		ZStringBuilder txtNew = json.toStringBuilderReadFormat();
		assertEqual(txtOri.length(),txtNew.length(),"The request does not match the original");
		if (!txtOri.equals(txtNew)) {
			System.err.println(txtNew);
		}
	}
}
