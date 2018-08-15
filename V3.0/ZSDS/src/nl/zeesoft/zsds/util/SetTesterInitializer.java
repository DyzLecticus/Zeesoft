package nl.zeesoft.zsds.util;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zsd.initialize.Initializer;

public class SetTesterInitializer extends Initializer {
	private TestConfiguration			configuration	= null;
	private List<TestCaseSetTester>		testers			= new ArrayList<TestCaseSetTester>(); 
	
	public SetTesterInitializer(TestConfiguration configuration) {
		super(configuration.getMessenger(),configuration.getUnion());
		this.configuration = configuration;
		initializeClasses();
	}
	
	protected void initializeClasses() {
		for (TestEnvironment env: configuration.getEnvironments()) {
			TestCaseSetTester tester = new TestCaseSetTester(configuration,env.name);
			addClass(env.name + "Tester",tester,configuration.getFullEnvironmentFileName(env.name));
			testers.add(tester);
		}
	}
	
	public List<TestCaseSetTester> getTesters() {
		return testers;
	}

	public TestCaseSetTester getTester(String environmentName) {
		TestCaseSetTester r = null;
		for (TestCaseSetTester tester: testers) {
			if (tester.getEnvironment().name.equals(environmentName)) {
				r = tester;
				break;
			}
		}
		return r;
	}
}
