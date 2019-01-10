package nl.zeesoft.zsmc.test;

import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;
import nl.zeesoft.zodb.test.ZODB;
import nl.zeesoft.zsmc.mod.ModZSMC;

public class ZSMC extends LibraryObject {
	public ZSMC(Tester tester) {
		super(tester);
		setNameAbbreviated("ZSMC");
		setNameFull("Zeesoft Symbolic Confabulators");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V4.0/ZSMC/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V4.0/ZSMC/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZSMC/");
		getDependencies().add(new ZDK(null));
		getDependencies().add(new ZODB(null));
	}

	public static void main(String[] args) {
		(new ZSMC(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Symbolic Multitrheaded Confabulators");
		System.out.println("============================================");
		System.out.println(ModZSMC.DESC);
		System.out.println();
		describeDependencies();
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZSMC.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestKnowledgeBase(getTester()));
	}
}
