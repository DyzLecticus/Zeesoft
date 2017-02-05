package nl.zeesoft.zidm.test;

import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;
import nl.zeesoft.zdm.test.ZDM;
import nl.zeesoft.zid.test.ZID;
import nl.zeesoft.zsc.test.ZSC;
import nl.zeesoft.zspr.test.ZSPR;

/**
 * Documents and tests the ZID.
 */
public class ZIDM extends LibraryObject {
	public ZIDM(Tester tester) {
		super(tester);
		setNameAbbreviated("ZIDM");
		setNameFull("Zeesoft Interactive Data Modelling");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZIDM/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZIDM/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZIDM/");
		getDependencies().add(new ZDK(null));
		getDependencies().add(new ZSC(null));
		getDependencies().add(new ZSPR(null));
		getDependencies().add(new ZID(null));
		getDependencies().add(new ZDM(null));
	}

	public static void main(String[] args) {
		(new ZIDM(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Interactive Data Modelling");
		System.out.println("==================================");
		System.out.println("Zeesoft Interactive Data Modelling (ZIDM) is an open source library for Java application development.");
		System.out.println("It provides support for interactive dialog based data modelling.");
		System.out.println();
		describeDependencies();
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZIDM.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		// TODO: Add tests
	}
}
