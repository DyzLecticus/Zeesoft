package nl.zeesoft.zmmt.test;

import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;

public class ZMMT extends LibraryObject {
	public ZMMT(Tester tester) {
		super(tester);
		setNameAbbreviated("ZMMT");
		setNameFull("Zeesoft MIDI Mod Tracker");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZMMT/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZMMT/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZMMT/");
		getDependencies().add(new ZDK(null));
	}

	public static void main(String[] args) {
		(new ZMMT(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft MIDI Mod Tracker");
		System.out.println("========================");
		System.out.println("Zeesoft MIDI Mod Tracker (ZMMT) is an old school mod tracker that implements the Java MIDI API.");
		describeDependencies();
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZMMT.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestSynthesizerConfiguration(getTester()));
		tests.add(new TestSettings(getTester()));
		tests.add(new TestComposition(getTester()));
	}
}
