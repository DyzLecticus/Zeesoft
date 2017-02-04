package nl.zeesoft.zspr.test;

import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;
import nl.zeesoft.zspr.pattern.PatternManager;

public class ZSPR extends LibraryObject {
	public ZSPR(Tester tester) {
		super(tester);
		setNameAbbreviated("ZSPR");
		setNameFull("Zeesoft Symbolic Pattern Recognition");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZSPR/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSPR/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSPR/");
		getDependencies().add(new ZDK(null));
	}

	public static void main(String[] args) {
		(new ZSPR(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Symbolic Pattern Recognition");
		System.out.println("====================================");
		System.out.println("Zeesoft Symbolic Pattern Recognition (ZSPR) is an open source library for Java application development.");
		System.out.println("It provides support for sequential symbolic pattern recognition.");
		describeDependencies();
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZSPR.class);
		System.out.println();
		System.out.println("Symbolic Pattern Recognition");
		System.out.println("----------------------------");
		System.out.println("When parsing symbolic sequences to discern meaning, certain patterns may be easily translated into primary objects like numbers, dates and duration.");
		System.out.println("This library provides an extendable, thread safe " + getTester().getLinkForClass(PatternManager.class) + " to do just that.");
		System.out.println("Please note that initializing the default *PatternManager* might take a few seconds and that it requires quite a lot of memory.");
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestPatternManager(getTester()));
		tests.add(new TestPatternManagerScan(getTester()));
	}
}
