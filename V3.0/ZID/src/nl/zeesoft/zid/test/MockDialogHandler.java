package nl.zeesoft.zid.test;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zid.dialog.DialogHandler;
import nl.zeesoft.zspr.pattern.PatternManager;

public class MockDialogHandler extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockDialogHandler*.");
		System.out.println("The *MockDialogHandler* uses the *MockDialogs* and the *PatternManager*.");
	}

	@Override
	protected Object initialzeMock() {
		// TODO: get mocks through tester
		@SuppressWarnings("unchecked")
		List<Dialog> dialogs = (List<Dialog>) (new MockDialogs()).initialzeMock();
				
		PatternManager manager = new PatternManager();
		manager.initializePatterns();
		
		Date start = new Date();
		DialogHandler handler = new DialogHandler(dialogs,manager);
		handler.initialize();
		System.out.println("Initializing dialog handler took " + ((new Date()).getTime() - start.getTime()) + " ms");
		System.out.println();
		return handler;
	}
}
