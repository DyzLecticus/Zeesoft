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
import nl.zeesoft.zid.session.Session;
import nl.zeesoft.zid.session.SessionDialogHandler;
import nl.zeesoft.zid.session.SessionManager;
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
		System.out.println("// Create session dialog handler");
		System.out.println("SessionDialogHandler handler = new DialogHandler(dialogs,patternManager);");
		System.out.println("// Initialize session dialog handler");
		System.out.println("handler.initialize();");
		System.out.println("// Handle session dialog input");
		System.out.println("handler.handleSessionInput(session);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("A *SessionDialogHandler* requires a list of dialogs and a *PatternManager*.  ");
		System.out.println("Sessions can be obtained by implementing a *SessionManager*.  ");
		System.out.println();
		getTester().describeMock(MockSessionDialogHandler.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestSessionDialogHandler.class));
		System.out.println(" * " + getTester().getLinkForClass(MockSessionDialogHandler.class));
		System.out.println(" * " + getTester().getLinkForClass(MockDialogs.class));
		System.out.println(" * " + getTester().getLinkForClass(MockPatternManager.class));
		System.out.println(" * " + getTester().getLinkForClass(SessionManager.class));
		System.out.println(" * " + getTester().getLinkForClass(SessionDialogHandler.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * The mock initialization duration.  ");
		System.out.println(" * The scripted session dialog handler input with corresponding output.  ");
		System.out.println(" * The average time spent thinking per response.  ");
		System.out.println(" * The session dialog handler log.  ");
	}

	@Override
	protected void test(String[] args) {
		SessionDialogHandler handler = (SessionDialogHandler) getTester().getMockedObject(MockSessionDialogHandler.class.getName());
		
		ZDKFactory factory = new ZDKFactory();
		Messenger messenger = factory.getMessenger();
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
		addScriptLine("gek","Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?");
		addScriptLine("van henkie","Wat is jouw voornaam?");
		addScriptLine("gekkie","Wat kan ik voor je doen Gekkie van Henkie?");

		// Handshake context switch
		addScriptLine("what is your name?","My name is Dyz Lecticus. What is your name?");
		addScriptLine("hoe heet jij?","Mijn naam is Dyz Lecticus. Wat is jouw naam?");
		
		testScript(handler,session);

		System.out.println(session.getLog());
		
		sessionManager.closeSession(session.getId());
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
