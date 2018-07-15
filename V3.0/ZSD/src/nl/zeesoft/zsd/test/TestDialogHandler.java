package nl.zeesoft.zsd.test;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.DialogHandler;
import nl.zeesoft.zsd.dialog.DialogHandlerConfiguration;
import nl.zeesoft.zsd.dialog.DialogRequest;
import nl.zeesoft.zsd.dialog.DialogResponse;
import nl.zeesoft.zsd.dialog.DialogResponseOutput;
import nl.zeesoft.zsd.sequence.SequenceClassifierResult;

public class TestDialogHandler extends TestInitializer {
	public TestDialogHandler(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDialogHandler(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test show how to use a *DialogHandler* to process an *DialogRequest*.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the DialogHandlerConfiguration");
		System.out.println("DialogHandlerConfiguration config = new DialogHandlerConfiguration();");
		System.out.println("// Add a listener");
		System.out.println("config.addListener(this);");
		System.out.println("// Start the initialization (and wait until it's done)");
		System.out.println("config.initialize();");
		System.out.println("// Create the dialog handler");
		System.out.println("DialogHandler handler = new DialogHandler(config);");
		System.out.println("// Create the handler request");
		System.out.println("DialogRequest request = new DialogRequest(\"The optional question that prompted the input\",\"The input sequence\");");
		System.out.println("// Use the handler to process the request");
		System.out.println("DialogResponse response = interpreter.handleDialogRequest(request);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestDialogHandler.class));
		System.out.println(" * " + getTester().getLinkForClass(DialogHandlerConfiguration.class));
		System.out.println(" * " + getTester().getLinkForClass(DialogRequest.class));
		System.out.println(" * " + getTester().getLinkForClass(DialogResponse.class));
		System.out.println(" * " + getTester().getLinkForClass(DialogHandler.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows several dialog handler requests and the debug log of corresponding responses.  ");
	}
	
	@Override
	protected void test(String[] args) {
		DialogHandlerConfiguration config = TestDialogHandlerConfiguration.getConfig(getTester());
		if (config==null) {
			System.out.println("This test has been skipped due to configuration initialization failure");
		} else {
			DialogHandler handler = new DialogHandler(config);
			testRequestResponse(handler,"",
				"hallo",
				"UNI_ABC:Hallo .",
				"Hallo. Mijn naam is Dyz Lecticus.","Wat is jouw naam?");
			System.out.println();
			testRequestResponse(handler,"",
				"Wie ben jij?",
				"UNI_ABC:Wie UNI_ABC:ben UNI_ABC:jij ?",
				"Mijn naam is Dyz Lecticus.","Wat is jouw naam?");
			System.out.println();
			testRequestResponse(handler,"What is your name?",
				"albert einstein",
				"UNI_ABC:Albert|ENG_NAM:firstName:UNI_ABC:Albert UNI_ABC:einstein|ENG_NAM:lastName:UNI_ABC:Einstein.",
				"","What can I do for you Albert Einstein?");
			System.out.println();
			testRequestResponse(handler,"",
				"mijn naam si gekste der henkies",
				"UNI_ABC:Mijn UNI_ABC:naam UNI_ABC:si UNI_ABC:gekste|NLD_NAM:firstName:UNI_ABC:Gekste NLD_PRE:6|UNI_ABC:der|NLD_NAM:preposition:NLD_PRE:6 UNI_ABC:henkies|NLD_NAM:lastName:UNI_ABC:Henkies.",
				"UNI_ABC:Mijn UNI_ABC:naam UNI_ABC:is UNI_ABC:gekste|NLD_NAM:firstName:UNI_ABC:Gekste NLD_PRE:6|UNI_ABC:der|NLD_NAM:preposition:NLD_PRE:6 UNI_ABC:henkies|NLD_NAM:lastName:UNI_ABC:Henkies.",
				"","Wat kan ik voor je doen Gekste der Henkies?");
			System.out.println();
			testRequestResponse(handler,"",
				"Who created you?",
				"UNI_ABC:Who UNI_ABC:created UNI_ABC:you ?",
				"My software was written by André van der Zee.","");
			System.out.println();
			testRequestResponse(handler,"",
				"Why were you created?",
				"UNI_ABC:Why UNI_ABC:were UNI_ABC:you UNI_ABC:created ?",
				"My goal is to understand and help people.","");
		}
	}
	
	protected void testRequestResponse(DialogHandler si,String prompt,String input, String expectedTranslation, String expectedOutput, String expectedPrompt) {
		testRequestResponse(si,prompt,input,expectedTranslation,expectedTranslation,expectedOutput,expectedPrompt);
	}

	protected void testRequestResponse(DialogHandler si,String prompt,String input, String expectedTranslation, String expectedTranslationCorrected, String expectedOutput, String expectedPrompt) {
		Date started = new Date();	
		DialogRequest request = new DialogRequest(prompt,input);
		request.setAllActions(true);
		request.randomizeOutput = false;
		DialogResponse response = si.handleDialogRequest(request);
		showRequestResponse(response);
		System.out.println("Handling the request took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		assertEqual(response.entityValueTranslation,new ZStringSymbolParser(expectedTranslation),"Translation does not match expectation");
		if (expectedTranslationCorrected!=null) {
			assertEqual(response.entityValueTranslationCorrected,new ZStringSymbolParser(expectedTranslationCorrected),"Corrected translation does not match expectation");
		}
		if (response.contextOutputs.size()>0) {
			DialogResponseOutput dro = response.contextOutputs.get(response.responseContexts.get(0).symbol);
			assertEqual(dro.output,new ZStringSymbolParser(expectedOutput),"Output does not match expectation");
			if (expectedPrompt.length()>0) {
				assertEqual(dro.prompt,new ZStringSymbolParser(expectedPrompt),"Prompt does not match expectation");
			}
		} else if (expectedOutput.length()>0) {
			assertEqual(response.contextOutputs.size()>0,true,"Number of outputs does not match expectation");
		}
	}
	
	protected void showRequestResponse(DialogResponse response) {
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
