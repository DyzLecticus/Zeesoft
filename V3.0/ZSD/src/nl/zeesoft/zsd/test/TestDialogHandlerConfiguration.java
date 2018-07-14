package nl.zeesoft.zsd.test;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.dialog.DialogHandlerConfiguration;
import nl.zeesoft.zsd.initialize.InitializeClass;

public class TestDialogHandlerConfiguration extends TestInitializer {
	public TestDialogHandlerConfiguration(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDialogHandlerConfiguration(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test uses a lot of JSON files to initialize a *DialogHandlerConfiguration* instance.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the DialogHandlerConfiguration");
		System.out.println("DialogHandlerConfiguration config = new DialogHandlerConfiguration();");
		System.out.println("// Add a listener");
		System.out.println("config.addListener(this);");
		System.out.println("// Start the initialization");
		System.out.println("config.initialize();");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockDialogHandlerConfiguration.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestDialogHandlerConfiguration.class));
		System.out.println(" * " + getTester().getLinkForClass(DialogHandlerConfiguration.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the time it takes to initialize all remaining objects simultaneously.  ");
	}
	
	@Override
	protected void test(String[] args) {
		DialogHandlerConfiguration config = (DialogHandlerConfiguration) getTester().getMockedObject(MockDialogHandlerConfiguration.class.getName());
		config.addListener(this);
		List<InitializeClass> clss = config.getInitializeClasses();
		System.out.println("Classes:");
		for (InitializeClass cls: clss) {
			if (cls.fileNames.size()>0) {
				ZStringBuilder files = new ZStringBuilder();
				for (String fileName: cls.fileNames) {
					if (files.length()>0) {
						files.append(", ");
					}
					files.append(fileName);
				}
				System.out.println("- " + cls.obj.getClass().getName() + " (" + cls.name + ") <= " + files);
			} else {
				System.out.println("- " + cls.obj.getClass().getName() + " (" + cls.name + ")");
			}
		}
		System.out.println();
		List<String> classNames = new ArrayList<String>();
		classNames.add("DialogSet");
		config.initialize(classNames);
		while (!config.isDone()) {
			sleep(100);
		}
		assertEqual(getInitialized(),1,"The number of initialized classes does not match expectation");
		assertEqual(getErrors(),0,"The number of errors does not match expectation");
	}
	
	protected static DialogHandlerConfiguration getConfig(Tester tester) {
		DialogHandlerConfiguration config = (DialogHandlerConfiguration) tester.getMockedObject(MockDialogHandlerConfiguration.class.getName());
		TestDialogHandlerConfiguration tst = null;
		for (TestObject test: tester.getTests()) {
			if (test instanceof TestDialogHandlerConfiguration) {
				tst = (TestDialogHandlerConfiguration) test;
				break;
			}
		}
		if (tst==null || tst.getInitialized()==0) {
			System.out.println("Initializing the dialog handler configuration ...");
			List<String> classNames = new ArrayList<String>();
			classNames.add("DialogSet");
			config.initialize(classNames);
			while (!config.isDone()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		for (InitializeClass cls: config.getClasses()) {
			if (cls.errors.length()>0) {
				System.err.println(cls.errors);
				config = null;
				break;
			}
		}
		return config;
	}
}
