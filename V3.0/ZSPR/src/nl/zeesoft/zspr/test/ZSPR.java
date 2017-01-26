package nl.zeesoft.zspr.test;

import java.util.List;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class ZSPR {

	public static void main(String[] args) {
		Tester.getInstance().setBaseUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSPR");
		
		String version = "";
		if (args!=null && args.length>=1 && args[0]!=null && args[0].length()>0) {
			version = args[0];
		} else {
			version = "[???]";
		}
		
		System.out.println("Zeesoft Symbolic Pattern Recognition");
		System.out.println("====================================");
		System.out.println("Zeesoft Symbolic Pattern Recognition (ZSPR) is an open source library for Java application development.");
		System.out.println("It provides support for sequential symbolic pattern recognition.");
		System.out.println("The ZSPR extends the [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK) (ZDK).");
		System.out.println();
		System.out.println("**Release downloads**  ");
		System.out.println("Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSPR/releases/zspr-" + version + ".zip) to download the latest ZSPR release (version " + version + ").");
		System.out.println("All ZSPR releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZSPR/releases).");
		System.out.println("ZSPR releases contain;  ");
		System.out.println(" * the ZSPR jar file.  ");
		System.out.println(" * the corresponding ZDK jar file.  ");
		System.out.println(" * this README file.  ");
		System.out.println(" * Separate zip files containing the generated java documentation for each jar file.  ");
		System.out.println();
		System.out.println("*All jar files in the release include source code and build scripts.*  ");
		System.out.println();
		System.out.println("**Self documenting and self testing**  ");
		System.out.println("The tests used to develop the ZSPR are also used to generate this README file.");		
		System.out.println("Run the " + Tester.getInstance().getLinkForClass(ZSPR.class) + " class as a java application to print this documentation to the standard out.");
		System.out.println("Click [here](#test-results) to scroll down to the test result summary.");
		System.out.println();
		
		List<TestObject> tests = Tester.getInstance().getTests();
		tests.add(new TestPatternManager());
		tests.add(new TestPatternManagerScan());
		boolean success = Tester.getInstance().test(args);
		if (success) {
			System.exit(0);
		} else {
			System.exit(1);
		}
	}

}
