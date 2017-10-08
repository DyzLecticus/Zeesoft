package nl.zeesoft.zid.test;

public class MockSessionDialogs extends MockDialogs {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockSessionDialogs*.");
		System.out.println("The dialogs created by this mock use the *HandshakeDialogController*.");
	}

	@Override
	protected String getControllerName() {
		return HandshakeDialogController.class.getName();
	}
}
