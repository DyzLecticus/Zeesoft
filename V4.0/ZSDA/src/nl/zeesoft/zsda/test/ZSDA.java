package nl.zeesoft.zsda.test;

import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;
import nl.zeesoft.zodb.test.ZODB;
import nl.zeesoft.zsda.mod.ModZSDA;

public class ZSDA extends LibraryObject {
	public ZSDA(Tester tester) {
		super(tester);
		setNameAbbreviated("ZSDA");
		setNameFull("Zeesoft Streaming Data Analyzer");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V4.0/ZSDA/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V4.0/ZSDA/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZSDA/");
		getDependencies().add(new ZDK(null));
		getDependencies().add(new ZODB(null));
	}

	public static void main(String[] args) {
		(new ZSDA(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Streaming Data Analyzer");
		System.out.println("===============================");
		System.out.println(ModZSDA.DESC);
		System.out.println();
		describeDependencies();
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZSDA.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		//tests.add(new TestGeneticCode(getTester()));
	}
}
