package nl.zeesoft.zjmo.test;

import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;
import nl.zeesoft.zjmo.Orchestrator;

public class ZJMO extends LibraryObject {
	public ZJMO(Tester tester) {
		super(tester);
		setNameAbbreviated("ZJMO");
		setNameFull("Zeesoft JSON Machine Orchestration");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZJMO/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZJMO/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/");
		getDependencies().add(new ZDK(null));
	}

	public static void main(String[] args) {
		if (args!=null && args.length>1 && 
			(
				args[0].equals(Orchestrator.GENERATE) || 
				args[0].equals(Orchestrator.UPDATE) || 
				args[0].equals(Orchestrator.START) || 
				args[0].equals(Orchestrator.STOP)
			)
			) {
			Orchestrator.main(args);
		} else {
			(new ZJMO(new Tester())).describeAndTest(args);
		}
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft JSON Machine Orchestration");
		System.out.println("==================================");
		System.out.println("Zeesoft JSON Machine Orchestration (ZJMO) is an open source library for Java application development.");
		System.out.println("The aim of the ZJMO project is to provide a scalable, high availability, JSON based, work distribution architecture.");
		System.out.println("Imagine an orchestra where the conductor directs the other members to play a certain composition.");
		System.out.println("All the members speak JSON on two TCP ports; one for control and another one for work.");
		describeDependencies();
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZJMO.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestTestOrchestra(getTester()));
		tests.add(new TestConductor(getTester()));
		tests.add(new TestMemberOnlineOffline(getTester()));
		tests.add(new TestWorkRequest(getTester()));
	}
}
