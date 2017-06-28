package nl.zeesoft.zeetracker.test;

import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;
import nl.zeesoft.zeetracker.ZeeTracker;
import nl.zeesoft.zmmt.test.ZMMT;

public class ZT extends LibraryObject {
	public ZT(Tester tester) {
		super(tester);
		setNameAbbreviated("ZT");
		setNameFull("ZeeTracker");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZeeTracker/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZeeTracker/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZeeTracker/");
		getDependencies().add(new ZDK(null));
		getDependencies().add(new ZMMT(null));
	}

	public static void main(String[] args) {
		(new ZT(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("ZeeTracker");
		System.out.println("==========");
		System.out.print(ZeeTracker.getDescription());
		System.out.println();
		System.out.print(ZeeTracker.getRequirementsAndDownload());
		System.out.println();
		describeDependencies();
		System.out.println();
		describeTesting(ZT.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestSettings(getTester()));
	}
}
