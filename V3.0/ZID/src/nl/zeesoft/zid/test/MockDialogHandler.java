package nl.zeesoft.zid.test;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zid.dialog.DialogHandler;
import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.test.MockPatternManager;

public class MockDialogHandler extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockDialogHandler*.");
		System.out.println("The *MockDialogHandler* uses the *MockDialogs* and the *PatternManager*.");
	}

	@Override
	protected Object initialzeMock() {
		@SuppressWarnings("unchecked")
		List<Dialog> dialogs = (List<Dialog>) Tester.getInstance().getMockedObject(MockDialogs.class.getName());
				
		PatternManager manager = (PatternManager) Tester.getInstance().getMockedObject(MockPatternManager.class.getName());
		manager.initializePatterns();
		
		Date start = new Date();
		DialogHandler handler = new DialogHandler(dialogs,manager);
		handler.initialize();
		System.out.println("Initializing dialog handler took " + ((new Date()).getTime() - start.getTime()) + " ms");
		System.out.println();
		return handler;
	}
}
