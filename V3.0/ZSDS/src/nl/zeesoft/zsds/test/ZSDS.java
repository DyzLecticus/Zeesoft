package nl.zeesoft.zsds.test;

import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;
import nl.zeesoft.zsd.test.ZSD;

/**
 * Documents and tests the ZSD.
 */
public class ZSDS extends LibraryObject {
	public ZSDS(Tester tester) {
		super(tester);
		setNameAbbreviated("ZSDS");
		setNameFull("Zeesoft Smart Dialog Server");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZSDS/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSDS/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSDS/");
		getDependencies().add(new ZDK(null));
		getDependencies().add(new ZSD(null));
	}

	public static void main(String[] args) {
		(new ZSDS(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Smart Dialog Server");
		System.out.println("===========================");
		System.out.println("Zeesoft Smart Dialog Server (ZSDS) is an open source application server exposes the Zeesoft Smart Dialogs (ZSD) API.  ");
		System.out.println();
		describeDependencies();
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZSDS.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestTestConfiguration(getTester()));
		tests.add(new TestTestCaseSet(getTester()));
	}
}
