package nl.zeesoft.zwc.test;

import java.util.List;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zwc.page.PageParser;
import nl.zeesoft.zwc.page.PageReader;

public class TestPageParser extends TestObject {
	public TestPageParser(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestPageParser(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create and use a *PageParser* instance.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create page reader");
		System.out.println("PageParser reader = new PageReader();");
		System.out.println("// Read a page");
		System.out.println("ZStringBuilder page = reader.getPageAtUrl(\"http://www.w3.org/TR/html401/\");");
		System.out.println("// Create page parser");
		System.out.println("PageParser parser = new PageParser(page);");
		System.out.println("// Parse the page meta tags");
		System.out.println("List<ZStringBuilder> metaTags = parser.getTags(\"meta\",true);");
		System.out.println("// Parse the page anchor tags");
		System.out.println("List<ZStringBuilder> anchorTags = parser.getTags(\"a\",false);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("A *PageParser* can parse an HTML web page in order to obtain a list of HTML elements for a specified tag name.");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestPageParser.class));
		System.out.println(" * " + getTester().getLinkForClass(PageParser.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows a subset of the meta and anchor tags for the page at; http://www.w3.org/TR/html401/.");
	}

	@Override
	protected void test(String[] args) {
		ZDKFactory factory = new ZDKFactory();
		Messenger messenger = factory.getMessenger();
		messenger.start();
		PageReader reader = new PageReader(messenger);
		ZStringBuilder page = reader.getPageAtUrl("http://www.w3.org/TR/html401/");
		messenger.stop();
		factory.getWorkerUnion(messenger).stopWorkers();
		messenger.whileWorking();
		
		PageParser parser = new PageParser(page);
		List<ZStringBuilder> tags = null;
		
		tags = parser.getTags("meta",true);
		assertEqual(tags.size(),1,"Meta tags array size does not match expectation");
		if (tags.size()>0) {
			System.out.println(tags.get(0));
		}
		
		System.out.println("");

		tags = parser.getTags("a",false);
		assertEqual(tags.size(),467,"Anchor tags array size does not match expectation");
		if (tags.size()>2) {
			System.out.println(tags.get(0));
			System.out.println(tags.get(1));
			System.out.println(tags.get(2));
		}
	}
}
