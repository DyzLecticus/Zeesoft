package nl.zeesoft.zid.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zid.dialog.DialogHandler;
import nl.zeesoft.zid.session.Session;
import nl.zeesoft.zid.session.SessionDialogHandler;
import nl.zeesoft.zid.session.SessionManager;
import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.patterns.UniversalAlphabetic;
import nl.zeesoft.zspr.test.MockPatternManager;

public class TestSessionDialogHandler extends TestObject {
	private List<String>				keys	= new ArrayList<String>();
	private SortedMap<String,String>	script	= new TreeMap<String,String>();
	
	public TestSessionDialogHandler(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestSessionDialogHandler(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		// TODO: Describe
		System.out.println("This test shows how to create a *SessionDialogHandler* and then use it to produce dialog output for input.  ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create dialog handler");
		System.out.println("DialogHandler handler = new DialogHandler(dialogs,patternManager);");
		System.out.println("// Initialize dialog handler");
		System.out.println("handler.initialize();");
		System.out.println("// Handle dialog input");
		System.out.println("ZStringSymbolParser output = handler.handleInput(new ZStringSymbolParser(\"hello\"));");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("A *DialogHandler* requires a list of dialogs and a *PatternManager*.  ");
		System.out.println();
		getTester().describeMock(MockSessionDialogs.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestSessionDialogHandler.class));
		System.out.println(" * " + getTester().getLinkForClass(MockDialogHandler.class));
		System.out.println(" * " + getTester().getLinkForClass(MockDialogs.class));
		System.out.println(" * " + getTester().getLinkForClass(DialogHandler.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * The mock initialization duration.  ");
		System.out.println(" * The scripted dialog handler input with corresponding output.  ");
		System.out.println(" * The average time spent thinking per response.  ");
		System.out.println(" * The dialog handler log.  ");
	}

	@Override
	protected void test(String[] args) {
		ZDKFactory factory = new ZDKFactory();
		Messenger messenger = factory.getMessenger();
		
		@SuppressWarnings("unchecked")
		List<Dialog> dialogs = (List<Dialog>) getTester().getMockedObject(MockSessionDialogs.class.getName());
		
		PatternManager patternManager = (PatternManager) getTester().getMockedObject(MockPatternManager.class.getName());
		
		SessionDialogHandler handler = new SessionDialogHandler(dialogs,patternManager);
		handler.initialize();
		UniversalAlphabetic pattern = (UniversalAlphabetic) patternManager.getPatternByClassName(UniversalAlphabetic.class.getName());
		if (pattern!=null) {
			pattern.setKnownSymbols(handler.getAllSequenceSymbols());
		}
		
		SessionManager sessionManager = new SessionManager(messenger);
		
		Session session = sessionManager.openSession();
		
		// Known user handshake
		addScriptLine("hello","Hello. My name is Dyz Lecticus. What is your name?");
		addScriptLine("my name is andre van der zee","Nice to interact with you again Andre van der Zee.");

		// Extended handshake
		addScriptLine("hallo,ik heet karel de grote.","Wat kan ik voor je doen Karel de Grote?");
		
		// Handshake prompt sequence
		addScriptLine("hallo?","Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?");
		addScriptLine("gekke henkie","Wat kan ik voor je doen Gekke Henkie?");
		addScriptLine("gekke","Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?");
		addScriptLine("van henkie","Wat is jouw voornaam?");
		addScriptLine("gekke","Wat kan ik voor je doen Gekke van Henkie?");

		// Handshake context switch
		addScriptLine("what is your name?","My name is Dyz Lecticus. What is your name?");
		addScriptLine("hoe heet jij?","Mijn naam is Dyz Lecticus. Wat is jouw naam?");
		
		testScript(handler,session);

		System.out.println(session.getLog());
	}
	
	private void addScriptLine(String key,String value) {
		keys.add(key);
		script.put(key, value);
	}
	
	private void testScript(SessionDialogHandler handler, Session session) {
		Date started = new Date();
		for (String key: keys) {
			String value = script.get(key);
			session.setInput(new ZStringSymbolParser(key));
			handler.handleSessionInput(session);
			System.out.println("<<< " + key);
			System.out.println(">>> " + session.getOutput());
			assertEqual(session.getOutput().toString(),value,"Output does not match expectation");
		}
		System.out.println();
		long time = ((new Date()).getTime() - started.getTime()) / 10; 
		System.out.println("Average time spent thinking per response: " + time + " ms");
		System.out.println();
	}
}
