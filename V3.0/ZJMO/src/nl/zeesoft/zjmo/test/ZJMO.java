package nl.zeesoft.zjmo.test;

import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;

public class ZJMO extends LibraryObject {
	public ZJMO(Tester tester) {
		super(tester);
		setNameAbbreviated("ZJMO");
		setNameFull("Zeesoft JSON Machine Orchestration");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZJMO/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZJMO/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJM/");
		getDependencies().add(new ZDK(null));
	}

	public static void main(String[] args) {
		(new ZJMO(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft JSON Machine Orchestration");
		System.out.println("======================");
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
		tests.add(new TestJson(this.getTester()));
		tests.add(new TestTestOrchestra(this.getTester()));
		tests.add(new TestConductor(this.getTester()));
		tests.add(new TestMemberOnlineOffline(this.getTester()));
	}
}
