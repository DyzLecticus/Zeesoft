package nl.zeesoft.zmc.test;

import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;
import nl.zeesoft.zmc.mod.ModZMC;
import nl.zeesoft.zodb.test.ZODB;

public class ZMC extends LibraryObject {
	public ZMC(Tester tester) {
		super(tester);
		setNameAbbreviated("ZMC");
		setNameFull("Zeesoft MIDI Composer");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V4.0/ZMC/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V4.0/ZMC/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZMC/");
		getDependencies().add(new ZDK(null));
		getDependencies().add(new ZODB(null));
	}

	public static void main(String[] args) {
		(new ZMC(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft MIDI Composer");
		System.out.println("=====================");
		System.out.println(ModZMC.DESC);
		System.out.println();
		describeDependencies();
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZMC.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		//tests.add(new TestGeneticCode(getTester()));
	}
}
