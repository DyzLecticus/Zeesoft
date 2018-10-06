package nl.zeesoft.zals.test;

import java.util.List;

import nl.zeesoft.zals.mod.ModZALS;
import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;
import nl.zeesoft.zodb.test.ZODB;
import nl.zeesoft.zsc.test.ZSC;

public class ZALS extends LibraryObject {
	public ZALS(Tester tester) {
		super(tester);
		setNameAbbreviated("ZALS");
		setNameFull("Zeesoft Artificial Life Simulator");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V4.0/ZALS/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V4.0/ZALS/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZALS/");
		getDependencies().add(new ZDK(null));
		getDependencies().add(new ZODB(null));
		getDependencies().add(new ZSC(null));
	}

	public static void main(String[] args) {
		(new ZALS(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Artificial Life Simulator");
		System.out.println("============================");
		System.out.println(ModZALS.DESC);
		System.out.println();
		describeDependencies();
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZALS.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestDialog(getTester()));
	}
}
