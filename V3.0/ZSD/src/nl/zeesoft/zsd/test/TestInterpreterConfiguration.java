package nl.zeesoft.zsd.test;

import java.util.List;

import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.dialog.DialogSet;
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
		/*
		System.out.println("This test shows how to use an *Initializer* instance to instantiate and initialize multiple classes simultaneously.");
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
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestInterpreterConfiguration.class));
		System.out.println(" * " + getTester().getLinkForClass(Initializer.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test the time it takes to initialize two objects simultaneously.  ");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		DialogSet ds = new DialogSet();
		ds.initialize();
		InterpreterConfiguration config = new InterpreterConfiguration(ds);
		config.setBaseDir("resources/");
		config.addListener(this);
		List<InitializeClass> clss = config.getInitializeClasses();
		for (InitializeClass cls: clss) {
			if (cls.fileName.length()>0) {
				System.out.println(cls.obj.getClass().getName() + "(" + cls.name + ") <= " + cls.fileName);
			} else {
				System.out.println(cls.obj.getClass().getName() + "(" + cls.name + ")");
			}
		}
		System.out.println();
		config.initialize();
		while (!config.isDone()) {
			sleep(100);
		}
		assertEqual(getInitialized(),8,"The number of initialized classes does not match expectation");
	}
}
