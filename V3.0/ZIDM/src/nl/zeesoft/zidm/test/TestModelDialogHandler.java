package nl.zeesoft.zidm.test;

import java.util.Date;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdm.test.ZDM;
import nl.zeesoft.zid.dialog.DialogHandler;
import nl.zeesoft.zidm.dialog.ModelDialogFactory;
import nl.zeesoft.zidm.dialog.model.DialogModel;
import nl.zeesoft.zidm.dialog.pattern.ModelPatternManager;

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
		System.out.println("Initializing model dialog handler ...");
		Date start = new Date();
		ModelDialogFactory mdf = new ModelDialogFactory();
		ModelPatternManager mpm = mdf.getModelPatternManager();
		mpm.initializePatterns();
		DialogModel model = mdf.getDialogModel();
		DialogHandler handler = new DialogHandler(mdf.getModelDialogs(model),mpm);
		handler.initialize();
		System.out.println("Initializing model dialog handler took " + ((new Date()).getTime() - start.getTime()) + " ms");
		System.out.println();

		ZStringSymbolParser output = handler.handleInput(new ZStringSymbolParser("A primitive is an object that has a value"));
		assertEqual(output.toString(),ModelDialogFactory.defaultAnswer,"Output does not match expectation");
		output = handler.handleInput(new ZStringSymbolParser("Primitives is the plural of primitive"));
		assertEqual(output.toString(),ModelDialogFactory.defaultAnswer,"Output does not match expectation");
		output = handler.handleInput(new ZStringSymbolParser("A string is a primitive"));
		assertEqual(output.toString(),ModelDialogFactory.defaultAnswer,"Output does not match expectation");
		output = handler.handleInput(new ZStringSymbolParser("Strings is the plural form of string"));
		assertEqual(output.toString(),ModelDialogFactory.defaultAnswer,"Output does not match expectation");
		output = handler.handleInput(new ZStringSymbolParser("A name is a string"));
		assertEqual(output.toString(),ModelDialogFactory.defaultAnswer,"Output does not match expectation");
		output = handler.handleInput(new ZStringSymbolParser("Names is the plural for name"));
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
		assertEqual(output.toString(),"What are humans?","Output does not match expectation");
		output = handler.handleInput(new ZStringSymbolParser("humans is the plural of human"));
		assertEqual(output.toString(),ModelDialogFactory.defaultAnswer,"Output does not match expectation");
		
		System.out.println(handler.getLog());
		
		ZDM.describeModelVersionLogs(model);
		ZDM.describeModelPackages(model,true);
	}
}
