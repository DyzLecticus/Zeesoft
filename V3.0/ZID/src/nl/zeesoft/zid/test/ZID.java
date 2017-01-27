package nl.zeesoft.zid.test;

import java.util.List;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

/**
 * Documents and tests the ZID.
 */
public class ZID {
	public static void main(String[] args) {
		Tester.getInstance().setBaseUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID");

		String version = "";
		if (args!=null && args.length>=1 && args[0]!=null && args[0].length()>0) {
			version = args[0];
		} else {
			version = "[???]";
		}
		
		System.out.println("Zeesoft Intelligent Dialogs");
		System.out.println("==============================");
		System.out.println("Zeesoft Intelligent Dialogs (ZID) is an open source library for Java application development.");
		System.out.println("It provides support for defining and handling intelligent dialogs; translate speech into parameterized program calls.");
		System.out.println("The ZID combines and extends the following Zeesoft libraries;  ");
		System.out.println(" * [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK)  ");
		System.out.println(" * [Zeesoft Symbolic Cognition](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZSC)  ");
		System.out.println(" * [Zeesoft Symbolic Pattern Recognition](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZSPR)  ");
		System.out.println();
		System.out.println("**Release downloads**  ");
		System.out.println("Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZID/releases/zid-" + version + ".zip) to download the latest ZID release (version " + version + ").");
		System.out.println("All ZID releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZID/releases).");
		System.out.println("ZID releases contain;  ");
		System.out.println(" * the ZID jar file.  ");
		System.out.println(" * the corresponding ZDK, ZSC and ZSPR jar files.  ");
		System.out.println(" * this README file.  ");
		System.out.println(" * Separate zip files containing the generated java documentation for each jar file.  ");
		System.out.println();
		System.out.println("*All jar files in the release include source code and build scripts.*  ");
		System.out.println();
		System.out.println("**Self documenting and self testing**  ");
		System.out.println("The tests used to develop the ZID are also used to generate this README file.");		
		System.out.println("Run the " + Tester.getInstance().getLinkForClass(ZID.class) + " class as a java application to print this documentation to the standard out.");
		System.out.println("Click [here](#test-results) to scroll down to the test result summary.");
		System.out.println();

		List<TestObject> tests = Tester.getInstance().getTests();
		tests.add(new TestDialogHandler());
		boolean success = Tester.getInstance().test(args);
		if (success) {
			System.exit(0);
		} else {
			System.exit(1);
		}
	}
}
