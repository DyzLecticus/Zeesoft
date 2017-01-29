package nl.zeesoft.zdk.test.impl;

import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

/**
 * Documents and tests the ZDK.
 */
public class ZDK extends LibraryObject {
	public ZDK() {
		setNameAbbreviated("ZDK");
		setNameFull("Zeesoft Development Kit");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZDK/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/");
	}

	public static void main(String[] args) {
		(new ZDK()).describeAndTest(args);
	}

	@Override
	public void describe() {
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
		describeRelease();
		System.out.println();
		describeTesting(ZDK.class);
		System.out.println();
	}

	@Override
	public boolean test(String[] args) {
		List<TestObject> tests = Tester.getInstance().getTests();
		tests.add(new TestEncoderDecoder());
		tests.add(new TestSymbolParser());
		tests.add(new TestMessenger());
		return Tester.getInstance().test(args);
	}
}
