package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.build.ClassPathBuilder;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestClassPathBuilder extends TestObject {
	public TestClassPathBuilder(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestClassPathBuilder(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use the *ClassPathBuilder* to obtain a class path.");
		System.out.println("It is not included in the test set because it requires a source directory.");
	}

	@Override
	protected void test(String[] args) {
		ClassPathBuilder cpb = new ClassPathBuilder();
		String classPath = cpb.getClassPath("src");
		System.out.println("Class path: " + classPath);
	}
}
