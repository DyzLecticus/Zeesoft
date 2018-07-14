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
		System.out.println("This test show how to use a *SequenceInterpreter* to process an *InterpreterRequest*.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the InterpreterConfiguration");
		System.out.println("InterpreterConfiguration config = new InterpreterConfiguration();");
		System.out.println("// Add a listener");
		System.out.println("config.addListener(this);");
		System.out.println("// Start the initialization (and wait until it's done)");
		System.out.println("config.initialize();");
		System.out.println("// Create the sequence interpreter");
		System.out.println("SequenceInterpreter interpreter = new SequenceInterpreter(config);");
		System.out.println("// Create the interpreter request");
		System.out.println("InterpreterRequest request = new InterpreterRequest(\"The optional output that prompted the input\",\"The input sequence\");");
		System.out.println("// Use the interpreter to process the request");
		System.out.println("InterpreterResponse response = interpreter.handleInterpreterRequest(request);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestSequenceInterpreter.class));
		System.out.println(" * " + getTester().getLinkForClass(InterpreterConfiguration.class));
		System.out.println(" * " + getTester().getLinkForClass(InterpreterRequest.class));
		System.out.println(" * " + getTester().getLinkForClass(InterpreterResponse.class));
		System.out.println(" * " + getTester().getLinkForClass(SequenceInterpreter.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows several sequence interpreter requests and the debug log of corresponding responses.  ");
	}
	
	@Override
	protected void test(String[] args) {
		InterpreterConfiguration config = TestInterpreterConfiguration.getConfig(getTester());
		if (config==null) {
			System.out.println("This test has been skipped due to configuration initialization failure");
		} else {
			SequenceInterpreter interpreter = new SequenceInterpreter(config);
			testRequestResponse(interpreter,"",
				"hallo",
				"UNI_ABC:Hallo .");
			System.out.println();
			testRequestResponse(interpreter,"",
				"mijn naam si gekste der henkies",
				"UNI_ABC:Mijn UNI_ABC:naam UNI_ABC:si UNI_ABC:gekste|NLD_NAM:firstName:UNI_ABC:Gekste NLD_PRE:6|UNI_ABC:der|NLD_NAM:preposition:NLD_PRE:6 UNI_ABC:henkies|NLD_NAM:lastName:UNI_ABC:Henkies.",
				"UNI_ABC:Mijn UNI_ABC:naam UNI_ABC:is UNI_ABC:gekste|NLD_NAM:firstName:UNI_ABC:Gekste NLD_PRE:6|UNI_ABC:der|NLD_NAM:preposition:NLD_PRE:6 UNI_ABC:henkies|NLD_NAM:lastName:UNI_ABC:Henkies.");
			System.out.println();
			testRequestResponse(interpreter,"What is your name?",
				"albert einstein",
				"UNI_ABC:Albert|ENG_NAM:firstName:UNI_ABC:Albert UNI_ABC:einstein|ENG_NAM:lastName:UNI_ABC:Einstein.");
			System.out.println();
			testRequestResponse(interpreter,"",
				"wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv",
				"UNI_ABC:Wruio UNI_ABC:wwtiop UNI_ABC:wtwrpoi UNI_ABC:weptiwpipw UNI_ABC:ipwopkm UNI_ABC:eopipwqwrqqiop UNI_ABC:qwerqwer UNI_ABC:qrqpoqe UNI_ABC:qpxnxc UNI_ABC:qwpgsjkdbvhsdfkljjv .");
		}
	}
	
	protected void testRequestResponse(SequenceInterpreter si,String prompt,String input, String expectedTranslation) {
		testRequestResponse(si,prompt,input,expectedTranslation,expectedTranslation);
	}

	protected void testRequestResponse(SequenceInterpreter si,String prompt,String input, String expectedTranslation, String expectedTranslationCorrected) {
		Date started = new Date();	
		InterpreterRequest request = new InterpreterRequest(prompt,input);
		request.setAllActions(true);
		InterpreterResponse response = si.handleInterpreterRequest(request);
		showRequestResponse(response);
		System.out.println("Interpreting the request took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		assertEqual(response.entityValueTranslation,new ZStringSymbolParser(expectedTranslation),"Translation does not match expectation");
		if (expectedTranslationCorrected!=null) {
			assertEqual(response.entityValueTranslationCorrected,new ZStringSymbolParser(expectedTranslationCorrected),"Corrected translation does not match expectation");
		}
	}
	
	protected void showRequestResponse(InterpreterResponse response) {
		System.out.println("Request prompt: '" + response.request.prompt + "', input: '" + response.request.input + "'");
		System.out.println("Response debug log;");
		System.out.println(response.debugLog);
	}

	protected void showResults(List<SequenceClassifierResult> results) {
		for (SequenceClassifierResult res: results) {
			System.out.println("  - " + res.symbol + ": " + res.prob + " / " + res.probNormalized);
		}
	}
}
