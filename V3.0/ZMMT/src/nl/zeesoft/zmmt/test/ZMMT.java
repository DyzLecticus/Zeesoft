package nl.zeesoft.zmmt.test;

import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;

public class ZMMT extends LibraryObject {
	public ZMMT(Tester tester) {
		super(tester);
		setNameAbbreviated("ZJMO");
		setNameFull("Zeesoft JSON Machine Orchestration");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZJMO/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZJMO/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/");
		getDependencies().add(new ZDK(null));
	}

	public static void main(String[] args) {
		(new ZMMT(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft JSON Machine Orchestration");
		System.out.println("==================================");
		System.out.println("Zeesoft JSON Machine Orchestration (ZJMO) is an open source library for Java application development.");
		System.out.println("The aim of the ZJMO project is to provide a scalable, high availability, JSON based, work distribution architecture.");
		System.out.println("Imagine an orchestra where the conductor directs the other members to play a certain composition.");
		System.out.println("All the members speak JSON on two TCP ports; one for control and another one for work.");
		System.out.println("One or more backup members can be created for conductors and players alike.");
		describeDependencies();
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZMMT.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		//tests.add(new TestTestOrchestra(getTester()));
		//tests.add(new TestConductor(getTester()));
		//tests.add(new TestMemberOnlineOffline(getTester()));
		//tests.add(new TestWorkRequest(getTester()));
		//tests.add(new TestConductorConnector(getTester()));
		//tests.add(new TestPublishRequest(getTester()));
	}
}
