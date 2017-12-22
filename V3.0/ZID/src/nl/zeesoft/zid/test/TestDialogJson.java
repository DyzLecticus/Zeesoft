package nl.zeesoft.zid.test;

import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zid.dialog.io.DialogJson;

public class TestDialogJson extends TestObject {
	public TestDialogJson(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDialogJson(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to convert *Dialog* objects to and from JSON.  ");
		System.out.println("ZID provides the *DialogJson* class to do this.  ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create dialog");
		System.out.println("Dialog dialog = new Dialog(\"Handshake\",Language.ENG,HandshakeController.class.getName());");
		System.out.println("// Create a dialog JSON io");
		System.out.println("DialogJson io = new DialogJson();");
		System.out.println("// Convert dialog to JSON");
		System.out.println("JsFile json = io.toJson(dialog);");
		System.out.println("// Convert dialog from JSON");
		System.out.println("dialog = io.fromJson(json);");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockDialogs.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestDialogJson.class));
		System.out.println(" * " + getTester().getLinkForClass(MockDialogs.class));
		System.out.println(" * " + getTester().getLinkForClass(Dialog.class));
		System.out.println(" * " + getTester().getLinkForClass(DialogJson.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the JSON structure of the English mock dialog.  ");
	}

	@Override
	protected void test(String[] args) {
		@SuppressWarnings("unchecked")
		List<Dialog> dialogs = (List<Dialog>) getTester().getMockedObject(MockDialogs.class.getName());
		DialogJson io = new DialogJson();
		JsFile file = io.toJson(dialogs.get(0));
		ZStringBuilder js = file.toStringBuilderReadFormat();
		assertEqual(file.rootElement.children.size(),5,"Number of child elements does not match expectation");
		Dialog d = io.fromJson(file);
		file = io.toJson(d);
		ZStringBuilder jsCopy = file.toStringBuilderReadFormat();
		assertEqual(js,jsCopy,"Copy does not match original");
		System.out.println(jsCopy);
	}
}
