package nl.zeesoft.zodb.test;

import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;
import nl.zeesoft.zodb.mod.ModZODB;

public class ZODB extends LibraryObject {
	public ZODB(Tester tester) {
		super(tester);
		setNameAbbreviated("ZODB");
		setNameFull("Zeesoft Object Database");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V4.0/ZODB/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V4.0/ZODB/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZODB/");
		getDependencies().add(new ZDK(null));
	}

	public static void main(String[] args) {
		(new ZODB(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Object Database");
		System.out.println("=======================");
		System.out.println(ModZODB.DESC);
		System.out.println();
		describeDependencies();
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZODB.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestConfig(getTester()));
		tests.add(new TestDatabaseRequest(getTester()));
		tests.add(new TestDatabaseResponse(getTester()));
	}
}
