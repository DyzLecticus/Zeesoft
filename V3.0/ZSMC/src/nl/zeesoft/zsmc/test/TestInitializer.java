package nl.zeesoft.zsmc.test;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsmc.EntityValueTranslator;
import nl.zeesoft.zsmc.SequenceClassifier;
import nl.zeesoft.zsmc.TsvToJson;
import nl.zeesoft.zsmc.initialize.InitializeClass;
import nl.zeesoft.zsmc.initialize.Initializer;
import nl.zeesoft.zsmc.initialize.InitializerListener;
import nl.zeesoft.zsmc.sequence.Analyzer;

public class TestInitializer extends TestObject implements InitializerListener {
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
		System.out.println(" * " + getTester().getLinkForClass(TsvToJson.class));
		System.out.println(" * " + getTester().getLinkForClass(Analyzer.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the input TSV data and the converted JSON.  ");
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
	}

	@Override
	public void initializedClass(InitializeClass cls, boolean done) {
		System.out.println("Initializing " + cls.className + " took " + cls.ms + " ms");
		if (done) {
			System.out.println("Initialized all classes");
		}
	}
}
