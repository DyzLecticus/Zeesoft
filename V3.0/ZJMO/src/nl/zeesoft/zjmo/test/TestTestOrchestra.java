package nl.zeesoft.zjmo.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.test.mocks.TestOrchestra;
import nl.zeesoft.zjmo.test.mocks.MockTestOrchestra;

public class TestTestOrchestra extends TestObject {
	public TestTestOrchestra(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestTestOrchestra(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create and initialize a *TestOrchestra* instance.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create orchestra");
		System.out.println("TestOrchestra orch = new TestOrchestra();");
		System.out.println("// Initialize the orchestra");
		System.out.println("orch.initialize();");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockTestOrchestra.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestTestOrchestra.class));
		System.out.println(" * " + getTester().getLinkForClass(MockTestOrchestra.class));
		System.out.println(" * " + getTester().getLinkForClass(TestOrchestra.class));
		System.out.println(" * " + getTester().getLinkForClass(Orchestra.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the JSON structure of the *TestOrchestra*.  ");
	}

	@Override
	protected void test(String[] args) {
		TestOrchestra orch = (TestOrchestra) getTester().getMockedObject(MockTestOrchestra.class.getName());
		ZStringBuilder before = orch.toJson(false).toStringBuilderReadFormat();
		orch.fromJson(orch.toJson(true));
		ZStringBuilder after = orch.toJson(false).toStringBuilderReadFormat();
		assertEqual(after,before,"The after conversion orchestra does not match expectation");
		System.out.println(after);
	}
}
