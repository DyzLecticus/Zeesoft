package nl.zeesoft.zsc.test;

import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;

/**
 * Documents and tests the ZSC.
 */
public class ZSC extends LibraryObject {
	public ZSC() {
		setNameAbbreviated("ZSC");
		setNameFull("Zeesoft Symbolic Confabulation");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZSC/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSC/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSC/");
		getDependencies().add(new ZDK());
	}

	public static void main(String[] args) {
		(new ZSC()).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Symbolic Confabulation");
		System.out.println("==============================");
		System.out.println("Zeesoft Symbolic Confabulation (ZSC) is an open source library for Java application development.");
		System.out.println("It provides support for confabulation; the process of learning, generating and forgetting context sensitive symbolic sequences.");
		describeDependencies();
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZSC.class);
		System.out.println();
	}

	@Override
	public boolean test(String[] args) {
		List<TestObject> tests = Tester.getInstance().getTests();
		tests.add(new TestConfabulatorTraining());
		tests.add(new TestConfabulatorContextConfabulation());
		tests.add(new TestConfabulatorCorrectionConfabulation());
		tests.add(new TestConfabulatorExtensionConfabulation());
		return Tester.getInstance().test(args);
	}
}
