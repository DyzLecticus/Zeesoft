package nl.zeesoft.zsd.test;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.SequenceInterpreter;
import nl.zeesoft.zsd.interpret.InterpreterConfiguration;
import nl.zeesoft.zsd.interpret.InterpreterRequest;
import nl.zeesoft.zsd.interpret.InterpreterResponse;
import nl.zeesoft.zsd.sequence.SequenceClassifierResult;

public class TestSequenceInterpreter extends TestInitializer {
	public TestSequenceInterpreter(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestSequenceInterpreter(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		/*
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
		System.out.println(" * " + getTester().getLinkForClass(TestSequenceInterpreter.class));
		System.out.println(" * " + getTester().getLinkForClass(InterpreterConfiguration.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test the time it takes to initialize all objects simultaneously.  ");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		InterpreterConfiguration config = TestInterpreterConfiguration.getConfig(getTester());
		if (config==null) {
			System.out.println("This test has been skipped due to configuration initialization failure");
		} else {
			SequenceInterpreter interpreter = new SequenceInterpreter(config);
			testRequestResponse(interpreter,
				"hallo",
				"UNI_ABC:Hallo .");
			testRequestResponse(interpreter,
				"mijn naam si gekste der henkies",
				"UNI_ABC:Mijn UNI_ABC:naam UNI_ABC:si|NLD_NAM:firstName:UNI_ABC:si UNI_ABC:gekste|NLD_NAM:lastName:UNI_ABC:gekste NLD_PRE:6|UNI_ABC:der UNI_ABC:henkies.");
		}
	}

	protected void testRequestResponse(SequenceInterpreter si,String input, String expectedTranslation) {
		Date started = new Date();	
		InterpreterRequest request = new InterpreterRequest();
		request.input.append(input);
		request.setAllActions(true);
		InterpreterResponse response = si.interpretRequest(request);
		showRequestResponse(response);
		System.out.println("Interpreting the request took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		assertEqual(response.entityValueTranslation,new ZStringSymbolParser(expectedTranslation),"Translation does not match expectation");
	}
	
	protected void showRequestResponse(InterpreterResponse response) {
		System.out.println();
		System.out.println("Request prompt: '" + response.request.prompt + "', input: '" + response.request.input + "'");
		System.out.println("Response;");
		System.out.println("- Languages;");
		showResults(response.responseLanguages);
		System.out.println("- Master contexts;");
		showResults(response.responseMasterContexts);
		System.out.println("- Contexts;");
		showResults(response.responseContexts);
		System.out.println("- Corrected input: '" + response.correctedInput + "'");
		System.out.println("- Translation: '" + response.entityValueTranslation + "'");
	}

	protected void showResults(List<SequenceClassifierResult> results) {
		for (SequenceClassifierResult res: results) {
			System.out.println("  - " + res.symbol + ": " + res.prob + " / " + res.probThreshold);
		}
	}
}
