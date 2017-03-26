package nl.zeesoft.zjmo.test.mocks;

public class MockConductor2 extends MockConductor1 {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockConductor2*.");
		System.out.println("The *MockConductor2* uses the *MockTestOrchestra*.");
	}

	@Override
	protected int getPositionBackupNumber() {
		return 1;
	}
}
