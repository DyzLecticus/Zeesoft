package nl.zeesoft.zsmc.test;

import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;

/**
 * Documents and tests the ZSMC.
 */
public class ZSMC extends LibraryObject {
	public ZSMC(Tester tester) {
		super(tester);
		setNameAbbreviated("ZSMC");
		setNameFull("Zeesoft Symbolic Multithreaded Confabulation");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZSMC/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSMC/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSMC/");
		getDependencies().add(new ZDK(null));
	}

	public static void main(String[] args) {
		(new ZSMC(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Symbolic Multithreaded Confabulation");
		System.out.println("============================================");
		System.out.println("The Zeesoft Symbolic Multithreaded Confabulation (ZSMC) is an open source library for Java application development.  ");
		System.out.println();
		System.out.println("It provides support for;  ");
		System.out.println(" * Basic spelling checking  ");
		System.out.println(" * Symbolic Multithreaded Confabulation  ");
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZSMC.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestSpellingChecker(getTester()));
		tests.add(new TestKnowledgeBases(getTester()));
	}
}
