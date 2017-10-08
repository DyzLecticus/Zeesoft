package nl.zeesoft.zid.test;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zid.session.SessionDialogHandler;
import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.test.MockPatternManager;

public class MockSessionDialogHandler extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockSessionDialogHandler*.");
		System.out.println("The *MockSessionDialogHandler* uses the *MockDialogs* and the *MockPatternManager*.");
	}

	@Override
	protected Object initialzeMock() {
		@SuppressWarnings("unchecked")
		List<Dialog> dialogs = (List<Dialog>) getTester().getMockedObject(MockDialogs.class.getName());
		PatternManager patternManager = (PatternManager) getTester().getMockedObject(MockPatternManager.class.getName());
		Date start = new Date();
		SessionDialogHandler handler = new SessionDialogHandler(dialogs,patternManager);
		handler.initialize();
		System.out.println("Initializing dialog handler took " + ((new Date()).getTime() - start.getTime()) + " ms");
		System.out.println();
		return handler;
	}
}
