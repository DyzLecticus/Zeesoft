package nl.zeesoft.zso.test;

import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;
import nl.zeesoft.zjmo.Orchestrator;
import nl.zeesoft.zjmo.test.ZJMO;

public class ZSO extends LibraryObject {
	public ZSO(Tester tester) {
		super(tester);
		setNameAbbreviated("ZSO");
		setNameFull("Zeesoft Sample Orchestration");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZSO/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSO/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSO/");
		getDependencies().add(new ZDK(null));
		getDependencies().add(new ZJMO(null));
	}

	public static void main(String[] args) {
		if (args!=null && args.length>1 && 
			(args[0].equals(Orchestrator.GENERATE) || args[0].equals(Orchestrator.START) || args[0].equals(Orchestrator.STOP))
			) {
			Orchestrator.main(args);
		} else {
			(new ZSO(new Tester())).describeAndTest(args);
		}
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Sample Orchestration");
		System.out.println("============================");
		System.out.println("Zeesoft Sample Orchestration (ZSO) is an extendable sample based orchestra that can play compositions.");
		System.out.println();
		describeDependencies();
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZSO.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestDemoComposition(getTester()));
	}
}
