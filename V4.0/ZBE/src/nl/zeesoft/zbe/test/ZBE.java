package nl.zeesoft.zbe.test;

import java.util.List;

import nl.zeesoft.zbe.mod.ModZBE;
import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;
import nl.zeesoft.zodb.test.ZODB;

public class ZBE extends LibraryObject {
	public ZBE(Tester tester) {
		super(tester);
		setNameAbbreviated("ZBE");
		setNameFull("Zeesoft Brain Evolver");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V4.0/ZBE/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V4.0/ZBE/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZBE/");
		getDependencies().add(new ZDK(null));
		getDependencies().add(new ZODB(null));
	}

	public static void main(String[] args) {
		(new ZBE(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Brain Evolver");
		System.out.println("=====================");
		System.out.println(ModZBE.DESC);
		System.out.println();
		describeDependencies();
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZBE.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestGeneticCode(getTester()));
	}
}
