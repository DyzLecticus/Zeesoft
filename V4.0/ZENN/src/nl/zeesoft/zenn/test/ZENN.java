package nl.zeesoft.zenn.test;

import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;
import nl.zeesoft.zenn.mod.ModZENN;
import nl.zeesoft.zodb.test.ZODB;

public class ZENN extends LibraryObject {
	public ZENN(Tester tester) {
		super(tester);
		setNameAbbreviated("ZENN");
		setNameFull("Zeesoft Evolutionary Neural Networks");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V4.0/ZENN/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V4.0/ZENN/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZENN/");
		getDependencies().add(new ZDK(null));
		getDependencies().add(new ZODB(null));
	}

	public static void main(String[] args) {
		(new ZENN(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Evolutionary Neural Networks");
		System.out.println("====================================");
		System.out.println(ModZENN.DESC);
		System.out.println();
		describeDependencies();
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZENN.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestGeneticCode(getTester()));
		tests.add(new TestAnimalNN(getTester()));
		tests.add(new TestTrainingProgram(getTester()));
	}
}
