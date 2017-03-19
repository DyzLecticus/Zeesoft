package nl.zeesoft.zso.test;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zjmo.Orchestrator;
import nl.zeesoft.zso.orchestra.SampleOrchestra;

public class TestOrchestrator extends TestObject {
	public TestOrchestrator(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestOrchestrator(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to how the *Orchestrator* can be used to prepare machine contents.");
		System.out.println("It is not included in the ZSO test set because it writes files.");
	}

	@Override
	protected void test(String[] args) {
		String[] params = new String[2];
		params[0] = Orchestrator.GENERATE;
		params[1] = SampleOrchestra.class.getName();
		Orchestrator.main(params);
	}
}
