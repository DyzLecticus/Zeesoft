package nl.zeesoft.zid.test;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zid.session.Session;
import nl.zeesoft.zid.session.SessionManager;

public class TestSessionManager extends TestObject {
	public TestSessionManager(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestSessionManager(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create a *SessionManager* and then use it to manage sessions.  ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create session manager");
		System.out.println("SessionManager sessionManager = new SessionManager(messenger);");
		System.out.println("// Open session");
		System.out.println("Session session = sessionManager.openSession();");
		System.out.println("// Close session");
		System.out.println("sessionManager.closeSession(session.getId());");
		System.out.println("// Close inactive session");
		System.out.println("sessionManager.closeInactiveSessions(1000);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestSessionManager.class));
		System.out.println(" * " + getTester().getLinkForClass(SessionManager.class));
		System.out.println(" * " + getTester().getLinkForClass(Session.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the result of opening and closing sessions in several ways.  ");
	}

	@Override
	protected void test(String[] args) {
		ZDKFactory factory = new ZDKFactory();
		Messenger messenger = factory.getMessenger();
		SessionManager sessionManager = new SessionManager(messenger);
		
		Session session1 = sessionManager.openSession();
		assertEqual(sessionManager.getSessionIds().size(),1,"Number of sessions does not match expectation");
		assertEqual((int)session1.getId(),1,"Session id does not match expectation");
		
		Session session2 = sessionManager.openSession();
		assertEqual((int)session2.getId(),2,"Session id does not match expectation");
		assertEqual(sessionManager.getSessionIds().size(),2,"Number of sessions does not match expectation");
		
		sessionManager.closeSession(session1.getId());
		assertEqual(sessionManager.getSessionIds().size(),1,"Number of sessions does not match expectation");
		
		sleep(1000);
		sessionManager.closeInactiveSessions(500);
		if (assertEqual(sessionManager.getSessionIds().size(),0,"Number of sessions does not match expectation")) {
			System.out.println("Opened and closed all sessions as expected");
		}
	}
}
