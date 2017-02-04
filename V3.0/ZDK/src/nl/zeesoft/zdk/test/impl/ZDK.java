package nl.zeesoft.zdk.test.impl;

import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

/**
 * Documents and tests the ZDK.
 */
public class ZDK extends LibraryObject {
	public ZDK(Tester tester) {
		super(tester);
		setNameAbbreviated("ZDK");
		setNameFull("Zeesoft Development Kit");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZDK/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/");
	}

	public static void main(String[] args) {
		(new ZDK(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Development Kit");
		System.out.println("=======================");
		System.out.println("The Zeesoft Development Kit (ZDK) is an open source library for Java application development.  ");
		System.out.println();
		System.out.println("It provides support for;  ");
		System.out.println(" * Extended StringBuilder manipulation and validation  ");
		System.out.println(" * Multi threading  ");
		System.out.println(" * Application message handling");
		System.out.println(" * Self documenting and testing libraries  ");
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZDK.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestZStringEncoder(getTester()));
		tests.add(new TestZStringSymbolParser(getTester()));
		tests.add(new TestMessenger(getTester()));
	}
}
