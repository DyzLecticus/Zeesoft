package nl.zeesoft.zwc.test;

import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zwc.Crawler;
import nl.zeesoft.zwc.page.PageParser;

public class TestCrawler extends TestObject {
	public TestCrawler(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestCrawler(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		// TODO: Describe
		/*
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
		getTester().describeMock(MockPage.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestCrawler.class));
		System.out.println(" * " + getTester().getLinkForClass(PageParser.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows a subset of the meta and anchor tags for the page.");
		*/
	}

	@Override
	protected void test(String[] args) {
		Crawler crawler = new Crawler(MockPage.TEST_URL);
		crawler.initialize();
		crawler.start();
		
		while(!crawler.isDone()) {
			sleep(1000);
		}
		
		assertEqual(crawler.getCrawledUrls().size(),1,"Crawled URL list size does not match expectation");
	}
}
