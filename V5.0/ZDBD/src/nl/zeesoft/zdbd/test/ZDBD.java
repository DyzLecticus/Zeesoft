package nl.zeesoft.zdbd.test;

import java.util.List;

import nl.zeesoft.zdk.test.ZDK;
import nl.zeesoft.zdk.test.util.LibraryObject;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;

/**
 * Documents and tests the ZDBD.
 */
public class ZDBD extends LibraryObject {
	public ZDBD(Tester tester) {
		super(tester);
		setNameAbbreviated("ZDBD");
		setNameFull("Zeesoft Drum & Bass Dreamer");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V5.0/ZDBD/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V5.0/ZDBD/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDBD/");
		getDependencies().add(new ZDK(null));
	}

	public static void main(String[] args) {
		(new ZDBD(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Drum & Bass Dreamer");
		System.out.println("=================================");
		System.out.println("Zeesoft Drum & Bass Dreamer is an application that can be used to generate drum & bass MIDI using HTM.  ");
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZDBD.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestEncoders(getTester()));
		tests.add(new TestInstrumentNetwork(getTester()));
		tests.add(new TestGenerator(getTester()));
	}
}
