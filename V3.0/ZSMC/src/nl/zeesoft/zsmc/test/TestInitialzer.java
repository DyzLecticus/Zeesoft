package nl.zeesoft.zsmc.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsmc.EntityValueTranslator;
import nl.zeesoft.zsmc.SequenceClassifier;
import nl.zeesoft.zsmc.TsvToJson;
import nl.zeesoft.zsmc.initialize.InitializeClass;
import nl.zeesoft.zsmc.initialize.Initializer;
import nl.zeesoft.zsmc.initialize.InitializerListener;
import nl.zeesoft.zsmc.sequence.Analyzer;

public class TestInitialzer extends TestObject implements InitializerListener {
	public TestInitialzer(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestInitialzer(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *TsvToJson* instance to convert a TSV file into a JSON file.");
		System.out.println("Both formats can be used to initialize *Analyzer* instances.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the TsvToJson instance");
		System.out.println("TsvToJson convertor = new TsvToJson();");
		System.out.println("// Convert some TSV data");
		System.out.println("JsFile json = convertor.parseTsv(new ZStringBuilder(\"Question\\tAnswer\\tContext\\nWhat?\\tThat!\\tWTF\\n\"));");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestInitialzer.class));
		System.out.println(" * " + getTester().getLinkForClass(TsvToJson.class));
		System.out.println(" * " + getTester().getLinkForClass(Analyzer.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the input TSV data and the converted JSON.  ");
	}
	
	@Override
	protected void test(String[] args) {
		Initializer init = new Initializer(null,null);
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
