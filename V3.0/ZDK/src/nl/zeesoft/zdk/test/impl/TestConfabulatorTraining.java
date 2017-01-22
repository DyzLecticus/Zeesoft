package nl.zeesoft.zdk.test.impl;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.confabulator.Confabulator;
import nl.zeesoft.zdk.confabulator.Link;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestConfabulatorTraining extends TestObject {
	public static void main(String[] args) {
		(new TestConfabulatorTraining()).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create and train a *Confabulator* instance.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create confabulator");
		System.out.println("Confabulator confabulator = new Confabulator();");
		System.out.println("// Train confabulator");
		System.out.println("confabulator.learnSequence(\"Example symbol sequence.\",\"Optional Example Context Symbols\");");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("A *Confabulator* can learn symbol sequences (i.e. sentences), optionally combined with certain context symbols (i.e. subject(s)).");
		System.out.println("When trained, a *Confabulator* can be used to;");
		System.out.println(" * Confabulate one or more context symbols for a certain input sequence.");
		System.out.println(" * Confabulate a correction for a certain input sequence, optionally restricted by one or more context symbols.");
		System.out.println(" * Confabulate a starter sequence or an extension for an input sequence, optionally restricted by one or more context symbols.");
		System.out.println();
		System.out.println("By default, confabulators limit their maximum link distance to 8 and their maximum link count to 1000.");
		System.out.println("Deviations from these defaults can be specified using the *Confabulator* initialization method.");
		System.out.println("When the link count of one of the links hits the specified count maximum, all *Confabulator* link counts are divided by 2.");
		System.out.println("Links that have a count of 1 are removed by this division process.");
		System.out.println("When repeatedly confronted with a slowly changing training set, this mechanism allows the *Confabulator* to slowly forget links that are no longer part of the training set.");
		System.out.println();
		Tester.getInstance().describeMock(MockConfabulator.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + Tester.getInstance().getLinkForClass(TestConfabulatorTraining.class));
		System.out.println(" * " + Tester.getInstance().getLinkForClass(MockConfabulator.class));
		System.out.println(" * " + Tester.getInstance().getLinkForClass(Confabulator.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows a brief summary of the link data that is created based on the training set.");
		System.out.println("Please note the relatively large amount of links compared to the size of the training set.");
	}

	@Override
	protected void test(String[] args) {
		Date start = new Date();
		long ms = 0;
		Confabulator confabulator = (Confabulator) Tester.getInstance().getMockedObject(MockConfabulator.class.getName());
		ms = (new Date()).getTime() - start.getTime();
		int i = 0;
		List<Link> links = confabulator.getLinks();
		for (Link lnk: links) {
			i++;
			if (i<=10 || i>(links.size()-10)) {
				System.out.println("Link: "+ i + ", from: '" + lnk.getSymbolFrom() + "', to: '" + lnk.getSymbolTo() + "', distance: " + lnk.getDistance() + ", count: " + lnk.getCount() + ", context: '" + lnk.getSymbolContext() + "'");
			} else if (i==11) {
				System.out.println();
				System.out.println("[ ... ]");
				System.out.println();
			}
		}
		System.out.println("Total Links: " + i + " (" + ms + " ms)");
		
		assertEqual(links.size(),1082,"Total number of links does not match expectation");
	}
}
