package nl.zeesoft.zid.test;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zid.dialog.DialogHandler;
import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.test.MockPatternManager;

public class MockDialogHandler extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockDialogHandler*.");
		System.out.println("The *MockDialogHandler* uses the *MockDialogs* and the *MockPatternManager*.");
	}

	@Override
	protected Object initialzeMock() {
		// TODO: get mocks through tester
		@SuppressWarnings("unchecked")
		List<Dialog> dialogs = (List<Dialog>) getTester().getMockedObject(MockDialogs.class.getName());
				
		PatternManager manager = (PatternManager) getTester().getMockedObject(MockPatternManager.class.getName());
		
		Date start = new Date();
		DialogHandler handler = new DialogHandler(dialogs,manager);
		handler.initialize();
		System.out.println("Initializing dialog handler took " + ((new Date()).getTime() - start.getTime()) + " ms");
		System.out.println();
		return handler;
	}
}
