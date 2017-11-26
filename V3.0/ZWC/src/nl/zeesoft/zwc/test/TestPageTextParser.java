package nl.zeesoft.zwc.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zwc.page.PageTextParser;

public class TestPageTextParser extends TestObject {
	public TestPageTextParser(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestPageTextParser(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create and use a *PageTextParser* instance.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create page reader");
		System.out.println("PageParser reader = new PageReader();");
		System.out.println("// Read a page");
		System.out.println("ZStringBuilder page = reader.getPageAtUrl(\"http://www.w3.org/TR/html401/\");");
		System.out.println("// Create page text parser");
		System.out.println("PageTextParser parser = new PageTextParser(page);");
		System.out.println("// Parse the text");
		System.out.println("ZStringSymbolParser text = parser.getText();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("A *PageTextParser* can parse an HTML web page in order to obtain all text from the page.");
		System.out.println();
		getTester().describeMock(MockPage.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestPageTextParser.class));
		System.out.println(" * " + getTester().getLinkForClass(PageTextParser.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows a substring of the text for the page.");
	}

	@Override
	protected void test(String[] args) {
		ZStringBuilder page = (ZStringBuilder) getTester().getMockedObject(MockPage.class.getName());
		
		PageTextParser parser = new PageTextParser(page);
		
		ZStringSymbolParser text = parser.getText();
		assertEqual(text.length(),2958,"Text length does not match expectation");
		System.out.println(text.substring(0,150));
	}
}
