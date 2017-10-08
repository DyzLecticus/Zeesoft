package nl.zeesoft.zid.test;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zid.session.SessionDialogHandler;
import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.patterns.UniversalAlphabetic;
import nl.zeesoft.zspr.test.MockPatternManager;

public class MockSessionDialogHandler extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockSessionDialogHandler*.");
		System.out.println("The *MockSessionDialogHandler* uses the *MockSessionDialogs* and the *MockPatternManager*.");
	}

	@Override
	protected Object initialzeMock() {
		@SuppressWarnings("unchecked")
		List<Dialog> dialogs = (List<Dialog>) getTester().getMockedObject(MockSessionDialogs.class.getName());
		
		PatternManager patternManager = (PatternManager) getTester().getMockedObject(MockPatternManager.class.getName());
		
		Date start = new Date();
		SessionDialogHandler handler = new SessionDialogHandler(dialogs,patternManager);
		handler.initialize();
		UniversalAlphabetic pattern = (UniversalAlphabetic) patternManager.getPatternByClassName(UniversalAlphabetic.class.getName());
		if (pattern!=null) {
			pattern.setKnownSymbols(handler.getAllSequenceSymbols());
		}
		System.out.println("Initializing dialog handler took " + ((new Date()).getTime() - start.getTime()) + " ms");
		System.out.println();
		return handler;
	}
}
