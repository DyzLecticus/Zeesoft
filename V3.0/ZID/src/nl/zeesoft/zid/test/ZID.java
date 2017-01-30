package nl.zeesoft.zid.test;

import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;
import nl.zeesoft.zsc.test.ZSC;
import nl.zeesoft.zspr.test.ZSPR;

/**
 * Documents and tests the ZID.
 */
public class ZID extends LibraryObject {
	public ZID() {
		setNameAbbreviated("ZID");
		setNameFull("Zeesoft Intelligent Dialogs");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZID/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZID/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/");
		getDependencies().add(new ZDK());
		getDependencies().add(new ZSC());
		getDependencies().add(new ZSPR());
	}

	public static void main(String[] args) {
		(new ZID()).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Intelligent Dialogs");
		System.out.println("==============================");
		System.out.println("Zeesoft Intelligent Dialogs (ZID) is an open source library for Java application development.");
		System.out.println("It provides support for defining and handling intelligent dialogs; translate speech into parameterized program calls.");
		System.out.println();
		describeDependencies();
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZID.class);
		System.out.println();
	}

	@Override
	public boolean test(String[] args) {
		List<TestObject> tests = Tester.getInstance().getTests();
		tests.add(new TestDialogHandler());
		return Tester.getInstance().test(args);
	}
}
