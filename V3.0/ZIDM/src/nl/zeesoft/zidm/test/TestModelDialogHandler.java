package nl.zeesoft.zidm.test;

import java.util.Date;

import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdm.test.ZDM;
import nl.zeesoft.zid.dialog.DialogHandler;
import nl.zeesoft.zid.test.TestDialogHandlerObject;
import nl.zeesoft.zidm.dialog.ModelDialogFactory;
import nl.zeesoft.zidm.dialog.model.DialogModel;
import nl.zeesoft.zidm.dialog.pattern.ModelPatternManager;

public class TestModelDialogHandler extends TestDialogHandlerObject {
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

		addScriptLine("A primitive is an object that has a value",ModelDialogFactory.defaultAnswer);
		addScriptLine("Primitives is the plural of primitive",ModelDialogFactory.defaultAnswer);
		addScriptLine("A string is a primitive",ModelDialogFactory.defaultAnswer);
		addScriptLine("Strings is the plural form of string",ModelDialogFactory.defaultAnswer);
		addScriptLine("A name is a string",ModelDialogFactory.defaultAnswer);
		addScriptLine("Names is the plural for name",ModelDialogFactory.defaultAnswer);
		
		addScriptLine("An organism is an object",ModelDialogFactory.defaultAnswer);
		addScriptLine("An animal is an organism",ModelDialogFactory.defaultAnswer);
		addScriptLine("A plant is an organism",ModelDialogFactory.defaultAnswer);
		addScriptLine("A primate is an animal",ModelDialogFactory.defaultAnswer);
		addScriptLine("A human is a primate",ModelDialogFactory.defaultAnswer);
		addScriptLine("humans have names","What are humans?");
		addScriptLine("humans is the plural of human",ModelDialogFactory.defaultAnswer);

		testScript(handler);
		
		System.out.println(handler.getLog());
		
		ZDM.describeModelVersionLogs(model);
		ZDM.describeModelPackages(model,true);
	}
}
