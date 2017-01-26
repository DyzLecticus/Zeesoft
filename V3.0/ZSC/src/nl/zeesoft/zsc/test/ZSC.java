package nl.zeesoft.zsc.test;

import java.util.List;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

/**
 * Documents and tests the ZSC.
 */
public class ZSC {
	public static void main(String[] args) {
		Tester.getInstance().setBaseUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSC");

		String version = "";
		if (args!=null && args.length>=1 && args[0]!=null && args[0].length()>0) {
			version = args[0];
		} else {
			version = "[???]";
		}
		
		System.out.println("Zeesoft Symbolic Confabulation");
		System.out.println("==============================");
		System.out.println("Zeesoft Symbolic Confabulation (ZSC) is an open source library for Java application development.");
		System.out.println("It provides support for confabulation; the process of learning, generating and forgetting context sensitive symbolic sequences.");
		System.out.println();
		System.out.println("**Release downloads**  ");
		System.out.println("Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSC/releases/zsc-" + version + ".zip) to download the latest ZSC release (version " + version + ").");
		System.out.println("All ZSC releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZSC/releases).");
		System.out.println("ZSC releases contain;  ");
		System.out.println(" * the ZDMK jar file.  ");
		System.out.println(" * the corresponding ZDK jar file.  ");
		System.out.println(" * this README file.  ");
		System.out.println(" * Separate zip files containing the generated java documentation for each jar file.  ");
		System.out.println();
		System.out.println("*All jar files in the release include source code and build scripts.*  ");
		System.out.println();
		System.out.println("**Self documenting and self testing**  ");
		System.out.println("The tests used to develop the ZSC are also used to generate this README file.");		
		System.out.println("Run the " + Tester.getInstance().getLinkForClass(ZSC.class) + " class as a java application to print this documentation to the standard out.");
		System.out.println("Click [here](#test-results) to scroll down to the test result summary.");
		System.out.println();

		List<TestObject> tests = Tester.getInstance().getTests();
		tests.add(new TestConfabulatorTraining());
		tests.add(new TestConfabulatorContextConfabulation());
		tests.add(new TestConfabulatorCorrectionConfabulation());
		tests.add(new TestConfabulatorExtensionConfabulation());
		boolean success = Tester.getInstance().test(args);
		if (success) {
			System.exit(0);
		} else {
			System.exit(1);
		}
	}
}
