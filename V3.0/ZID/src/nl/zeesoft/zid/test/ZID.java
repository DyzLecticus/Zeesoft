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
	public ZID(Tester tester) {
		super(tester);
		setNameAbbreviated("ZID");
		setNameFull("Zeesoft Intelligent Dialogs");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZID/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZID/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/");
		getDependencies().add(new ZDK(null));
		getDependencies().add(new ZSC(null));
		getDependencies().add(new ZSPR(null));
	}

	public static void main(String[] args) {
		(new ZID(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Intelligent Dialogs");
		System.out.println("==============================");
		System.out.println("Zeesoft Intelligent Dialogs (ZID) is an open source library for Java application development.");
		System.out.println("It provides support for defining and handling written dialogs while translating the input into parameterized program calls.");
		System.out.println();
		describeDependencies();
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZID.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestDialog(getTester()));
		tests.add(new TestDialogHandler(getTester()));
	}
}
