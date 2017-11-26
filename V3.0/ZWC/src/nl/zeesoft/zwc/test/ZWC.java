package nl.zeesoft.zwc.test;

import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;

/**
 * Documents and tests the ZSC.
 */
public class ZWC extends LibraryObject {
	public ZWC(Tester tester) {
		super(tester);
		setNameAbbreviated("ZWC");
		setNameFull("Zeesoft Web Crawler");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZWC/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZWC/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZWC/");
		getDependencies().add(new ZDK(null));
	}

	public static void main(String[] args) {
		(new ZWC(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Web Crawler");
		System.out.println("===================");
		System.out.println("Zeesoft Web Crawler (ZWC) is an open source library for Java application development.");
		System.out.println("It provides support for crawling web sites in order to extract data.");
		describeDependencies();
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZWC.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestPageReader(getTester()));
		tests.add(new TestPageParser(getTester()));
		tests.add(new TestRobotsParser(getTester()));
		tests.add(new TestCrawler(getTester()));
	}
}
