package nl.zeesoft.zidm.test;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zid.dialog.DialogHandler;
import nl.zeesoft.zidm.dialog.ModelDialogFactory;
import nl.zeesoft.zspr.pattern.PatternManager;

public class TestModelDialogHandler extends TestObject {
	public TestModelDialogHandler(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestModelDialogHandler(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		// TODO: describe
		/*
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
		System.out.println(" * " + getTester().getLinkForClass(TestModelDialogHandler.class));
		System.out.println(" * " + getTester().getLinkForClass(MockDialogHandler.class));
		System.out.println(" * " + getTester().getLinkForClass(MockDialogs.class));
		System.out.println(" * " + getTester().getLinkForClass(DialogHandler.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the mock initialization duration, the dialog handler log and the average time spent thinking per response.  ");
		*/
	}

	@Override
	protected void test(String[] args) {
		ModelDialogFactory mdf = new ModelDialogFactory();
		PatternManager pm = new PatternManager();
		pm.initializePatterns();
		DialogHandler handler = new DialogHandler(mdf.getModelDialogs(),pm);
		handler.initialize();

		ZStringSymbolParser output = handler.handleInput(new ZStringSymbolParser("A primitive is an object that has a value"));
		assertEqual(output.toString(),ModelDialogFactory.defaultAnswer,"Output does not match expectation");
		output = handler.handleInput(new ZStringSymbolParser("Primitives is the plural of primitive"));
		assertEqual(output.toString(),ModelDialogFactory.defaultAnswer,"Output does not match expectation");
		output = handler.handleInput(new ZStringSymbolParser("A string is a primitive"));
		assertEqual(output.toString(),ModelDialogFactory.defaultAnswer,"Output does not match expectation");
		output = handler.handleInput(new ZStringSymbolParser("Strings the plural form of string"));
		assertEqual(output.toString(),ModelDialogFactory.defaultAnswer,"Output does not match expectation");
		output = handler.handleInput(new ZStringSymbolParser("A name is a string"));
		assertEqual(output.toString(),ModelDialogFactory.defaultAnswer,"Output does not match expectation");
		output = handler.handleInput(new ZStringSymbolParser("Names the plural for name"));
		assertEqual(output.toString(),ModelDialogFactory.defaultAnswer,"Output does not match expectation");
		
		output = handler.handleInput(new ZStringSymbolParser("An organism is an object"));
		assertEqual(output.toString(),ModelDialogFactory.defaultAnswer,"Output does not match expectation");
		output = handler.handleInput(new ZStringSymbolParser("An animal is an organism"));
		assertEqual(output.toString(),ModelDialogFactory.defaultAnswer,"Output does not match expectation");
		output = handler.handleInput(new ZStringSymbolParser("A plant is an organism"));
		assertEqual(output.toString(),ModelDialogFactory.defaultAnswer,"Output does not match expectation");
		output = handler.handleInput(new ZStringSymbolParser("A primate is an animal"));
		assertEqual(output.toString(),ModelDialogFactory.defaultAnswer,"Output does not match expectation");
		output = handler.handleInput(new ZStringSymbolParser("A human is a primate"));
		assertEqual(output.toString(),ModelDialogFactory.defaultAnswer,"Output does not match expectation");
		output = handler.handleInput(new ZStringSymbolParser("humans have names"));
		assertEqual(output.toString(),ModelDialogFactory.defaultAnswer,"Output does not match expectation");
		
		System.out.println(handler.getLog());
		
		/*
		DialogHandler handler = (DialogHandler) getTester().getMockedObject(MockDialogHandler.class.getName());
		
		Date started = new Date();
		
		// Known user handshake
		ZStringSymbolParser output = handler.handleInput(new ZStringSymbolParser("hello"));
		assertEqual(output.toString(),"Hello. My name is Dyz Lecticus. What is your name?","Output does not match expectation");
		output = handler.handleInput(new ZStringSymbolParser("my name is andre van der zee"));
		assertEqual(output.toString(),"Nice to interact with you again Andre van der Zee.","Output does not match expectation");
		
		// Extended handshake
		output = handler.handleInput(new ZStringSymbolParser("hallo,ik heet karel de grote."));
		assertEqual(output.toString(),"Wat kan ik voor je doen Karel de Grote?","Output does not match expectation");
		
		// Handshake prompt sequence
		output = handler.handleInput(new ZStringSymbolParser("hallo?"));
		assertEqual(output.toString(),"Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?","Output does not match expectation");
		output = handler.handleInput(new ZStringSymbolParser("gekke henkie"));
		assertEqual(output.toString(),"Wat kan ik voor je doen Gekke Henkie?","Output does not match expectation");
		output = handler.handleInput(new ZStringSymbolParser("gekke"));
		assertEqual(output.toString(),"Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?","Output does not match expectation");
		output = handler.handleInput(new ZStringSymbolParser("van henkie"));
		assertEqual(output.toString(),"Wat is jouw voornaam?","Output does not match expectation");
		output = handler.handleInput(new ZStringSymbolParser("gekke"));
		assertEqual(output.toString(),"Wat kan ik voor je doen Gekke van Henkie?","Output does not match expectation");

		// Handshake context switch
		output = handler.handleInput(new ZStringSymbolParser("what is your name?"));
		assertEqual(output.toString(),"My name is Dyz Lecticus. What is your name?","Output does not match expectation");
		output = handler.handleInput(new ZStringSymbolParser("hoe heet jij?"));
		assertEqual(output.toString(),"Mijn naam is Dyz Lecticus. Wat is jouw naam?","Output does not match expectation");

		System.out.println(handler.getLog());

		long time = ((new Date()).getTime() - started.getTime()) / 10; 
		System.out.println("Average time spent thinking per response: " + time + " ms");
		*/
	}
}
