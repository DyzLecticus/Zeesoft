package nl.zeesoft.zsd.test;

import java.util.List;

import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.initialize.InitializeClass;
import nl.zeesoft.zsd.interpret.InterpreterConfiguration;

public class TestInterpreterConfiguration extends TestInitializer {
	public TestInterpreterConfiguration(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestInterpreterConfiguration(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test uses a lot of JSON files to initialize an *InterpreterConfiguration* instance.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the Initializer");
		System.out.println("Initializer init = new Initializer();");
		System.out.println("// Add a listener");
		System.out.println("init.addListener(this);");
		System.out.println("// Add a class to initialize");
		System.out.println("init.addClass(\"uniqueName\",SequenceClassifier.class.getName(),\"" + TestSequenceClassifier.QNA_FILE_NAME + "\");");
		System.out.println("// Start the initialization");
		System.out.println("init.start();");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockEntityValueTranslator.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestInterpreterConfiguration.class));
		System.out.println(" * " + getTester().getLinkForClass(MockInterpreterConfiguration.class));
		System.out.println(" * " + getTester().getLinkForClass(InterpreterConfiguration.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test the time it takes to initialize all objects simultaneously.  ");
	}
	
	@Override
	protected void test(String[] args) {
		InterpreterConfiguration config = (InterpreterConfiguration) getTester().getMockedObject(MockInterpreterConfiguration.class.getName());
		config.addListener(this);
		List<InitializeClass> clss = config.getInitializeClasses();
		System.out.println("Classes:");
		for (InitializeClass cls: clss) {
			if (cls.fileName.length()>0) {
				System.out.println("- " + cls.obj.getClass().getName() + " (" + cls.name + ") <= " + cls.fileName);
			} else {
				System.out.println("- " + cls.obj.getClass().getName() + " (" + cls.name + ")");
			}
		}
		System.out.println();
		config.initialize();
		while (!config.isDone()) {
			sleep(100);
		}
		assertEqual(getInitialized(),8,"The number of initialized classes does not match expectation");
		assertEqual(getErrors(),0,"The number of errors does not match expectation");
	}
}
