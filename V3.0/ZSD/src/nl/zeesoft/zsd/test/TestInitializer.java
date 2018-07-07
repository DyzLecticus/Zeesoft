package nl.zeesoft.zsd.test;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.SequenceClassifier;
import nl.zeesoft.zsd.initialize.InitializeClass;
import nl.zeesoft.zsd.initialize.Initializer;
import nl.zeesoft.zsd.initialize.InitializerListener;

public class TestInitializer extends TestObject implements InitializerListener {
	private int initialized = 0;
	private int errors = 0;
	
	public TestInitializer(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestInitializer(new Tester())).test(args);
	}

	@Override
	protected void describe() {
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
		System.out.println(" * " + getTester().getLinkForClass(TestInitializer.class));
		System.out.println(" * " + getTester().getLinkForClass(Initializer.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test the time it takes to initialize two objects simultaneously.  ");
	}
	
	@Override
	protected void test(String[] args) {
		Initializer init = new Initializer();
		init.addListener(this);
		init.addClass("Classifier",SequenceClassifier.class.getName(),TestSequenceClassifier.QNA_FILE_NAME);
		init.addClass("Translator",EntityValueTranslator.class.getName(),"");
		init.start();
		while (!init.isDone()) {
			sleep(100);
		}
		assertEqual(getInitialized(),2,"The number of initialized classes does not match expectation");
		assertEqual(getErrors(),0,"The number of errors does not match expectation");
	}

	@Override
	public void initializedClass(InitializeClass cls, boolean done) {
		System.out.println("Initializing " + cls.name + " took " + cls.ms + " ms");
		initialized++;
		if (cls.error.length()>0) {
			System.err.println(cls.error);
			errors++;
		}
		if (done) {
			System.out.println("Initialized all classes");
		}
	}
	
	protected int getInitialized() {
		return initialized;
	}

	protected int getErrors() {
		return errors;
	}
}
