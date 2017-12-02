package nl.zeesoft.zwc.test;

import java.util.List;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zwc.Crawler;

public class TestCrawler extends TestObject {
	public TestCrawler(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestCrawler(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create and use a *Crawler* instance.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create crawler");
		System.out.println("Crawler crawler = new Crawler(\"http://www.w3.org/TR/html401/\");");
		System.out.println("// Initialize crawler");
		System.out.println("String err = crawler.initialize();");
		System.out.println("// Start crawler");
		System.out.println("crawler.start();");
		System.out.println("// Get the crawled URLs");
		System.out.println("List<String> crawledUrls = crawler.getCrawledUrls();");
		System.out.println("// Get the crawled pages");
		System.out.println("TreeMap<String,ZStringBuilder> pages = crawler.getPages();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("A *Crawler* can crawl a web site in order to obtain all page data from the site.");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestCrawler.class));
		System.out.println(" * " + getTester().getLinkForClass(Crawler.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows a subset of the crawled URLs for the site under http://www.w3.org/TR/html401/.");
	}

	@Override
	protected void test(String[] args) {
		Crawler crawler = new Crawler(MockPage.TEST_URL);
		String err = crawler.initialize();
		assertEqual(err,"","Initializing crawler returned an unexpected error");
		crawler.start();
		
		while(!crawler.isDone()) {
			sleep(1000);
		}
		
		List<String> crawledUrls = crawler.getCrawledUrls();
		assertEqual(crawledUrls.size(),34,"Crawled URL list size does not match expectation");
		if (crawledUrls.size()>4) {
			System.out.println(crawledUrls.get(0));
			System.out.println(crawledUrls.get(1));
			System.out.println(crawledUrls.get(2));
			System.out.println(crawledUrls.get(3));
			System.out.println(crawledUrls.get(4));
		}
	}
}
