package nl.zeesoft.zevt.test;

import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;
import nl.zeesoft.zevt.app.AppZEVT;
import nl.zeesoft.zodb.test.ZODB;

/**
 * Documents and tests the ZSD.
 */
public class ZEVT extends LibraryObject {
	public ZEVT(Tester tester) {
		super(tester);
		setNameAbbreviated("ZEVT");
		setNameFull("Zeesoft Entity Value Translator");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V4.0/ZEVT/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V4.0/ZEVT/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZEVT/");
		getDependencies().add(new ZDK(null));
		getDependencies().add(new ZODB(null));
	}

	public static void main(String[] args) {
		(new ZEVT(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Entity Value Translator");
		System.out.println("===============================");
		System.out.println(AppZEVT.DESC);
		System.out.println();
		describeDependencies();
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZEVT.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestTranslator(getTester()));
	}
}
