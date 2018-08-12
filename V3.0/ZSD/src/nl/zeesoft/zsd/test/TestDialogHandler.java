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
import nl.zeesoft.zsd.dialog.dialogs.SupportRequest;
import nl.zeesoft.zsd.sequence.SequenceClassifierResult;

public class TestDialogHandler extends TestInitializer {
	private String filterContext = "";
	
	public TestDialogHandler(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDialogHandler(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test show how to use a *DialogHandler* to process a *DialogRequest*.");
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
				"what are you?",
				"UN_ABC:What UN_ABC:are UN_ABC:you ?",
				"I am an artificially intelligent personal assistant.","");
			System.out.println();
			testRequestResponse(handler,"",
				"wat ben jij?",
				"UN_ABC:Wat UN_ABC:ben UN_ABC:jij ?",
				"Ik ben een kunstmatig intelligente persoonlijk assistent.","");
			System.out.println();
			testRequestResponse(handler,"",
				"What is the answer?",
				"UN_ABC:What UN_ABC:is EN_PRE:5|UN_ABC:the UN_ABC:answer ?",
				"Fourtytwo.","");
			System.out.println();
			testRequestResponse(handler,"",
				"Wat is het antwoord?",
				"UN_ABC:Wat UN_ABC:is UN_ABC:het UN_ABC:antwoord ?",
				"Tweeenveertig.","");
			System.out.println();
			testRequestResponse(handler,"",
				"hallo",
				"UN_ABC:Hallo .",
				"Hallo. Mijn naam is Dyz Lecticus.","Wat is uw naam?");
			System.out.println();
			testRequestResponse(handler,"",
				"Wie ben jij?",
				"UN_ABC:Wie UN_ABC:ben UN_ABC:jij ?",
				"Mijn naam is Dyz Lecticus.","Wat is uw naam?");
			System.out.println();
			testRequestResponse(handler,"What is your name?",
				"albert einstein",
				"UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .",
				"","What can I do for you Albert Einstein?");
			System.out.println();
			testRequestResponse(handler,"",
				"mijn naam si gekste der henkies",
				"UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste NL_PRE:6|UN_ABC:der UN_ABC:henkies .",
				"UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .",
				"Hallo. Mijn naam is Dyz Lecticus.","Wat kan ik voor u doen Gekste der Henkies?");
			System.out.println();
			testRequestResponse(handler,"What is your firstname?",
				"mijn naam is jan de lange",
				"UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:jan|NL_NAM:firstName:UN_ABC:Jan NL_PRE:5|UN_ABC:de|NL_NAM:preposition:NL_PRE:5 UN_ABC:lange|NL_NAM:lastName:UN_ABC:Lange .",
				"Hallo. Mijn naam is Dyz Lecticus.","Wat kan ik voor u doen Jan de Lange?");
			System.out.println();
			testRequestResponse(handler,"",
				"Who created you?",
				"UN_ABC:Who UN_ABC:created UN_ABC:you ?",
				"My software was written by André van der Zee.","");
			System.out.println();
			testRequestResponse(handler,"",
				"You asshole!",
				"UN_ABC:You EN_PRF:1|UN_ABC:asshole !",
				"I do not appreciate that kind of language.","");
			System.out.println();
			testRequestResponse(handler,"",
				"Jij klootzak!",
				"UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak !",
				"Van zulk taalgebruik ben ik niet gediend.","");
			System.out.println();
			testRequestResponse(handler,"",
				"Can I book a room for 5 people?",
				"UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:5 UN_ABC:people ?",
				"Okay.","On what date?");
			System.out.println();
			testRequestResponse(handler,"",
				"Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?",
				"UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:12 UN_ABC:people UN_ABC:on EN_DAT:2018-10-01 UN_ABC:at EN_TIM:12:00:00 UN_ABC:for EN_DUR:02:00 ?",
				"Okay.","Do I understand correctly that you want a room on october first twothousandeighteen, for 12 people, from twelve o'clock, for two hours?");
			System.out.println();
			testRequestResponse(handler,"",
				"What does it cost to book a room?",
				"UN_ABC:What UN_ABC:does UN_ABC:it UN_ABC:cost UN_ABC:to UN_ABC:book UN_ABC:a UN_ABC:room ?",
				"There are no costs attached to booking a room.","");
			System.out.println();
			testRequestResponse(handler,"",
				"How much is ten times fourty divided by twenty plus three times six?",
				"UN_ABC:How UN_ABC:much UN_ABC:is EN_NUM:10|UN_ABC:ten EN_MTH:M|UN_ABC:times EN_NUM:40|UN_ABC:fourty EN_MTH:D EN_NUM:20|UN_ABC:twenty EN_MTH:A|UN_ABC:plus EN_NUM:3|UN_ABC:three EN_MTH:M|UN_ABC:times EN_NUM:6|UN_ABC:six ?",
				"UN_ABC:How UN_ABC:much UN_ABC:is EN_NUM:10|UN_ABC:ten EN_MTH:M EN_NUM:40|UN_ABC:fourty EN_MTH:D EN_NUM:20|UN_ABC:twenty EN_MTH:A|UN_ABC:plus EN_NUM:3|UN_ABC:three EN_MTH:M EN_NUM:6|UN_ABC:six ?",
				"Exactly thirtyeight.","What else can I do for you?");
			System.out.println();
			testRequestResponse(handler,"",
				"Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?",
				"UN_ABC:Hoeveel UN_ABC:is NL_NUM:10|UN_ABC:tien NL_MTH:M|UN_ABC:keer NL_NUM:40|UN_ABC:veertig NL_MTH:D NL_NUM:20|UN_ABC:twintig NL_MTH:A|UN_ABC:plus NL_NUM:3|UN_ABC:drie NL_MTH:M|UN_ABC:keer NL_NUM:6|UN_ABC:zes ?",
				"UN_ABC:Hoeveel UN_ABC:is NL_NUM:10|UN_ABC:tien NL_MTH:M NL_NUM:40|UN_ABC:veertig NL_MTH:D NL_NUM:20|UN_ABC:twintig NL_MTH:A|UN_ABC:plus NL_NUM:3|UN_ABC:drie NL_MTH:M NL_NUM:6|UN_ABC:zes ?",
				"Precies achtendertig.","Kan ik nog meer voor je doen?");
			System.out.println();
			testRequestResponse(handler,"",
				"What languages do you speak?",
				"UN_ABC:What UN_ABC:languages UN_ABC:do UN_ABC:you UN_ABC:speak ?",
				"I speak English and Dutch.","");
			System.out.println();
			testRequestResponse(handler,"",
				"Spreek je duits?",
				"UN_ABC:Spreek UN_ABC:je NL_LNG:DE|UN_ABC:duits ?",
				"UN_ABC:Spreek UN_ABC:je NL_LNG:DE|UN_ABC:Duits ?",
				"Nee, ik spreek Engels en Nederlands.","");
			System.out.println();
			testRequestResponse(handler,"",
				"Spreek je engels?",
				"UN_ABC:Spreek UN_ABC:je NL_LNG:EN|UN_ABC:engels ?",
				"UN_ABC:Spreek UN_ABC:je NL_LNG:EN|UN_ABC:Engels ?",
				"Ja, ik spreek Engels en Nederlands.","");
			System.out.println();
			testRequestResponse(handler,"",
				"Wat kost overboeken naar buitenland?",
				"UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar UN_ABC:buitenland ?",
				"Een overboeking naar het buitenland kost vijf euro.","");
			System.out.println();
			testRequestResponse(handler,"",
				"Wat kost overboeken naar italie?",
				"UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar UN_ABC:italie ?",
				"UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar NL_CNT:IT|UN_ABC:italië ?",
				"Een overboeking naar Italië kost vijf euro.","");
			System.out.println();
			testRequestResponse(handler,"",
				"Je begrijpt me niet.",
				"UN_ABC:Je UN_ABC:begrijpt UN_ABC:me UN_ABC:niet .",
				"Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar dyz.lecticus@zeesoft.nl.","Kan ik u ergens anders mee proberen te helpen?");
			System.out.println();
			filterContext = SupportRequest.FILTER_CONTEXT_TRANSFER;
			testRequestResponse(handler,"",
				"Je begrijpt me niet.",
				"UN_ABC:Je UN_ABC:begrijpt UN_ABC:me UN_ABC:niet .",
				"Ik ben nog aan het leren.","Zal ik u doorverbinden met een mens?");
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
		if (filterContext.length()>0) {
			request.filterContexts.add(filterContext);
		}
		DialogResponse response = si.handleDialogRequest(request);
		showRequestResponse(response);
		System.out.println("Handling the request took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		assertEqual(response.entityValueTranslation,new ZStringSymbolParser(expectedTranslation),"Translation does not match expectation");
		if (expectedTranslationCorrected!=null) {
			assertEqual(response.entityValueTranslationCorrected,new ZStringSymbolParser(expectedTranslationCorrected),"Corrected translation does not match expectation");
		}
		if (response.contextOutputs.size()>0) {
			DialogResponseOutput dro = response.contextOutputs.get(0);
			assertEqual(dro.output,new ZStringSymbolParser(expectedOutput),"Output does not match expectation");
			assertEqual(dro.prompt,new ZStringSymbolParser(expectedPrompt),"Prompt does not match expectation");
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
