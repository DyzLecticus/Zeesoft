package nl.zeesoft.zdk.test.impl;

import java.util.List;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

/**
 * Documents and tests the ZDK.
 */
public class ZDK {
	public static void main(String[] args) {
		Tester.getInstance().setBaseUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK");

		String version = "";
		if (args!=null && args.length>=1 && args[0]!=null && args[0].length()>0) {
			version = args[0];
		} else {
			version = "[???]";
		}
		
		System.out.println("Zeesoft Development Kit");
		System.out.println("=======================");
		System.out.println("The Zeesoft Development Kit (ZDK) is an open source library for Java application development.");
		System.out.println();
		System.out.println("It provides support for;");
		System.out.println(" * [Advanced encoding and decoding](" + Tester.getInstance().getAnchorUrlForTest(TestEncoderDecoder.class) + ").");
		System.out.println(" * [Extended StringBuilder manipulation](" + Tester.getInstance().getAnchorUrlForTest(TestSymbolParser.class) + ").");
		System.out.println(" * [Multi threading](" + Tester.getInstance().getAnchorUrlForTest(TestMessenger.class) + ").");
		System.out.println(" * [Application message handling](" + Tester.getInstance().getAnchorUrlForTest(TestMessenger.class) + ").");
		System.out.println();
		System.out.println("**Release downloads**  ");
		System.out.println("Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZDK/releases/zdk-" + version + ".zip) to download the latest ZDK release (version " + version + ").");
		System.out.println("All ZDK releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK/releases).");
		System.out.println("ZDK releases contain the ZDK jar file (includes source code and build scripts), this README file, and a separate zip file containing the generated java documentation.");
		System.out.println();
		System.out.println("**Self documenting and self testing**  ");
		System.out.println("The tests used to develop the ZDK are also used to generate this README file.");		
		System.out.println("Run the " + Tester.getInstance().getLinkForClass(ZDK.class) + " class as a java application to print this documentation to the standard out.");
		System.out.println("Click [here](#test-results) to scroll down to the test result summary.");
		System.out.println();

		List<TestObject> tests = Tester.getInstance().getTests();
		tests.add(new TestEncoderDecoder());
		tests.add(new TestSymbolParser());
		tests.add(new TestMessenger());
		boolean success = Tester.getInstance().test(args);
		if (success) {
			System.exit(0);
		} else {
			System.exit(1);
		}
	}
}
