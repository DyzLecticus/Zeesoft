package nl.zeesoft.zsds.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zsd.initialize.InitializeClass;
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
			
			InitializeClass cls = new InitializeClass();
			cls.name = env.name + "Tester";
			cls.obj = tester;
			File dir = new File(configuration.getFullEnvironmentDirectory(env.name));
			if (dir.exists()) {
				File[] files = dir.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].getName().endsWith(".json")) {
						cls.fileNames.add(files[i].getAbsolutePath());
					}
				}
			} else {
				configuration.warn(this,"Directory not found: " + dir.getAbsolutePath());
			}
			addClass(cls);
			
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
