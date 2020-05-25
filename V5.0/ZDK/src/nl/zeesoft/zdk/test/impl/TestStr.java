package nl.zeesoft.zdk.test.impl;

import java.util.List;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestStr extends TestObject {
	public TestStr(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestStr(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how a *Str* instance can be used to split a comma separated string into a list of *Str* instances. ");
		System.out.println("The *Str* class is designed to add features of the Java String to a Java StringBuilder. ");
		System.out.println("It also contains methods for file writing and reading. ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the Str");
		System.out.println("Str str = new Str(\"qwer,asdf,zxcv\");");
		System.out.println("// Split the Str");
		System.out.println("List<Str> strs = str.split(\",\");");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockStr.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestStr.class));
		System.out.println(" * " + getTester().getLinkForClass(Str.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the string input and lists the *Str* objects.  ");
	}

	@Override
	protected void test(String[] args) {
		Str test = new Str((Str)getTester().getMockedObject(MockStr.class.getName()));
		assertEqual(test.trim(),new Str("qwer,asdf,zxcv"),"Encoding does not match expectation");
		System.out.println("Input: " + test);
		List<Str> strs = test.split(",");
		System.out.println("Split; ");
		for (Str str: strs) {
			System.out.println("- " + str);
		}
		assertEqual(strs.size(),3,"List size does not match expectation");
		assertEqual(strs.get(0),new Str("qwer"),"List element does not match expectation");
		assertEqual(strs.get(0).startsWith(new Str("qw")),true,"StartsWith result does not match expectation");
		assertEqual(strs.get(0).endsWith(new Str("er")),true,"EndsWith result does not match expectation");
		assertEqual(strs.get(0).equalsIgnoreCase(new Str("QWER")),true,"Equals ignore case result does not match expectation");
		assertEqual(strs.get(0).toUpperCase(),new Str("QWER"),"To upper case result does not match expectation");
		assertEqual(strs.get(0).replace("WE","UAZA"),new Str("QUAZAR"),"Replace result does not match expectation");
		assertEqual(test.merge(strs,","),new Str("QUAZAR,asdf,zxcv"),"Merge result does not match expectation");
	}
}
