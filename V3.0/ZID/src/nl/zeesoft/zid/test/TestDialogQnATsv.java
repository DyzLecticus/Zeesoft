package nl.zeesoft.zid.test;

import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zid.dialog.io.DialogQnATsv;
import nl.zeesoft.zspr.Language;

public class TestDialogQnATsv extends TestObject {
	public TestDialogQnATsv(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDialogQnATsv(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to convert 'Question and Answer' *Dialog* objects to and from TSV (Tab Separated Values).  ");
		System.out.println("ZID provides the *DialogQnATsv* class to do this.  ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create dialog");
		System.out.println("Dialog dialog = new Dialog(\"Handshake\",Language.ENG,HandshakeController.class.getName());");
		System.out.println("// Create a dialog QnA TSV io");
		System.out.println("DialogQnATsv io = new DialogQnATsv();");
		System.out.println("// Convert dialog to QnA TSV");
		System.out.println("ZStringBuilder tsv = io.toQnATsv(dialog);");
		System.out.println("// Convert dialog from QnA TSV");
		System.out.println("dialog = io.fromQnATsv(tsv,Language.ENG,HandshakeDialogController.class.getName());");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockDialogs.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestDialogQnATsv.class));
		System.out.println(" * " + getTester().getLinkForClass(MockDialogs.class));
		System.out.println(" * " + getTester().getLinkForClass(Dialog.class));
		System.out.println(" * " + getTester().getLinkForClass(DialogQnATsv.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the TSV structure of the English mock dialog.  ");
	}

	@Override
	protected void test(String[] args) {
		@SuppressWarnings("unchecked")
		List<Dialog> dialogs = (List<Dialog>) getTester().getMockedObject(MockDialogs.class.getName());
		DialogQnATsv io = new DialogQnATsv();
		ZStringBuilder tsv = io.toQnATsv(dialogs.get(0));
		List<ZStringBuilder> lines = tsv.split("\n");
		assertEqual(lines.size(),30,"Number of lines does not match expectation");
		Dialog d = io.fromQnATsv(tsv,Language.ENG,HandshakeDialogController.class.getName());
		assertEqual(d.getExamples().size(),10,"Number of examples does not match expectation");
		assertEqual(d.getVariables().size(),4,"Number of variables does not match expectation");
		if (d.getVariables().size()>0) {
			assertEqual(d.getVariables().get(0).getExamples().size(),10,"Number of variable examples does not match expectation");
		}
		System.out.println(tsv);
		// Test split dialogs
		List<Dialog> split = io.fromQnATsv(tsv,Language.ENG,HandshakeDialogController.class.getName(),true);
		assertEqual(d.getVariables().size(),4,"Number of dialogs does not match expectation");
		tsv = io.toQnATsv(split);
		assertEqual(tsv.length(),1678,"TSV length does not match expectation");
	}
}
