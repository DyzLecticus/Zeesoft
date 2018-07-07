package nl.zeesoft.zsd.test;

import java.util.List;

import nl.zeesoft.zdk.test.TestObject;
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
		System.out.println("// Create a DialogSet");
		System.out.println("DialogSet ds = new DialogSet();");
		System.out.println("// Initialize the DialogSet");
		System.out.println("ds.initialize();");
		System.out.println("// Create the InterpreterConfiguration");
		System.out.println("InterpreterConfiguration config = new InterpreterConfiguration(ds);");
		System.out.println("// Add a listener");
		System.out.println("config.addListener(this);");
		System.out.println("// Start the initialization");
		System.out.println("config.initialize();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestInterpreterConfiguration.class));
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
	
	protected static InterpreterConfiguration getConfig(Tester tester) {
		InterpreterConfiguration config = (InterpreterConfiguration) tester.getMockedObject(MockInterpreterConfiguration.class.getName());
		TestInterpreterConfiguration tst = null;
		for (TestObject test: tester.getTests()) {
			if (test instanceof TestInterpreterConfiguration) {
				tst = (TestInterpreterConfiguration) test;
				break;
			}
		}
		if (tst==null || tst.getInitialized()==0) {
			System.out.println("Initializing the interpreter configuration ...");
			config.initialize();
			while (!config.isDone()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		for (InitializeClass cls: config.getClasses()) {
			if (cls.error.length()>0) {
				config = null;
				break;
			}
		}
		return config;
	}
}
