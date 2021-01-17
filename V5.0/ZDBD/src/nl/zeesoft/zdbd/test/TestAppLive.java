package nl.zeesoft.zdbd.test;

import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.test.util.Tester;

public class TestAppLive extends TestApp {
	public TestAppLive(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestAppLive(new Tester())).runTest(args);
	}

	@Override
	protected void test(String[] args) {
		FileIO.mockIO = false;
		super.test(args);
	}
}
