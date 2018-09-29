package nl.zeesoft.zsdm.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zevt.type.Types;
import nl.zeesoft.znlb.lang.Languages;
import nl.zeesoft.zsdm.dialog.Dialog;

public class TestDialog extends TestObject {
	public TestDialog(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDialog(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to convert *Dialog* instances to and from JSON.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the dialog");
		System.out.println("Dialog dialog = new Dialog();");
		System.out.println("// Convert the dialog to JSON");
		System.out.println("JsFile json = dialog.toJson();");
		System.out.println("// Convert the dialog from JSON");
		System.out.println("dialog.fromJson(json);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestDialog.class));
		System.out.println(" * " + getTester().getLinkForClass(Dialog.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the converted JSON.  ");
	}
	
	@Override
	protected void test(String[] args) {
		Dialog dialog = new Dialog();
		dialog.setlanguage(Languages.ENG);
		dialog.setMasterContext("MasterContext");
		dialog.setContext("Context");
		dialog.addExample("Test input 1.","Test output 1.");
		dialog.addExample("Test input 2.","Test output 2.");
		dialog.addVariable("testVariable1",Types.NUMERIC);
		dialog.addVariablePrompt("testVariable1","Test prompt 1?");
		dialog.addVariablePrompt("testVariable1","Test prompt 2?");
		dialog.addVariable("testVariable2",Types.CURRENCY);
		dialog.addVariablePrompt("testVariable2","Test prompt 1?");
		dialog.addVariablePrompt("testVariable2","Test prompt 2?");
		
		JsFile json = dialog.toJson();
		ZStringBuilder oriStr = json.toStringBuilderReadFormat();
		System.out.println(oriStr);
		
		dialog = new Dialog();
		dialog.fromJson(json);
		json = dialog.toJson();
		ZStringBuilder newStr = json.toStringBuilderReadFormat();
		if (!newStr.equals(oriStr)) {
			assertEqual(false,true,"Converted JSON does not match original");
			System.err.println(newStr);
		}
	}
}
