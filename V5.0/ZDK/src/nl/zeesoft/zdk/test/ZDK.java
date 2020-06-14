package nl.zeesoft.zdk.test;

import java.util.List;

import nl.zeesoft.zdk.test.collection.TestCollections;
import nl.zeesoft.zdk.test.http.TestHttpServer;
import nl.zeesoft.zdk.test.thread.TestCodeRunnerChain;
import nl.zeesoft.zdk.test.thread.TestRunCode;
import nl.zeesoft.zdk.test.util.LibraryObject;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;

/**
 * Documents and tests the ZDK.
 */
public class ZDK extends LibraryObject {
	public ZDK(Tester tester) {
		super(tester);
		setNameAbbreviated("ZDK");
		setNameFull("Zeesoft Development Kit");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V5.0/ZDK/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V5.0/ZDK/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/");
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
		System.out.println(" * Self documenting and testing libraries  ");
		System.out.println(" * Extended StringBuilder manipulation and validation  ");
		System.out.println(" * (Mock) File writing and reading  ");
		System.out.println(" * Multi threading  ");
		System.out.println(" * Object persistence  ");
		System.out.println(" * HTTP servers and requests  ");
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZDK.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestStr(getTester()));
		tests.add(new TestRunCode(getTester()));
		tests.add(new TestCodeRunnerChain(getTester()));
		tests.add(new TestCollections(getTester()));
		tests.add(new TestHttpServer(getTester()));
	}
}