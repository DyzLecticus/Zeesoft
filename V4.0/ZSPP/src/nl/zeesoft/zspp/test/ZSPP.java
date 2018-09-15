package nl.zeesoft.zspp.test;

import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;
import nl.zeesoft.zodb.test.ZODB;
import nl.zeesoft.zspp.mod.ModZSPP;

public class ZSPP extends LibraryObject {
	public ZSPP(Tester tester) {
		super(tester);
		setNameAbbreviated("ZSPP");
		setNameFull("Zeesoft Sequence Preprocessor");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V4.0/ZSPP/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V4.0/ZSPP/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZSPP/");
		getDependencies().add(new ZDK(null));
		getDependencies().add(new ZODB(null));
	}

	public static void main(String[] args) {
		(new ZSPP(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Sequence Preprocessor");
		System.out.println("=============================");
		System.out.println(ModZSPP.DESC);
		System.out.println();
		describeDependencies();
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZSPP.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestPreprocessorRequestResponse(getTester()));
	}
}
