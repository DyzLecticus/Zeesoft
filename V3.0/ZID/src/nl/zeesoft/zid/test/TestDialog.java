package nl.zeesoft.zid.test;

import java.util.List;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zid.dialog.DialogControllerObject;
import nl.zeesoft.zid.dialog.DialogExample;
import nl.zeesoft.zid.dialog.DialogVariable;
import nl.zeesoft.zid.dialog.DialogVariableExample;
import nl.zeesoft.zspr.Language;
import nl.zeesoft.zspr.pattern.PatternObject;

public class TestDialog extends TestObject {
	public TestDialog(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDialog(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create *Dialog* objects.  ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create dialog");
		System.out.println("Dialog dialog = new Dialog(\"Handshake\",Language.ENG,HandshakeController.class.getName());");
		System.out.println("// Add an example");
		System.out.println("dialog.addExample(\"Hello. My name is {firstName} {preposition} {lastName}.\",\"Hello {fullName}.\");");
		System.out.println("// Add a variable");
		System.out.println("dialog.addVariable(\"firstName\",PatternObject.TYPE_ALPHABETIC);");
		System.out.println("// Add a variable example");
		System.out.println("dialog.addVariableExample(\"firstName\",\"What is your name?\",\"My name is {firstName} {preposition} {lastName}.\");");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("A *Dialog* requires a controller class name that refers to a class that extends the *DialogControllerObject*.  ");
		System.out.println("This controller is instantiated by the *DialogHandler* when it detects that the input requires it.  ");
		System.out.println("The dialog controller is then notified whenever the *DialogHandler* has updated dialog variable values.  ");
		System.out.println("Dialog controller can update dialog handler variables which can then be accessed by other dialog controllers.  ");
		System.out.println();
		getTester().describeMock(MockDialogs.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestDialog.class));
		System.out.println(" * " + getTester().getLinkForClass(MockDialogs.class));
		System.out.println(" * " + getTester().getLinkForClass(Dialog.class));
		System.out.println(" * " + getTester().getLinkForClass(DialogControllerObject.class));
		System.out.println(" * " + getTester().getLinkForClass(HandshakeController.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the dialog structure of the mock dialogs.  ");
	}

	@Override
	protected void test(String[] args) {
		@SuppressWarnings("unchecked")
		List<Dialog> dialogs = (List<Dialog>) getTester().getMockedObject(MockDialogs.class.getName());
		for (Dialog dialog: dialogs) {
			System.out.println("Dialog: " + dialog.getName() + ", language: " + dialog.getLanguage().getCode() + ", controller: " + dialog.getControllerClassName());
			System.out.println("- Examples;");
			for (DialogExample example: dialog.getExamples()) {
				System.out.println("  - " + example.getInput() + " " + example.getOutput());
			}
			System.out.println("- Variables;");
			for (DialogVariable variable: dialog.getVariables()) {
				System.out.println("  - Variable: " + variable.getName() + ", type: " + variable.getType());
				if (variable.getExamples().size()>0) {
					System.out.println("    - Variable examples;");
					for (DialogVariableExample example: variable.getExamples()) {
						System.out.println("      - " + example.getQuestion() + " " + example.getAnswer());
					}
				}
			}
		}
		assertEqual(dialogs.size(),2,"Number of dialogs does not match expectation");
		if (dialogs.size()>0) {
			assertEqual(dialogs.get(0).getLanguage().getCode(),Language.ENG,"Dialog language does not match expectation");
			assertEqual(dialogs.get(0).getControllerClassName(),HandshakeController.class.getName(),"Dialog controller does not match expectation");
			assertEqual(dialogs.get(0).getExamples().size(),11,"Number of dialog examples does not match expectation");
			assertEqual(dialogs.get(0).getVariables().size(),4,"Number of dialog variables does not match expectation");
			if (dialogs.get(0).getVariables().size()>0) {
				assertEqual(dialogs.get(0).getVariables().get(0).getType(),PatternObject.TYPE_ALPHABETIC,"Dialog variable type does not match expectation");
				assertEqual(dialogs.get(0).getVariables().get(0).getExamples().size(),10,"Number of dialog variable examples does not match expectation");
			}
		}
	}
}
