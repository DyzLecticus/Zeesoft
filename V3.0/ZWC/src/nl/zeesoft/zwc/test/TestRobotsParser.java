package nl.zeesoft.zwc.test;

import java.util.List;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zwc.page.PageReader;
import nl.zeesoft.zwc.page.RobotsParser;

public class TestRobotsParser extends TestObject {
	public TestRobotsParser(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestRobotsParser(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create and use a *RobotsParser* instance.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create robots parser");
		System.out.println("RobotsParser robots = new RobotsParser(new PageReader(),\"http://www.w3.org/TR/html401/\");");
		System.out.println("// Get the disallowed URL list");
		System.out.println("List<String> disallowedUrls = robots.getDisallowedUrls();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("A *RobotsParser* can parse the robots.txt file and return the disallowed URL list.");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestRobotsParser.class));
		System.out.println(" * " + getTester().getLinkForClass(RobotsParser.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows a subset of the disallowed for the web site at http://www.w3.org/.");
	}

	@Override
	protected void test(String[] args) {
		RobotsParser robots = new RobotsParser(new PageReader(),MockPage.TEST_URL);
		List<String> disallowedUrls = robots.getDisallowedUrls();
		assertEqual(disallowedUrls.size(),74,"Disallowed URL list size does not match expectation");
		if (disallowedUrls.size()>2) {
			System.out.println(disallowedUrls.get(0));
			System.out.println(disallowedUrls.get(1));
			System.out.println(disallowedUrls.get(2));
		}
	}
}
