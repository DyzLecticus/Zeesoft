package nl.zeesoft.zipd.test;

import java.util.List;

import nl.zeesoft.zdk.test.ZDK;
import nl.zeesoft.zdk.test.util.LibraryObject;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;

/**
 * Documents and tests the ZDK.
 */
public class ZIPD extends LibraryObject {
	public ZIPD(Tester tester) {
		super(tester);
		setNameAbbreviated("ZIPD");
		setNameFull("Zeesoft Instrument Patch Designer");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V5.0/ZIPD/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V5.0/ZIPD/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZIPD/");
		getDependencies().add(new ZDK(null));
	}

	public static void main(String[] args) {
		(new ZIPD(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Instrument Patch Designer");
		System.out.println("=================================");
		System.out.println("Zeesoft Instrument Patch Designer is an application that can be used to design advanced MIDI instrument patches.  ");
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZIPD.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		//tests.add(new TestStr(getTester()));
	}
}
