package nl.zeesoft.zid.test;

import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zid.dialog.DialogHandler;

public class TestDialogHandler extends TestDialogHandlerObject {
	public TestDialogHandler(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDialogHandler(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create a *DialogHandler* and then use it to produce dialog output for input.  ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create dialog handler");
		System.out.println("DialogHandler handler = new DialogHandler(dialogs,patternManager);");
		System.out.println("// Initialize dialog handler");
		System.out.println("handler.initialize();");
		System.out.println("// Handle dialog input");
		System.out.println("ZStringSymbolParser output = handler.handleInput(new ZStringSymbolParser(\"hello\"));");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("A *DialogHandler* requires a list of dialogs and a *PatternManager*.  ");
		System.out.println();
		getTester().describeMock(MockDialogHandler.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestDialogHandler.class));
		System.out.println(" * " + getTester().getLinkForClass(MockDialogHandler.class));
		System.out.println(" * " + getTester().getLinkForClass(MockDialogs.class));
		System.out.println(" * " + getTester().getLinkForClass(DialogHandler.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the mock initialization duration, the dialog handler log and the average time spent thinking per response.  ");
	}

	@Override
	protected void test(String[] args) {
		DialogHandler handler = (DialogHandler) getTester().getMockedObject(MockDialogHandler.class.getName());
		
		// Known user handshake
		addScriptLine("hello","Hello. My name is Dyz Lecticus. What is your name?");
		addScriptLine("my name is andre van der zee","Nice to interact with you again Andre van der Zee.");
				
		// Extended handshake
		addScriptLine("hallo,ik heet karel de grote.","Wat kan ik voor je doen Karel de Grote?");
		
		// Handshake prompt sequence
		addScriptLine("hallo?","Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?");
		addScriptLine("gekke henkie","Wat kan ik voor je doen Gekke Henkie?");
		addScriptLine("gekke","Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?");
		addScriptLine("van henkie","Wat is jouw voornaam?");
		addScriptLine("gekke","Wat kan ik voor je doen Gekke van Henkie?");

		// Handshake context switch
		addScriptLine("what is your name?","My name is Dyz Lecticus. What is your name?");
		addScriptLine("hoe heet jij?","Mijn naam is Dyz Lecticus. Wat is jouw naam?");
		
		testScript(handler);

		System.out.println(handler.getLog());
	}
}
