package nl.zeesoft.zwc.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zwc.page.PageReader;

public class TestPageReader extends TestObject {
	public TestPageReader(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestPageReader(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create and use a *PageReader* instance.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create page reader");
		System.out.println("PageReader reader = new PageReader();");
		System.out.println("// Read a page");
		System.out.println("ZStringBuilder page = reader.getPageAtUrl(\"http://www.w3.org/TR/html401/\");");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("A *PageReader* can read a web page at a specified URL.");
		System.out.println();
		getTester().describeMock(MockPage.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestPageReader.class));
		System.out.println(" * " + getTester().getLinkForClass(PageReader.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows a substring of the page.");
	}

	@Override
	protected void test(String[] args) {
		ZStringBuilder page = (ZStringBuilder) getTester().getMockedObject(MockPage.class.getName());
		
		assertEqual(page.length(),54146,"Page length does not match expectation");
		
		System.out.println(page.substring(0,1000));
	}
}
