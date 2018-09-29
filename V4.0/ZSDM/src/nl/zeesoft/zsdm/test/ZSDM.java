package nl.zeesoft.zsdm.test;

import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;
import nl.zeesoft.zevt.test.ZEVT;
import nl.zeesoft.znlb.test.ZNLB;
import nl.zeesoft.zodb.test.ZODB;
import nl.zeesoft.zsc.test.ZSC;
import nl.zeesoft.zsdm.mod.ModZSDM;

public class ZSDM extends LibraryObject {
	public ZSDM(Tester tester) {
		super(tester);
		setNameAbbreviated("ZSDM");
		setNameFull("Zeesoft Smart Dialog Manager");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V4.0/ZSDM/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V4.0/ZSDM/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZSDM/");
		getDependencies().add(new ZDK(null));
		getDependencies().add(new ZODB(null));
		getDependencies().add(new ZNLB(null));
		getDependencies().add(new ZEVT(null));
		getDependencies().add(new ZSC(null));
	}

	public static void main(String[] args) {
		(new ZSDM(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Smart Dialog Manager");
		System.out.println("============================");
		System.out.println(ModZSDM.DESC);
		System.out.println();
		describeDependencies();
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZSDM.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestDialog(getTester()));
	}
}
